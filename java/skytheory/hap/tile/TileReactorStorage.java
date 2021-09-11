package skytheory.hap.tile;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import skytheory.hap.HeatAndProcessing;
import skytheory.hap.init.GuiHandler;
import skytheory.lib.capability.itemhandler.ItemAccessor;
import skytheory.lib.capability.itemhandler.ItemHandler;
import skytheory.lib.capability.itemhandler.ItemProviderSided;
import skytheory.lib.capability.itemhandler.MultiItemHandler;
import skytheory.lib.network.tile.TileSync;
import skytheory.lib.tile.ISidedTileDirectional;
import skytheory.lib.tile.ITileInventory;
import skytheory.lib.util.EnumSide;
import skytheory.lib.util.FacingUtils;

public class TileReactorStorage extends TileEntity implements ISidedTileDirectional, ITileInventory, ITileInteract {

	public IItemHandler input;
	public IItemHandler output;

	@Override
	public void setWorld(World worldIn) {
		super.setWorld(worldIn);
		if (worldIn.isRemote) {
			TileSync.request(this, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, FacingUtils.SET_SINGLE_NULL);
		}
	}

	@Override
	public ICapabilityProvider createInventoryProvider() {
		this.input = new ItemHandler(18).addListener(this);
		this.output = new ItemHandler(18).addListener(this);

		IItemHandler accessorInput = ItemAccessor.insertOnly(input);
		IItemHandler accessorOutput = ItemAccessor.extractOnly(output);

		IItemHandler accessorInputInverted = ItemAccessor.extractOnly(input);
		IItemHandler accessorOutputInverted = ItemAccessor.insertOnly(output);

		IItemHandler accessor = new MultiItemHandler(accessorInput, accessorOutput);

		ItemProviderSided provider = new ItemProviderSided(this, input, output);

		provider.addDataToSide(accessor, EnumSide.TOP);
		provider.addDataToSide(accessor, EnumSide.BOTTOM);
		provider.addDataToSide(accessor, EnumSide.LEFT);
		provider.addDataToSide(accessor, EnumSide.FRONT);
		provider.addDataToSide(accessor, EnumSide.BACK);
		provider.addDataToSide(accessorInputInverted, EnumSide.RIGHT);
		provider.addDataToSide(accessorOutputInverted, EnumSide.RIGHT);

		return provider;
	}

	@Override
	public EnumFacing getFacing() {
		return world.getBlockState(pos).getValue(BlockHorizontal.FACING);
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}

	@Override
	public boolean onRightClick(World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing facing) {
		if (hand == EnumHand.MAIN_HAND) {
			player.openGui(HeatAndProcessing.INSTANCE, GuiHandler.TILE_GUI, world, pos.getX(), pos.getY(), pos.getZ());
			return true;
		}
		return false;
	}

}
