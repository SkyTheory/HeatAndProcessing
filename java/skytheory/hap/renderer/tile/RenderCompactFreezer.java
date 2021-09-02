package skytheory.hap.renderer.tile;

import defeatedcrow.hac.api.climate.DCHeatTier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import skytheory.hap.HeatAndProcessing;
import skytheory.hap.model.ModelCompactFreezer;
import skytheory.hap.tile.TileCompactFreezer;
import skytheory.lib.renderer.ITileModel;
import skytheory.lib.renderer.TileRendererFromRotation;

public class RenderCompactFreezer extends TileRendererFromRotation<TileCompactFreezer> {

	public static final ResourceLocation TEXTURE_COLD = new ResourceLocation(HeatAndProcessing.MOD_ID, "textures/tile/compact_freezer_cold.png");
	public static final ResourceLocation TEXTURE_FROSTBITE = new ResourceLocation(HeatAndProcessing.MOD_ID, "textures/tile/compact_freezer_frostbite.png");
	public static final ResourceLocation TEXTURE_CRYOGENIC = new ResourceLocation(HeatAndProcessing.MOD_ID, "textures/tile/compact_freezer_cryogenic.png");
	public static final ResourceLocation TEXTURE_AVSOLUTE = new ResourceLocation(HeatAndProcessing.MOD_ID, "textures/tile/compact_freezer_absolute.png");
	public static final ModelCompactFreezer MODEL = new ModelCompactFreezer();

	@Override
	public ResourceLocation getTexture(TileCompactFreezer tile) {
		if(tile != null) {
			if (tile.getHeatTier() == DCHeatTier.COLD) return TEXTURE_COLD;
			if (tile.getHeatTier() == DCHeatTier.FROSTBITE) return TEXTURE_FROSTBITE;
			if (tile.getHeatTier() == DCHeatTier.CRYOGENIC) return TEXTURE_CRYOGENIC;
			if (tile.getHeatTier() == DCHeatTier.ABSOLUTE) return TEXTURE_AVSOLUTE;
		}
		return TEXTURE_COLD;
	}

	@Override
	public ITileModel<TileCompactFreezer> getModel(TileCompactFreezer tile) {
		return MODEL;
	}

	@Override
	public ResourceLocation getTexture(ItemStack stack) {
		return TEXTURE_COLD;
	}

}
