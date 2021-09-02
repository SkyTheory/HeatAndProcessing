package skytheory.hap.block;

import defeatedcrow.hac.api.climate.DCHeatTier;
import defeatedcrow.hac.api.climate.IHeatTile;
import defeatedcrow.hac.machine.block.TileHeatExchanger;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import skytheory.hap.tile.TileHeatController;
import skytheory.lib.block.IWrenchBlock;
import skytheory.lib.util.EnumSide;

public abstract class BlockHeatController extends BlockTorque implements IHeatTile, IWrenchBlock {

	public BlockHeatController() {
		super(Material.ROCK);
		this.setHardness(1.5F);
		this.setSoundType(SoundType.METAL);
		this.lightOpacity = 0;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public DCHeatTier getHeatTier(World world, BlockPos target, BlockPos source) {
		TileEntity tile = world.getTileEntity(source);
		if (tile instanceof TileHeatController) {
			TileHeatController controller = (TileHeatController) tile;
			DCHeatTier heat = controller.getActualHeat();
			EnumFacing facing = controller.getFacing(EnumSide.FRONT);
			if (heat != DCHeatTier.NORMAL) {
				// HeatTierController自身 ClimateCheckerなどで参照する
				if (target.equals(source)) return heat;
				// 直上がHeatExchangerの場合、そこに熱を伝える
				if (target.equals(source.up()) && world.getTileEntity(target) instanceof TileHeatExchanger) {
					return heat;
				}
				// 正面1ブロックに限定して熱を伝える
				BlockPos front = source.offset(facing);
				if (target.equals(front)) {
					return heat;
				}
			}
		}
		return DCHeatTier.NORMAL;
	}

}
