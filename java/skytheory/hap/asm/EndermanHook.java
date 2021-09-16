package skytheory.hap.asm;

import defeatedcrow.hac.core.util.DCUtil;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import skytheory.hap.init.ItemsHaP;

public class EndermanHook {

	public static ItemStack WHITE_BLUE = new ItemStack(ItemsHaP.amulet_wu);

	// ここでtrueを返せば視線を合わせてもエンダーマンが敵対しない
	public static boolean cancelHostility(EntityEnderman enderman, EntityPlayer player) {
		if (DCUtil.hasCharmItem(player, WHITE_BLUE)) return true;
		return false;
	}

}