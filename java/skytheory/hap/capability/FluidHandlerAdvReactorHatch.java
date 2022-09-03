package skytheory.hap.capability;

import net.minecraftforge.fluids.FluidStack;
import skytheory.hap.tile.EnumFluidMode;
import skytheory.hap.tile.TileReactorFluidPort;
import skytheory.lib.capability.fluidhandler.FluidThru;
import skytheory.lib.util.EnumSide;

public class FluidHandlerAdvReactorHatch extends FluidThru {

	TileReactorFluidPort tile;
	public FluidHandlerAdvReactorHatch(TileReactorFluidPort tile, EnumSide side) {
		super(tile, side);
		this.tile = tile;
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) {
		if (tile.modeMap.get(side) == EnumFluidMode.IMPORT) {
			return 0;
		}
		return super.fill(resource, doFill);
	}

	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain) {
		if (tile.modeMap.get(side) == EnumFluidMode.EXPORT) {
			return null;
		}
		return super.drain(resource, doDrain);
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		if (tile.modeMap.get(side) == EnumFluidMode.EXPORT) {
			return null;
		}
		return super.drain(maxDrain, doDrain);
	}

}
