package skytheory.hap.util;

import java.util.ArrayList;
import java.util.List;

import defeatedcrow.hac.api.climate.DCAirflow;
import defeatedcrow.hac.api.climate.DCHeatTier;
import defeatedcrow.hac.api.climate.DCHumidity;
import defeatedcrow.hac.api.climate.IClimate;
import defeatedcrow.hac.api.recipe.IClimateSmelting;
import defeatedcrow.hac.api.recipe.RecipeAPI;
import defeatedcrow.hac.main.api.IHeatTreatment;
import defeatedcrow.hac.main.api.MainAPIManager;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;

public class SmeltingHelper {


	/**
	 *  材料と気候を基に気候精錬を試みる
	 *  完成品または空のスタックを返す
	 * @param stack
	 * @param climate
	 * @return smelting result or empty list
	 */
	public static ItemStack onClimateSmelting(ItemStack stack, IClimate climate) {
		IClimateSmelting crecipe = RecipeAPI.registerSmelting.getRecipe(() -> climate, stack);
		if (crecipe != null) {
			List<ItemStack> result = new ArrayList<>();
			// レシピの生産物を追加
			ItemStack output = crecipe.getOutput();
			if (!output.isEmpty()) {
				result.add(output);
			}
			return output;
		}
		return ItemStack.EMPTY;
	}

	/**
	 * 材料と気候を基に熱処理を試みる
	 * 完成品または空のスタックを返す
	 * @param stack
	 * @param climate
	 * @return
	 */
	public static ItemStack onHeatTreatment(ItemStack stack, IClimate climate) {
		IHeatTreatment hrecipe = MainAPIManager.heatTreatmentRegister.getRecipe(stack);
		if (hrecipe != null) {
			ActionResult<ItemStack> actionresult = hrecipe.getCurrentOutput(stack, climate);
			if (actionresult.getType() == EnumActionResult.SUCCESS) {
				return actionresult.getResult();
			}
		}
		return ItemStack.EMPTY;
	}


	/**
	 * 材料と気候を基に精錬を試みる
	 * 完成品または空のスタックを返す
	 * @param stack
	 * @param climate
	 * @return
	 */
	public static ItemStack onVannilaSmelting(ItemStack stack, IClimate climate) {
		// 湿度がUNDERWATERでない場合のみ精錬する
		if (climate.getHumidity() != DCHumidity.UNDERWATER) {
			ItemStack resultstack = FurnaceRecipes.instance().getSmeltingResult(stack).copy();
			if (!resultstack.isEmpty()) {
				// 完成品が食品の場合、温度がOVEN以上であれば精錬する
				if (resultstack.getItem() instanceof ItemFood) {
					if (climate.getHeat().getTier() >= DCHeatTier.OVEN.getTier()) {
						return resultstack;
					}
					// それ以外はTIGHTかつSMELTINGを要求する
				} else {
					if (climate.getAirflow() == DCAirflow.TIGHT &&
							climate.getHeat().getTier() >= DCHeatTier.SMELTING.getTier()) {
						return resultstack;
					}
				}
			}
		}
		return ItemStack.EMPTY;
	}

	public static ItemStack onSmelting(ItemStack stack, IClimate climate) {
		ItemStack ingredient = stack.copy();
		ingredient.setCount(1);
		ItemStack cResult = onClimateSmelting(stack, climate);
		if (!cResult.isEmpty()) return cResult;
		ItemStack hResult = onHeatTreatment(stack, climate);
		if (!hResult.isEmpty()) return hResult;
		ItemStack vResult = onVannilaSmelting(stack, climate);
		if (!vResult.isEmpty()) return vResult;
		return ItemStack.EMPTY;
	}

	public static boolean canSmelting(ItemStack stack, IClimate climate) {
		ItemStack ingredient = stack.copy();
		ingredient.setCount(1);
		if (!onClimateSmelting(stack, climate).isEmpty()) return true;
		if (!onHeatTreatment(stack, climate).isEmpty()) return true;
		if (!onVannilaSmelting(stack, climate).isEmpty()) return true;
		return false;
	}
}
