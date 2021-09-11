package skytheory.hap.config;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.config.GuiConfig;
import skytheory.hap.HeatAndProcessing;

public class HaPConfigGuiScreen extends GuiConfig {
	public HaPConfigGuiScreen(GuiScreen parent) {
		super(parent, HaPConfig.getConfigElements(), HeatAndProcessing.MOD_ID, false, false, I18n.format("hap.tip.config"));
	}

	@Override
	public void onGuiClosed() {
		if (HaPConfig.CONFIG.hasChanged()) HaPConfig.save();
		super.onGuiClosed();
	}
}
