package skytheory.hap.renderer.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import skytheory.hap.HeatAndProcessing;
import skytheory.hap.model.ModelReactorStorage;
import skytheory.hap.tile.TileReactorStorage;
import skytheory.lib.renderer.ITileModel;
import skytheory.lib.renderer.TileRendererFromState;

public class RenderReactorStorage extends TileRendererFromState<TileReactorStorage> {

	public static ResourceLocation TEXTURE = new ResourceLocation(HeatAndProcessing.MOD_ID, "textures/tile/reactor_storage.png");

	@Override
	public ResourceLocation getTexture(TileReactorStorage tile) {
		return TEXTURE;
	}

	@Override
	public ResourceLocation getTexture(ItemStack stack) {
		return TEXTURE;
	}

	@Override
	public ITileModel<TileReactorStorage> getModel(TileReactorStorage tile) {
		return ModelReactorStorage.INSTANCE;
	}

}
