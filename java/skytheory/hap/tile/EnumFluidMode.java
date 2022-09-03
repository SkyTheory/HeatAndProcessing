package skytheory.hap.tile;

public enum EnumFluidMode {
	NONE(0),
	IMPORT(1),
	EXPORT(2);

	public final int index;

	EnumFluidMode(int i) {
		this.index = i;
	}

	public static EnumFluidMode fromIndex(int i) {
		if (i >= 0 && i < EnumFluidMode.values().length) {
			return EnumFluidMode.values()[i];
		}
		return NONE;
	}
}