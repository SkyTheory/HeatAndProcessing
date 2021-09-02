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
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import skytheory.hap.tile.TileConveyor;
import skytheory.lib.block.IWrenchBlock;
import skytheory.lib.plugin.waila.IWailaTipBlock;
import skytheory.lib.util.EnumSide;
import skytheory.lib.util.FacingHelper;
import skytheory.lib.util.ItemHandlerUtils;
import skytheory.lib.util.STLibConstants;
import skytheory.lib.util.WrenchHelper;

public class BlockConveyor extends BlockContainer implements IWrenchBlock, IWailaTipBlock {

	public static final IProperty<EnumFacing> FACING = BlockHorizontal.FACING;
	public static final IProperty<Boolean> FLOOR = PropertyBool.create("floor");

	protected static final AxisAlignedBB AABB_CONVEYOR_X = new AxisAlignedBB(0.0d, 0.25d, 0.125d, 1.0d, 0.375d, 0.875d);
	protected static final AxisAlignedBB AABB_CONVEYOR_Z = new AxisAlignedBB(0.125d, 0.25d, 0.0d, 0.875d, 0.375d, 1.0d);

	protected static final AxisAlignedBB AABB_CONVEYOR_X_FLOOR = new AxisAlignedBB(0.0d, 0.0d, 0.125d, 1.0d, 0.125d, 0.875d);
	protected static final AxisAlignedBB AABB_CONVEYOR_Z_FLOOR = new AxisAlignedBB(0.125d, 0.0d, 0.0d, 0.875d, 0.125d, 1.0d);

	protected double conveyor_speed = 0.05d;
	protected double conveyor_attract = 0.035d;
	protected double max_speed = 0.35d;

	public BlockConveyor() {
		super(Material.ROCK);
		this.setHardness(1.5F);
		this.setSoundType(SoundType.METAL);
		this.lightOpacity = 0;
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(FLOOR, false));
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileConveyor();
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
			for (int j = 0; j < i; j++) {
				f = FacingHelper.rotateY(f);
			}
			IBlockState side = worldIn.getBlockState(pos.offset(f, -1));
			if (side.getBlock() instanceof BlockReactorAdvanced) {
				EnumFacing sidefacing = side.getValue(BlockReactorStorage.FACING);
				if (EnumSide.getSide(sidefacing, f) == EnumSide.LEFT) {
					return state.withProperty(FACING, sidefacing).withProperty(FLOOR, false);
				}
			}
		}
		return state.withProperty(FACING, horizontal).withProperty(FLOOR, false);
	}

	public IBlockState getStateFromMeta(int meta) {
		IBlockState iblockstate = this.getDefaultState();
		EnumFacing facing = EnumFacing.getHorizontal(meta & 0b0011);
		boolean floor = (meta & 0b0100) == 0b0100;
		iblockstate = iblockstate.withProperty(FACING, facing).withProperty(FLOOR, floor);
		return iblockstate;
	}

	public int getMetaFromState(IBlockState state) {
		EnumFacing facing = state.getValue(FACING);
		int meta = facing.getHorizontalIndex();
		if (state.getValue(FLOOR)) meta += 4;
		return meta;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {
				FACING,
				FLOOR
		});
	}

	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
		if (entity != null) {
			if (!(entity instanceof EntityItem) && !entity.isSneaking()) {
				EnumFacing facing = state.getValue(FACING);
				double moveX = 0.0d;
				double moveZ = 0.0d;
				switch (facing) {
				case NORTH:
					moveZ = -this.conveyor_speed;
					break;
				case SOUTH:
					moveZ = this.conveyor_speed;
					break;
				case EAST:
					moveX = this.conveyor_speed;
					break;
				case WEST:
					moveX = -this.conveyor_speed;
					break;
				default:
					break;
				}
				switch (facing.getAxis()) {
				case X:
					if (entity.posZ < pos.getZ() + 0.4f) {
						moveZ = this.conveyor_attract;
					}
					if (entity.posZ > pos.getZ() + 0.6f) {
						moveZ = -this.conveyor_attract;
					}
					break;
				case Z:
					if (entity.posX < pos.getX() + 0.4f) {
						moveX = this.conveyor_attract;
					}
					if (entity.posX > pos.getX() + 0.6f) {
						moveX = -this.conveyor_attract;
					}
					break;
				default:
					break;
				}
				double minX = Math.min(entity.motionX, -this.max_speed);
				double maxX = Math.max(entity.motionX, this.max_speed);
				double minZ = Math.min(entity.motionZ, -this.max_speed);
				double maxZ = Math.max(entity.motionZ, this.max_speed);
				entity.motionX = MathHelper.clamp(entity.motionX + moveX, minX, maxX);
				entity.motionZ = MathHelper.clamp(entity.motionZ + moveZ, minZ, maxZ);
			}
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

	public void onRightClickWithWrench(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side) {
		if (player.isSneaking()) {
			WrenchHelper.cycleProperty(world, pos, FLOOR);
		} else {
			WrenchHelper.rotateFacing(world, pos, EnumFacing.UP);
		}
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
	public void getWailaTips(ItemStack stack, List<String> tips, IWailaDataAccessor accessor) {
		IBlockState state = accessor.getBlockState();
		tips.add(I18n.format(STLibConstants.TIP_FACING, state.getValue(FACING).getName()));
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		EnumFacing facing = state.getValue(FACING);
		boolean floor = state.getValue(FLOOR);
		if (floor) {
			switch (facing.getAxis()) {
			case X:
				return AABB_CONVEYOR_X_FLOOR;
			case Z:
				return AABB_CONVEYOR_Z_FLOOR;
			default:
				break;
			}
		} else {
			switch (facing.getAxis()) {
			case X:
				return AABB_CONVEYOR_X;
			case Z:
				return AABB_CONVEYOR_Z;
			default:
				break;
			}
		}
		return FULL_BLOCK_AABB;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
		if (ClimateCore.proxy.isShiftKeyDown()) {
			tooltip.add(TextFormatting.YELLOW.toString() + TextFormatting.BOLD.toString() + "=== Requirement ===");
			tooltip.add(DCName.NON_POWERED.getLocalizedName());
			tooltip.add(TextFormatting.YELLOW.toString() + TextFormatting.BOLD.toString() + "=== Output ===");
			tooltip.add(DCName.ITEM.getLocalizedName() + DCName.TRANSPORT.getLocalizedName() + ": 1 item/8t");
			tooltip.add(TextFormatting.YELLOW.toString() + TextFormatting.BOLD.toString() + "=== Tips ===");
			tooltip.add(I18n.format("dcs.tip.conveyor3"));
			tooltip.add(I18n.format("dcs.tip.conveyor"));
			tooltip.add(TextFormatting.RED.toString() + "SMELTING+" + TextFormatting.GRAY
					.toString() + " and " + TextFormatting.DARK_BLUE.toString() + "TIGHT" + TextFormatting.GRAY
					.toString() + ": " + I18n.format("dcs.tip.conveyor2"));
		} else {
			tooltip.add(TextFormatting.ITALIC.toString() + "=== Lshift key: expand tooltip ===");
		}
	}

}
