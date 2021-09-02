package skytheory.hap.tile;

import java.util.List;
import java.util.Objects;

import defeatedcrow.hac.api.energy.ITorqueReceiver;
import defeatedcrow.hac.api.energy.capability.ITorqueHandler;
import defeatedcrow.hac.api.energy.capability.TorqueCapabilityHandler;
import defeatedcrow.hac.core.energy.TileTorqueBase;
import defeatedcrow.hac.machine.block.TileShaft_TA;
import defeatedcrow.hac.machine.block.TileShaft_TB;
import defeatedcrow.hac.machine.block.TileShaft_X;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import skytheory.hap.util.ConstantsHaP;
import skytheory.lib.network.tile.TileSync;
import skytheory.lib.plugin.waila.IWailaTipTile;
import skytheory.lib.tile.ISidedTile;
import skytheory.lib.util.EnumSide;
import skytheory.lib.util.FacingUtils;
import skytheory.lib.util.TextUtils;
import skytheory.lib.util.UpdateFrequency;

public abstract class TileTorque extends TileEntity implements ITickable, ITorqueTile, ITorqueReceiver, ISidedTile, IWailaTipTile {

	// トルクの更新頻度
	// 覚書：DCTileEntity.getMaxCool()の値と同値にすること
	public static final int FREQUENCY = 20;

	// トルク伝達の摩擦係数、トルクの減衰にかかわる
	public static final float TORQUE_FRICTION = 0.995f;

	// これ以下のトルクは受け取る際に切り捨てる
	public static final float TORQUE_GATE = 8.0f;

	private UpdateFrequency freq = new UpdateFrequency(FREQUENCY);

	private float nexttorque = 0.0f;
	private float torque = 0.0f;

	// 入出力面の設定
	public boolean isInputSide(EnumFacing side) {return this.isInputSide(this.getSide(side));}
	public boolean isOutputSide(EnumFacing side) {return this.isOutputSide(this.getSide(side));}

	public abstract boolean isInputSide(EnumSide side);
	public abstract boolean isOutputSide(EnumSide side);

	public void setTorque(float amount) {this.torque = amount;}
	public void setNextTorque(float amount) {this.nexttorque = amount;}
	public float getTorque() {return torque;}
	public float getNextTorque() {return nexttorque;}
	public float getFriction() {return TORQUE_FRICTION;}
	public abstract float getMaxTorque();

	@Override
	public void update() {
		if (!world.isRemote) {
			if (this.freq.shouldUpdate()) {
				this.updateTorque();
				TileSync.sendToClient(this, TorqueCapabilityHandler.TORQUE_HANDLER_CAPABILITY, FacingUtils.SET_SINGLE_NULL);
			}
		}
	}

	/**
	 * トルクの現在値を更新する
	 */
	public void updateTorque() {
		//this.drawTorque();
		this.torque = this.nexttorque;
		this.nexttorque = 0.0f;
		this.provideTorque();
	}

	/**
	 *
	 * HeatAndClimateはCapabilityにはTorqueを送ってくれないので吸出し処理を自作した<br>
	 * ……のだが、仕様変更に備えて素直にITorqueReceiverを実装することに<br>
	 * HaCが仕様を変更し、それが意図した方向でなかった際に備えコードは残しておく
	 * @deprecated unused
	 */
	protected void drawTorque() {
		for (EnumFacing input : EnumFacing.values()) {
			if (isInputSide(input)) {
				BlockPos target = pos.offset(input);
				TileEntity tile = world.getTileEntity(target);
				if (Objects.isNull(tile)) continue;
				EnumFacing toutput = input.getOpposite();
				if (tile instanceof TileTorqueBase) {
					ITorqueHandler handler = tile.getCapability(TorqueCapabilityHandler.TORQUE_HANDLER_CAPABILITY, toutput);
					float amount = handler.provideTorque(handler.getTorqueAmount(), toutput, true);
					if (tile instanceof TileShaft_TA) {
						amount *= 0.5f;
					} else if (tile instanceof TileShaft_TB) {
						amount *= 0.5f;
					} else if (tile instanceof TileShaft_X) {
						amount /= 3;
					}
					if (amount > TORQUE_GATE) {
						float margin = this.getMaxTorque() - this.getNextTorque();
						this.setNextTorque(this.getNextTorque() + Math.min(margin, amount));
					}
				}
			}
		}
	}

	protected void provideTorque() {
		for (EnumFacing output : EnumFacing.values()) {
			if (isOutputSide(output)) {
				BlockPos target = pos.offset(output);
				ITorqueHandler handler = this.getCapability(TorqueCapabilityHandler.TORQUE_HANDLER_CAPABILITY, null);
				if (handler != null) {
					handler.provideTorque(world, target, output, false);
				}
			}
		}
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}

	@Override
	public void getWailaTips(ItemStack stack, List<String> tips, IWailaDataAccessor accessor) {
		tips.add(TextUtils.format(ConstantsHaP.TIP_TORQUE, String.format("%.2f", this.torque)));
	}

	/*
	 * 以下はITorqueReceiverの実装
	 * 現状のHaCはTorqueHandlerを持つかではなくITorqueReceiverを持つかでトルクの送信を行っているので……
	 */
	public boolean canReceiveTorque(float amount, EnumFacing side) {
		return this.isInputSide(side) && amount > TORQUE_GATE;
	}

	public float receiveTorque(float amount, EnumFacing side, boolean sim) {
		float torque = this.getNextTorque();
		float margin = this.getMaxTorque() - torque;
		float result = MathHelper.clamp(amount, 0.0f, margin);
		if (!sim) {
			this.setNextTorque(torque + result);
		}
		return amount - result;
	}

	public EnumFacing getBaseSide() {return EnumFacing.DOWN;}
	public EnumFacing getFaceSide() {return EnumFacing.DOWN;}
	public void setBaseSide(EnumFacing side) {	}
	public void setFaceSide(EnumFacing side) {	}
	public void rotateFace() {}
	public float getEffectiveAcceleration() {return 0.0f;}
	public float getCurrentAcceleration() {return 0.0f;}
	public float getFrictionalForce() {return this.getFriction();}
	public float getGearTier() {return Float.MAX_VALUE;}
	public float getCurrentTorque() {return this.getTorque();}
	public float getRotationalSpeed() {return 0.0f;}
	public boolean hasFaceSide() {return false;}

}
