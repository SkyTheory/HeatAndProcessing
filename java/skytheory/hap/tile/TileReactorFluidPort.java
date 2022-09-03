package skytheory.hap.tile;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import skytheory.hap.capability.FluidHandlerAdvReactorAccessor;
import skytheory.hap.capability.FluidHandlerAdvReactorHatch;
import skytheory.lib.capability.datasync.DataSyncHandler;
import skytheory.lib.capability.datasync.IDataSync;
import skytheory.lib.capability.fluidhandler.FluidProviderSided;
import skytheory.lib.capability.fluidhandler.FluidThru;
import skytheory.lib.capability.itemhandler.ItemProviderSided;
import skytheory.lib.capability.itemhandler.ItemThru;
import skytheory.lib.network.tile.TileSync;
import skytheory.lib.tile.ISidedTileDirectional;
import skytheory.lib.tile.ITileInventory;
import skytheory.lib.tile.ITileTank;
import skytheory.lib.util.EnumSide;
import skytheory.lib.util.FacingUtils;

public class TileReactorFluidPort extends TileEntity implements ISidedTileDirectional, ITileInventory, ITileTank, IDataSync {

	public static final EnumSide[] CONTROLLABLE = {EnumSide.TOP, EnumSide.BACK, EnumSide.BOTTOM};

	public Map<EnumSide, EnumFluidMode> modeMap = new HashMap<>();

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
		IFluidHandler thruTop = new FluidHandlerAdvReactorHatch(this, EnumSide.TOP);
		IFluidHandler thruLeft = new FluidThru(this, EnumSide.LEFT);
		IFluidHandler thruBack = new FluidHandlerAdvReactorHatch(this, EnumSide.BACK);
		IFluidHandler thruBottom = new FluidHandlerAdvReactorHatch(this, EnumSide.BOTTOM);

		IFluidHandler thruRight = new FluidThru(this, EnumSide.RIGHT);

		IFluidHandler thruRightForTop = new FluidHandlerAdvReactorAccessor(this, EnumSide.RIGHT, EnumSide.TOP);
		IFluidHandler thruRightForBack = new FluidHandlerAdvReactorAccessor(this, EnumSide.RIGHT, EnumSide.BACK);
		IFluidHandler thruRightForBottom = new FluidHandlerAdvReactorAccessor(this, EnumSide.RIGHT, EnumSide.BOTTOM);

		// 右面からの接続に対して左面、背面、天面と底面への流出入を許可する
		provider.addDataToSide(thruTop, EnumSide.RIGHT);
		provider.addDataToSide(thruLeft, EnumSide.RIGHT);
		provider.addDataToSide(thruBack, EnumSide.RIGHT);
		provider.addDataToSide(thruBottom, EnumSide.RIGHT);

		// 左面からの接続は右面へ処理を渡す
		provider.addDataToSide(thruRight, EnumSide.LEFT);

		// 残りの面には設定に応じて
		provider.addDataToSide(thruRightForTop, EnumSide.TOP);
		provider.addDataToSide(thruRightForBack, EnumSide.BACK);
		provider.addDataToSide(thruRightForBottom, EnumSide.BOTTOM);

		return provider;
	}

	@Override
	public void setWorld(World worldIn) {
		super.setWorld(worldIn);
		if (worldIn.isRemote) {
			TileSync.request(this, DataSyncHandler.SYNC_DATA_CAPABILITY, FacingUtils.SET_SINGLE_NULL);
		}
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

	public void cycleMode(EnumSide side) {
		if (side == EnumSide.TOP || side == EnumSide.BACK || side == EnumSide.BOTTOM) {
			int index = modeMap.getOrDefault(side, EnumFluidMode.NONE).index;
			modeMap.put(side, EnumFluidMode.fromIndex(index + 1));
		}
		this.markDirty();
	}

	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
		return oldState.getBlock() != newSate.getBlock();
	}

	@Override
	public NBTTagCompound serializeSync() {
		NBTTagCompound nbt = new NBTTagCompound();
		for (EnumSide side : CONTROLLABLE) {
			nbt.setInteger(side.getName(), modeMap.getOrDefault(side, EnumFluidMode.NONE).index);
		}
		return nbt;
	}

	@Override
	public void deserializeSync(NBTTagCompound compound) {
		for (EnumSide side : CONTROLLABLE) {
			if (compound.hasKey(side.getName(), NBT.TAG_INT)) {
				this.modeMap.put(side, EnumFluidMode.fromIndex(compound.getInteger(side.getName())));
			}
		}
	}
}
