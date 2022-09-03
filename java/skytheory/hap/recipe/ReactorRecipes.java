package skytheory.hap.recipe;

import java.util.Arrays;
import java.util.List;

import defeatedcrow.hac.api.climate.DCHeatTier;
import defeatedcrow.hac.api.recipe.IReactorRecipe;
import defeatedcrow.hac.api.recipe.RecipeAPI;
import defeatedcrow.hac.food.FoodInit;
import defeatedcrow.hac.magic.MagicInit;
import defeatedcrow.hac.main.MainInit;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import skytheory.hap.init.ItemsHaP;
import skytheory.lib.SkyTheoryLib;

public class ReactorRecipes {

	// モリブデンコート剤
	public static IReactorRecipe coating_molybdenum = new ReactorRecipeBuilder()
			.outputItem(new ItemStack(ItemsHaP.coating_molybdenum))
			.inputItem(
					OreDictionary.getOres("dustMolybdenum"),
					OreDictionary.getOres("dustSulfur"))
			.inputFluid(MainInit.fuelOil, 100)
			.heatTier(DCHeatTier.NORMAL)
			.build();

	// 真鍮と硫黄と鉛を黄金にする マグナム・オパス・イン・"アル"ケミカルリアクター！
	public static IReactorRecipe alchemic_conversion = new ReactorRecipeBuilder()
			.outputItem(new ItemStack(MainInit.oreItem, 1, 8))
			.inputItem(
					OreDictionary.getOres("ingotBrass"),
					OreDictionary.getOres("ingotLead"),
					OreDictionary.getOres("dustSulfur"),
					OreDictionary.getOres("dustMana")
					)
			.catalyst(new ItemStack(MagicInit.timeCage, 1, 0), new ItemStack(MagicInit.timeCage, 1, 1), new ItemStack(MagicInit.timeCage, 1, 2), new ItemStack(MagicInit.timeCage, 1, 3))
			.heatTier(Arrays.asList(DCHeatTier.FROSTBITE, DCHeatTier.SMELTING))
			.build();

	// 白キューブのアナザーレシピ
	public static IReactorRecipe activate_white = new ReactorRecipeBuilder()
			.outputItem(new ItemStack(MagicInit.colorCube, 1, 9))
			.inputItem(
					OreDictionary.getOres("dropWhite"),
					OreDictionary.getOres("extractWhite"),
					OreDictionary.getOres("oreWhite"),
					OreDictionary.getOres("oreLargeWhite")
					)
			.inputFluid(MainInit.steam, 200)
			.catalyst(new ItemStack(MagicInit.colorCube, 1, 9))
			.heatTier(DCHeatTier.COLD)
			.build();

	// 青キューブのアナザーレシピ
	public static IReactorRecipe activate_blue = new ReactorRecipeBuilder()
			.outputItem(new ItemStack(MagicInit.colorCube, 1, 5))
			.inputItem(
					OreDictionary.getOres("dropBlue"),
					OreDictionary.getOres("extractBlue"),
					OreDictionary.getOres("oreBlue"),
					OreDictionary.getOres("oreLargeBlue")
					)
			.inputFluid(MainInit.hydrogen, 200)
			.catalyst(new ItemStack(MagicInit.colorCube, 1, 5))
			.heatTier(DCHeatTier.CRYOGENIC)
			.build();

	// 黒キューブのアナザーレシピ
	public static IReactorRecipe activate_black = new ReactorRecipeBuilder()
			.outputItem(new ItemStack(MagicInit.colorCube, 1, 8))
			.inputItem(
					OreDictionary.getOres("dropBlack"),
					OreDictionary.getOres("extractBlack"),
					OreDictionary.getOres("oreBlack"),
					OreDictionary.getOres("oreLargeBlack")
					)
			.inputFluid(MainInit.blackLiquor, 200)
			.catalyst(new ItemStack(MagicInit.colorCube, 1, 8))
			.heatTier(DCHeatTier.KILN)
			.build();

	// 赤キューブのアナザーレシピ
	public static IReactorRecipe activate_red = new ReactorRecipeBuilder()
			.outputItem(new ItemStack(MagicInit.colorCube, 1, 7))
			.inputItem(
					OreDictionary.getOres("dropRed"),
					OreDictionary.getOres("extractRed"),
					OreDictionary.getOres("oreRed"),
					OreDictionary.getOres("oreLargeRed")
					)
			.inputFluid(FoodInit.netherWine, 200)
			.catalyst(new ItemStack(MagicInit.colorCube, 1, 7))
			.heatTier(DCHeatTier.UHT)
			.build();

	// 緑キューブのアナザーレシピ
	public static IReactorRecipe activate_green = new ReactorRecipeBuilder()
			.outputItem(new ItemStack(MagicInit.colorCube, 1, 6))
			.inputItem(
					OreDictionary.getOres("dropGreen"),
					OreDictionary.getOres("extractGreen"),
					OreDictionary.getOres("oreGreen"),
					OreDictionary.getOres("oreLargeGreen")
					)
			.inputFluid(MainInit.greenTea, 200)
			.catalyst(new ItemStack(MagicInit.colorCube, 1, 6))
			.heatTier(DCHeatTier.WARM)
			.build();

	public static void register() {
		register(coating_molybdenum);
		register(alchemic_conversion);
		register(activate_white);
		register(activate_blue);
		register(activate_black);
		register(activate_red);
		register(activate_green);
	}

	public static void register(IReactorRecipe recipe) {
		if (validateRecipe(recipe)) {
			RecipeAPI.registerReactorRecipes.addRecipe(recipe);
		} else {
			SkyTheoryLib.LOGGER.warn(String.format("Reactor recipe registy skipped: %s", recipe.getOutput()));
		}
	}

	public static boolean validateRecipe(IReactorRecipe recipe) {
		for (Object ingredient : recipe.getProcessedInput()) {
			if (!validateReactorRecipe(ingredient)) return false;
		}
		if (recipe.getOutput() == null || recipe.getOutput().isEmpty()) {
			if (recipe.getOutputFluid() == null) return false;
		}
		return true;
	}

	public static boolean validateReactorRecipe(Object ingredient) {
		if (ingredient instanceof ItemStack) return true;
		if (ingredient instanceof String) {
			String ore = (String) ingredient;
			if (OreDictionary.getOres(ore).isEmpty()) return false;
			return true;
		}
		if (ingredient instanceof List) {
			@SuppressWarnings("unchecked")
			List<Object> ingredients = (List<Object>) ingredient;
			if (ingredients.isEmpty()) return false;
			for (Object item : ingredients) if (!validateReactorRecipe(item)) return false;
		}
		return true;
	}
}
