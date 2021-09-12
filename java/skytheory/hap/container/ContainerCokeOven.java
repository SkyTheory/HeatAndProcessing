package skytheory.hap.container;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraftforge.items.CapabilityItemHandler;
import skytheory.hap.tile.TileCokeOven;
import skytheory.lib.container.ContainerPlayerInventory;
import skytheory.lib.container.SlotHelper;
import skytheory.lib.container.SlotItemHandler;
import skytheory.lib.network.tile.TileSync;

public class ContainerCokeOven extends ContainerPlayerInventory {

	public static final int INPUT_X = 80;
	public static final int INPUT_Y = 58;

	public static final int OUTPUT_X = 134;
	public static final int OUTPUT_Y = 21;

	public final TileCokeOven tile;

	public final List<SlotItemHandler> inputSlots;
	public final List<SlotItemHandler> outputSlots;

	public ContainerCokeOven(EntityPlayer player, TileCokeOven tile) {
		super(player, 8, 84);
		this.tile = tile;
		this.inputSlots = this.addSlotFromInventory(tile.input, INPUT_X, INPUT_Y, 9);
		this.outputSlots = this.addSlotFromInventory(tile.output, OUTPUT_X, OUTPUT_Y, 1);
	}

	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		if (!tile.getWorld().isRemote) {
			TileSync.sendToClient(tile, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
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
		return playerIn.getDistanceSq(x, y, z) <= 64.0D;
	}

}
