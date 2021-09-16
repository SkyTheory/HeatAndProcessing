package skytheory.hap.tile;

import java.util.List;

import defeatedcrow.hac.api.climate.ClimateAPI;
import defeatedcrow.hac.api.climate.DCHumidity;
import defeatedcrow.hac.config.CoreConfigDC;
import defeatedcrow.hac.main.block.plant.BlockHedge;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.oredict.OreDictionary;
import skytheory.hap.block.BlockWaterCollector;
import skytheory.hap.util.ConstantsHaP;
import skytheory.lib.capability.DataProvider;
import skytheory.lib.capability.fluidhandler.FluidHandler;
import skytheory.lib.network.tile.TileSync;
import skytheory.lib.plugin.waila.IWailaTipTile;
import skytheory.lib.tile.ITileTank;
import skytheory.lib.util.FacingUtils;
import skytheory.lib.util.TextUtils;
import skytheory.lib.util.UpdateFrequency;

public class TileWaterCollector extends TileEntity implements ITickable, ITileTank, ITileInteract, IWailaTipTile {

	// タンクの最大容量
	public static final int CAPACITY = 18000;
	// 300mbごとに描画上の水量を更新する
	public static final int STEP = CAPACITY / 4;
	// 条件を満たしているときに水を増やす量
	public static final int CONDENSE = 10;
	// 判定頻度
	public static final int FREQUENCY = 20;
	// 判定距離
	public static final int RANGE = 3;

	public FluidHandler handler;
	public UpdateFrequency freq = new UpdateFrequency(FREQUENCY);

	@Override
	public ICapabilityProvider createFluidProvider() {
		this.handler = new FluidHandler(CAPACITY)
				.setCanFill(new FluidStack(FluidRegistry.WATER, Fluid.BUCKET_VOLUME))
				.addListener(this);
		return new DataProvider<IFluidHandler>(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, handler);
	}

	@Override
	public void update() {
		if (world.isRemote) return;
		if (this.handler.getFluidAmount() < CAPACITY) {
			if (freq.shouldUpdate()) {
				Block block = world.getBlockState(pos.up()).getBlock();
				if (block == Blocks.WATER || block == Blocks.FLOWING_WATER) {
					this.handler.fill(new FluidStack(FluidRegistry.WATER, CAPACITY), true);
					return;
				}
				DCHumidity humidity = ClimateAPI.calculator.getHumidity(world, pos, CoreConfigDC.ranges[1], false);
				if (humidity == DCHumidity.WET) {
					BlockPos checkPos = this.getPos();
					for (int i = 0; i < RANGE; i++) {
						checkPos = checkPos.up();
						Block checkBlock = world.getBlockState(checkPos).getBlock();
						if (isLeaves(checkBlock)) {
							this.handler.fill(new FluidStack(FluidRegistry.WATER, CONDENSE), true);
							return;
						}
					}
				}
			}
		}
	}

	public static boolean isLeaves(Block block) {
		if (block instanceof BlockHedge) {
			return true;
		}
		ItemStack stack = new ItemStack(block);
		if (!stack.isEmpty()) {
			NonNullList<ItemStack> oreLeaves = OreDictionary.getOres("treeLeaves");
			return oreLeaves.stream().anyMatch(ore -> OreDictionary.itemMatches(ore, stack, false));
		}
		return false;
	}

	@Override
	public boolean onRightClick(World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing facing) {
		if (hand != EnumHand.MAIN_HAND) return false;
		ItemStack stack = player.getHeldItem(hand);
		if (stack.isEmpty()) return false;
		stack = stack.copy();
		IFluidHandlerItem itemFluid = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
		if (itemFluid != null) {
			if (!world.isRemote){
				FluidStack toFill = itemFluid.drain(new FluidStack(FluidRegistry.WATER, CAPACITY), false);
				if (toFill != null) {
					itemFluid.drain(new FluidStack(FluidRegistry.WATER, CAPACITY), true);
					handler.fill(toFill, true);
					world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
					if (!player.isCreative()) {
						stack.shrink(1);
						if (stack.isEmpty()) {
							player.replaceItemInInventory(player.inventory.currentItem, itemFluid.getContainer());
						} else {
							ItemHandlerHelper.giveItemToPlayer(player, itemFluid.getContainer());
						}
					}
				} else {
					FluidStack toDrain = FluidUtil.tryFluidTransfer(itemFluid, handler, CAPACITY, false);
					if (toDrain != null) {
						FluidUtil.tryFluidTransfer(itemFluid, handler, toDrain, true);
						world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
						if (!player.isCreative()) {
							stack.shrink(1);
							if (stack.isEmpty()) {
								player.replaceItemInInventory(player.inventory.currentItem, itemFluid.getContainer());
							} else {
								ItemHandlerHelper.giveItemToPlayer(player, itemFluid.getContainer());
							}
						}
					}
				}
			}
			return true;
		}
		return false;
	}

	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
		return oldState.getBlock() != newSate.getBlock();
	}

	@Override
	public void onFluidHandlerChanged(IFluidHandler handler) {
		if (this.hasWorld() && !this.getWorld().isRemote) {
			this.markDirty();
			int amount = this.handler.getFluidAmount();
			int rate = (amount + STEP - 1) / STEP;
			if (amount == CAPACITY) rate = 5;
			if (world.getBlockState(pos).getValue(BlockWaterCollector.AMOUNT) != rate) {
				world.setBlockState(pos, world.getBlockState(pos).withProperty(BlockWaterCollector.AMOUNT, rate));
			}
		}
	}

	@Override
	public void getWailaTips(ItemStack stack, List<String> tips, IWailaDataAccessor accessor) {
		TileSync.request(this, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, FacingUtils.SET_SINGLE_NULL);
		tips.add(TextUtils.format(ConstantsHaP.TIP_AMOUNT, this.handler.getFluidAmount(), CAPACITY));
	}
}