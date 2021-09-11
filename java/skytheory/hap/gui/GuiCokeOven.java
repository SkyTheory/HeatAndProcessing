package skytheory.hap.gui;

import defeatedcrow.hac.main.ClimateMain;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import skytheory.hap.container.ContainerCokeOven;
import skytheory.hap.tile.TileCokeOven;
import skytheory.lib.gui.GuiBase;

public class GuiCokeOven extends GuiBase<ContainerCokeOven> {

	public static final ResourceLocation TEXTURE_REACTOR_GUI = new ResourceLocation(ClimateMain.MOD_ID, "textures/gui/chamber_main_gui.png");
	public static final ResourceLocation TEXTURE_ICON = new ResourceLocation("dcs_climate", "textures/gui/gui_icons.png");
	public static final int GUI_WIDTH = 176;
	public static final int GUI_HEIGHT = 166;

	public GuiCokeOven(EntityPlayer player, TileCokeOven tile) {
		super(new ContainerCokeOven(player, tile), GUI_WIDTH, GUI_HEIGHT, TEXTURE_REACTOR_GUI);
	}

}
