package skytheory.hap.creativetab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import skytheory.hap.HeatAndProcessing;
import skytheory.hap.init.ItemsHaP;

public class CreativeTabsHaP {

	public static final CreativeTabs MAIN = new CreativeTabs(HeatAndProcessing.MOD_ID) {
		@Override
		public ItemStack getTabIconItem() {
			ItemStack icon = new ItemStack(ItemsHaP.wrench);
			return icon;
		}
	};

}
