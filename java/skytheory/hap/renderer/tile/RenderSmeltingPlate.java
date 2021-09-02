package skytheory.hap.renderer.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import skytheory.hap.model.ModelSmeltingPlate;
import skytheory.hap.tile.TileSmeltingPlate;
import skytheory.lib.renderer.ITileModel;
import skytheory.lib.renderer.TileRendererBase;

public class RenderSmeltingPlate extends TileRendererBase<TileSmeltingPlate> {

	public static ModelSmeltingPlate MODEL = new ModelSmeltingPlate();

	@Override
	public ResourceLocation getTexture(TileSmeltingPlate tile) {
		return null;
	}

	@Override
	public ResourceLocation getTexture(ItemStack stack) {
		return null;
	}

	@Override
	public ITileModel<TileSmeltingPlate> getModel(TileSmeltingPlate tile) {
		return MODEL;
	}
}
