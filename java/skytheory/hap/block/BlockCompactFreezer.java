package skytheory.hap.block;

import java.util.List;

import javax.annotation.Nullable;

import defeatedcrow.hac.core.ClimateCore;
import defeatedcrow.hac.main.util.DCName;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import skytheory.hap.tile.TileCompactFreezer;
import skytheory.hap.util.ConstantsHaP;

public class BlockCompactFreezer extends BlockHeatController {

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileCompactFreezer();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
		if (ClimateCore.proxy.isShiftKeyDown()) {
			tooltip.add(TextFormatting.YELLOW.toString() + TextFormatting.BOLD
					.toString() + "=== Requirement ===");
			tooltip.add(String.format(DCName.STAGE.getLocalizedName() + "1: 8.0+ Torque/s"));
			tooltip.add(String.format(DCName.STAGE.getLocalizedName() + "2: 16.0+ Torque/s"));
			tooltip.add(String.format(DCName.STAGE.getLocalizedName() + "3: 32.0+ Torque/s"));
			tooltip.add(String.format(DCName.STAGE.getLocalizedName() + "4: 64.0+ Torque/s"));
			tooltip.add(TextFormatting.YELLOW.toString() + TextFormatting.BOLD
					.toString() + "=== Output ===");
			tooltip.add(String.format(DCName.STAGE.getLocalizedName() + "1 HeatTier: " + TextFormatting.AQUA.toString() + "COLD"));
			tooltip.add(String.format(DCName.STAGE.getLocalizedName() + "2 HeatTier: " + TextFormatting.AQUA.toString() + "FROSTBITE"));
			tooltip.add(String.format(DCName.STAGE.getLocalizedName() + "3 HeatTier: " + TextFormatting.AQUA.toString() + "CRYOGENIC"));
			tooltip.add(String.format(DCName.STAGE.getLocalizedName() + "4 HeatTier: " + TextFormatting.AQUA.toString() + "ABSOLUTE"));
			tooltip.add(TextFormatting.YELLOW.toString() + TextFormatting.BOLD.toString() + "=== Tips ===");
			tooltip.add(I18n.format(ConstantsHaP.TIP_FREEZER));
		} else {
			tooltip.add(TextFormatting.ITALIC.toString() + "=== Lshift key: expand tooltip ===");
		}
		tooltip.add(TextFormatting.BOLD.toString() + "Tier 3");
	}
}
