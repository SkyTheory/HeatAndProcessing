package skytheory.hap.plugin.jei;

import defeatedcrow.hac.core.plugin.DCsJEIPluginLists;
import defeatedcrow.hac.core.plugin.jei.DCsJEIPlugin;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;
import skytheory.hap.gui.GuiCokeOven;
import skytheory.hap.gui.GuiReactor;
import skytheory.hap.init.ItemsHaP;
import skytheory.hap.recipe.CokeOvenRecipes;

@JEIPlugin
public class JeiPlugin implements IModPlugin {

	public static final String UID_COKEOVEN = "hap.plugin.jei.coke_oven";

	@Override
	public void register(IModRegistry registry) {
		DCsJEIPluginLists.reactors.add(new ItemStack(ItemsHaP.reactor_advanced));
		// 覚書：レシピのタブのアイコンがAdvanced Reactorに変わる原因が判明するまで保留
//		registry.addRecipeCatalyst(new ItemStack(ItemsHaP.reactor_advanced), DCsJEIPlugin.REACTOR_UID);
		registry.addRecipeClickArea(GuiReactor.class, 47, 57, 11, 20, DCsJEIPlugin.REACTOR_UID);
		registry.addRecipes(CokeOvenRecipes.getRecipes(), UID_COKEOVEN);
		registry.addRecipeCatalyst(new ItemStack(ItemsHaP.coke_oven), UID_COKEOVEN);
		registry.addRecipeClickArea(GuiCokeOven.class, GuiCokeOven.BAR_X, GuiCokeOven.BAR_Y, 32, 3, UID_COKEOVEN);
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		IJeiHelpers helpers = registry.getJeiHelpers();
		registry.addRecipeCategories(new CokeOvenRecipeCategory(helpers));
	}

}
