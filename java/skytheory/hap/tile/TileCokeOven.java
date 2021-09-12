package skytheory.hap.tile;

import defeatedcrow.hac.api.climate.ClimateAPI;
import defeatedcrow.hac.api.climate.DCHeatTier;
import defeatedcrow.hac.api.climate.DCHumidity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import skytheory.hap.HeatAndProcessing;
import skytheory.hap.init.GuiHandler;
import skytheory.hap.recipe.CokeOvenRecipe;
import skytheory.hap.recipe.CokeOvenRecipes;
import skytheory.lib.capability.DataProvider;
import skytheory.lib.capability.datasync.DataSyncHandler;
import skytheory.lib.capability.datasync.IDataSync;
import skytheory.lib.capability.itemhandler.ItemAccessor;
import skytheory.lib.capability.itemhandler.ItemHandler;
import skytheory.lib.capability.itemhandler.MultiItemHandlerSerializable;
import skytheory.lib.network.tile.TileSync;
import skytheory.lib.tile.ITileInventory;

public class TileCokeOven extends TileEntity implements ITickable, ITileInventory, ITileInteract, IDataSync {

	public static final String KEY_STATE = "State";
	public static final String KEY_PROGRESS = "Progress";
	public static final String KEY_SECONDARY = "Secondary";

	public IItemHandler input;
	public IItemHandler output;
	public int state;

	// 覚書：バニラかまどの精錬には200tick
	// 8倍くらいでいいかなー？
	public static final int TOTAL_COUNT = 1600;
	// 最遅で精錬すれば副産物が確実に手に入るように
	public static final float CHANCE_MODIFIER = 0.000625f; // 1.0f / 1600.0f

	public int progress;
	public float chance;
	public CokeOvenRecipe recipe;
	private boolean skipRecipe;

	@Override
	public void setWorld(World worldIn) {
		super.setWorld(worldIn);
		if (worldIn.isRemote) {
			TileSync.request(this, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
			TileSync.request(this, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
		}
	}

	@Override
	public ICapabilityProvider createInventoryProvider() {
		IItemHandler input = new ItemHandler(1).addListener(this);
		IItemHandler output = new ItemHandler(3).addListener(this);
		this.input = input;
		this.output = output;
		IItemHandler accessor = new MultiItemHandlerSerializable(ItemAccessor.insertOnly(input), ItemAccessor.extractOnly(output));
		return new DataProvider<IItemHandler>(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, accessor);
	}

	@Override
	public void update() {
		if (world.isRemote) return;
		if (!skipRecipe) {
			skipRecipe = true;
			if (!checkRecipe()) {
				this.state = 0;
				resetProgress();
			}
		}
		if (this.recipe != null) {
			DCHeatTier heat = ClimateAPI.calculator.getHeat(world, pos, 2, false);
			DCHumidity humidity = ClimateAPI.calculator.getHumidity(world, pos);
			if (humidity == DCHumidity.DRY && heat.getTier() > 3) {
				if (progress < TOTAL_COUNT) {
					int adv = heat.getTier() - 3;
					this.state = adv;
					if (adv > 0) {
						// 加工プロセスはHeatTierに応じて早くなる
						// ただし、副産物の精製チャンスはHeatTierに依らずTick毎に増加
						// 結果として、高温で加工すると生産は早くなるけれど副産物は少なくなる
						this.progress += adv;
						this.chance += CHANCE_MODIFIER;
						TileSync.sendToClient(this, DataSyncHandler.SYNC_DATA_CAPABILITY);
					}
				} else {
					if (this.recipe.canOutput(output)) {
						this.recipe.process(input, output, chance, world.rand);
						resetProgress();
					}
				}
			} else {
				this.state = 0;
				resetProgress();
			}
		}
	}

	public void resetProgress() {
		this.progress = 0;
		this.chance = 0.0f;
		TileSync.sendToClient(this, DataSyncHandler.SYNC_DATA_CAPABILITY);
	}

	public boolean checkRecipe() {
		CokeOvenRecipe prev = recipe;
		CokeOvenRecipe next = CokeOvenRecipes.getRecipe(input);
		this.recipe = next;
		if (prev == null && next != null) {
			return true;
		}
		if (prev == next) {
			return true;
		}
		return false;
	}

	@Override
	public void onItemHandlerChanged(IItemHandler handler, int slot) {
		this.markDirty();
		if (!this.getWorld().isRemote) {
			if (handler == this.input) skipRecipe = false;
			TileSync.sendToClient(this, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
		}
	}

	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
		return oldState.getBlock() != newSate.getBlock();
	}

	@Override
	public NBTTagCompound serializeSync() {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setInteger(KEY_STATE, state);
		compound.setInteger(KEY_PROGRESS, progress);
		compound.setFloat(KEY_SECONDARY, chance);
		return compound;
	}

	@Override
	public void deserializeSync(NBTTagCompound compound) {
		if (compound.hasKey(KEY_STATE, Constants.NBT.TAG_INT)) {
			this.state = MathHelper.clamp(compound.getInteger(KEY_STATE), 0, 2);
		}
		if (compound.hasKey(KEY_PROGRESS, Constants.NBT.TAG_INT)) {
			this.progress = MathHelper.clamp(compound.getInteger(KEY_PROGRESS), 0, TOTAL_COUNT);
		}
		if (compound.hasKey(KEY_SECONDARY, Constants.NBT.TAG_FLOAT)) {
			this.chance = Math.max(0.0f, compound.getFloat(KEY_SECONDARY));
		}
	}

	@Override
	public boolean onRightClick(World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing facing) {
		if (hand == EnumHand.MAIN_HAND) {
			player.openGui(HeatAndProcessing.INSTANCE, GuiHandler.TILE_GUI, world, pos.getX(), pos.getY(), pos.getZ());
			return true;
		}
		return false;
	}

}
