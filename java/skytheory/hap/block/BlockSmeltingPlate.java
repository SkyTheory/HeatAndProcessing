package skytheory.hap.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import skytheory.hap.tile.TileSmeltingPlate;
import skytheory.lib.block.IWrenchBlock;
import skytheory.lib.util.ItemHandlerUtils;
import skytheory.lib.util.WrenchHelper;

public class BlockSmeltingPlate extends BlockContainer implements IWrenchBlock {

	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	public static final PropertyBool SIDE = PropertyBool.create("side");

	protected static final AxisAlignedBB AABB_PLATE_BOTTOM = new AxisAlignedBB(0.0d, 0.0, 0.0d, 1.0d, 0.0625d, 1.0d);
	protected static final AxisAlignedBB AABB_PLATE_TOP = new AxisAlignedBB(0.0d, 0.9375d, 0.0d, 1.0d, 1.0d, 1.0d);

	public BlockSmeltingPlate() {
		super(Material.IRON);
		this.setHardness(1.5F);
		this.setSoundType(SoundType.METAL);
		this.lightOpacity = 0;
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileSmeltingPlate();
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
		boolean bottom = true;
		EnumFacing horizontal = facing;
		switch(facing) {
		case DOWN:
			horizontal = placer.getHorizontalFacing().getOpposite();
			bottom = false;
			break;
		case UP:
			horizontal = placer.getHorizontalFacing().getOpposite();
			break;
		default:
			bottom = hitY <= 0.5D;
			break;
		}
		state = state.withProperty(FACING, horizontal);
		state = state.withProperty(SIDE, bottom);
		return state;
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int meta) {
		IBlockState iblockstate = this.getDefaultState();
		EnumFacing facing = EnumFacing.getHorizontal(meta);
		iblockstate = iblockstate.withProperty(FACING, facing);
		return iblockstate;
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState state) {
		EnumFacing facing = state.getValue(FACING);
		return facing.getHorizontalIndex();

	}

	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		TileEntity tile = worldIn.getTileEntity(pos);
		if (tile instanceof TileSmeltingPlate) {
			if (hand == EnumHand.MAIN_HAND) {
				TileSmeltingPlate plate = (TileSmeltingPlate) tile;
				plate.onRightClick(worldIn, pos, state, playerIn);
			}
			return true;
		}
		return false;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {
				FACING,
				SIDE
		});
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return state.getValue(SIDE) ? AABB_PLATE_BOTTOM : AABB_PLATE_TOP;
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntity tile = worldIn.getTileEntity(pos);
		if (tile != null) {
			IItemHandler handler = ItemHandlerUtils.getItemHandler(tile);
			ItemHandlerUtils.dropInventoryItems(worldIn, pos, handler);
		}
		super.breakBlock(worldIn, pos, state);
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
			WrenchHelper.cycleProperty(world, pos, SIDE);
		} else {
			WrenchHelper.rotateFacing(world, pos, EnumFacing.UP);
		}
	}
}
