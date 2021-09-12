package skytheory.hap.tile;

import java.util.List;

import defeatedcrow.hac.api.climate.ClimateAPI;
import defeatedcrow.hac.api.climate.IClimate;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import skytheory.hap.util.SmeltingHelper;
import skytheory.lib.capability.datasync.DataSyncHandler;
import skytheory.lib.capability.datasync.IDataSync;
import skytheory.lib.capability.itemhandler.ItemHandler;
import skytheory.lib.capability.itemhandler.ItemProviderSided;
import skytheory.lib.network.tile.TileSync;
import skytheory.lib.tile.ISidedTileDirectional;
import skytheory.lib.tile.ITileInventory;
import skytheory.lib.util.EnumSide;
import skytheory.lib.util.FacingUtils;
import skytheory.lib.util.ItemHandlerUtils;
import skytheory.lib.util.TickHolder;

public class TileConveyor extends TileEntity implements ITickable, ISidedTileDirectional, ITileInventory, IDataSync {

	public static String KEY_PROGRESS = "Progress";
	public static String KEY_RECIPE = "Recipe";

	public static int PROGRESS_COUNT = 8;

	private int lastUpdated = -1;
	private int progress = 0;
	private int prevProgress = 0;
	private boolean skip = false;

	private ItemHandler input;
	private ItemHandler output;

	@Override
	public void setWorld(World worldIn) {
		super.setWorld(worldIn);
		if (worldIn.isRemote) {
			TileSync.request(this, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, FacingUtils.SET_SINGLE_NULL);
		}
	}

	@Override
	public ICapabilityProvider createInventoryProvider() {
		this.input = new ItemHandler(1);
		this.output = new ItemHandler(1);

		input.setSlotLimit(1);
		input.setCanInsert((slot, stack) -> ItemHandlerUtils.isEmpty(output));
		input.setCanExtract(false);
		output.setCanInsert(false);

		input.addListener(this);
		output.addListener(this);

		ItemProviderSided provider = new ItemProviderSided(this, input, output);

		for (EnumSide side : EnumSide.values()) {
			provider.addDataToSide(input, side);
			provider.addDataToSide(output, side);
		}

		return provider;
	}

	@Override
	public void update() {
		if (world.isRemote) return;
		this.lastUpdated = TickHolder.serverTicks;
		if (skip) {
			skip = false;
			return;
		}
		prevProgress = progress;
		if (ItemHandlerUtils.isEmpty(input, output)) {
			progress = 0;
			// インベントリが空の場合には隣のTileからの自動搬入を試みる
			EnumFacing facing = this.getFacing(EnumSide.BACK);
			TileEntity tile = this.getWorld().getTileEntity(this.getPos().offset(facing));
			if (this.insertFromTile(tile, facing.getOpposite())) return;
			if (this.insertFromWorld()) return;
		} else {
			if (progress < PROGRESS_COUNT) {
				progress++;
			}
			boolean extract = progress >= PROGRESS_COUNT;
			boolean process = progress >= PROGRESS_COUNT / 2;
			if (process && !ItemHandlerUtils.isEmpty(input) && ItemHandlerUtils.isEmpty(output)) {
				// アイテムが中央まで来た時点で精錬を試みる
				this.processItem();
			}
			if (extract && !ItemHandlerUtils.isEmpty(output)) {
				// カウントが最後まで達した場合
				// アイテムが端まで来たなら、搬出を行う
				BlockPos pos = this.getPos();
				EnumFacing facing = this.getFacing(EnumSide.FRONT);
				BlockPos target = pos.offset(facing);
				if (this.getWorld().isAirBlock(target)) {
					// 送り先がAirブロックの場合
					this.extractToWorld(target);
				} else {
					// 送り先にブロックがある場合
					this.extractToTile(target, facing.getOpposite());
				}
			}
		}
		if (ItemHandlerUtils.isEmpty(input, output)) {
			progress = 0;
		}
		if (prevProgress != progress) {
			this.markDirty();
		}
	}

	@Override
	public void markDirty() {
		super.markDirty();
		if (!world.isRemote) {
			TileSync.sendToClient(this, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
			TileSync.sendToClient(this, DataSyncHandler.SYNC_DATA_CAPABILITY);
		}
	}

	public boolean extractToWorld(BlockPos targetPos) {
		double x = (double) targetPos.getX() + 0.5d;
		double y = (double) targetPos.getY() + 0.5d;
		double z = (double) targetPos.getZ() + 0.5d;
		for (int i = 0; i < output.getSlots(); i++) {
			ItemStack stack = output.getStackInSlot(i);
			if (!stack.isEmpty()) {
				EntityItem drop = new EntityItem(world, x, y, z, stack);
				drop.motionX = 0.0D;
				drop.motionY = 0.0D;
				drop.motionZ = 0.0D;
				if (world.spawnEntity(drop)) {
					output.setStackInSlot(i, ItemStack.EMPTY);
				}
			}
		}
		return ItemHandlerUtils.isEmpty(output);
	}

	public boolean extractToTile(BlockPos target, EnumFacing facing) {
		// 送り先がTileEntityの場合
		TileEntity tile = this.getWorld().getTileEntity(target);
		if (tile != null) {
			IItemHandler targetHandler = ItemHandlerUtils.getItemHandler(tile, facing);
			if (targetHandler != null) {
				boolean transfered = false;
				for (int i = 0; i < output.getSlots(); i++) {
					ItemStack stack = output.getStackInSlot(i);
					if (!stack.isEmpty()) {
						ItemHandlerUtils.transfer(output, targetHandler, 64);
						transfered = true;
					}
				}
				if (transfered && tile instanceof TileConveyor) {
					TileConveyor conveyor = (TileConveyor) tile;
					if (!conveyor.isUpdatedInTick()) {
						conveyor.skip = true;
					}
				}
			}
			return ItemHandlerUtils.isEmpty(output);
		}
		return false;
	}

	public void processItem() {
		ItemStack ingredient = input.getStackInSlot(0).copy();
		IClimate climate = ClimateAPI.calculator.getClimate(world, pos);
		ItemStack product = SmeltingHelper.onSmelting(ingredient, climate);
		if (!product.isEmpty()) {
			ItemStack outputStack = output.getStackInSlot(0);
			if (!outputStack.isEmpty() && product.isItemEqual(outputStack)) return;
			ingredient.shrink(1);
			if (ingredient.isEmpty()) ingredient = ItemStack.EMPTY;
			input.setStackInSlot(0, ingredient);
			product.grow(outputStack.getCount());
			output.setStackInSlot(0, product);
			this.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, 0.25f, 0.85f);
		} else {
			this.thruItem();
		}
	}

	public void thruItem() {
		output.setStackInSlot(0, input.getStackInSlot(0));
		input.setStackInSlot(0, ItemStack.EMPTY);
	}

	public void playSound(SoundEvent sound, float volume, float pitch) {
		double x = pos.getX() + 0.5d;
		double y = pos.getY() + 0.5d;
		double z = pos.getZ() + 0.5d;
		world.playSound(null, x, y, z, sound, SoundCategory.BLOCKS, volume, pitch);
	}

	public boolean insertFromTile(TileEntity tile, EnumFacing facing) {
		if (tile != null && !(tile instanceof TileConveyor)) {
			IItemHandler targetHandler = ItemHandlerUtils.getItemHandler(tile, facing);
			if (targetHandler != null) {
				for (int i = 0; i < targetHandler.getSlots(); i++) {
					ItemStack item = targetHandler.extractItem(i, 1, true);
					if (!item.isEmpty()) {
						item = item.copy();
						ItemStack remainder = ItemHandlerHelper.insertItem(this.input, item, false);
						targetHandler.extractItem(i, item.getCount() - remainder.getCount(), false);
						return true;
					}
				}
			}
		}
		return false;
	}

	public boolean insertFromWorld() {
		double minX = getPos().getX();
		double maxX = minX + 1.0d;
		double minY = getPos().getY();
		double maxY = minY + 1.0d;
		double minZ = getPos().getZ();
		double maxZ = minZ + 1.0d;
		List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ));

		for (Entity entity : list) {
			if (entity instanceof EntityItem) {
				EntityItem drop = (EntityItem) entity;
				ItemStack item = drop.getItem();
				ItemStack insert = item.copy();
				if (!item.isEmpty()) {
					ItemStack remainder = this.input.insertItem(0, insert, false);
					item.shrink(item.getCount() - remainder.getCount());
					if (item.isEmpty()) {
						drop.setDead();
					}
					return true;
				}
			}
		}
		return false;
	}

	public boolean isUpdatedInTick() {
		return this.lastUpdated == TickHolder.serverTicks;
	}

	@Override
	public EnumFacing getFacing() {
		return this.world.getBlockState(pos).getValue(BlockHorizontal.FACING);
	}

	public ItemStack getModelItem() {
		ItemStack model = output.getStackInSlot(0);
		if (!model.isEmpty()) {
			return model;
		}
		model = input.getStackInSlot(0);
		if (!model.isEmpty()) {
			return model;
		}
		return ItemStack.EMPTY;
	}

	public float getProgress(float partialTicks) {
		float bit = 1.0f / ((float) PROGRESS_COUNT);
		float count = this.progress;
		float position = ((float) count) * bit;
		return position;
	}

	@Override
	public NBTTagCompound serializeSync() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger(KEY_PROGRESS, progress);
		return nbt;
	}

	@Override
	public void deserializeSync(NBTTagCompound compound) {
		this.progress = MathHelper.clamp(compound.getInteger(KEY_PROGRESS), 0, PROGRESS_COUNT);
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}

	@Override
	public void onItemHandlerChanged(IItemHandler handler, int slot) {
		this.markDirty();
	}
}
