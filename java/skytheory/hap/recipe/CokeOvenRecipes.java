package skytheory.hap.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;

import defeatedcrow.hac.machine.MachineInit;
import defeatedcrow.hac.main.MainInit;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class CokeOvenRecipes {

	private static final Map<String, CokeOvenRecipe> RECIPES_MAP = new HashMap<>();
	private static final List<CokeOvenRecipe> RECIPES = new ArrayList<>();

	public static void addRecipe(String id, CokeOvenRecipe recipe) {
		if (RECIPES_MAP.containsKey(id)) removeRecipe(id);
		RECIPES_MAP.put(id, recipe);
		RECIPES.add(recipe);
	}

	public static boolean removeRecipe(String id) {
		if (RECIPES_MAP.containsKey(id)) {
			RECIPES.remove(RECIPES_MAP.remove(id));
			return true;
		}
		return false;
	}

	public static CokeOvenRecipe getRecipe(String id) {
		return RECIPES_MAP.get(id);
	}

	public static CokeOvenRecipe getRecipe(IItemHandler input) {
		for (CokeOvenRecipe recipe : RECIPES) {
			if (recipe.canInput(input)) {
				return recipe;
			}
		}
		return null;
	}

	public static void register() {
		addRecipe("process_coal",
				new CokeOvenRecipe(
						new ItemStack(Items.COAL, 1, 0),
						new ItemStack(MachineInit.reagent, 1, 13),
						new ItemStack(MachineInit.reagent, 1, 0)
						)
				);
		addRecipe("process_charcoal",
				new CokeOvenRecipe(
						new ItemStack(Items.COAL, 2, 1),
						new ItemStack(MachineInit.reagent, 1, 13),
						new ItemStack(MachineInit.reagent, 1, 0)
						)
				);
		addRecipe("process_tar",
				new CokeOvenRecipe(
						new ItemStack(MachineInit.reagent, 1, 0),
						new ItemStack(MachineInit.reagent, 1, 13),
						new ItemStack(MainInit.miscDust, 1, 7)
						)
				);
		addRecipe("process_fuel_coke",
				new CokeOvenRecipe(
						new ItemStack(MachineInit.reagent, 1, 13),
						new ItemStack(MachineInit.reagent, 1, 9)
						)
				);

	}

	public static List<CokeOvenRecipe> getRecipes() {
		return ImmutableList.copyOf(RECIPES);
	}
}
