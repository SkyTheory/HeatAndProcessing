package skytheory.hap.plugin.jei;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import skytheory.hap.HeatAndProcessing;
import skytheory.hap.recipe.CokeOvenRecipe;

public class CokeOvenRecipeCategory implements IRecipeCategory<CokeOvenRecipe> {

	private final IDrawableStatic background;
	public static final ResourceLocation TEXTURE_JEI_COKEOVEN = new ResourceLocation(HeatAndProcessing.MOD_ID, "textures/gui/jei_cokeoven.png");

	public CokeOvenRecipeCategory(IJeiHelpers helpers) {
		background = helpers.getGuiHelper().createDrawable(TEXTURE_JEI_COKEOVEN, 0, 0, 150, 40);
	}

	@Override
	public String getUid() {
		return JeiPlugin.UID_COKEOVEN;
	}

	@Override
	public String getTitle() {
		return I18n.format(JeiPlugin.UID_COKEOVEN);
	}

	@Override
	public String getModName() {
		return HeatAndProcessing.MOD_NAME;
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, CokeOvenRecipe recipe, IIngredients ingredients) {
		recipeLayout.getItemStacks().init(0, true, 43, 11);
		recipeLayout.getItemStacks().set(0, recipe.ingredient);

		recipeLayout.getItemStacks().init(1, false, 95, 11);
		recipeLayout.getItemStacks().set(1, recipe.result);

		recipeLayout.getItemStacks().init(2, false, 115, 11);
		recipeLayout.getItemStacks().set(2, recipe.secondary);
	}

}
