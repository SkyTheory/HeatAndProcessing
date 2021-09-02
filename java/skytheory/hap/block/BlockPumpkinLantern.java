package skytheory.hap.block;

import java.util.List;

import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import skytheory.hap.util.ConstantsHaP;
import skytheory.lib.block.IWrenchBlock;
import skytheory.lib.plugin.waila.IWailaTipBlock;
import skytheory.lib.util.STLibConstants;
import skytheory.lib.util.WrenchHelper;

public class BlockPumpkinLantern extends Block implements IWrenchBlock, IWailaTipBlock {

	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	public static final PropertyEnum<BlockPumpkinLantern.EnumPeg> PEG = PropertyEnum.<BlockPumpkinLantern.EnumPeg>create("type", BlockPumpkinLantern.EnumPeg.class);
	protected static final AxisAlignedBB AABB_FLOOR = new AxisAlignedBB(0.25d, 0.0d, 0.25d, 0.75d, 0.5d, 0.75d);
	protected static final AxisAlignedBB AABB_CEIL = new AxisAlignedBB(0.25d, 0.25d, 0.25d, 0.75d, 0.75d, 0.75d);
	protected static final AxisAlignedBB AABB_WALL_NORTH = new AxisAlignedBB(0.25d, 0.125d, 0.4375d, 0.75d, 0.625d, 0.9375d);
	protected static final AxisAlignedBB AABB_WALL_EAST = new AxisAlignedBB(0.0625d, 0.125d, 0.25d, 0.5625d, 0.625d, 0.75d);
	protected static final AxisAlignedBB AABB_WALL_SOUTH = new AxisAlignedBB(0.25d, 0.125d, 0.0625d, 0.75d, 0.625d, 0.5625d);
	protected static final AxisAlignedBB AABB_WALL_WEST = new AxisAlignedBB(0.4375d, 0.125d, 0.25d, 0.9375d, 0.625d, 0.75d);

	public BlockPumpkinLantern() {
		super(Material.GOURD);
		this.setHardness(1.0F);
		this.setSoundType(SoundType.WOOD);
		this.setLightLevel(1.0F);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(PEG, EnumPeg.CEIL));
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		EnumPeg peg = state.getValue(PEG);
		switch (peg) {
		case CEIL:
			return AABB_CEIL;
		case FLOOR:
			return AABB_FLOOR;
		case WALL:
			switch (state.getValue(FACING)) {
			case NORTH:
				return AABB_WALL_NORTH;
			case SOUTH:
				return AABB_WALL_SOUTH;
			case WEST:
				return AABB_WALL_WEST;
			case EAST:
				return AABB_WALL_EAST;
			default:
				break;
			}
			return AABB_WALL_NORTH;
		default:
			return FULL_BLOCK_AABB;
		}
	}

	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		IBlockState state = this.blockState.getBaseState();
		EnumFacing horizontal;
		switch(facing) {
		case DOWN:
			state = state.withProperty(PEG, EnumPeg.CEIL);
			horizontal = placer.getHorizontalFacing().getOpposite();
			break;
		case UP:
			state = state.withProperty(PEG, EnumPeg.FLOOR);
			horizontal = placer.getHorizontalFacing().getOpposite();
			break;
		default:
			state = state.withProperty(PEG, EnumPeg.WALL);
			horizontal = facing;
			break;
		}
		state = state.withProperty(FACING, horizontal);
		return state;
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int meta) {
		IBlockState iblockstate = this.getDefaultState();
		if (meta >= 8) {
			iblockstate = iblockstate.withProperty(PEG, EnumPeg.FLOOR);
		} else if (meta >= 4) {
			iblockstate = iblockstate.withProperty(PEG, EnumPeg.WALL);
		}
		int index = meta % 4;
		EnumFacing facing = EnumFacing.getHorizontal(index);
		iblockstate = iblockstate.withProperty(FACING, facing);
		return iblockstate;
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState state) {
		int i = 0;
		EnumFacing facing = state.getValue(FACING);
		i = i + facing.getHorizontalIndex();
		EnumPeg peg = state.getValue(PEG);
		if (peg == EnumPeg.WALL) {
			i = i + 4;
		}
		if (peg == EnumPeg.FLOOR) {
			i = i + 8;
		}
		return i;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {
				FACING,
				PEG
		});
	}

	public static enum EnumPeg implements IStringSerializable {

		CEIL("ceil"),
		FLOOR("floor"),
		WALL("wall");

		private final String name;

		private EnumPeg(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}

		@Override
		public String getName() {
			return name;
		}
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
			WrenchHelper.cycleProperty(world, pos, PEG);
		} else {
			WrenchHelper.rotateFacing(world, pos, EnumFacing.UP);
		}
	}

	@Override
	public void getWailaTips(ItemStack stack, List<String> tips, IWailaDataAccessor accessor) {
		IBlockState state = accessor.getBlockState();
		tips.add(I18n.format(STLibConstants.TIP_FACING, state.getValue(FACING).getName()));
		tips.add(I18n.format(ConstantsHaP.TIP_PEG, I18n.format("hap.tip.peg." + state.getValue(PEG).getName())));
	}
}
