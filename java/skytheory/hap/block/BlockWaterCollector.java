package skytheory.hap.block;

import java.util.List;

import javax.annotation.Nullable;

import defeatedcrow.hac.api.climate.DCHumidity;
import defeatedcrow.hac.api.climate.IHumidityTile;
import defeatedcrow.hac.core.ClimateCore;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import skytheory.hap.tile.TileWaterCollector;
import skytheory.hap.util.ConstantsHaP;

public class BlockWaterCollector extends BlockContainer implements IHumidityTile {

	public static PropertyInteger AMOUNT = PropertyInteger.create("amount", 0, 5);

	public BlockWaterCollector() {
		super(Material.IRON);
		this.setHardness(1.0F);
		this.setDefaultState(this.blockState.getBaseState().withProperty(AMOUNT, 0));
	}

	public IBlockState getStateFromMeta(int meta) {
		IBlockState state = this.getDefaultState();
		return state.withProperty(AMOUNT, MathHelper.clamp(meta, 0, 5));
	}

	public int getMetaFromState(IBlockState state) {
		return state.getValue(AMOUNT);
	}

	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		TileEntity tile = worldIn.getTileEntity(pos);
		if (tile instanceof TileWaterCollector) {
			return ((TileWaterCollector) tile).onRightClick(worldIn, pos, playerIn, hand, facing);
		}
		return false;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {
				AMOUNT
		});
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileWaterCollector();
	}

	@Override
	public DCHumidity getHumidity(World world, BlockPos target, BlockPos source) {
		if (world.getBlockState(source).getBlock() == this) {
			if (world.getBlockState(source).getValue(AMOUNT) != 0) {
				if (!target.equals(source)) return DCHumidity.WET;
			}
		}
		return DCHumidity.NORMAL;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
		if (ClimateCore.proxy.isShiftKeyDown()) {
			tooltip.add(TextFormatting.YELLOW.toString() + TextFormatting.BOLD.toString() + "=== Requirement ===");
			tooltip.add(String.format("Humidity: " + TextFormatting.AQUA.toString() + "WET"));
			tooltip.add(TextFormatting.YELLOW.toString() + TextFormatting.BOLD.toString() + "=== Tips ===");
			tooltip.add(I18n.format(ConstantsHaP.TIP_WATER_COLLECTOR));
		} else {
			tooltip.add(TextFormatting.ITALIC.toString() + "=== Lshift key: expand tooltip ===");
		}
	}
}
