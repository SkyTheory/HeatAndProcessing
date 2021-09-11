package skytheory.hap.block;

import java.util.List;

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
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import skytheory.hap.tile.ITileInteract;
import skytheory.hap.tile.TileCokeOven;
import skytheory.lib.block.IWrenchBlock;
import skytheory.lib.plugin.waila.IWailaTipBlock;
import skytheory.lib.util.FacingHelper;
import skytheory.lib.util.STLibConstants;

public class BlockCokeOven extends BlockContainer implements IWrenchBlock, IWailaTipBlock {

	public static final IProperty<EnumFacing> FACING = BlockHorizontal.FACING;

	protected BlockCokeOven() {
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
		world.setBlockState(pos, state.withProperty(FACING, FacingHelper.rotateY(facing)));
	}


	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack) {
		ItemStack itemStack = new ItemStack(Item.getItemFromBlock(this));
		TileEntity tile = worldIn.getTileEntity(pos);
		if (tile instanceof TileCokeOven) {
			TileCokeOven cokeoven = (TileCokeOven) tile;
			NBTTagCompound nbt = cokeoven.writeToNBT(new NBTTagCompound());
			itemStack.getOrCreateSubCompound("BlockEntityTag").merge(nbt);
		}
        spawnAsEntity(worldIn, pos, itemStack);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		TileEntity tile = worldIn.getTileEntity(pos);
		if (tile instanceof ITileInteract) {
			return ((ITileInteract) tile).onRightClick(worldIn, pos, playerIn, hand, facing);
		}
		return false;
	}

}
