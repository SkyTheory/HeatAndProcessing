package skytheory.hap.tile;

import skytheory.lib.util.EnumSide;

public class TileShaftTShaped extends TileShaftBase {

	@Override
	public boolean isInputSide(EnumSide side) {
		return side == EnumSide.BACK;
	}

	@Override
	public boolean isOutputSide(EnumSide side) {
		return side == EnumSide.FRONT || side == EnumSide.TOP;
	}

	// Lossless Bifurcated
	public float getFriction() {return 0.5f;}
}
