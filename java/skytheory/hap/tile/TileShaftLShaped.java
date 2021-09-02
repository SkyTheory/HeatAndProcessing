package skytheory.hap.tile;

import skytheory.lib.util.EnumSide;

public class TileShaftLShaped extends TileShaftBase {

	@Override
	public boolean isInputSide(EnumSide side) {
		return side == EnumSide.BACK;
	}

	@Override
	public boolean isOutputSide(EnumSide side) {
		return side == EnumSide.TOP;
	}

	// Lossless
	public float getFriction() {return 1.0f;}
}
