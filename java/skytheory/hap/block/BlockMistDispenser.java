package skytheory.hap.block;

import java.util.List;

import javax.annotation.Nullable;

import defeatedcrow.hac.api.climate.DCHumidity;
import defeatedcrow.hac.api.climate.IHumidityTile;
import defeatedcrow.hac.core.ClimateCore;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import skytheory.hap.tile.TileMistDispenser;
import skytheory.hap.util.ConstantsHaP;
import skytheory.lib.block.IWrenchBlock;
import skytheory.lib.util.EnumSide;

public class BlockMistDispenser extends BlockTorque implements IHumidityTile, IWrenchBlock {

	public BlockMistDispenser() {
		super(Material.ROCK);
		this.setHardness(1.5F);
		this.setSoundType(SoundType.METAL);
		this.lightOpacity = 0;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileMistDispenser();
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public DCHumidity getHumidity(World world, BlockPos target, BlockPos source) {
		TileEntity tile = world.getTileEntity(source);
		if (tile instanceof TileMistDispenser) {
			TileMistDispenser mist = (TileMistDispenser) tile;
			DCHumidity humidity = mist.getHumidity();
			if (target.equals(source) || target.equals(source.offset(mist.getFacing(EnumSide.FRONT)))) {
				return humidity;
			}
		}
		return DCHumidity.NORMAL;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
		if (ClimateCore.proxy.isShiftKeyDown()) {
			tooltip.add(TextFormatting.YELLOW.toString() + TextFormatting.BOLD
					.toString() + "=== Requirement ===");
			tooltip.add(String.format("16.0+ Torque/s"));
			tooltip.add(TextFormatting.YELLOW.toString() + TextFormatting.BOLD
					.toString() + "=== Output ===");
			tooltip.add(String.format("Humidity: " + TextFormatting.AQUA.toString()) + "WET");
			tooltip.add(TextFormatting.YELLOW.toString() + TextFormatting.BOLD.toString() + "=== Tips ===");
			tooltip.add(I18n.format(ConstantsHaP.TIP_MIST));
		} else {
			tooltip.add(TextFormatting.ITALIC.toString() + "=== Lshift key: expand tooltip ===");
		}
		tooltip.add(TextFormatting.BOLD.toString() + "Tier 1");
	}
}
