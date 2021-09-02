package skytheory.hap.item;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import defeatedcrow.hac.api.item.IPlatingTool;
import defeatedcrow.hac.main.util.DCName;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import skytheory.lib.SkyTheoryLib;

public class ItemCoatingTool extends Item implements IPlatingTool {

	public final Enchantment[] enchantments;

	public ItemCoatingTool(Enchantment... enchantments) {
		this.enchantments = enchantments;
	}

	@Override
	public Enchantment[] getEnchantments(int meta) {
		return enchantments;
	}

	public static boolean canEnchant(ItemStack target, Enchantment enchantment) {
		if (!enchantment.canApply(target)) return false;
		return EnchantmentHelper.getEnchantmentLevel(enchantment, target) < enchantment.getMaxLevel();
	}

	@Override
	public boolean canEnchant(ItemStack target, ItemStack platingTool) {
		if (!target.isEmpty() && !platingTool.isEmpty()) {
			if (target.hasTagCompound() && target.getTagCompound().getByte("dcs.plated") >= 3) return false;
			int meta = platingTool.getItemDamage();
			for (Enchantment enc : getEnchantments(meta)) {
				if (canEnchant(target, enc)) return true;
			}
		}
		return false;
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (!stack.isEmpty()) {
			int meta = stack.getItemDamage();
			Enchantment[] enchantments = this.getEnchantments(meta);
			if (enchantments != null && enchantments.length > 0) {
				tooltip.add(TextFormatting.BOLD.toString() + "Enchantments: ");
				for (Enchantment enchantment : enchantments) {
					tooltip.add(TextFormatting.AQUA.toString() + enchantment.getTranslatedName(1));
				}
			}
			tooltip.add(TextFormatting.YELLOW.toString() + TextFormatting.BOLD.toString() + "=== Tips ===");
			tooltip.add(DCName.ON_ANVIL.getLocalizedName());
		}
	}

	@Override
	public ItemStack getEnchantedItem(ItemStack target, ItemStack platingTool) {
		Enchantment[] enchantments = this.getEnchantments(platingTool.getItemDamage());
		ItemStack result = target.copy();
		Map<Enchantment, Integer> enchantmentMap = EnchantmentHelper.getEnchantments(result);
		for (Enchantment enchantment : enchantments) {
			if (canEnchant(result, enchantment)) {
				int i = EnchantmentHelper.getEnchantmentLevel(enchantment, result);
				enchantmentMap.put(enchantment, i + 1);
			}
		}
		if (!enchantmentMap.isEmpty()) {
			EnchantmentHelper.setEnchantments(enchantmentMap, result);
			if (!result.hasTagCompound()) result.setTagCompound(new NBTTagCompound());
			byte platedCount = result.getTagCompound().getByte("dcs.plated");
			result.getTagCompound().setByte("dcs.plated", (byte) (platedCount + 1));
			return result;
		}

		SkyTheoryLib.LOGGER.warn(String.format("Unable to apply plating tool:%s , %s", platingTool, target));
		return result;
	}

}
