package skytheory.hap.block;

import java.util.List;

import javax.annotation.Nullable;

import defeatedcrow.hac.core.ClimateCore;
import defeatedcrow.hac.main.util.DCName;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import skytheory.hap.tile.TileReactorAdvanced;
import skytheory.hap.tile.TileReactorFluidPort;
import skytheory.hap.util.ConstantsHaP;
import skytheory.lib.block.IWrenchBlock;
import skytheory.lib.plugin.waila.IWailaTipBlock;
import skytheory.lib.util.EnumSide;
import skytheory.lib.util.FacingHelper;
import skytheory.lib.util.STLibConstants;
import skytheory.lib.util.WrenchHelper;

public class BlockReactorFluidPort extends BlockContainer implements IWrenchBlock, IWailaTipBlock{

	public static final IProperty<EnumFacing> FACING = BlockHorizontal.FACING;

	public BlockReactorFluidPort() {
		super(Material.ROCK);
		this.setHardness(1.5F);
		this.setSoundType(SoundType.METAL);
		this.lightOpacity = 0;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileReactorFluidPort();
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
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
			BlockPos targetPos = pos.offset(f, -1);
			TileEntity targetTile = worldIn.getTileEntity(targetPos);
			if (targetTile instanceof TileReactorAdvanced) {
				EnumFacing sidefacing = ((TileReactorAdvanced) targetTile).getFacing();
				if (EnumSide.getSide(sidefacing, f) == EnumSide.LEFT) {
					return state.withProperty(FACING, sidefacing);
				}
			}
			Block targetBlock = worldIn.getBlockState(targetPos).getBlock();
			if (targetBlock instanceof BlockReactorStorage) {
				EnumFacing sidefacing = worldIn.getBlockState(targetPos).getValue(BlockHorizontal.FACING);
				if (EnumSide.getSide(sidefacing, f) == EnumSide.RIGHT) {
					return state.withProperty(FACING, sidefacing);
				}
			}
			if (targetBlock instanceof BlockReactorFluidPort) {
				EnumFacing sidefacing = worldIn.getBlockState(targetPos).getValue(BlockHorizontal.FACING);
				if (EnumSide.getSide(sidefacing, f) == EnumSide.LEFT || EnumSide.getSide(sidefacing, f) == EnumSide.RIGHT) {
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
	public void onLeftClickWithWrench(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side) {
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		if (world.isRemote) {
			SoundType soundtype = block.getSoundType(state, world, pos, player);
			world.playSound(player, pos, soundtype.getBreakSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
		} else {
			if (!player.isCreative()) {
				block.harvestBlock(world, player, pos, state, world.getTileEntity(pos), player.getHeldItem(hand));
			}
			world.setBlockToAir(pos);
		}
	}

	@Override
	public void onRightClickWithWrench(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side) {
		if (player.isSneaking()) {
			WrenchHelper.invertFacing(world, pos);
		} else {
			WrenchHelper.rotateFacing(world, pos, EnumFacing.UP);
		}
	}

	@Override
	public void getWailaTips(ItemStack stack, List<String> tips, IWailaDataAccessor accessor) {
		IBlockState state = accessor.getBlockState();
		tips.add(I18n.format(STLibConstants.TIP_FACING, state.getValue(FACING).getName()));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
		if (ClimateCore.proxy.isShiftKeyDown()) {
			tooltip.add(TextFormatting.YELLOW.toString() + TextFormatting.BOLD.toString() + "=== Requirement ===");
			tooltip.add(DCName.NON_POWERED.getLocalizedName());
			tooltip.add(TextFormatting.YELLOW.toString() + TextFormatting.BOLD.toString() + "=== Tips ===");
			tooltip.add(I18n.format(ConstantsHaP.TIP_REACTOR_FLUIDPORT));
		} else {
			tooltip.add(TextFormatting.ITALIC.toString() + "=== Lshift key: expand tooltip ===");
		}
	}
}
