package skytheory.hap.item;

import defeatedcrow.hac.api.energy.IWrenchDC;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import skytheory.lib.item.IWrench;
import skytheory.lib.util.IWrenchType;
import skytheory.lib.util.WrenchRegistry;

/**
 * ツルハシ特性を失った代わりに無限耐久を得たレンチ
 * クリックによる処理はWrenchEventの方を参照
 * デバイスの向きの反転だけでなく、回転も可能
 */
public class ItemWrench extends Item implements IWrench, IWrenchDC {

	public ItemWrench() {
		// ツールとしての特性を失い、代わりにEventの方でブロックの即時回収を行うように変更
		// まあ、無限耐久の鋼鉄ツルハシとして使えたらバランスに大打撃を与えるので……
		this.setFull3D();
		this.setMaxStackSize(1);
	}

	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		IWrenchType type = WrenchRegistry.getType(world.getBlockState(pos).getBlock());
		if (!type.skipActivateBlock(player, world, pos, side)) {
			return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
		}
		return EnumActionResult.SUCCESS;
	}

	// アイテムの耐久は減らさない
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		return true;
	}

	// アイテムの耐久は減らさない
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving){
		return true;
	}

}