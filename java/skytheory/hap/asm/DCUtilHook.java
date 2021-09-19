package skytheory.hap.asm;

import defeatedcrow.hac.api.magic.CharmType;
import defeatedcrow.hac.api.magic.IJewelCharm;
import defeatedcrow.hac.core.plugin.baubles.DCPluginBaubles;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.Loader;
import skytheory.hap.config.HaPConfig;

public class DCUtilHook {

	/*
	 * 置き換え先のメソッド
	 * インベントリソート系のModがかなーり使いづらかったので……。
	 * 余談：OPって良く言われてるらしいけれど、装備コストは相応だと思うけれどなあ？
	 * 比較的強力な銀指輪が、他Modで銀をショートカットできる時の作成コストが低いのが原因だとは思う
	 */
	public static NonNullList<ItemStack> playerCharmHook(EntityPlayer player, CharmType type) {
		NonNullList<ItemStack> charms = NonNullList.create();
		int range = HaPConfig.charm_extend ? 36 : 18;
		if (player == null) return charms;
		for (int index = 9, count = 0; index < range && count < HaPConfig.charm_max; index++) {
			ItemStack stack = player.inventory.getStackInSlot(index);
			if (!stack.isEmpty() && stack.getItem() instanceof IJewelCharm) {
				if (type == null || type.equals(((IJewelCharm) stack.getItem()).getCharmType(stack.getItemDamage()))) {
					addCharm(charms, stack);
					count += stack.getCount();
				}
			}
		}

		if (Loader.isModLoaded("baubles")) {
			NonNullList<ItemStack> baubles = DCPluginBaubles.getBaublesCharm(player, type);
			if (!baubles.isEmpty()) {
				baubles.stream().filter(s -> s.getItem() instanceof IJewelCharm).forEach(charm -> addCharm(charms, charm));
			}
		}

		return charms;
	}

	private static void addCharm(NonNullList<ItemStack> charms, ItemStack stack) {
		ItemStack charm = stack.copy();
		ItemStack match = charms.stream().filter(charm::isItemEqual).findFirst().orElse(ItemStack.EMPTY);
		if (!match.isEmpty()) {
			match.grow(charm.getCount());
		} else {
			charm = charm.copy();
			charms.add(charm);
		}
	}

}
