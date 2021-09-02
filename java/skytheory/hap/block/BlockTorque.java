package skytheory.hap.block;

import defeatedcrow.hac.api.energy.capability.TorqueCapabilityHandler;
import defeatedcrow.hac.main.MainInit;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import skytheory.hap.tile.ITileInteract;
import skytheory.hap.tile.ITorqueTile;
import skytheory.lib.block.IWrenchBlock;
import skytheory.lib.tile.ISidedTileDirectional;
import skytheory.lib.tile.ISidedTileRotational;
import skytheory.lib.util.WrenchHelper;

public abstract class BlockTorque extends BlockContainer implements IWrenchBlock {

	public BlockTorque() {
		super(Material.ROCK);
		this.setHardness(1.5F);
		this.setSoundType(SoundType.METAL);
		this.lightOpacity = 0;
	}

	public BlockTorque(Material materialIn) {
		super(materialIn);
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
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		TileEntity tile = worldIn.getTileEntity(pos);
		if (tile instanceof ITileInteract) {
			return ((ITileInteract) tile).onRightClick(worldIn, pos, playerIn, hand, facing);
		}
		return false;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	public float getTorqueAmount(World world, BlockPos pos, IBlockState state) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof ITorqueTile) {
			if (tile.hasCapability(TorqueCapabilityHandler.TORQUE_HANDLER_CAPABILITY, null));
			return tile.getCapability(TorqueCapabilityHandler.TORQUE_HANDLER_CAPABILITY, null).getTorqueAmount();
		}
		return 0.0f;
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
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof ISidedTileRotational) {
			ISidedTileRotational rotational = (ISidedTileRotational) tile;
			if (player.isSneaking()) {
				rotational.setRotation(rotational.getRotation().getOpposite());
			} else if (player.getHeldItem(hand).getItem() == MainInit.wrench) {
				rotational.setRotation(rotational.getRotation().rotate(rotational.getRotation().getFront()));
			} else {
				rotational.setRotation(rotational.getRotation().rotate(side));
			}
		} else if (tile instanceof ISidedTileDirectional) {
			WrenchHelper.rotateFacing(world, pos, EnumFacing.UP);
		}
	}

}
