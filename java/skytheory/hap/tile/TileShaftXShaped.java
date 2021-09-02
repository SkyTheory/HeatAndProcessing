package skytheory.hap.tile;

import skytheory.lib.util.EnumSide;

public class TileShaftXShaped extends TileShaftBase {

	@Override
	public boolean isInputSide(EnumSide side) {
		return side == EnumSide.BACK;
	}

	@Override
	public boolean isOutputSide(EnumSide side) {
		return side == EnumSide.FRONT || side == EnumSide.TOP || side == EnumSide.BOTTOM;
	}

	// Lossless Trifurcated
	public float getFriction() {return 1.0f / 3.0f;}
}
