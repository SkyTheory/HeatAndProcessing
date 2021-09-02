package skytheory.hap.renderer.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import skytheory.hap.HeatAndProcessing;
import skytheory.hap.model.ModelMistDispenser;
import skytheory.hap.tile.TileMistDispenser;
import skytheory.lib.renderer.ITileModel;
import skytheory.lib.renderer.TileRendererFromRotation;

public class RenderMistDispenser extends TileRendererFromRotation<TileMistDispenser> {

	public static final ResourceLocation TEXTURE_ON = new ResourceLocation(HeatAndProcessing.MOD_ID, "textures/tile/mist_dispenser_on.png");
	public static final ResourceLocation TEXTURE_OFF = new ResourceLocation(HeatAndProcessing.MOD_ID, "textures/tile/mist_dispenser_off.png");
	public static final ModelMistDispenser MODEL = new ModelMistDispenser();

	@Override
	public ResourceLocation getTexture(TileMistDispenser tile) {
		if (tile != null && !tile.isActive) return TEXTURE_OFF;
		return TEXTURE_ON;
	}

	@Override
	public ResourceLocation getTexture(ItemStack stack) {
		return TEXTURE_ON;
	}

	@Override
	public ITileModel<TileMistDispenser> getModel(TileMistDispenser tile) {
		return MODEL;
	}

}
