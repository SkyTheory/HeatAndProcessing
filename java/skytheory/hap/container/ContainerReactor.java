package skytheory.hap.container;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import skytheory.hap.tile.TileReactorAdvanced;
import skytheory.lib.capability.datasync.DataSyncHandler;
import skytheory.lib.container.ContainerPlayerInventory;
import skytheory.lib.container.SlotHelper;
import skytheory.lib.container.SlotItemHandler;
import skytheory.lib.network.tile.TileSync;

public class ContainerReactor extends ContainerPlayerInventory {

	public static final int INPUT_X = 8;
	public static final int INPUT_Y = 36;
	public static final int CATALYST_X = 80;
	public static final int CATALYST_Y = 36;
	public static final int OUTPUT_X = 17;
	public static final int OUTPUT_Y = 82;
	public static final int FLUID_X = 103;
	public static final int FLUID_Y_1 = 12;
	public static final int FLUID_Y_2 = 39;
	public static final int FLUID_Y_3 = 66;
	public static final int FLUID_Y_4 = 93;
	public static final int FILTER_X = 8;
	public static final int FILTER_Y = 12;

	public final TileReactorAdvanced tile;

	public final List<SlotItemHandler> inputSlots;
	public final List<SlotItemHandler> outputSlots;
	public final List<SlotItemHandler> fluidContainerSlots;
	public final SlotItemHandler catalystSlot;
	public final List<SlotItemHandler> filterSlots;

	public ContainerReactor(EntityPlayer player, TileReactorAdvanced tile) {
		super(player, 8, 119);
		this.tile = tile;
		this.inputSlots = this.addSlotFromInventory(tile.itemInput, INPUT_X, INPUT_Y, 4);
		this.catalystSlot = this.addSlotToContainer(tile.itemCatalyst, 0, CATALYST_X, CATALYST_Y);
		this.outputSlots = this.addSlotFromInventory(tile.itemOutput, OUTPUT_X, OUTPUT_Y, 4);
		this.fluidContainerSlots = new ArrayList<SlotItemHandler>();
		this.fluidContainerSlots.add(this.addSlotToContainer(new SlotContainerReactorBucket(this, tile.itemFluidContainer, 0, FLUID_X, FLUID_Y_1)));
		this.fluidContainerSlots.add(this.addSlotToContainer(new SlotContainerReactorBucket(this, tile.itemFluidContainer, 1, FLUID_X, FLUID_Y_2)));
		this.fluidContainerSlots.add(this.addSlotToContainer(new SlotContainerReactorBucket(this, tile.itemFluidContainer, 2, FLUID_X, FLUID_Y_3)));
		this.fluidContainerSlots.add(this.addSlotToContainer(new SlotContainerReactorBucket(this, tile.itemFluidContainer, 3, FLUID_X, FLUID_Y_4)));
		this.fluidContainerSlots.add(this.addSlotToContainer(new SlotContainerReactorBucket(this, tile.itemFluidContainer, 4, FLUID_X, FLUID_Y_1)));
		this.fluidContainerSlots.add(this.addSlotToContainer(new SlotContainerReactorBucket(this, tile.itemFluidContainer, 5, FLUID_X, FLUID_Y_2)));
		this.fluidContainerSlots.add(this.addSlotToContainer(new SlotContainerReactorBucket(this, tile.itemFluidContainer, 6, FLUID_X, FLUID_Y_3)));
		this.fluidContainerSlots.add(this.addSlotToContainer(new SlotContainerReactorBucket(this, tile.itemFluidContainer, 7, FLUID_X, FLUID_Y_4)));
		this.filterSlots = this.addFilterFromInventory(tile.itemFilter, FILTER_X, FILTER_Y, 4);
		for (SlotItemHandler output : outputSlots) {
			output.setFilter(stack -> false);
		}
		this.detectAndSendChanges();
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		if (!this.tile.getWorld().isRemote) {
			TileSync.sendToClient(this.tile, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
			TileSync.sendToClient(this.tile, DataSyncHandler.SYNC_DATA_CAPABILITY);
		}
	}

	protected void updateBucketSlot() {
		for (int i = 0; i < 4; i++) {
			if (tile.itemFluidContainer.getStackInSlot(i + 4).isEmpty()) {
				this.fluidContainerSlots.get(i).enable();
				this.fluidContainerSlots.get(i + 4).disable();
			} else {
				this.fluidContainerSlots.get(i).disable();
				this.fluidContainerSlots.get(i + 4).enable();
			}
		}
	}

	@Override
	public void transferFromPlayerHotBar(EntityPlayer player, Slot slot) {
		SlotHelper.transferStack(slot, inputSlots);
	}

	@Override
	public void transferFromPlayerInventory(EntityPlayer player, Slot slot) {
		SlotHelper.transferStack(slot, inputSlots);
	}

	@Override
	public void transferFromContainer(EntityPlayer player, Slot slot) {
		SlotHelper.transferStack(slot, playerMainInventory);
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		double x = (double) this.tile.getPos().getX() + 0.5d;
		double y = (double) this.tile.getPos().getY() + 0.5d;
		double z = (double) this.tile.getPos().getZ() + 0.5d;
		return playerIn.getDistanceSq(x, y, z) <= 64.0d;
	}

}
