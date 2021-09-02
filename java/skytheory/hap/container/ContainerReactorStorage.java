package skytheory.hap.container;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import skytheory.hap.tile.TileReactorStorage;
import skytheory.lib.container.ContainerPlayerInventory;
import skytheory.lib.container.SlotHelper;
import skytheory.lib.container.SlotItemHandler;

public class ContainerReactorStorage extends ContainerPlayerInventory {

	public static final int INPUT_X = 8;
	public static final int INPUT_Y = 17;

	public static final int OUTPUT_X = 8;
	public static final int OUTPUT_Y = 65;

	public final TileReactorStorage tile;

	public final List<SlotItemHandler> inputSlots;
	public final List<SlotItemHandler> outputSlots;

	public ContainerReactorStorage(EntityPlayer player, TileReactorStorage tile) {
		super(player, 8, 119);
		this.tile = tile;
		this.inputSlots = this.addSlotFromInventory(tile.input, INPUT_X, INPUT_Y, 9);
		this.outputSlots = this.addSlotFromInventory(tile.output, OUTPUT_X, OUTPUT_Y, 9);
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
		return playerIn.getDistanceSq(x, y, z) <= 64.0D;
	}

}
