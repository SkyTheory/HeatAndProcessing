package skytheory.hap.config;

import java.util.ArrayList;
import java.util.List;

import defeatedcrow.hac.config.CoreConfigDC;
import defeatedcrow.hac.core.client.AdvancedHUDEvent;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.IConfigElement;
import skytheory.hap.util.ConstantsHaP;

public class HaPConfig {

	public static final String CATEGORY_CHARM = "charm";
	public static final String CATEGORY_BLOCK = "block";
	public static final String CATEGORY_HUD = "hud";
	public static final String CATEGORY_RECIPE = "recipe";

	public static Configuration CONFIG;

	public static boolean asm_charm;
	public static boolean charm_extend;
	public static int charm_max;
	public static boolean shrink_shaft;
	public static boolean hide_hud;
	public static boolean recipe_reactor;

	public static List<IConfigElement> getConfigElements() {
		List<IConfigElement> elements = new ArrayList<>();
		elements.add(new ConfigElement(CONFIG.get(CATEGORY_BLOCK, "ShrinkShaftHitBox", true, "Shrink shaft hitbox to center box size.")));
		elements.add(new ConfigElement(CONFIG.get(CATEGORY_CHARM, "AllowASMforCharms", false, "Allow to use ASM for charm. If this set to false, the charm settings will not work.").setRequiresMcRestart(true)));
		elements.add(new ConfigElement(CONFIG.get(CATEGORY_CHARM, "ExtendCharmSearch", false, "Search charm in all of inventory.")));
		elements.add(new ConfigElement(CONFIG.get(CATEGORY_CHARM, "MaxCharms", 9, "Max count of charms to apply.", 0, 27)));
		elements.add(new ConfigElement(CONFIG.get(CATEGORY_HUD, "AutoHideHUD", true, "Hide climate HUD while viewing chat log.")));
		elements.add(new ConfigElement(CONFIG.get(CATEGORY_RECIPE, "ReactorRecipe", true, "Enable some alchemical reactor recipes.").setRequiresMcRestart(true)));
		return elements;
	}

	public static void init(Configuration cfg) {
		CONFIG = cfg;
		CONFIG.load();
		read();
		save();
	}

	public static void read() {
		asm_charm  = CONFIG.getBoolean("AllowASMforCharms", CATEGORY_CHARM, false, "Allow to use ASM for charm. If this set to false, the charm settings will not work.", ConstantsHaP.DESC_ASM_CHARM);
		charm_extend = CONFIG.getBoolean("ExtendCharmSearch", CATEGORY_CHARM, false, "Search charm in all of inventory.", ConstantsHaP.DESC_EXTEND);
		charm_max = CONFIG.getInt("MaxCharms", CATEGORY_CHARM, 9, 0, 27, "Max count of charms to apply.", ConstantsHaP.DESC_COUNT);
		shrink_shaft = CONFIG.getBoolean("ShrinkShaftHitBox", CATEGORY_BLOCK, true, "Shrink shaft hitbox to center box size.", ConstantsHaP.DESC_SHAFT);
		hide_hud = CONFIG.getBoolean("AutoHideHUD", CATEGORY_HUD, true, "Hide climate HUD while viewing chat log.", ConstantsHaP.DESC_HUD);
		recipe_reactor = CONFIG.getBoolean("ReactorRecipe", CATEGORY_RECIPE, true, "Enable some alchemical reactor recipes.", ConstantsHaP.DESC_RECIPE);

		if (!hide_hud) AdvancedHUDEvent.enable = CoreConfigDC.enableAdvHUD;
	}

	public static void save() {
		CONFIG.save();
	}

}