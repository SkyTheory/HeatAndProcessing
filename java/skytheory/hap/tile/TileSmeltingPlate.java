package skytheory.hap.tile;

import java.util.function.Predicate;

import defeatedcrow.hac.api.climate.ClimateAPI;
import defeatedcrow.hac.api.climate.IClimate;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import skytheory.hap.util.SmeltingHelper;
import skytheory.lib.capability.itemhandler.ItemHandler;
import skytheory.lib.capability.itemhandler.ItemProviderSided;
import skytheory.lib.network.tile.TileSync;
import skytheory.lib.tile.ISidedTileDirectional;
import skytheory.lib.tile.ITileInventory;
import skytheory.lib.util.EnumSide;
import skytheory.lib.util.ItemHandlerUtils;

public class TileSmeltingPlate extends TileEntity implements ITickable, ISidedTileDirectional, ITileInventory {

	public static String KEY_PROCESS = "CoolTime";
	public static String KEY_RECIPE = "Recipe";
	public static int PROCESS_SPEED = 40;

	private int progress;
	private ItemHandler input;
	private ItemHandler output;
	private ItemStack recipe = ItemStack.EMPTY;

	@Override
	public ICapabilityProvider createInventoryProvider() {
		this.input = new ItemHandler(1) {
			@Override
			public int getSlotLimit(int slot) {
				ItemStack result = output.getStackInSlot(0);
				if (result.isEmpty()) return 64;
				return result.getMaxStackSize() - result.getCount();
			}
		};
		this.output = new ItemHandler(1);

		this.input.addListener(this);
		this.output.addListener(this);

		Predicate<ItemStack> condition1 = stack -> this.recipe.isEmpty();
		Predicate<ItemStack> condition2 = stack -> ItemHandlerHelper.canItemStacksStack(recipe, stack);

		this.input.setCanInsert((slot, stack) -> condition1.or(condition2).test(stack));
		this.input.setCanExtract(false);
		this.output.setCanInsert(false);

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
		if (!ItemHandlerUtils.isEmpty(input)) {
			if (this.progress++ > PROCESS_SPEED) {
				// 素材スロットに中身があればカウントを進め、カウントが溜まれば加工を試みる
				this.processItem();
				// 加工した後、カウントをリセット
				this.progress = 0;
			}
		} else {
			// inputが空ならばカウントをリセット
			this.progress = 0;
		}
		if (!ItemHandlerUtils.isEmpty(output)) {
			IClimate climate = ClimateAPI.calculator.getClimate(world, pos);
			if (!ItemHandlerUtils.isEmpty(output)) {
				// 完成品スロットにあるものが加工可能なら差し戻し
				ItemStack ingredient = output.extractItem(0, 1, true);
				if (SmeltingHelper.canSmelting(ingredient, climate)) {
					ItemStack back = output.extractItem(0, 64, false);
					input.insertItem(0, back, false);
				}
			}
		}
		if (!this.recipe.isEmpty() && ItemHandlerUtils.isEmpty(input, output)) {
			// インベントリが空になれば、レシピをリセットする
			this.recipe = ItemStack.EMPTY;
			this.markDirty();
		}
	}

	public void processItem() {
		ItemStack ingredient = input.extractInternal(0, 1, true);
		IClimate climate = ClimateAPI.calculator.getClimate(world, pos);
		ItemStack product = SmeltingHelper.onSmelting(ingredient, climate);
		this.recipe = ingredient;
		if (!product.isEmpty()) {
			ItemStack remainder = output.insertInternal(0, product, true);
			if (remainder.isEmpty()) {
				this.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, 0.25f, 0.85f);
				input.extractInternal(0, 1, false);
				output.insertInternal(0, product, false);
			}
		} else {
			ItemStack stack = input.extractInternal(0, 64, true);
			ItemStack remainder = output.insertInternal(0, stack, true);
			int count = stack.getCount() - remainder.getCount();
			stack.setCount(count);
			input.extractInternal(0, count, false);
			output.insertInternal(0, stack, false);
		}
	}

	public void playSound(SoundEvent sound, float volume, float pitch) {
		double x = pos.getX() + 0.5d;
		double y = pos.getY() + 0.5d;
		double z = pos.getZ() + 0.5d;
		world.playSound(null, x, y, z, sound, SoundCategory.BLOCKS, volume, pitch);
	}

	public void onRightClick(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		if (ItemHandlerUtils.isEmpty(input, output)) {
			// プレートへアイテムを挿入する
			if (ItemHandlerUtils.putItemFromPlayer(input, 0, player, false)) {
				playSound(SoundEvents.ENTITY_ITEM_PICKUP, 0.2f, 1.5f);
			}
		} else {
			// プレートからアイテムを取り出す
			if (!ItemHandlerUtils.isEmpty(output)) {
				// 完成品スロットにアイテムがあれば、そちらを優先して取り出す
				ItemHandlerHelper.giveItemToPlayer(player, output.getStackInSlot(0), player.inventory.currentItem);
				output.setStackInSlot(0, ItemStack.EMPTY);
				return;
			} else {
				// 完成品スロットが空であるなら、材料スロットから取り出す
				ItemHandlerHelper.giveItemToPlayer(player, input.getStackInSlot(0), player.inventory.currentItem);
				input.setStackInSlot(0, ItemStack.EMPTY);
				return;
			}
		}
	}

	@Override
	public EnumFacing getFacing() {
		return this.world.getBlockState(pos).getValue(BlockHorizontal.FACING);
	}

	@Override
	public void markDirty() {
		super.markDirty();
		if (!world.isRemote) {
			TileSync.sendToClient(this, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey(KEY_PROCESS, Constants.NBT.TAG_INT)) {
			int count = MathHelper.clamp(compound.getInteger(KEY_PROCESS), 0, PROCESS_SPEED);
			this.progress = count;
		}
		if (compound.hasKey(KEY_RECIPE, Constants.NBT.TAG_COMPOUND)) {
			this.recipe = new ItemStack(compound.getCompoundTag(KEY_RECIPE));
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setInteger(KEY_PROCESS, this.progress);
		if (!this.recipe.isEmpty()) {
			NBTTagCompound itemtag = new NBTTagCompound();
			recipe.writeToNBT(itemtag);
			compound.setTag(KEY_RECIPE, itemtag);
		}
		return compound;
	}

	public ItemStack getModelItem() {
		for (int i = 0; i < output.getSlots(); i++) {
			ItemStack stack = output.getStackInSlot(i);
			if (!stack.isEmpty()) {
				return stack;
			}
		}
		for (int i = 0; i < input.getSlots(); i++) {
			ItemStack stack = input.getStackInSlot(i);
			if (!stack.isEmpty()) {
				return stack;
			}
		}
		return ItemStack.EMPTY;
	}

	@Override
	public void onItemHandlerChanged(IItemHandler handler, int slot) {
		this.markDirty();
	}
}
