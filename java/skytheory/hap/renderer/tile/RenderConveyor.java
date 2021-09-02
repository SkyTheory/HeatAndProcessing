package skytheory.hap.renderer.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import skytheory.hap.model.ModelConveyor;
import skytheory.hap.tile.TileConveyor;
import skytheory.lib.renderer.ITileModel;
import skytheory.lib.renderer.TileRendererBase;

public class RenderConveyor extends TileRendererBase<TileConveyor> {

	public static ModelConveyor MODEL = new ModelConveyor();

	@Override
	public ResourceLocation getTexture(TileConveyor tile) {
		return null;
	}

	@Override
	public ResourceLocation getTexture(ItemStack stack) {
		return null;
	}

	@Override
	public ITileModel<TileConveyor> getModel(TileConveyor tile) {
		return MODEL;
	}

}
