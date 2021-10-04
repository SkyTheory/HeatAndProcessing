package skytheory.hap.block;

import java.util.List;

import javax.annotation.Nullable;

import defeatedcrow.hac.core.ClimateCore;
import defeatedcrow.hac.main.util.DCName;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import skytheory.hap.tile.TileReactorAdvanced;
import skytheory.hap.util.ConstantsHaP;
import skytheory.lib.util.EnumSide;
import skytheory.lib.util.FacingHelper;

public class BlockReactorAdvanced extends BlockTorque {

	public static final IProperty<EnumFacing> FACING = BlockHorizontal.FACING;

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileReactorAdvanced();
	}

	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		IBlockState state = this.blockState.getBaseState();
		EnumFacing horizontal;
		switch(facing) {
		case DOWN:
		case UP:
			horizontal = placer.getHorizontalFacing().getOpposite();
			break;
		default:
			horizontal = facing;
			break;
		}
		for (int i = 0; i < 4; i++) {
			EnumFacing f = horizontal;
			for (int j = 0; j < i; j++) f = FacingHelper.rotateY(f);
			IBlockState side = worldIn.getBlockState(pos.offset(f, -1));
			if (side.getBlock() instanceof BlockReactorStorage || side.getBlock() instanceof BlockReactorFluidPort) {
				EnumFacing sidefacing = side.getValue(BlockHorizontal.FACING);
				if (EnumSide.getSide(sidefacing, f) == EnumSide.RIGHT) {
					return state.withProperty(FACING, sidefacing);
				}
			}
		}
		return state.withProperty(FACING, horizontal);
	}

	public IBlockState getStateFromMeta(int meta) {
		IBlockState iblockstate = this.getDefaultState();
		EnumFacing facing = EnumFacing.getHorizontal(meta);
		iblockstate = iblockstate.withProperty(FACING, facing);
		return iblockstate;
	}

	public int getMetaFromState(IBlockState state) {
		EnumFacing facing = state.getValue(FACING);
		return facing.getHorizontalIndex();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {
				FACING,
		});
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		ItemStack itemStack = new ItemStack(Item.getItemFromBlock(this));
		TileEntity tile = world.getTileEntity(pos);
		if (tile != null) {
			NBTTagCompound nbt = tile.writeToNBT(new NBTTagCompound());
			itemStack.getOrCreateSubCompound("BlockEntityTag").merge(nbt);
		}
		drops.add(itemStack);
	}

	@Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
    	TileEntity tile = world.getTileEntity(pos);
    	if (tile instanceof TileReactorAdvanced) {
    		((TileReactorAdvanced) tile).skipInputItem = false;
    		((TileReactorAdvanced) tile).skipOutputItem = false;
    		((TileReactorAdvanced) tile).skipInputFluid = false;
    		((TileReactorAdvanced) tile).skipOutputFluid = false;
    	}
    }

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
		if (ClimateCore.proxy.isShiftKeyDown()) {
			tooltip.add(TextFormatting.YELLOW.toString() + TextFormatting.BOLD.toString() + "=== Requirement ===");
			tooltip.add(String.format("64.0+ Torque/s"));
			tooltip.add(TextFormatting.YELLOW.toString() + TextFormatting.BOLD.toString() + "=== Output ===");
			tooltip.add(DCName.OUTPUT_ITEM.getLocalizedName());
			tooltip.add("1024.0F /cycle");
			tooltip.add(TextFormatting.YELLOW.toString() + TextFormatting.BOLD.toString() + "=== Tips ===");
			tooltip.add(I18n.format("dcs.tip.reactor"));
			tooltip.add(I18n.format(ConstantsHaP.TIP_REACTOR));
		} else {
			tooltip.add(TextFormatting.ITALIC.toString() + "=== Lshift key: expand tooltip ===");
		}
	}
}
