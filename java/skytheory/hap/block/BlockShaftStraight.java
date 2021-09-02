package skytheory.hap.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import skytheory.hap.config.HaPConfig;
import skytheory.hap.tile.TileShaftStraight;
import skytheory.lib.util.EnumSide;

public class BlockShaftStraight extends BlockShaftBase {

	public BlockShaftStraight(int torqueTier) {
		super(torqueTier);
	}

	public static final AxisAlignedBB SHAFT_X = new AxisAlignedBB(0.0d, 0.3125d, 0.3125d, 1.0d, 0.6875d, 0.6875d);
	public static final AxisAlignedBB SHAFT_Y = new AxisAlignedBB(0.3125d, 0.0d, 0.375d, 0.6875d, 1.0d, 0.6875d);
	public static final AxisAlignedBB SHAFT_Z = new AxisAlignedBB(0.3125d, 0.3125d, 0.0d, 0.6875d, 0.625d, 1.0d);

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileShaftStraight();
	}

	@Override
	public RayTraceResult collisionRayTrace(IBlockState blockState, World worldIn, BlockPos pos, Vec3d start, Vec3d end) {
		AxisAlignedBB hitbox = HaPConfig.shrink_shaft ? GEAR_BOX : FULL_BLOCK_AABB;
			TileEntity tile = worldIn.getTileEntity(pos);
			if (tile instanceof TileShaftStraight) {
				EnumFacing.Axis axis = ((TileShaftStraight) tile).getFacing(EnumSide.FRONT).getAxis();
				switch (axis) {
				case X:
					return rayTrace(pos, start, end, SHAFT_X);
				case Y:
					return rayTrace(pos, start, end, SHAFT_Y);
				case Z:
					return rayTrace(pos, start, end, SHAFT_Z);
				}
			}
		return rayTrace(pos, start, end, hitbox);
	}
}
