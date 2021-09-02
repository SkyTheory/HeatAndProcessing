package skytheory.hap.util;

import defeatedcrow.hac.api.blockstate.DCState;
import defeatedcrow.hac.api.blockstate.EnumSide;
import defeatedcrow.hac.core.energy.TileTorqueBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import skytheory.lib.util.EnumRotation;
import skytheory.lib.util.FacingHelper;

/**
 * DCStateをFacingのように回転させるためのユーティリティー
 * @author skytheory
 *
 */
public class DCSideHelper {

	public static EnumSide rotateX(EnumSide side) {
		return EnumSide.fromFacing(FacingHelper.rotateX(side.getFacing()));
	}

	public static EnumSide rotateY(EnumSide side) {
		return EnumSide.fromFacing(FacingHelper.rotateY(side.getFacing()));
	}

	public static EnumSide rotateZ(EnumSide side) {
		return EnumSide.fromFacing(FacingHelper.rotateZ(side.getFacing()));
	}

	public static EnumSide rotateXCCW(EnumSide side) {
		return EnumSide.fromFacing(FacingHelper.rotateXCCW(side.getFacing()));
	}

	public static EnumSide rotateYCCW(EnumSide side) {
		return EnumSide.fromFacing(FacingHelper.rotateYCCW(side.getFacing()));
	}

	public static EnumSide rotateZCCW(EnumSide side) {
		return EnumSide.fromFacing(FacingHelper.rotateZCCW(side.getFacing()));
	}

	public static EnumSide rotate(EnumSide side, EnumFacing axis) {
		return EnumSide.fromFacing(FacingHelper.rotate(side.getFacing(), axis));
	}

	public static EnumSide rotateOrInvert(EnumSide side, EnumFacing axis) {
		return EnumSide.fromFacing(FacingHelper.rotateOrInvert(side.getFacing(), axis));
	}

	public static EnumSide rotateCCW(EnumSide side, EnumFacing axis) {
		return EnumSide.fromFacing(FacingHelper.rotateCCW(side.getFacing(), axis));
	}

	public static EnumSide rotateCCWorInvert(EnumSide side, EnumFacing axis) {
		return EnumSide.fromFacing(FacingHelper.rotateCCWorInvert(side.getFacing(), axis));
	}

	public static EnumSide getOpposite(EnumSide side) {
		return EnumSide.fromFacing(side.getFacing().getOpposite());
	}

	public static EnumRotation getRotation(EnumSide side, int angle) {
		int r = angle;
		EnumFacing facing = side.getFacing();
		if (facing.getAxis().isVertical()) {
			r += 2;
		}
		return EnumRotation.fromFacingAndAngle(facing, r);
	}

	public static void setRotation(World world, BlockPos pos, EnumRotation rotation) {
		IBlockState state = world.getBlockState(pos);
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileTorqueBase) {
			TileTorqueBase device = (TileTorqueBase) tile;
			EnumFacing facing = rotation.getFront();
			EnumFacing anglef;
			int angle = rotation.getAngle();
			switch (angle) {
			case 0:
				anglef = EnumFacing.NORTH;
				break;
			case 1:
				anglef = EnumFacing.EAST;
				break;
			case 2:
				anglef = EnumFacing.SOUTH;
				break;
			case 3:
				anglef = EnumFacing.WEST;
				break;
			default:
				anglef = EnumFacing.NORTH;
			}
			if (facing.getAxis().isVertical()) {
				anglef = anglef.getOpposite();
			}
			EnumSide dcside = EnumSide.fromFacing(facing);
			world.setBlockState(pos, state.withProperty(DCState.SIDE, dcside));
			device.setFaceSide(anglef);
		}
	}
}
