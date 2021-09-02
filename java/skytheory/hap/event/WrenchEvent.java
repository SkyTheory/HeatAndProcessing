package skytheory.hap.event;

import java.util.List;

import defeatedcrow.hac.core.base.BlockContainerDC;
import defeatedcrow.hac.core.energy.BlockTorqueBase;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import skytheory.hap.item.ItemWrench;

public class WrenchEvent {

	public static final int COOLTIME = 5;
	public static int countClient = 0;
	public static int countServer = 0;

	@SubscribeEvent
	public static void onLeftClick(PlayerInteractEvent.LeftClickBlock event) {
		ItemStack stack = event.getItemStack();
		if (stack.getItem() instanceof ItemWrench) {
			EntityPlayer player = event.getEntityPlayer();
			World world = player.world;
			BlockPos pos = event.getPos();
			IBlockState state = player.world.getBlockState(pos);
			Block block = state.getBlock();
			if (block instanceof BlockTorqueBase || block instanceof BlockContainerDC) {
				detachDCDevice(world, pos, state, player, stack);
				removeDCDevice(world, pos, state, player, stack);
				event.setCanceled(true);
			}
		}
	}

	private static void detachDCDevice(World world, BlockPos pos, IBlockState state, EntityPlayer player, ItemStack stack) {
		Block block = state.getBlock();
		if (world.isRemote) {
			SoundType soundtype = block.getSoundType(state, world, pos, player);
			world.playSound(player, pos, soundtype.getBreakSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
		} else {
			if (!player.isCreative()) block.harvestBlock(world, player, pos, state, world.getTileEntity(pos), stack);
			world.setBlockToAir(pos);
		}
	}

	/*
	 * 主にテストワールド用のオマケ機能
	 * クリエイティブの場合、ドロップしたアイテムを消去する
	 * DCTileBlockなどはsetBlockToAirを行っても内容物をドロップさせるため
	 */
	private static void removeDCDevice(World world, BlockPos pos, IBlockState state, EntityPlayer player, ItemStack stack) {
		if (player.isCreative()) {
			Block block = state.getBlock();
			List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos));
			items.forEach(entity -> {
				ItemStack item = entity.getItem();
				ItemStack drop = block.getPickBlock(state, new RayTraceResult(player), world, pos, player);
				if (item.isItemEqual(drop)) {
					entity.setDead();
				}
			});
		}
	}
}
