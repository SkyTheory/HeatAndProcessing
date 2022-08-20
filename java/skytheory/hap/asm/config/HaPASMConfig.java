package skytheory.hap.asm.config;

import net.minecraftforge.common.config.Configuration;

public class HaPASMConfig {

	public static final String CATEGORY_ASM = "asm";

	public static final String DESC_ASM_CHARM = "Allow to use ASM for charm. If this set to false, the charm settings will not work.";
	public static final String DESC_ASM_ENDERMAN = "Allow to use ASM for Enderman. If this set to false, some amulet effects will not work.";

	public static boolean asm_charm;
	public static boolean asm_enderman;
	public static boolean asm_tweaks;

	public static Configuration CONFIG;

	public static void init(Configuration cfg) {
		CONFIG = cfg;
		CONFIG.load();
		read();
		if (cfg.hasChanged()) {
			cfg.save();
		}
	}

	public static void read() {
		asm_charm = CONFIG.getBoolean("AllowASMforCharms", CATEGORY_ASM, true, DESC_ASM_CHARM);
		asm_enderman = CONFIG.getBoolean("AllowASMforEnderman", CATEGORY_ASM, true, DESC_ASM_ENDERMAN);
	}

	public static void save() {
		CONFIG.save();
	}

}