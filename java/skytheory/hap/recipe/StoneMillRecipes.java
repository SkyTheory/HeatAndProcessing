package skytheory.hap.recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;

import defeatedcrow.hac.api.recipe.IMillRecipe;
import defeatedcrow.hac.api.recipe.RecipeAPI;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import skytheory.hap.config.HaPConfig;
import skytheory.lib.SkyTheoryLib;

// OreDictioanryからStoneMillRecipeを自動生成して登録する
public class StoneMillRecipes {

	public static void register() {
		List<String> allDictionaryNames = Lists.newArrayList(OreDictionary.getOreNames());

		allDictionaryNames.removeAll(Arrays.asList(HaPConfig.recipe_ignore));

		// 既に対応するレシピがあるなら除外する
		for (IMillRecipe recipe : RecipeAPI.registerMills.getRecipeList()) {
			Object inputObj = recipe.getInput();
			if (inputObj instanceof String) {
				String input = (String) inputObj;
				allDictionaryNames.remove(input);
			}
		}

		List<String> oreNames = new ArrayList<>();
		List<String> gemNames = new ArrayList<>();
		List<String> ingotNames = new ArrayList<>();

		for (String name : allDictionaryNames) {
			if (name.startsWith("ore")) {
				oreNames.add(name);
			}
			if (name.startsWith("gem")) {
				gemNames.add(name);
			}
			if (name.startsWith("ingot")) {
				ingotNames.add(name);
			}
		}

		// Oreを素材とするレシピを登録する
		for (String oreName : oreNames) {
			if (OreDictionary.getOres(oreName, false).isEmpty()) continue;
			String gemName = oreName.replaceFirst("ore", "gem");
			List<ItemStack> gems = OreDictionary.getOres(gemName, false);
			if (!gems.isEmpty()) {
				ItemStack gem = gems.get(0).copy();
				gem.setCount(2);
				processRecipe(oreName, gem);
				// 覚書：Ore -> Gemを登録したならOre -> Dustはスキップ
				continue;
			}
			String dustName = oreName.replaceFirst("ore", "dust");
			List<ItemStack> dusts = OreDictionary.getOres(dustName, false);
			if (!dusts.isEmpty()) {
				ItemStack dust = dusts.get(0).copy();
				dust.setCount(2);
				processRecipe(oreName, dust);
			}
		}

		// Gemを素材とするレシピを登録する
		for (String gemName : gemNames) {
			if (OreDictionary.getOres(gemName, false).isEmpty()) continue;
			String dustName = gemName.replaceFirst("gem", "dust");
			List<ItemStack> dusts = OreDictionary.getOres(dustName, false);
			if (!dusts.isEmpty()) {
				ItemStack dust = dusts.get(0).copy();
				dust.setCount(1);
				processRecipe(gemName, dust);
			}
		}

		// Ingotを素材とするレシピを登録する
		for (String ingotName : ingotNames) {
			if (OreDictionary.getOres(ingotName, false).isEmpty()) continue;
			String dustName = ingotName.replaceFirst("ingot", "dust");
			List<ItemStack> dusts = OreDictionary.getOres(dustName, false);
			if (!dusts.isEmpty()) {
				ItemStack dust = dusts.get(0).copy();
				dust.setCount(1);
				processRecipe(ingotName, dust);
			}
		}
	}

	public static void processRecipe(String ingredients, ItemStack result) {
		SkyTheoryLib.LOGGER.info(String.format("Register %s to %s recipe for Stone Mill", ingredients, result.getUnlocalizedName()));
	}
}
