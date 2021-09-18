package skytheory.hap.asm;

import java.util.Set;

import defeatedcrow.hac.api.magic.MagicColor;
import defeatedcrow.hac.magic.item.ItemColorPendant2;
import defeatedcrow.hac.main.util.MainUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class ColorPendant2Hook {

	public static boolean cancelLumberjack(ItemColorPendant2 pendant, EntityLivingBase owner, BlockPos pos, IBlockState state, ItemStack charm) {
		if (pendant.getColor(charm.getItemDamage()) == MagicColor.GREEN) {
			Set<BlockPos> lumberList = MainUtil.getLumberTargetList(owner.world, pos, state.getBlock(), 192);
			if (lumberList.isEmpty()) {
				return true;
			}
		}
		return false;
	}
}
