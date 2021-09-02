package skytheory.hap.block;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import defeatedcrow.hac.core.ClimateCore;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import skytheory.hap.config.HaPConfig;
import skytheory.hap.tile.TileShaftBase;

public abstract class BlockShaftBase extends BlockTorque {

	public static final AxisAlignedBB GEAR_BOX = new AxisAlignedBB(0.3125d, 0.3125d, 0.3125d, 0.6875d, 0.6875d, 0.6875d);

	public static final AxisAlignedBB SHAFT_DOWN = new AxisAlignedBB(0.3125d, 0.0d, 0.3125d, 0.6875d, 0.3125d, 0.6875d);
	public static final AxisAlignedBB SHAFT_UP = new AxisAlignedBB(0.3125d, 0.6875d, 0.3125d, 0.6875d, 1.0d, 0.6875d);
	public static final AxisAlignedBB SHAFT_NORTH = new AxisAlignedBB(0.3125d, 0.3125d, 0.0, 0.6875d, 0.6875d, 0.3125d);
	public static final AxisAlignedBB SHAFT_SOUTH = new AxisAlignedBB(0.3125d, 0.3125d, 0.6875d, 0.6875d, 0.6875d, 1.0d);
	public static final AxisAlignedBB SHAFT_WEST = new AxisAlignedBB(0.0d, 0.3125d, 0.3125d, 0.3125d, 0.6875d, 0.6875d);
	public static final AxisAlignedBB SHAFT_EAST = new AxisAlignedBB(0.6875d, 0.3125d, 0.3125d, 1.0d, 0.6875d, 0.6875d);

	public final int torqueTier;

	public BlockShaftBase(int torqueTier) {
		this.torqueTier = torqueTier;
	}

	@Override
	public RayTraceResult collisionRayTrace(IBlockState blockState, World worldIn, BlockPos pos, Vec3d start, Vec3d end) {
		AxisAlignedBB hitbox = HaPConfig.shrink_shaft ? GEAR_BOX : FULL_BLOCK_AABB;
		return rayTrace(pos, start, end, hitbox);
	}

	public static double getDistanceSq(Vec3d pos1, Vec3d pos2) {
		double xd = pos1.x - pos2.x;
		double yd = pos1.y - pos2.y;
		double zd = pos1.z - pos2.z;
		return xd * xd + yd * yd + zd * zd;
	}

	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
		getBoundingBoxes(state, worldIn, pos).forEach(box -> addCollisionBoxToList(pos, entityBox, collidingBoxes, box));
	}

	public static List<AxisAlignedBB> getBoundingBoxes(IBlockState state, World worldIn, BlockPos pos) {
		List<AxisAlignedBB> list = new ArrayList<>();
		TileEntity tile = worldIn.getTileEntity(pos);
		if (tile instanceof TileShaftBase) {
			TileShaftBase shaft = ((TileShaftBase) tile);
			list.add(GEAR_BOX);
			for (EnumFacing facing : EnumFacing.values()) {
				if (shaft.isInputSide(facing) || shaft.isOutputSide(facing)) {
					switch (facing) {
					case DOWN:
						list.add(SHAFT_DOWN);
						break;
					case UP:
						list.add(SHAFT_UP);
						break;
					case NORTH:
						list.add(SHAFT_NORTH);
						break;
					case SOUTH:
						list.add(SHAFT_SOUTH);
						break;
					case WEST:
						list.add(SHAFT_WEST);
						break;
					case EAST:
						list.add(SHAFT_EAST);
						break;
					}
				}
			}
		} else {
			list.add(FULL_BLOCK_AABB);
		}
		return list;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
		if (ClimateCore.proxy.isShiftKeyDown()) {
			tooltip.add(TextFormatting.YELLOW.toString() + TextFormatting.BOLD
					.toString() + "=== Transmission device ===");
			tooltip.add(String.format("Max torque: %d.0F", this.getMaxTorque()));
		} else {
			tooltip.add(TextFormatting.ITALIC.toString() + "=== Lshift key: expand tooltip ===");
		}
		tooltip.add(TextFormatting.BOLD.toString() + String.format("Tier %d", torqueTier));
	}

	private int getMaxTorque() {
		switch(torqueTier) {
		case 2:
			return 128;
		case 3:
			return 512;
		default:
			return Integer.MAX_VALUE;
		}
	}
}
