package skytheory.hap.capability;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import defeatedcrow.hac.api.energy.capability.ITorqueHandler;
import defeatedcrow.hac.api.energy.capability.TorqueCapabilityHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import skytheory.hap.tile.ITorqueTile;

// Capabilityに対応させるために作成したTorqueHandler
public class TorqueData implements ITorqueHandler, INBTSerializable<NBTTagCompound> {

	public static final String KEY_TORQUE = "Torque";
	public static final float TORQUE_GATE = 8.0f;

	public final ITorqueTile tile;

	public TorqueData(ITorqueTile tile) {
		this.tile = tile;
	}

	@Override
	public List<EnumFacing> getOutputSide() {
		return Arrays.asList(EnumFacing.values()).stream().filter(f -> tile.isOutputSide(f)).collect(Collectors.toList());
	}

	@Override
	public float getTorqueAmount() {
		return tile.getTorque();
	}

	public float getProvideAmount() {
		float amount = tile.getTorque() * tile.getFriction();
		// 8.0f未満は切り捨てる
		if (amount <= TORQUE_GATE) amount = 0.0f;
		return amount;
	}

	@Override
	public boolean canProvideTorque(World world, BlockPos outputPos, EnumFacing output) {
		float amount = this.getProvideAmount();
		if (!canProvideTorque(amount, output)) return false;
		EnumFacing input = output.getOpposite();
		TileEntity target = world.getTileEntity(outputPos);
		if (Objects.isNull(target)) return false;
		if (target.hasCapability(TorqueCapabilityHandler.TORQUE_HANDLER_CAPABILITY, null)) {
			ITorqueHandler handler = target.getCapability(TorqueCapabilityHandler.TORQUE_HANDLER_CAPABILITY, null);
			return handler.canReceiveTorque(amount, input);
		}
		return false;
	}

	@Override
	public float provideTorque(World world, BlockPos outputPos, EnumFacing output, boolean sim) {
		if (canProvideTorque(world, outputPos, output)) {
			float amount = this.getProvideAmount();
			EnumFacing input = output.getOpposite();
			TileEntity target = world.getTileEntity(outputPos);
			ITorqueHandler handler = target.getCapability(TorqueCapabilityHandler.TORQUE_HANDLER_CAPABILITY, null);
			return handler.receiveTorque(amount, input, sim);
		}
		return 0;
	}

	@Override
	public boolean canProvideTorque(float amount, EnumFacing output) {
		return amount != 0.0f && tile.isOutputSide(output);
	}

	@Override
	public boolean canReceiveTorque(float amount, EnumFacing input) {
		return tile.isInputSide(input) && tile.getNextTorque() < tile.getMaxTorque();
	}

	@Override
	public float receiveTorque(float amount, EnumFacing input, boolean sim) {
		float torque = tile.getNextTorque();
		float margin = tile.getMaxTorque() - torque;
		float result = MathHelper.clamp(amount, 0.0f, margin);
		if (!sim) {
			tile.setNextTorque(torque + result);
		}
		return result;
	}

	/*
	 * Torqueの吸出し処理、TorqueHandlerWrapperではこれが呼ばれるとTileのTorqueを吸い出し、吸い出した量を返す
	 * TorqueDataでは何もしない
	 */
	@Override
	public float provideTorque(float amount, EnumFacing output, boolean sim) {
		return 0;
	}

	@Override
	public void readFromTag(NBTTagCompound tag) {
		this.deserializeNBT(tag);
	}

	@Override
	public NBTTagCompound writeToNBTTag(NBTTagCompound tag) {
		return this.serializeNBT();
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setFloat(KEY_TORQUE, getTorqueAmount());
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		if (nbt.hasKey(KEY_TORQUE, Constants.NBT.TAG_FLOAT)) {
			tile.setTorque(Math.max(nbt.getFloat(KEY_TORQUE), 0.0f));
		}
	}

}
