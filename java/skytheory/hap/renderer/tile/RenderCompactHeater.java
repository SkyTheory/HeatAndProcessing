package skytheory.hap.renderer.tile;

import defeatedcrow.hac.api.climate.DCHeatTier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import skytheory.hap.HeatAndProcessing;
import skytheory.hap.model.ModelCompactHeater;
import skytheory.hap.tile.TileCompactHeater;
import skytheory.lib.renderer.ITileModel;
import skytheory.lib.renderer.TileRendererFromRotation;

public class RenderCompactHeater extends TileRendererFromRotation<TileCompactHeater> {

	public static final ResourceLocation TEXTURE_OVEN = new ResourceLocation(HeatAndProcessing.MOD_ID, "textures/tile/compact_heater_oven.png");
	public static final ResourceLocation TEXTURE_KILN = new ResourceLocation(HeatAndProcessing.MOD_ID, "textures/tile/compact_heater_kiln.png");
	public static final ResourceLocation TEXTURE_SMELTING = new ResourceLocation(HeatAndProcessing.MOD_ID, "textures/tile/compact_heater_smelting.png");
	public static final ResourceLocation TEXTURE_UHT = new ResourceLocation(HeatAndProcessing.MOD_ID, "textures/tile/compact_heater_uht.png");
	public static final ModelCompactHeater MODEL = new ModelCompactHeater();

	@Override
	public ResourceLocation getTexture(TileCompactHeater tile) {
		if (tile != null) {
			DCHeatTier tier = tile.getHeatTier();
			if (tier == DCHeatTier.OVEN) return TEXTURE_OVEN;
			if (tier == DCHeatTier.KILN) return TEXTURE_KILN;
			if (tier == DCHeatTier.SMELTING) return TEXTURE_SMELTING;
			if (tier == DCHeatTier.UHT) return TEXTURE_UHT;
		}
		return TEXTURE_OVEN;
	}

	@Override
	public ResourceLocation getTexture(ItemStack stack) {
		return TEXTURE_OVEN;
	}

	@Override
	public ITileModel<TileCompactHeater> getModel(TileCompactHeater tile) {
		return MODEL;
	}

}
