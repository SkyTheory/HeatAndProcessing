package skytheory.hap.event;

import java.util.List;

import defeatedcrow.hac.config.CoreConfigDC;
import defeatedcrow.hac.core.client.AdvancedHUDEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiIngame;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import skytheory.hap.config.HaPConfig;

public class ClimateHUDEvent {

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void toggleHUD(RenderGameOverlayEvent.Post event) {
		if (CoreConfigDC.enableAdvHUD) {
			if (HaPConfig.hide_hud) {
				GuiIngame gui = Minecraft.getMinecraft().ingameGUI;
				List<ChatLine>lines = gui.getChatGUI().drawnChatLines;
				if (!lines.isEmpty()) {
					ChatLine line = lines.get(0);
					int counter = gui.updateCounter - line.getUpdatedCounter();
					if (counter < 200) {
						AdvancedHUDEvent.enable = false;
					} else {
						AdvancedHUDEvent.enable = true;
					}
				}
			}
		}
	}

}
