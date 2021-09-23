package skytheory.hap.asm;

import java.util.Collections;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MainUtilHook {

	public static boolean hookLumberjack (World world, BlockPos origin, Block block) {
		for (EnumFacing facing : EnumFacing.values()) {
			if (world.getBlockState(origin.offset(facing)).getBlock() == block) {
				return false;
			}
		}
		return true;
	}

	public static Set<BlockPos> hookLumberjack (BlockPos origin) {
		return Collections.singleton(origin);
	}
}