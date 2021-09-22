package skytheory.hap.tile;

import java.util.List;

import defeatedcrow.hac.api.climate.DCHumidity;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import skytheory.hap.util.ClimateTextHelper;
import skytheory.hap.util.ConstantsHaP;
import skytheory.lib.capability.datasync.DataSyncHandler;
import skytheory.lib.capability.datasync.IDataSync;
import skytheory.lib.network.tile.TileSync;
import skytheory.lib.util.EnumSide;
import skytheory.lib.util.FacingUtils;
import skytheory.lib.util.TextUtils;

public class TileMistDispenser extends TileTorqueRotational implements ITileInteract, IDataSync {

	public static float TORQUE_REQUIRED = 16.0f;
	public static String Active = "Active";
	public boolean isActive = false;

	@Override
	public boolean isInputSide(EnumSide side) {
		return side == EnumSide.BACK;
	}

	@Override
	public boolean isOutputSide(EnumSide side) {
		return false;
	}

	@Override
	public float getMaxTorque() {
		return 128.0f;
	}

	public boolean isEnoughTorque() {
		return this.getCurrentTorque() >= TORQUE_REQUIRED;
	}

	public DCHumidity getHumidity() {
		if (!this.isEnoughTorque() || !this.isActive) return DCHumidity.NORMAL;
		return DCHumidity.WET;
	}

	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey(Active, Constants.NBT.TAG_BYTE)) {
			this.isActive = compound.getBoolean(Active);
		}
	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setBoolean(Active, isActive);
		return compound;
	}

	@Override
	public NBTTagCompound serializeSync() {
		NBTTagCompound compound = super.serializeSync();
		compound.setBoolean(Active, isActive);
		return compound;
	}

	@Override
	public void deserializeSync(NBTTagCompound compound) {
		super.deserializeSync(compound);
		if (compound.hasKey(Active, Constants.NBT.TAG_BYTE)) {
			this.isActive = compound.getBoolean(Active);
		}
	}

	@Override
	public boolean onRightClick(World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing facing) {
		if (hand == EnumHand.MAIN_HAND) {
			if (world.isRemote) {
				world.playSound(player, pos, SoundEvents.BLOCK_METAL_PRESSPLATE_CLICK_ON, SoundCategory.BLOCKS, 0.3f, 1.0f);
			} else {
				this.isActive = !isActive;
				TileSync.sendToClient(this, DataSyncHandler.SYNC_DATA_CAPABILITY, FacingUtils.SET_SINGLE_NULL);
			}
			return true;
		}
		return false;
	}

	@Override
	public void getWailaTips(ItemStack stack, List<String> tips, IWailaDataAccessor accessor) {
		super.getWailaTips(stack, tips, accessor);
		if (!isEnoughTorque()) {
			String tip = TextUtils.format(ConstantsHaP.TIP_INACTIVE);
			tips.add(tip);
		} else {
			String l = ClimateTextHelper.getHumidityText(this.getHumidity());
			tips.add(TextUtils.format(ConstantsHaP.TIP_ACTIVE));
			tips.add(TextUtils.format(ConstantsHaP.TIP_HUMIDITY, l));
		}
	}
}
