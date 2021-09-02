package skytheory.hap.util;

import defeatedcrow.hac.api.blockstate.DCState;
import defeatedcrow.hac.api.blockstate.EnumSide;
import defeatedcrow.hac.core.energy.TileTorqueBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import skytheory.lib.util.EnumRotation;
import skytheory.lib.util.WrenchHelper;

/**
 * DCState.SIDEに対応するWrenchHelper
 * @author skytheory
 *
 */
public class WrenchHelperHaP {


	public static void rotateDCSide(World world, BlockPos pos, EnumFacing side) {
		IBlockState state = world.getBlockState(pos);
		EnumSide dcside = state.getValue(DCState.SIDE);
		EnumSide result = DCSideHelper.rotateOrInvert(dcside, side);
		WrenchHelper.setProperty(world, pos, state, DCState.SIDE, result);
	}

	public static void invertDCSide(World world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		EnumSide dcside = state.getValue(DCState.SIDE);
		EnumSide invert = EnumSide.fromFacing(dcside.getFacing().getOpposite());
		WrenchHelper.setProperty(world, pos, state, DCState.SIDE, invert);
	}

	public static void rotateDCSideAngle(EntityPlayer player, World world, BlockPos pos, EnumFacing side) {
		IBlockState state = world.getBlockState(pos);
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileTorqueBase && player instanceof EntityPlayerMP) {
			TileTorqueBase device = (TileTorqueBase) tile;
			EnumRotation rotation = DCSideHelper.getRotation(state.getValue(DCState.SIDE), device.facing);
			DCSideHelper.setRotation(world, pos, rotation.rotate(side));
			// HeatAndClimateのBlockTorqueBase、Tileの角度を同期していない気がする
			((EntityPlayerMP) player).connection.sendPacket(device.getUpdatePacket());
		}
	}

	public static void invertDCSideAngle(EntityPlayer player, World world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileTorqueBase && player instanceof EntityPlayerMP) {
			TileTorqueBase device = (TileTorqueBase) tile;
			EnumRotation rotation = DCSideHelper.getRotation(state.getValue(DCState.SIDE), device.facing);
			DCSideHelper.setRotation(world, pos, rotation.getOpposite());
			// HeatAndClimateのBlockTorqueBase、Tileの角度を同期していない気がする
			((EntityPlayerMP) player).connection.sendPacket(device.getUpdatePacket());
		}
	}

	public static void rotateDCSideY(World world, BlockPos pos, EntityPlayer player) {
		IBlockState state = world.getBlockState(pos);
		// 何らかの手違いでUPまたはDOWNを向いていた場合はPlayerの視線の方向に向けておく
		if (state.getValue(DCState.SIDE) == EnumSide.UP) {
			WrenchHelper.setProperty(world, pos, state, DCState.SIDE, EnumSide.fromFacing(player.getHorizontalFacing()));
			return;
		}
		rotateDCSide(world, pos, EnumFacing.UP);
	}

	public static void rotateFaucet(World world, BlockPos pos, EnumFacing side) {
		IBlockState state = world.getBlockState(pos);
		EnumSide current = state.getValue(DCState.SIDE);
		EnumSide next = EnumSide.fromFacing(side);
		if (current == next) next = DCSideHelper.getOpposite(next);
		if (next == EnumSide.UP) next = EnumSide.DOWN;
		WrenchHelper.setProperty(world, pos, state, DCState.SIDE, next);
	}

	public static void rotateFaucetReversed(World world, BlockPos pos, EnumFacing side) {
		IBlockState state = world.getBlockState(pos);
		EnumSide current = state.getValue(DCState.SIDE);
		EnumSide next = EnumSide.fromFacing(side);
		if (current == next) next = DCSideHelper.getOpposite(next);
		if (next == EnumSide.DOWN) next = EnumSide.UP;
		WrenchHelper.setProperty(world, pos, state, DCState.SIDE, next);
	}

}
