package skytheory.hap.item;

import java.util.List;

import javax.annotation.Nullable;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import defeatedcrow.hac.api.magic.CharmType;
import defeatedcrow.hac.api.magic.IJewelCharm;
import defeatedcrow.hac.api.magic.MagicColor;
import defeatedcrow.hac.api.magic.MagicType;
import defeatedcrow.hac.core.ClimateCore;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import skytheory.hap.util.ConstantsHaP;

@Optional.Interface(iface = "baubles.api.IBauble", modid = "baubles")
public class ItemAmuletBR extends Item implements IJewelCharm, IBauble {

	public ItemAmuletBR() {
		this.setMaxStackSize(1);
	}

	@Override
	public IRarity getForgeRarity(ItemStack stack) {
        return EnumRarity.RARE;
    }

	@Override
	public CharmType getCharmType(int meta) {
		return CharmType.SPECIAL;
	}

	@Override
	public MagicType getType(int meta) {
		return MagicType.AMULET;
	}

	@Override
	public MagicColor getColor(int meta) {
		return MagicColor.BLACK_RED;
	}

	@Override
	public float reduceDamage(DamageSource source, ItemStack charm) {
		return 0.0f;
	}

	@Override
	public boolean onDiffence(DamageSource source, EntityLivingBase owner, float damage, ItemStack charm) {
		return false;
	}

	@Override
	public float increaceDamage(EntityLivingBase target, DamageSource source, ItemStack charm) {
		return 1.0f;
	}

	@Override
	public boolean onAttacking(EntityLivingBase owner, EntityLivingBase target, DamageSource source, float damage,
			ItemStack charm) {
		return false;
	}

	@Override
	public boolean onPlayerAttacking(EntityPlayer owner, EntityLivingBase target, DamageSource source, float damage,
			ItemStack charm) {
		return false;
	}

	@Override
	public boolean onToolUsing(EntityLivingBase owner, BlockPos pos, IBlockState state, ItemStack charm) {
		return false;
	}

	@Override
	public void constantEffect(EntityLivingBase owner, ItemStack charm) {
	}

	@Override
	public boolean onUsing(EntityPlayer owner, ItemStack charm) {
		return false;
	}

	@Override
	public boolean isActive(ItemStack charm) {
		return false;
	}

	@Override
	public void setActive(ItemStack charm, boolean flag) {
	}

	@Override
	public ItemStack consumeCharmItem(ItemStack stack) {
		return stack;
	}

	@Override
	public BaubleType getBaubleType(ItemStack arg0) {
		return BaubleType.AMULET;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(TextFormatting.AQUA.toString() + I18n.format(ConstantsHaP.TIP_AMULET_BR_TITLE));
		if (ClimateCore.proxy.isShiftKeyDown()) {
			tooltip.add(TextFormatting.YELLOW.toString() + TextFormatting.BOLD
					.toString() + "=== Tips ===");
			tooltip.add(TextFormatting.YELLOW.toString() + I18n.format(ConstantsHaP.TIP_AMULET_BR_CONTENTS));
		} else {
			tooltip.add("Lshift key: expand tooltip");
		}
	}
}
