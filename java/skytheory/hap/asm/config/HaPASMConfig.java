package skytheory.hap.asm.config;

import net.minecraftforge.common.config.Configuration;
import skytheory.hap.util.ConstantsHaP;

public class HaPASMConfig {

	public static final String CATEGORY_ASM = "asm";

	public static final String DESC_ASM_ENDERMAN = "Allow to use ASM for Enderman. If this set to false, some amulet effects will not work.";
	public static final String DESC_ASM_CHARM = "Allow to use ASM for charm. If this set to false, the charm settings will not work.";
	public static final String DESC_ASM_LUMBER = "Allow to use ASM for Green Gold Pendant. This is experimental settings and it is NOT ensured!";

	public static boolean asm_charm;
	public static boolean asm_enderman;
	public static boolean asm_lumber;

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
		asm_enderman = CONFIG.getBoolean("AllowASMforEnderman", CATEGORY_ASM, true, DESC_ASM_ENDERMAN, ConstantsHaP.CFG_ASM_ENDERMAN);
		asm_charm = CONFIG.getBoolean("AllowASMforCharms", CATEGORY_ASM, true, DESC_ASM_CHARM, ConstantsHaP.CFG_ASM_CHARM);
		asm_lumber = CONFIG.getBoolean("AllowASMforLumber", CATEGORY_ASM, false, DESC_ASM_LUMBER, ConstantsHaP.CFG_ASM_CHARM);
	}

	public static void save() {
		CONFIG.save();
	}

}
