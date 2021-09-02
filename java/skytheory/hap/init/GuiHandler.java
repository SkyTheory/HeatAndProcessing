package skytheory.hap.init;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import skytheory.hap.container.ContainerReactor;
import skytheory.hap.container.ContainerReactorStorage;
import skytheory.hap.gui.GuiReactor;
import skytheory.hap.gui.GuiReactorStorage;
import skytheory.hap.tile.TileReactorAdvanced;
import skytheory.hap.tile.TileReactorStorage;

public class GuiHandler implements IGuiHandler {

	public static final IGuiHandler INSTANCE = new GuiHandler();

	public static final int TILE_GUI = 0;

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		if (id == TILE_GUI) {
			TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
			if (tile instanceof TileReactorAdvanced) {
				return new ContainerReactor(player, (TileReactorAdvanced) tile);
			}
			if (tile instanceof TileReactorStorage) {
				return new ContainerReactorStorage(player, (TileReactorStorage) tile);
			}
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		if (id == TILE_GUI) {
			TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
			if (tile instanceof TileReactorAdvanced) {
				return new GuiReactor(player, (TileReactorAdvanced) tile);
			}
			if (tile instanceof TileReactorStorage) {
				return new GuiReactorStorage(player, (TileReactorStorage) tile);
			}
		}
		return null;
	}

}
