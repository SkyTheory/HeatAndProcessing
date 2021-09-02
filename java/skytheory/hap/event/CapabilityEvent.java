package skytheory.hap.event;

import defeatedcrow.hac.api.energy.capability.ITorqueHandler;
import defeatedcrow.hac.api.energy.capability.TorqueCapabilityHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import skytheory.hap.HeatAndProcessing;
import skytheory.hap.capability.TorqueData;
import skytheory.hap.tile.ITorqueTile;
import skytheory.lib.capability.DataProvider;

public class CapabilityEvent {

	public static final ResourceLocation KEY_TORQUE = new ResourceLocation(HeatAndProcessing.MOD_ID, "Torque");

	@SubscribeEvent
	public static void onAttachCapabilityTileEvent(AttachCapabilitiesEvent<TileEntity> event) {
		TileEntity tile = event.getObject();
		if (tile instanceof ITorqueTile) {
			ITorqueTile torque = (ITorqueTile) tile;
			ITorqueHandler handler = new TorqueData(torque);
			ICapabilityProvider provider = new DataProvider<ITorqueHandler>(TorqueCapabilityHandler.TORQUE_HANDLER_CAPABILITY, handler);
			event.addCapability(KEY_TORQUE, provider);
		}
	}
}
