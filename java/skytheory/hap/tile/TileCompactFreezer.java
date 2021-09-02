package skytheory.hap.tile;

import java.util.Arrays;
import java.util.List;

import defeatedcrow.hac.api.climate.DCHeatTier;

public class TileCompactFreezer extends TileHeatController {

	@Override
	public List<DCHeatTier> getHeatTiers() {
		return Arrays.asList(DCHeatTier.COLD, DCHeatTier.FROSTBITE, DCHeatTier.CRYOGENIC, DCHeatTier.ABSOLUTE);
	}

	@Override
	public float getTorqueRequired(DCHeatTier tier) {
		switch (tier) {
		case COLD:
			return 8.0f;
		case FROSTBITE:
			return 16.0f;
		case CRYOGENIC:
			return 32.0f;
		case ABSOLUTE:
			return 64.0f;
		default :
			return 0.0f;
		}
	}

}
