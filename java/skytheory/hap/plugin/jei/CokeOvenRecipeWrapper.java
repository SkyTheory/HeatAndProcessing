package skytheory.hap.plugin.jei;

import java.util.Arrays;
import java.util.Collections;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import skytheory.hap.recipe.CokeOvenRecipe;

public class CokeOvenRecipeWrapper implements IRecipeWrapper {

	public CokeOvenRecipe recipe;

	public CokeOvenRecipeWrapper (CokeOvenRecipe recipe) {
		this.recipe = recipe;
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputs(VanillaTypes.ITEM, Collections.singletonList(recipe.ingredient));
		ingredients.setOutputs(VanillaTypes.ITEM, Arrays.asList(recipe.result, recipe.secondary));
	}

	@Override
	public void drawInfo(Minecraft mc, int wid, int hei, int mouseX, int mouseY) {
		if (!recipe.secondary.isEmpty()) {
			mc.fontRenderer.drawString("20-100% ", 108, 2, 0x0099FF, false);
		}
	}
}
