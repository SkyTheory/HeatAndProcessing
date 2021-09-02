package skytheory.hap.tile;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import skytheory.lib.capability.fluidhandler.FluidAccessor;
import skytheory.lib.capability.fluidhandler.FluidProviderSided;
import skytheory.lib.capability.fluidhandler.FluidThru;
import skytheory.lib.capability.itemhandler.ItemProviderSided;
import skytheory.lib.capability.itemhandler.ItemThru;
import skytheory.lib.tile.ISidedTileDirectional;
import skytheory.lib.tile.ITileInventory;
import skytheory.lib.tile.ITileTank;
import skytheory.lib.util.EnumSide;

public class TileReactorFluidPort extends TileEntity implements ISidedTileDirectional, ITileInventory, ITileTank {

	@Override
	public ICapabilityProvider createInventoryProvider() {
		ItemProviderSided provider = new ItemProviderSided(this);
		IItemHandler thruLeft = new ItemThru(this, EnumSide.LEFT);
		IItemHandler thruRight = new ItemThru(this, EnumSide.RIGHT);

		provider.addDataToSide(thruRight, EnumSide.LEFT);
		provider.addDataToSide(thruLeft, EnumSide.RIGHT);
		return provider;
	}

	@Override
	public ICapabilityProvider createFluidProvider() {
		FluidProviderSided provider = new FluidProviderSided(this);
		IFluidHandler thruTop = new FluidThru(this, EnumSide.TOP);
		IFluidHandler thruLeft = new FluidThru(this, EnumSide.LEFT);
		IFluidHandler thruRight = new FluidThru(this, EnumSide.RIGHT);
		IFluidHandler thruBack = new FluidThru(this, EnumSide.BACK);
		IFluidHandler thruBottom = new FluidThru(this, EnumSide.BOTTOM);

		IFluidHandler fillTop = FluidAccessor.fillOnly(thruTop);
		IFluidHandler fillBottom = FluidAccessor.fillOnly(thruBottom);
		IFluidHandler drainAll = FluidAccessor.drainOnly(thruTop, thruBack, thruRight, thruLeft);

		// 右面からの接続は左面と背面と天面への流出入、底面への流出を許可する
		provider.addDataToSide(thruTop, EnumSide.RIGHT);
		provider.addDataToSide(thruLeft, EnumSide.RIGHT);
		provider.addDataToSide(thruBack, EnumSide.RIGHT);
		provider.addDataToSide(fillBottom, EnumSide.RIGHT);

		// 左面からの接続は天面への流入のみを許可する
		provider.addDataToSide(fillTop, EnumSide.LEFT);

		// 背面からの接続は天面への流出入を許可
		provider.addDataToSide(thruTop, EnumSide.BACK);

		// 底面からの接続は天面と左面と右面と背面からの流出を許可する
		provider.addDataToSide(drainAll, EnumSide.BOTTOM);
		return provider;
	}

	@Override
	public void onItemHandlerChanged(IItemHandler handler, int slot) {
	}


	@Override
	public void onFluidHandlerChanged(IFluidHandler handler) {
	}

	@Override
	public EnumFacing getFacing() {
		return world.getBlockState(pos).getValue(BlockHorizontal.FACING);
	}

}
