package skytheory.hap.config;

import java.util.ArrayList;
import java.util.List;

import defeatedcrow.hac.config.CoreConfigDC;
import defeatedcrow.hac.core.client.AdvancedHUDEvent;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.IConfigElement;
import skytheory.hap.HeatAndProcessing;
import skytheory.hap.util.ConstantsHaP;
import skytheory.lib.SkyTheoryLib;

public class HaPConfig {

	public static boolean hide_hud;
	public static boolean shrink_shaft;
	public static boolean charm_extend;
	public static int charm_max;

	public static List<IConfigElement> getConfigElements() {
		List<IConfigElement> elements = new ArrayList<>();
		Configuration config = HeatAndProcessing.proxy.config;
		elements.addAll(new ConfigElement(config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements());
		return elements;
	}

	public static void readConfig() {
		Configuration config = HeatAndProcessing.proxy.config;
		try {
			config.load();
			initConfig(config);
		} catch (Exception e) {
			SkyTheoryLib.LOGGER.error(e);
		} finally {
			if (config.hasChanged()) {
				config.save();
			}
		}
	}

	public static void initConfig(Configuration cfg) {
		hide_hud = cfg.getBoolean("AutoHideHUD", Configuration.CATEGORY_GENERAL, true, I18n.format(ConstantsHaP.DESC_HUD));
		shrink_shaft = cfg.getBoolean("ShrinkShaftHitBox", Configuration.CATEGORY_GENERAL, true, I18n.format(ConstantsHaP.DESC_SHAFT));
		if (!hide_hud) {
			AdvancedHUDEvent.enable = CoreConfigDC.enableAdvHUD;
		}
		charm_extend = cfg.getBoolean("ExtendCharmSearch", Configuration.CATEGORY_GENERAL, false, I18n.format(ConstantsHaP.DESC_EXTEND));
		charm_max = cfg.getInt("MaxCharms", Configuration.CATEGORY_GENERAL, 9, 0, 27, I18n.format(ConstantsHaP.DESC_COUNT));
	}
}