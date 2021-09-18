package skytheory.hap.recipe;

import java.util.ArrayList;
import java.util.List;

import defeatedcrow.hac.api.recipe.IMillRecipe;
import defeatedcrow.hac.api.recipe.RecipeAPI;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;
import skytheory.lib.SkyTheoryLib;

// OreDictioanryからStoneMillRecipeを自動生成して登録する
public class StoneMillRecipes {

	public static void register() {
		String[] allId = OreDictionary.getOreNames();
		List<String> oreNamesRaw = new ArrayList<>();
		List<String> oreNames = new ArrayList<>();
		List<String> gemNames = new ArrayList<>();

		for (String name : allId) {
			if (name.startsWith("ore")) {
				oreNamesRaw.add(name);
			}
		}

		for (String oreName : oreNamesRaw) {
			String gemName = oreName.replaceFirst("ore", "gem");
			if (OreDictionary.doesOreNameExist(gemName)) {
				if (!oreNames.contains(oreName)) oreNames.add(oreName);
				gemNames.add(gemName);
			}
			String dustName = oreName.replaceFirst("ore", "dust");
			if (OreDictionary.doesOreNameExist(dustName)) {
				if (!oreNames.contains(oreName)) oreNames.add(oreName);
			}
		}

		// 覚書：エイリアスとかどっかになかったっけ？
		oreNames.remove("oreAluminium");

		// 覚書：ExU2のgemRedstoneを除外
		gemNames.remove("gemRedstone");

		List<String> oreToGemNames = new ArrayList<>(oreNames);
		List<String> oreToDustNames = new ArrayList<>(oreNames);
		List<String> gemToDustNames = new ArrayList<>(gemNames);

		// 既に対応するレシピがあるなら除外する
		List<IMillRecipe> recipes = RecipeAPI.registerMills.getRecipeList();
		for (IMillRecipe recipe : recipes) {
			Object inputObj = recipe.getInput();
			if (inputObj instanceof String) {
				String input = (String) inputObj;
				oreToGemNames.remove(input);
				oreToDustNames.remove(input);
				gemToDustNames.remove(input);
			}
		}

		for (String gemName : gemToDustNames) {
			oreToDustNames.remove(gemName.replaceFirst("gem", "ore"));
		}

		// ore*とgem*をIMillRecipeとして登録する
		for (String oreName : oreToGemNames) {
			NonNullList<ItemStack> ores = OreDictionary.getOres(oreName, false);
			NonNullList<ItemStack> gems = OreDictionary.getOres(oreName.replaceFirst("ore", "gem"), false);
			if (!ores.isEmpty() && !gems.isEmpty()) {
				SkyTheoryLib.LOGGER.info(String.format("Register ore to gem recipe for Stone Mill: %s", oreName.substring(3)));
				ItemStack gem = gems.get(0).copy();
				gem.setCount(2);
				RecipeAPI.registerMills.addRecipe(gem, oreName);
			}
		}

		// ore*とdust*をIMillRecipeとして登録する
		for (String oreName : oreToDustNames) {
			NonNullList<ItemStack> ores = OreDictionary.getOres(oreName, false);
			NonNullList<ItemStack> dusts = OreDictionary.getOres(oreName.replaceFirst("ore", "dust"), false);
			if (!ores.isEmpty() && !dusts.isEmpty()) {
				SkyTheoryLib.LOGGER.info(String.format("Register ore to dust recipe for Stone Mill: %s", oreName.substring(3)));
				ItemStack dust = dusts.get(0).copy();
				dust.setCount(2);
				RecipeAPI.registerMills.addRecipe(dust, oreName);
			}
		}

		// gem*とdust*をIMillRecipeとして登録する
		for (String gemName : gemToDustNames) {
			NonNullList<ItemStack> gems = OreDictionary.getOres(gemName, false);
			NonNullList<ItemStack> dusts = OreDictionary.getOres(gemName.replaceFirst("gem", "dust"), false);
			if (!gems.isEmpty() && !dusts.isEmpty()) {
				SkyTheoryLib.LOGGER.info(String.format("Register gem to dust recipe for Stone Mill: %s", gemName.substring(3)));
				ItemStack dust = dusts.get(0).copy();
				RecipeAPI.registerMills.addRecipe(dust, gemName);
			}
		}
	}
}
