package skytheory.hap.renderer.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import skytheory.hap.HeatAndProcessing;
import skytheory.hap.model.ModelReactorAdvanced;
import skytheory.hap.tile.TileReactorAdvanced;
import skytheory.lib.renderer.ITileModel;
import skytheory.lib.renderer.TileRendererFromState;

public class RenderReactorAdvanced extends TileRendererFromState<TileReactorAdvanced> {

	public static ResourceLocation TEXTURE = new ResourceLocation(HeatAndProcessing.MOD_ID, "textures/tile/reactor_advanced.png");

	@Override
	public ResourceLocation getTexture(TileReactorAdvanced tile) {
		return TEXTURE;
	}

	@Override
	public ResourceLocation getTexture(ItemStack stack) {
		return TEXTURE;
	}

	@Override
	public ITileModel<TileReactorAdvanced> getModel(TileReactorAdvanced tile) {
		return ModelReactorAdvanced.INSTANCE;
	}

}
