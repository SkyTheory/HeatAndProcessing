package skytheory.hap.tile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import defeatedcrow.hac.api.climate.DCHeatTier;
import defeatedcrow.hac.api.recipe.IReactorRecipe;
import defeatedcrow.hac.api.recipe.RecipeAPI;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.oredict.OreDictionary;
import skytheory.hap.HeatAndProcessing;
import skytheory.hap.init.GuiHandler;
import skytheory.hap.util.ConstantsHaP;
import skytheory.lib.capability.DataProviderSided;
import skytheory.lib.capability.datasync.DataSyncHandler;
import skytheory.lib.capability.datasync.IDataSync;
import skytheory.lib.capability.fluidhandler.FluidAccessor;
import skytheory.lib.capability.fluidhandler.FluidHandler;
import skytheory.lib.capability.fluidhandler.FluidProviderSided;
import skytheory.lib.capability.itemhandler.ItemAccessor;
import skytheory.lib.capability.itemhandler.ItemHandler;
import skytheory.lib.capability.itemhandler.ItemProviderSided;
import skytheory.lib.network.tile.TileSync;
import skytheory.lib.tile.ITileInventory;
import skytheory.lib.tile.ITileTank;
import skytheory.lib.util.EnumSide;
import skytheory.lib.util.FacingUtils;
import skytheory.lib.util.ItemHandlerUtils;
import skytheory.lib.util.TextUtils;
import skytheory.lib.util.UpdateFrequency;


public class TileReactorAdvanced extends TileTorqueDirectional implements ITickable, ITileInventory, ITileTank, ITileInteract, IDataSync {

	public static final String KEY_TANK_ITEMS = "TankItems";
	public static final String KEY_TANK_LOCK = "TankLock";
	public static final String KEY_TANK_STACKS = "TankStack";
	public static final String KEY_HEAT_TIER = "HeatTier";
	public static final String KEY_PROGRESS = "Progress";
	public static final int TANK_CAPACITY = 4000;

	// Reactorの所要時間はレシピに係わらず、2tick毎の受付、1024Torqueで一定
	// と言いたいところだけれど、それより3Tickほど遅い模様。どこの処理が原因だー……？
	// 覚書： 1Tickごとに受付するなら2048Torqueだが、負荷の様子を見て調整すること
	public static final float TORQUE_PROCESS = 2048.0f;
	// 加工に必要な最低限のTorque
	public static final float TORQUE_REQUIRED = 64.0f;

	// 覚書：イニシャライザでの宣言だとEventに間に合わないため、Event内で呼び出すcreateProviderで作成
	public IItemHandler itemInput;
	public IItemHandler itemOutput;
	public IItemHandler itemFluidContainer;
	public IItemHandler itemCatalyst;
	public IItemHandler itemFilter;

	public float prevProgress = 0.0f;
	public float progress = 0.0f;
	private DCHeatTier heatTier = DCHeatTier.NORMAL;

	// 現在利用中のリアクターレシピ
	private IReactorRecipe recipe;

	public FluidTank tankInput1;
	public FluidTank tankInput2;
	public FluidTank tankOutput1;
	public FluidTank tankOutput2;

	public FluidTank[] tanks;
	private boolean[] tankLock = {false, false, false, false};
	private FluidStack[] tankFilter = new FluidStack[4];

	public boolean skipTransport = false;
	public boolean skipInputItem = false;
	public boolean skipOutputItem = false;
	public boolean skipInputFluid = false;
	public boolean skipOutputFluid = false;
	// インベントリに変更があるまで流体のアイテム・タンク間の輸送をスキップするフラグ
	public boolean skipInternalTank = false;
	public boolean[] skipInternalTanks = {true, true, true, true};
	// 出力先に空きができるまで加工をスキップするフラグ
	public boolean skipProcessItem;
	// レシピ判定をスキップするフラグ
	public boolean skipRecipe;

	// アイテムの取り寄せ周期
	public UpdateFrequency freq = new UpdateFrequency(16);

	@Override
	public void setWorld(World worldIn) {
		super.setWorld(worldIn);
		if (worldIn.isRemote) {
			TileSync.request(this, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, FacingUtils.SET_SINGLE_NULL);
		}
	}

	@Override
	public ICapabilityProvider createInventoryProvider() {
		this.itemInput = new ItemHandler(4)
				.setCanInsert((slot, stack) -> {
					if (ItemHandlerUtils.isEmpty(this.itemFilter)) return true;
					ItemStack filterItem = itemFilter.getStackInSlot(slot);
					return ItemStack.areItemsEqual(stack, filterItem);
				})
				.addListener(this);

		this.itemOutput = new ItemHandler(4)
				.addListener(this);

		this.itemFluidContainer = new ItemHandler(4)
				.setCanInsert((slot, stack) -> {
					return stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
				})
				.setSlotLimit(1)
				.addListener(this);

		this.itemCatalyst = new ItemHandler(1)
				.addListener(this);

		this.itemFilter = new ItemHandler(4)
				.addListener(this);

		IItemHandler accessInput = ItemAccessor.insertOnly(itemInput);
		IItemHandler accessOutput = ItemAccessor.extractOnly(itemOutput);

		DataProviderSided<IItemHandler> itemHandlerProvider =
				new ItemProviderSided(this, itemInput, itemOutput, itemFluidContainer, itemCatalyst, itemFilter);

		itemHandlerProvider.addDataToSide(accessInput, EnumSide.LEFT);
		itemHandlerProvider.addDataToSide(accessOutput, EnumSide.LEFT);

		// 利便性確保のために左面以外からもOutputだけはアクセスできるようにしておく
		itemHandlerProvider.addDataToSide(accessOutput, EnumSide.BOTTOM);
		itemHandlerProvider.addDataToSide(accessOutput, EnumSide.TOP);
		itemHandlerProvider.addDataToSide(accessOutput, EnumSide.FRONT);
		itemHandlerProvider.addDataToSide(accessOutput, EnumSide.RIGHT);
		itemHandlerProvider.addDataToSide(accessOutput, EnumSide.LEFT);

		return itemHandlerProvider;
	}

	@Override
	public ICapabilityProvider createFluidProvider() {
		this.tankInput1 = new FluidHandler(TANK_CAPACITY)
				.addListener(this)
				.setCanFill(stack -> {
					if (!tankLock[0]) return true;
					return tankFilter[0] != null && tankFilter[0].isFluidEqual(stack);
				});

		this.tankInput2 = new FluidHandler(TANK_CAPACITY)
				.addListener(this)
				.setCanFill(stack -> {
					if (!tankLock[1]) return true;
					return tankFilter[1] != null && tankFilter[1].isFluidEqual(stack);
				});

		this.tankOutput1 = new FluidHandler(TANK_CAPACITY)
				.addListener(this)
				.setCanFill(stack -> {
					if (!tankLock[2]) return true;
					return tankFilter[2] != null && tankFilter[2].isFluidEqual(stack);
				});

		this.tankOutput2 = new FluidHandler(TANK_CAPACITY)
				.addListener(this)
				.setCanFill(stack -> {
					if (!tankLock[3]) return true;
					return tankFilter[3] != null && tankFilter[3].isFluidEqual(stack);
				});


		this.tanks = new FluidTank[]{tankInput1, tankInput2, tankOutput1, tankOutput2};

		FluidProviderSided fluidProvider = new FluidProviderSided(this, tankInput1, tankInput2, tankOutput1, tankOutput2);

		IFluidHandler accessorTank1 = FluidAccessor.fillOnly(tankInput1);
		IFluidHandler accessorTank2 = FluidAccessor.fillOnly(tankInput2);
		IFluidHandler accessorTank3 = FluidAccessor.drainOnly(tankOutput1);
		IFluidHandler accessorTank4 = FluidAccessor.drainOnly(tankOutput2);

		fluidProvider.addDataToSide(accessorTank1, EnumSide.LEFT);
		fluidProvider.addDataToSide(accessorTank2, EnumSide.LEFT);
		fluidProvider.addDataToSide(accessorTank3, EnumSide.LEFT);
		fluidProvider.addDataToSide(accessorTank4, EnumSide.LEFT);

		return fluidProvider;
	}

	public FluidTank getTank(int index) {
		return tanks[index];
	}

	public float getTorqueProcess() {
		return TORQUE_PROCESS;
	}

	public float getTorqueRequired() {
		return TORQUE_REQUIRED;
	}

	@SideOnly(Side.CLIENT)
	public void setHeatTier(DCHeatTier heat) {
		this.heatTier = heat;
		TileSync.sendToServer(this, DataSyncHandler.SYNC_DATA_CAPABILITY);
	}

	@SideOnly(Side.CLIENT)
	public void incrHeatTier() {
		this.setHeatTier(this.heatTier.addTier(1));
	}

	@SideOnly(Side.CLIENT)
	public void decrHeatTier() {
		this.setHeatTier(this.heatTier.addTier(-1));
	}

	public DCHeatTier getHeatTier() {
		return this.heatTier;
	}

	public static long time;

	public void update() {
		super.update();
		if (!world.isRemote) {
			this.updateInternalTank();
			if (skipTransport) {
				// レシピが利用できるか、変更されていないかをチェック
				this.updateProcessing();
			} else {
				this.skipTransport = true;
				EnumFacing facing = this.getFacing(EnumSide.LEFT);
				TileEntity tile = world.getTileEntity(this.getPos().offset(this.getFacing(EnumSide.LEFT)));
				// 内部タンクのインベントリへ液体受け渡し
				if (tile != null) {
					IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite());
					IFluidHandler fluidHandler = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite());
					// アイテムの自動搬入処理
					this.updateInput(itemHandler, fluidHandler);
					// レシピが利用できるか、変更されていないかをチェック
					this.updateProcessing();
					// アイテムの自動搬出処理
					this.updateOutput(itemHandler, fluidHandler);
				}
			}
		}
	}

	public void updateInput(IItemHandler itemHandler, IFluidHandler fluidHandler) {
		if (!skipInputItem && itemHandler != null) {
			this.skipInputItem = true;
			if (!ItemHandlerUtils.isEmpty(itemFilter)) {
				retrieveItem(itemHandler);
			}
		}
		if (!skipInputFluid && fluidHandler != null) {
			this.skipInputFluid = true;
			retrieveFluid(fluidHandler);
		}
	}

	public void retrieveItem(IItemHandler handler) {
		ItemHandlerUtils.transfer(handler, itemInput, 1);
	}

	public void retrieveFluid(IFluidHandler handler) {
		if (tankLock[0] && tankFilter[0] != null) {
			retrieveFluid(handler, tanks[0], tankFilter[0].copy());
		}
		if (tankLock[1] && tankFilter[1] != null) {
			retrieveFluid(handler, tanks[1], tankFilter[1].copy());
		}
	}

	public void retrieveFluid(IFluidHandler handler, IFluidHandler tank, FluidStack stack) {
		FluidStack fs = stack.copy();
		fs.amount = TANK_CAPACITY;
		FluidUtil.tryFluidTransfer(tank, handler, fs, true);
	}

	public void updateInternalTank() {
		if (skipInternalTank) return;
		for (int i = 0; i < tanks.length; i++) {
			if (!skipInternalTanks[i]) {
				this.transferFluid(i);
				this.skipInternalTanks[i] = true;
			}
		}
		this.skipInternalTank = true;
	}

	public void transferFluid(int index) {
		ItemStack stack = this.itemFluidContainer.getStackInSlot(index).copy();
		if (!stack.isEmpty()) {
			IFluidHandlerItem itemFluid = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
			// リアクターへの充填を優先
			if (itemFluid != null) {
				int amount = tanks[index].getFluidAmount();
				stack = fillTank(stack, index);
				if (tanks[index].getFluidAmount() == amount) {
					stack = drainTank(stack, index);
				}
				((IItemHandlerModifiable) this.itemFluidContainer).setStackInSlot(index, stack);
			}
		}
	}

	public ItemStack fillTank(ItemStack stack, int index) {
		FluidTank tank = tanks[index];
		ItemStack container = stack.copy();
		IFluidHandlerItem itemFluid = container.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
		if (itemFluid == null) return container;
		if (tankLock[index]) {
			FluidStack filter = tankFilter[index];
			if (filter == null) return container;
			if (!filter.isFluidEqual(container)) return container;
		}
		FluidUtil.tryFluidTransfer(tank, itemFluid, TANK_CAPACITY, true);
		return itemFluid.getContainer();
	}

	public ItemStack drainTank(ItemStack stack, int index) {
		FluidTank tank = tanks[index];
		ItemStack container = stack.copy();
		IFluidHandlerItem itemFluid = container.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
		if (itemFluid == null) return container;
		FluidUtil.tryFluidTransfer(itemFluid, tank, TANK_CAPACITY, true);
		return itemFluid.getContainer();
	}

	public void updateProcessing() {
		// レシピに変更がないかをチェック
		if ((skipRecipe || this.validateRecipe())) {
			if (recipe != null) {
				this.prevProgress = progress;
				if (progress < this.getTorqueProcess() || skipProcessItem) {
					// Torqueが必要値を満たしているかをチェック
					if (this.getCurrentTorque() >= this.getTorqueRequired()) {
						// 満たしていれば、その分を進捗に追加
						this.progress = Math.min(progress + this.getCurrentTorque(), TORQUE_PROCESS);
					} else {
						// 満たしていなければ、ゆっくりと減衰させる
						this.progress = Math.max(progress - 16.0f, 0.0f);
					}
					if (skipProcessItem && freq.shouldUpdate()) {
						this.skipTransport = false;
						this.skipOutputItem = false;
						this.skipOutputFluid = false;
					}
				} else {
					// Torqueが必要値に達していれば加工を行う
					if (this.onProcess()) {
						// 加工に成功したなら進捗をリセット
						this.progress = 0.0f;
					} else {
						// 失敗したなら待機フラグを入れる
						this.skipProcessItem = true;
					}
				}
				if (prevProgress != progress) {
					TileSync.sendToClient(this, DataSyncHandler.SYNC_DATA_CAPABILITY);
				}
			} else {
				if (freq.shouldUpdate()) {
					this.skipTransport = false;
					this.skipInputItem = false;
					this.skipInputFluid = false;
				}
			}
		} else if (progress != 0.0f) {
			// レシピに変更があった場合は進捗をリセット
			this.progress = 0.0f;
			TileSync.sendToClient(this, DataSyncHandler.SYNC_DATA_CAPABILITY);
		}
	}

	/*
	 * インベントリからレシピを判定し、進捗をリセットするかどうかを返す
	 */
	public boolean validateRecipe() {
		this.skipRecipe = true;
		this.skipProcessItem = false;
		DCHeatTier heat = this.getHeatTier();
		FluidStack fluid1 = tankInput1.getFluid();
		FluidStack fluid2 = tankInput2.getFluid();
		// HaCのリアクターは配置によってレシピが反応したりしなかったりするので……。
		// 若干レシピ判定処理を変更しておく
		List<ItemStack> ingredients =
				ItemHandlerUtils.getItemStackList(this.itemInput).stream()
				.sorted(Comparator.comparing(stack -> stack.getUnlocalizedName()))
				.collect(Collectors.toList());
		ItemStack catalyst = this.itemCatalyst.getStackInSlot(0);
		IReactorRecipe next = RecipeAPI.registerReactorRecipes.getRecipe(heat, ingredients, fluid1, fluid2, catalyst);
		IReactorRecipe prev = this.recipe;
		this.recipe = next;
		if (recipe != null) {
			return prev == null || prev == next;
		}
		return false;
	}

	/*
	 * 素材を加工して完成品スロットに送り出す
	 */
	public boolean onProcess() {
		ItemStack resultItem1 = recipe.getOutput();
		ItemStack resultItem2 = recipe.getSecondary();
		FluidStack resultFluid1 = recipe.getOutputFluid();
		FluidStack resultFluid2 = recipe.getSubOutputFluid();
		// 出力先に空きがあるかのチェック処理
		// アイテム部分
		List<ItemStack> list = new ArrayList<>();
		ItemHandlerUtils.iterator(itemOutput).forEach((ItemHandlerUtils.SlotProperties slot) -> list.add(slot.getStack()));
		ItemStack[] mostacks = new ItemStack[list.size()];
		mostacks = list.toArray(mostacks);
		NonNullList<ItemStack> nnl = NonNullList.from(ItemStack.EMPTY, mostacks);
		if (!recipe.matchOutput(nnl, tankOutput1.getFluid(), tankOutput2.getFluid(), nnl.size())) return false;
		// 液体部分
		if (resultFluid1 != null) {
			if (tankOutput1.fill(resultFluid1, false) != resultFluid1.amount) return false;
		}
		if (resultFluid2 != null) {
			if (tankOutput2.fill(resultFluid2, false) != resultFluid2.amount) return false;
		}
		// 実際の加工処理
		this.consumeItems(recipe.getProcessedInput());
		this.consumeFluid(recipe.getInputFluid(), tankInput1);
		this.consumeFluid(recipe.getSubInputFluid(), tankInput2);
		ItemHandlerHelper.insertItem(itemOutput, resultItem1, false);
		// nextFloatでもいいと思うけれど、一応計算式は本家リアクターと同じにしておく
		if (world.rand.nextInt(100) < MathHelper.ceil(recipe.getSecondaryChance() * 100.0f)) {
			ItemHandlerHelper.insertItem(itemOutput, resultItem2, false);
		}
		if (resultFluid1 != null) this.tankOutput1.fill(resultFluid1, true);
		if (resultFluid2 != null) this.tankOutput2.fill(resultFluid2, true);
		return true;
	}

	@SuppressWarnings("unchecked")
	public void consumeItems(List<Object> items) {
		if (items.isEmpty()) return;
		if (!items.isEmpty()) {
			for (Object obj : items) {
				if (obj instanceof ItemStack) {
					consumeItem(Collections.singletonList((ItemStack) obj));
				} else if (obj instanceof List) {
					consumeItem((List<ItemStack>) obj);
				}
			}
		}

	}

	public void consumeItem(List<ItemStack> list) {
		if (list.isEmpty()) return;
		for (ItemStack stack : list) if (this.consumeItem(stack)) return;
		String msg = "";
		for (ItemStack stack : list) msg = msg.concat(stack.toString() + ", ");
		msg = msg.substring(0, msg.length() - 2);
		throw new RuntimeException(String.format("Unable consume ingredient: %s", msg));
	}

	public boolean consumeItem(ItemStack stack) {
		for (int i = 0; i < itemInput.getSlots(); i++) {
			ItemStack slotItem = itemInput.getStackInSlot(i);
			if (OreDictionary.itemMatches(stack, slotItem, false)) {
				if (!itemInput.extractItem(i, 1, false).isEmpty()) return true;
			}
		}
		return false;
	}

	public void consumeFluid(FluidStack require, IFluidHandler handler) {
		if (require == null) return;
		FluidStack drained1 = handler.drain(require, true);
		if (drained1 == null || drained1.amount < require.amount) {
			throw new RuntimeException(String.format("Unable consume fluid: %s", require.getUnlocalizedName()));
		}
	}

	public void updateOutput(IItemHandler itemHandler, IFluidHandler fluidHandler) {
		if (!skipOutputItem && itemHandler != null) {
			this.skipOutputItem = true;
			this.sendItem(itemHandler);
		}
		if (!skipOutputFluid && fluidHandler != null) {
			this.skipOutputFluid = true;
			this.sendFluid(fluidHandler);
		}
	}

	public void sendItem(IItemHandler handler) {
		ItemHandlerUtils.transfer(itemOutput, handler, 1);
	}

	public void sendFluid(IFluidHandler handler) {
		FluidUtil.tryFluidTransfer(handler, tankOutput1, tankOutput1.getFluid(), true);
		FluidUtil.tryFluidTransfer(handler, tankOutput2, tankOutput2.getFluid(), true);
	}

	@Override
	public boolean isInputSide(EnumSide side) {
		return side == EnumSide.BACK;
	}

	@Override
	public boolean isOutputSide(EnumSide side) {
		return false;
	}

	@Override
	public float getMaxTorque() {
		return 1024.0f;
	}

	@Override
	public boolean onRightClick(World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing facing) {
		if (hand == EnumHand.MAIN_HAND) {
			player.openGui(HeatAndProcessing.INSTANCE, GuiHandler.TILE_GUI, world, pos.getX(), pos.getY(), pos.getZ());
			return true;
		}
		return false;
	}

	public boolean getLockState(int index) {
		return tankLock[index];
	}

	public FluidStack getFilter(int index) {
		return tankFilter[index];
	}

	@SideOnly(Side.CLIENT)
	public void updateLock(int index, boolean state) {
		tankLock[index] = state;
		if (state) {
			FluidStack stack = tanks[index].getFluid();
			if (stack != null) {
				stack = stack.copy();
				stack.amount = 0;
			}
			this.tankFilter[index] = stack;
		}
		TileSync.sendToServer(this, DataSyncHandler.SYNC_DATA_CAPABILITY);
	}

	@Override
	public NBTTagCompound serializeSync() {
		NBTTagCompound compound = new NBTTagCompound();
		byte flags = 0;
		for (int i = 0; i < tankLock.length; i++) if (tankLock[i]) flags |= (1 << i);
		compound.setByte(KEY_TANK_LOCK, flags);
		this.serializeFilter(compound);
		compound.setFloat(KEY_PROGRESS, progress);
		compound.setInteger(KEY_HEAT_TIER, this.getHeatTier().getID());
		return compound;
	}

	@Override
	public void deserializeSync(NBTTagCompound compound) {
		byte flags = compound.getByte(KEY_TANK_LOCK);
		for (int i = 0; i < tankLock.length; i++) tankLock[i] = (flags & (1 << i)) != 0;
		this.deserializeFilter(compound);
		this.progress = MathHelper.clamp(compound.getFloat(KEY_PROGRESS), 0.0f, TORQUE_PROCESS);
		if (compound.hasKey(KEY_HEAT_TIER, Constants.NBT.TAG_INT)) {
			this.heatTier = DCHeatTier.getTypeByID(compound.getInteger(KEY_HEAT_TIER));
		}
		this.skipProcessItem = false;
		this.skipRecipe = false;
		this.markDirty();
	}

	public void serializeFilter(NBTTagCompound compound) {
		NBTTagList tags = new NBTTagList();
		for (int i = 0; i < tankFilter.length; i++) {
			NBTTagCompound fluidTag = new NBTTagCompound();
			if (tankLock[i] && tankFilter[i] != null) tankFilter[i].writeToNBT(fluidTag);
			tags.appendTag(fluidTag);
		}
		compound.setTag(KEY_TANK_STACKS, tags);
	}

	public void deserializeFilter(NBTTagCompound compound) {
		NBTTagList tags = compound.getTagList(KEY_TANK_STACKS, NBT.TAG_COMPOUND);
		for (int i = 0; i < tankFilter.length; i++) {
			if (i >= tags.tagCount()) break;
			if (tankLock[i]) this.tankFilter[i] = FluidStack.loadFluidStackFromNBT(tags.getCompoundTagAt(i));
		}
	}

	@Override
	public void onItemHandlerChanged(IItemHandler handler, int slot) {
		this.markDirty();
		if (!world.isRemote) {
			if (handler == this.itemFluidContainer) {
				this.skipInternalTank = false;
				this.skipInternalTanks[slot] = false;
			}
			if (handler == this.itemInput) {
				this.skipInputItem = false;
			}
			if (handler == this.itemOutput) {
				this.skipOutputItem = false;
				this.skipProcessItem = false;
			}
			this.skipTransport = false;
			this.skipRecipe = false;
			TileSync.sendToClient(this, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
		}
	}

	@Override
	public void onFluidHandlerChanged(IFluidHandler handler) {
		this.markDirty();
		if (!world.isRemote) {
			if (handler == this.tankInput1 || handler == this.tankInput2) {
				this.skipInputFluid = false;
			}
			if (handler == this.tankOutput1 || handler == this.tankOutput2) {
				this.skipOutputFluid = false;
				this.skipProcessItem = false;
			}
			this.skipTransport = false;
			this.skipRecipe = false;
			TileSync.sendToClient(this, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
		}
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}

	@Override
	public void getWailaTips(ItemStack stack, List<String> tips, IWailaDataAccessor accessor) {
		super.getWailaTips(stack, tips, accessor);
		float torque = this.getCurrentTorque();
		if (torque >= TORQUE_REQUIRED) {
			int percentage = MathHelper.floor((progress / this.getTorqueProcess()) * 100.0f);
			tips.add(TextUtils.format(ConstantsHaP.TIP_ACTIVE));
			tips.add(TextUtils.format(ConstantsHaP.TIP_PROGRESS, percentage));
		} else {
			tips.add(TextUtils.format(ConstantsHaP.TIP_SHORTAGE));
			tips.add(TextUtils.format(ConstantsHaP.TIP_REQUIRED, String.format("%.2f", this.getTorqueRequired())));
		}
	}
}
