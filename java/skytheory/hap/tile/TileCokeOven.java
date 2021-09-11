package skytheory.hap.tile;

import defeatedcrow.hac.api.climate.ClimateAPI;
import defeatedcrow.hac.api.climate.DCHeatTier;
import defeatedcrow.hac.api.climate.DCHumidity;
import defeatedcrow.hac.api.climate.IClimate;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
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
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import skytheory.hap.HeatAndProcessing;
import skytheory.hap.init.GuiHandler;
import skytheory.lib.capability.DataProvider;
import skytheory.lib.capability.datasync.IDataSync;
import skytheory.lib.capability.itemhandler.ItemAccessor;
import skytheory.lib.capability.itemhandler.ItemHandler;
import skytheory.lib.capability.itemhandler.MultiItemHandler;
import skytheory.lib.capability.itemhandler.MultiItemHandlerSerializable;
import skytheory.lib.network.tile.TileSync;
import skytheory.lib.tile.ITileInventory;
import skytheory.lib.util.FacingUtils;

public class TileCokeOven extends TileEntity implements ITickable, ITileInventory, ITileInteract, IDataSync {

	public static final String KEY_PROGRESS = "Progress";
	public static final String KEY_SECONDARY = "Secondary";

	public IItemHandler input;
	public IItemHandler output;

	// 覚書：バニラかまどの精錬には200tick
	// 4倍くらいでいいかなー？
	public static final int TOTAL_COUNT = 800;
	// 最遅で精錬すれば副産物が確実に手に入るように
	public static final float CHANCE_MODIFIER = 0.00125f; // 1.0f / 800.0f

	public int progress;
	public float chance;

	@Override
	public void setWorld(World worldIn) {
		super.setWorld(worldIn);
		if (worldIn.isRemote) {
			TileSync.request(this, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, FacingUtils.SET_SINGLE_NULL);
		}
	}

	@Override
	public ICapabilityProvider createInventoryProvider() {
		IItemHandler input = new ItemHandler(1);
		IItemHandler output = new ItemHandler(3);
		this.input = input;
		this.output = output;
		IItemHandler accessorInput = new ItemAccessor(input).setCanExtract(false);
		IItemHandler accessorOutput = new ItemAccessor(output).setCanInsert(false);
		IItemHandler accessor = new MultiItemHandler(accessorInput, accessorOutput);
		INBTSerializable<NBTBase> serializer = new MultiItemHandlerSerializable(input, output);
		return new DataProvider<IItemHandler>(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, accessor, serializer);
	}

	@Override
	public void update() {
		IClimate climate = ClimateAPI.calculator.getClimate(world, pos);
		if (climate.getHumidity() == DCHumidity.DRY) {
			DCHeatTier heat = climate.getHeat();
			int tier = heat.getTier() - 3;
			if (tier > 0) {
				// 加工プロセスはHeatTierに応じて早くなる
				// ただし、副産物の精製チャンスはHeatTierに依らずTick毎に増加
				// 結果として、高温で加工すると生産は早くなるけれど副産物は少なくなる
				this.progress += tier;
				this.chance += CHANCE_MODIFIER;
			}
		}
	}

	@Override
	public void onItemHandlerChanged(IItemHandler handler, int slot) {
		this.markDirty();
		if (this.hasWorld() && !this.getWorld().isRemote) {
			TileSync.sendToClient(this, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
		}
	}

	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
		return oldState.getBlock() != newSate.getBlock();
	}

	@Override
	public NBTTagCompound serializeSync() {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setInteger(KEY_PROGRESS, progress);
		compound.setFloat(KEY_SECONDARY, chance);
		return null;
	}

	@Override
	public void deserializeSync(NBTTagCompound compound) {
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
