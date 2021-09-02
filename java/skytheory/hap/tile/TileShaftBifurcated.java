package skytheory.hap.tile;

import skytheory.lib.util.EnumSide;

public class TileShaftBifurcated extends TileShaftBase {

	@Override
	public boolean isInputSide(EnumSide side) {
		return side == EnumSide.BACK;
	}

	@Override
	public boolean isOutputSide(EnumSide side) {
		return side == EnumSide.TOP || side == EnumSide.BOTTOM;
	}

	// Lossless Bifurcated
	public float getFriction() {return 0.5f;}
}
