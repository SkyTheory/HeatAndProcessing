package skytheory.hap.gui;

import defeatedcrow.hac.main.ClimateMain;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import skytheory.hap.container.ContainerCokeOven;
import skytheory.hap.tile.TileCokeOven;
import skytheory.lib.gui.GuiBase;

public class GuiCokeOven extends GuiBase<ContainerCokeOven> {

	public static final ResourceLocation TEXTURE_REACTOR_GUI = new ResourceLocation(ClimateMain.MOD_ID, "textures/gui/chamber_main_gui.png");
	public static final ResourceLocation TEXTURE_ICON = new ResourceLocation("dcs_climate", "textures/gui/gui_icons.png");

	public static final int ICON_X = 75;
	public static final int ICON_Y = 22;
	public static final int ICON_U = 176;
	public static final int ICON_V1 = 0;
	public static final int ICON_V2 = 26;
	public static final int BAR_X = 72;
	public static final int BAR_Y = 51;
	public static final int BAR_U = 202;
	public static final int BAR_V = 0;

	public static final int GUI_WIDTH = 176;
	public static final int GUI_HEIGHT = 166;

	private TileCokeOven tile;

	public GuiCokeOven(EntityPlayer player, TileCokeOven tile) {
		super(new ContainerCokeOven(player, tile), GUI_WIDTH, GUI_HEIGHT, TEXTURE_REACTOR_GUI);
		this.tile = tile;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		if (tile.state > 0) {
			int u = ICON_U;
			int v = ICON_V2;
			if (tile.state > 1) v = ICON_V1;
			this.drawTexturedModalRect(guiLeft + ICON_X, guiTop + ICON_Y, u, v, 26, 26);
			this.drawTexturedModalRect(guiLeft + BAR_X, guiTop + BAR_Y, BAR_U, BAR_V, getProgressBarLength(), 3);
		}
	}

	private int getProgressBarLength() {
		float rate = ((float) tile.progress) / ((float) TileCokeOven.TOTAL_COUNT);
		return MathHelper.ceil(32.0f * rate);
	}

}
