package skytheory.hap.util;

import defeatedcrow.hac.api.climate.DCHeatTier;
import defeatedcrow.hac.api.climate.DCHumidity;
import net.minecraft.util.text.TextFormatting;

public class ClimateTextHelper {

	public static String getHeatText(DCHeatTier heat) {
		String l = heat.localize();
		if (heat.getTier() < 0) {
			return TextFormatting.AQUA + l + TextFormatting.RESET;
		} else if (heat.getTier() > 0) {
			return TextFormatting.GOLD + l + TextFormatting.RESET;
		}
		return l;
	}

	public static String getHumidityText(DCHumidity humidity) {
		String l = humidity.localize();
		if (humidity == DCHumidity.DRY) return TextFormatting.DARK_RED + l + TextFormatting.RESET;
		if (humidity == DCHumidity.NORMAL) return TextFormatting.YELLOW + l + TextFormatting.RESET;
		if (humidity == DCHumidity.WET) return TextFormatting.AQUA + l + TextFormatting.RESET;
		if (humidity == DCHumidity.UNDERWATER) return TextFormatting.BLUE + l + TextFormatting.RESET;
		return l;
	}
}
