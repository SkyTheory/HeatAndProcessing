package skytheory.hap.block;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import defeatedcrow.hac.core.ClimateCore;
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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import skytheory.hap.tile.ITileInteract;
import skytheory.hap.tile.TileCokeOven;
import skytheory.hap.util.ConstantsHaP;
import skytheory.lib.block.IWrenchBlock;
import skytheory.lib.plugin.waila.IWailaTipBlock;
import skytheory.lib.util.FacingHelper;
import skytheory.lib.util.STLibConstants;

public class BlockCokeOven extends BlockContainer implements IWrenchBlock, IWailaTipBlock {

	public static final IProperty<EnumFacing> FACING = BlockHorizontal.FACING;

	public BlockCokeOven() {
		super(Material.IRON);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileCokeOven();
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		TileEntity tile = worldIn.getTileEntity(pos);
		if (tile instanceof TileCokeOven) {
			int state = ((TileCokeOven) tile).state;
			if (state > 0) {
				EnumFacing facing = stateIn.getValue(FACING);
				double xBase = (double)pos.getX() + 0.5d;
				double yBase = (double)pos.getY() + 0.75d;
				double zBase = (double)pos.getZ() + 0.5d;
				double xSpeed = rand.nextDouble() * 0.05d - 0.025d;
				double ySpeed = rand.nextDouble() * 0.05d;
				double zSpeed = rand.nextDouble() * 0.05d - 0.025d;
				if (facing == EnumFacing.NORTH) {
					xBase -= 0.46875d;
					zBase += 0.21875d;
				} else if (facing == EnumFacing.EAST) {
					xBase -= 0.21875d;
					zBase -= 0.46875d;
				} else if (facing == EnumFacing.SOUTH) {
					xBase += 0.46875d;
					zBase -= 0.21875d;
				} else if (facing == EnumFacing.WEST) {
					xBase += 0.21875d;
					zBase += 0.46875d;
				}
				worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, xBase, yBase, zBase, xSpeed, ySpeed, zSpeed);
				xSpeed = rand.nextDouble() * 0.05d - 0.025d;
				ySpeed = rand.nextDouble() * 0.05d + 0.025d;
				zSpeed = rand.nextDouble() * 0.05d - 0.025d;
				worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, xBase, yBase, zBase, xSpeed, ySpeed, zSpeed);
			}
		}
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
		return state.withProperty(FACING, horizontal);
	}

	public IBlockState getStateFromMeta(int meta) {
		EnumFacing facing = EnumFacing.getHorizontal(meta);
		return this.getDefaultState().withProperty(FACING, facing);
	}

	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getHorizontalIndex();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {
				FACING
		});
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
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
	public void getWailaTips(ItemStack stack, List<String> tips, IWailaDataAccessor accessor) {
		IBlockState state = accessor.getBlockState();
		tips.add(I18n.format(STLibConstants.TIP_FACING, state.getValue(FACING).getName()));
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
		IBlockState state = world.getBlockState(pos);
		EnumFacing facing = state.getValue(FACING);
		if (player.isSneaking()) {
			world.setBlockState(pos, state.withProperty(FACING, FacingHelper.invert(facing)));
		} else {
			world.setBlockState(pos, state.withProperty(FACING, FacingHelper.rotateY(facing)));
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		TileEntity tile = worldIn.getTileEntity(pos);
		if (tile instanceof ITileInteract) {
			return ((ITileInteract) tile).onRightClick(worldIn, pos, playerIn, hand, facing);
		}
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
		if (ClimateCore.proxy.isShiftKeyDown()) {
			tooltip.add(TextFormatting.YELLOW.toString() + TextFormatting.BOLD.toString() + "=== Requirement ===");
			tooltip.add(String.format("HeatTier: " + TextFormatting.GOLD.toString() + "OVEN+"));
			tooltip.add(String.format("Humidity: " + TextFormatting.DARK_RED.toString() + "DRY"));
			tooltip.add(TextFormatting.YELLOW.toString() + TextFormatting.BOLD.toString() + "=== Tips ===");
			tooltip.add(I18n.format(ConstantsHaP.TIP_COKE_OVEN));
		} else {
			tooltip.add(TextFormatting.ITALIC.toString() + "=== Lshift key: expand tooltip ===");
		}
	}
}
