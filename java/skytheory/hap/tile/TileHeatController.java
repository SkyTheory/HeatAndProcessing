package skytheory.hap.tile;

import java.util.List;

import defeatedcrow.hac.api.climate.DCHeatTier;
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
import skytheory.hap.util.ConstantsHaP;
import skytheory.hap.util.ClimateTextHelper;
import skytheory.lib.capability.datasync.DataSyncHandler;
import skytheory.lib.network.tile.TileSync;
import skytheory.lib.util.EnumSide;
import skytheory.lib.util.FacingUtils;
import skytheory.lib.util.TextUtils;

public abstract class TileHeatController extends TileTorqueRotational implements ITileInteract {

	public static final String KEY_HEAT = "HeatTier";
	private DCHeatTier heat = this.getHeatTiers().get(0);

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

	/**
	 * デバイスの取り得るHeatTierを設定する<br>
	 * 要求Torqueの少ない順に設定すること
	 **/
	public abstract List<DCHeatTier> getHeatTiers();

	public void setHeatTier(DCHeatTier heat) {
		if (this.getHeatTiers().contains(heat)) {
			this.heat = heat;
		}
	}

	/** デバイスに設定されているHeatTier */
	public DCHeatTier getHeatTier() {
		return this.heat;
	}

	/** 実際に出力するHeatTier */
	public DCHeatTier getActualHeat() {
		DCHeatTier heat = DCHeatTier.NORMAL;
		for (int i = 0; i <= this.getHeatTiers().indexOf(this.getHeatTier()); i++) {
			DCHeatTier next = this.getHeatTiers().get(i);
			if (this.isEnoughTorque(next)) {
				heat = next;
			}
		}
		return heat;
	}

	protected boolean isEnoughTorque(DCHeatTier tier) {
		return this.getTorque() >= this.getTorqueRequired(tier);
	}

	/** HeatTierに対するトルクの要求量を返す */
	public abstract float getTorqueRequired(DCHeatTier tier);

	public void nextHeat() {
		int index = this.getHeatTiers().indexOf(this.getHeatTier()) + 1;
		if (index >= this.getHeatTiers().size()) index = 0;
		this.setHeatTier(this.getHeatTiers().get(index));
	}

	public void prevHeat() {
		int index = this.getHeatTiers().indexOf(this.getHeatTier()) - 1;
		if (index < 0) index = this.getHeatTiers().size() - 1;
		this.setHeatTier(this.getHeatTiers().get(index));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setInteger(KEY_HEAT, this.heat.getID());
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey(KEY_HEAT, Constants.NBT.TAG_INT)) {
			DCHeatTier heat = DCHeatTier.getTypeByID(compound.getInteger(KEY_HEAT));
			this.setHeatTier(heat);
		}
	}

	@Override
	public boolean onRightClick(World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing facing) {
		if (hand == EnumHand.MAIN_HAND) {
			if (world.isRemote) {
				world.playSound(player, pos, SoundEvents.BLOCK_METAL_PRESSPLATE_CLICK_ON, SoundCategory.BLOCKS, 0.3f, 1.0f);
			} else {
				this.nextHeat();
				TileSync.sendToClient(this, DataSyncHandler.SYNC_DATA_CAPABILITY, FacingUtils.SET_SINGLE_NULL);
			}
			return true;
		}
		return false;
	}

	@Override
	public NBTTagCompound serializeSync() {
		NBTTagCompound nbt = super.serializeSync();
		nbt.setInteger(KEY_HEAT, this.heat.getID());
		return nbt;
	}

	@Override
	public void deserializeSync(NBTTagCompound nbt) {
		super.deserializeSync(nbt);
		if (nbt.hasKey(KEY_HEAT, Constants.NBT.TAG_INT)) {
			DCHeatTier heat = DCHeatTier.getTypeByID(nbt.getInteger(KEY_HEAT));
			this.setHeatTier(heat);
		}
	}

	@Override
	public void getWailaTips(ItemStack stack, List<String> tips, IWailaDataAccessor accessor) {
		super.getWailaTips(stack, tips, accessor);
		DCHeatTier prop = this.getHeatTier();
		DCHeatTier heat = this.getActualHeat();
		if (heat == DCHeatTier.NORMAL) {
			String tip = TextUtils.format(ConstantsHaP.TIP_INACTIVE);
			tips.add(tip);
		} else if (prop == heat) {
			String l = ClimateTextHelper.getHeatText(heat);
			tips.add(TextUtils.format(ConstantsHaP.TIP_ACTIVE));
			tips.add(TextUtils.format(ConstantsHaP.TIP_HEAT, l));
		} else {
			String l = ClimateTextHelper.getHeatText(heat);
			tips.add(TextUtils.format(ConstantsHaP.TIP_SHORTAGE));
			tips.add(TextUtils.format(ConstantsHaP.TIP_REQUIRED, String.format("%.2f", this.getTorqueRequired(prop))));
			tips.add(TextUtils.format(ConstantsHaP.TIP_HEAT, l));
		}
	}
}
