package skytheory.hap.config;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import skytheory.hap.HeatAndProcessing;

public class HaPConfigGuiScreen extends GuiConfig {
	public HaPConfigGuiScreen(GuiScreen parent) {
		super(parent, HaPConfig.getConfigElements(), HeatAndProcessing.MOD_ID, false, false, I18n.format("hap.tip.config"));
	}

	@Override
	public void onGuiClosed() {
		Configuration config = HeatAndProcessing.proxy.config;
		if (config.hasChanged()) {
			config.save();
			HaPConfig.initConfig(config);
		}
		super.onGuiClosed();
	}
}
