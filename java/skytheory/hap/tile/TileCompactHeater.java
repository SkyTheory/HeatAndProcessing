package skytheory.hap.tile;

import java.util.Arrays;
import java.util.List;

import defeatedcrow.hac.api.climate.DCHeatTier;

public class TileCompactHeater extends TileHeatController {

	@Override
	public List<DCHeatTier> getHeatTiers() {
		return Arrays.asList(DCHeatTier.OVEN, DCHeatTier.KILN, DCHeatTier.SMELTING, DCHeatTier.UHT);
	}

	@Override
	public float getTorqueRequired(DCHeatTier tier) {
		switch (tier) {
		case OVEN:
			return 8.0f;
		case KILN:
			return 16.0f;
		case SMELTING:
			return 32.0f;
		case UHT:
			return 64.0f;
		default :
			return 0.0f;
		}
	}

}
