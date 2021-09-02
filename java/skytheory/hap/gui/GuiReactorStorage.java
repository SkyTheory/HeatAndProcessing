package skytheory.hap.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import skytheory.hap.HeatAndProcessing;
import skytheory.hap.container.ContainerReactorStorage;
import skytheory.hap.tile.TileReactorStorage;
import skytheory.lib.gui.GuiBase;

public class GuiReactorStorage extends GuiBase<ContainerReactorStorage> {

	public static final ResourceLocation TEXTURE_REACTOR_GUI = new ResourceLocation(HeatAndProcessing.MOD_ID, "textures/gui/reactor_storage.png");
	public static final int GUI_WIDTH = 176;
	public static final int GUI_HEIGHT = 201;

	public GuiReactorStorage(EntityPlayer player, TileReactorStorage tile) {
		super(new ContainerReactorStorage(player, tile), GUI_WIDTH, GUI_HEIGHT, TEXTURE_REACTOR_GUI);
	}

}
