package skytheory.hap.capability;

import net.minecraftforge.fluids.FluidStack;
import skytheory.hap.tile.EnumFluidMode;
import skytheory.hap.tile.TileReactorFluidPort;
import skytheory.lib.capability.fluidhandler.FluidThru;
import skytheory.lib.util.EnumSide;

public class FluidHandlerAdvReactorAccessor extends FluidThru {

	TileReactorFluidPort tile;
	EnumSide controll;

	public FluidHandlerAdvReactorAccessor(TileReactorFluidPort tile, EnumSide side, EnumSide controll) {
		super(tile, side);
		this.tile = tile;
		this.controll = controll;
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) {
		if (tile.modeMap.get(controll) == EnumFluidMode.EXPORT) {
			return 0;
		}
		return super.fill(resource, doFill);
	}

	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain) {
		if (tile.modeMap.get(controll) == EnumFluidMode.IMPORT) {
			return null;
		}
		return super.drain(resource, doDrain);
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		if (tile.modeMap.get(controll) == EnumFluidMode.IMPORT) {
			return null;
		}
		return super.drain(maxDrain, doDrain);
	}

}
