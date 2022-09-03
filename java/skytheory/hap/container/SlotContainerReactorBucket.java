package skytheory.hap.container;

import net.minecraftforge.items.IItemHandler;
import skytheory.hap.event.TextureEvent;
import skytheory.lib.container.SlotItemHandler;

public class SlotContainerReactorBucket extends SlotItemHandler {

	private final ContainerReactor container;
	private final int index;

	public SlotContainerReactorBucket(ContainerReactor container, IItemHandler handler, int index, int xPosition, int yPosition) {
		super(handler, index, xPosition, yPosition);
		this.container = container;
		this.index = index;
		this.setTexture(TextureEvent.TEXTURE_BUCKET);
	}

	@Override
	public boolean isEnabled() {
		if (this.index < 4) {
			return !container.fluidContainerSlots.get(index + 4).getHasStack();
		} else {
			return !container.fluidContainerSlots.get(index - 4).isEnabled();
		}
	}
}
