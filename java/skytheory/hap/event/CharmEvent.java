package skytheory.hap.event;

import java.util.Comparator;
import java.util.List;

import defeatedcrow.hac.core.util.DCUtil;
import defeatedcrow.hac.main.ClimateMain;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.oredict.OreDictionary;
import skytheory.hap.init.ItemsHaP;

public class CharmEvent {

	private static ItemStack BLUE_BLACK = new ItemStack(ItemsHaP.amulet_ub);
	private static ItemStack BLACK_RED = new ItemStack(ItemsHaP.amulet_br);
	private static ItemStack RED_GREEN = new ItemStack(ItemsHaP.amulet_rg);
	private static ItemStack GREEN_WHITE = new ItemStack(ItemsHaP.amulet_gw);

	@SubscribeEvent
	public static void onPickupItem(EntityItemPickupEvent event) {
		EntityPlayer player = event.getEntityPlayer();
		if (DCUtil.hasCharmItem(player, BLUE_BLACK)) {
			ItemStack stack = event.getItem().getItem();
			if (stack.getItem().getRegistryName().getResourceDomain().equals("minecraft")) return;
			if (stack.isEmpty()) return;
			int[] oreIds = OreDictionary.getOreIDs(stack);
			for (int oreId : oreIds) {
				String oreName = OreDictionary.getOreName(oreId);
				if (oreName.startsWith("ore") || oreName.startsWith("ingot") || oreName.startsWith("dust")) {
					ItemStack converted = getConvertDestination(player, oreName, oreId);
					converted = converted.copy();
					if (!stack.isItemEqual(converted)) {
						event.setCanceled(true);
						converted.setCount(stack.getCount());
						EntityItem entity = event.getItem();
						entity.setItem(ItemStack.EMPTY);
						entity.setDead();
						EntityItem convertedEntity = new EntityItem(player.world, player.posX, player.posY, player.posZ, converted);
						convertedEntity.setNoPickupDelay();
						player.world.spawnEntity(convertedEntity);
						return;
					}
				}
			}
		}
	}

	private static ItemStack getConvertDestination(EntityPlayer player, String oreName, int oreId) {
		NonNullList<ItemStack> ores = OreDictionary.getOres(oreName);
		return ores.stream()
				.sorted(Comparator
						.comparing((ItemStack stack) -> stack.getMaxStackSize())
						.thenComparing((ItemStack stack) -> !stack.getItem().getRegistryName().getResourceDomain().equals(ClimateMain.MOD_ID))
						.thenComparing((ItemStack stack) -> stack.getUnlocalizedName().toString()))
				.findFirst()
				.get();
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public static void onHarvest(BlockEvent.HarvestDropsEvent event) {
		EntityPlayer player = event.getHarvester();
		if (DCUtil.hasCharmItem(player, BLACK_RED)) {
			World world = event.getWorld();
			List<ItemStack> drops = event.getDrops();
			IItemHandler handler = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);
			if (drops instanceof NonNullList) {
				boolean playSound = false;
				for (int i = 0; i < drops.size(); i++) {
					ItemStack drop = drops.get(i);
					if (!drop.isEmpty()) {
						if (world.rand.nextFloat() <= event.getDropChance()) {
							ItemStack remainder = ItemHandlerHelper.insertItemStacked(handler, drop, false);
							if (drop.getCount() != remainder.getCount()) {
								playSound = true;
							}
							drops.set(i, remainder);
						} else {
							drops.set(i, ItemStack.EMPTY);
						}
					}
				}
				if (playSound) {
					world.playSound(null, player.posX, player.posY + 0.5d, player.posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2f, ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7f + 1.0f) * 2.0f);
				}
			}
		}
	}

	@SubscribeEvent
	public static void onRightClick(PlayerInteractEvent.RightClickBlock event) {
		EntityPlayer player = event.getEntityPlayer();
		if (DCUtil.hasCharmItem(player, RED_GREEN)) {
			if (player.getHeldItemMainhand().isEmpty()) {
				World world = event.getWorld();
				BlockPos pos = event.getPos();
				EnumFacing facing = event.getFace();
				if (player.isSneaking()) facing = facing.getOpposite();
				BlockPos target = pos.offset(facing);
				IBlockState targetState = world.getBlockState(target);
				if (world.isAirBlock(target) || targetState.getMobilityFlag() == EnumPushReaction.DESTROY) {
					event.setCanceled(true);
					if (world.isRemote) return;
					if (event.getHand() == EnumHand.OFF_HAND) return;
					if (!player.getPosition().equals(target) && !player.getPosition().up().equals(target)) {
						IBlockState state = world.getBlockState(pos);
						if (BlockPistonBase.canPush(state, world, pos, facing.getOpposite(), false, facing)) {
							Block block = state.getBlock();
							SoundType soundtype = block.getSoundType(state, world, pos, null);
							world.playSound(null, pos, soundtype.getBreakSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
							world.setBlockState(target, state);
							world.setBlockState(pos, Blocks.AIR.getDefaultState());
							if (targetState.getMobilityFlag() == EnumPushReaction.DESTROY) {
								targetState.getBlock().harvestBlock(world, player, target, targetState, null, ItemStack.EMPTY);
							}
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onLeftClick(PlayerInteractEvent.LeftClickBlock event) {
		EntityPlayer player = event.getEntityPlayer();
		if (player.isCreative()) return;
		if (player.isSneaking()) return;
		if (event.getHand() == EnumHand.OFF_HAND) return;
		ItemStack stack = event.getItemStack();
		if (stack.getItem() instanceof ItemTool) {
			if (DCUtil.hasCharmItem(player, GREEN_WHITE)) {
				World world = event.getWorld();
				BlockPos pos = event.getPos();
				IBlockState state = world.getBlockState(pos);
				String effectiveTool = state.getBlock().getHarvestTool(state);
				if (!stack.getItem().getToolClasses(stack).contains(effectiveTool)) {
					for (int i = 0; i < player.inventory.mainInventory.size(); i++) {
						ItemStack slotStack = player.inventory.getStackInSlot(i);
						if (slotStack.isEmpty()) continue;
						if (slotStack.getItem() instanceof ItemTool) {
							if (slotStack.getItem().getToolClasses(slotStack).contains(effectiveTool)) {
								player.inventory.setInventorySlotContents(i, stack);
								player.inventory.setInventorySlotContents(player.inventory.currentItem, slotStack);
								event.setCanceled(true);
								return;
							}
						}
					}
				}
			}
		}
	}
}
