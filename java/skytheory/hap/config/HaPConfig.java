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
	public static final String CATEGORY_ENTITY = "entity";
	public static final String CATEGORY_HUD = "hud";
	public static final String CATEGORY_RECIPE = "recipe";

	public static Configuration CONFIG;

	public static final String DESC_SHRINK_SHAFT = "Shrink shaft hitbox to center box size.";
	public static final String DESC_CHARM_EXTEND = "Search charm in all of inventory.";
	public static final String DESC_CHARM_MAX = "Max count of charms to apply.";
	public static final String DESC_HIDE_HUD = "Hide climate HUD while viewing chat log.";
	public static final String DESC_RECIPE_MILL = "Enable stone mill recipes from ore dictionary.";
	public static final String DESC_RECIPE_CRUSHER = "Enable hammer mill recipes from ore dictionary.";
	public static final String DESC_RECIPE_REACTOR = "Enable some alchemical reactor recipes.";


	public static boolean charm_extend;
	public static int charm_max;
	public static boolean shrink_shaft;
	public static boolean hide_hud;
	public static boolean recipe_mill;
	public static boolean recipe_crusher;
	public static boolean recipe_reactor;

	public static List<IConfigElement> getConfigElements() {
		List<IConfigElement> elements = new ArrayList<>();
		elements.add(new ConfigElement(CONFIG.get(CATEGORY_BLOCK, "ShrinkShaftHitBox", true, DESC_SHRINK_SHAFT)));
		elements.add(new ConfigElement(CONFIG.get(CATEGORY_CHARM, "ExtendCharmSearch", false, DESC_CHARM_EXTEND)));
		elements.add(new ConfigElement(CONFIG.get(CATEGORY_CHARM, "MaxCharms", 9, DESC_CHARM_MAX, 0, 27)));
		elements.add(new ConfigElement(CONFIG.get(CATEGORY_HUD, "AutoHideHUD", true, DESC_HIDE_HUD)));
		elements.add(new ConfigElement(CONFIG.get(CATEGORY_RECIPE, "ReactorRecipe", true, DESC_RECIPE_REACTOR).setRequiresMcRestart(true)));
		elements.add(new ConfigElement(CONFIG.get(CATEGORY_RECIPE, "StoneMillRecipe", true, DESC_RECIPE_MILL).setRequiresMcRestart(true)));
		elements.add(new ConfigElement(CONFIG.get(CATEGORY_RECIPE, "HammerMillRecipe", true, DESC_RECIPE_CRUSHER).setRequiresMcRestart(true)));
		return elements;
	}

	public static void init(Configuration cfg) {
		CONFIG = cfg;
		CONFIG.load();
		read();
		if (cfg.hasChanged()) {
			save();
		}
	}

	public static void read() {
		charm_extend = CONFIG.getBoolean("ExtendCharmSearch", CATEGORY_CHARM, false, DESC_CHARM_EXTEND, ConstantsHaP.CFG_EXTEND);
		charm_max = CONFIG.getInt("MaxCharms", CATEGORY_CHARM, 9, 0, 27, DESC_CHARM_MAX, ConstantsHaP.CFG_COUNT);
		shrink_shaft = CONFIG.getBoolean("ShrinkShaftHitBox", CATEGORY_BLOCK, true, DESC_SHRINK_SHAFT, ConstantsHaP.CFG_SHAFT);
		hide_hud = CONFIG.getBoolean("AutoHideHUD", CATEGORY_HUD, true, DESC_HIDE_HUD, ConstantsHaP.CFG_HUD);
		recipe_reactor = CONFIG.getBoolean("ReactorRecipe", CATEGORY_RECIPE, true, DESC_RECIPE_REACTOR, ConstantsHaP.CFG_RECIPE);
		recipe_mill = CONFIG.getBoolean("StoneMillRecipe", CATEGORY_RECIPE, true, DESC_RECIPE_MILL, ConstantsHaP.CFG_RECIPE);
		recipe_crusher = CONFIG.getBoolean("HammerMillRecipe", CATEGORY_RECIPE, true, DESC_RECIPE_CRUSHER, ConstantsHaP.CFG_RECIPE);
		if (!hide_hud) AdvancedHUDEvent.enable = CoreConfigDC.enableAdvHUD;
	}

	public static void save() {
		CONFIG.save();
	}

}