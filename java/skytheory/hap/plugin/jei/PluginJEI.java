package skytheory.hap.plugin.jei;

import defeatedcrow.hac.core.plugin.DCsJEIPluginLists;
import defeatedcrow.hac.core.plugin.jei.DCsJEIPlugin;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import net.minecraft.item.ItemStack;
import skytheory.hap.gui.GuiReactor;
import skytheory.hap.init.ItemsHaP;

@JEIPlugin
public class PluginJEI implements IModPlugin {

	@Override
	public void register(IModRegistry registry) {
		DCsJEIPluginLists.reactors.add(new ItemStack(ItemsHaP.reactor_advanced));
		registry.addRecipeCatalyst(new ItemStack(ItemsHaP.reactor_advanced), DCsJEIPlugin.REACTOR_UID);
		registry.addRecipeClickArea(GuiReactor.class, 47, 57, 11, 20, DCsJEIPlugin.REACTOR_UID);
	}
}
