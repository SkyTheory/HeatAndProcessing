package skytheory.hap.recipe;

import java.util.function.BooleanSupplier;

import com.google.gson.JsonObject;

import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;
import skytheory.hap.config.HaPConfig;

public class RecipeConditionFactory implements IConditionFactory {

	@Override
	public BooleanSupplier parse(JsonContext context, JsonObject json) {
		if (JsonUtils.hasField(json, "condition")) {
			String condition = JsonUtils.getString(json, "condition");
			if (condition.equals("dustSteel")) return () -> HaPConfig.recipe_mill || HaPConfig.recipe_crusher;
		}
		return () -> true;
	}

}
