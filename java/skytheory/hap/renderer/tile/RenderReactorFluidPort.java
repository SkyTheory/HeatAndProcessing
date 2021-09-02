package skytheory.hap.renderer.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import skytheory.hap.HeatAndProcessing;
import skytheory.hap.model.ModelReactorFluidPort;
import skytheory.hap.tile.TileReactorFluidPort;
import skytheory.lib.renderer.ITileModel;
import skytheory.lib.renderer.TileRendererFromState;

public class RenderReactorFluidPort extends TileRendererFromState<TileReactorFluidPort> {

	public static ResourceLocation TEXTURE = new ResourceLocation(HeatAndProcessing.MOD_ID, "textures/tile/reactor_fluid_port.png");

	@Override
	public ResourceLocation getTexture(TileReactorFluidPort tile) {
		return TEXTURE;
	}

	@Override
	public ResourceLocation getTexture(ItemStack stack) {
		return TEXTURE;
	}

	@Override
	public ITileModel<TileReactorFluidPort> getModel(TileReactorFluidPort tile) {
		return ModelReactorFluidPort.INSTANCE;
	}

}
