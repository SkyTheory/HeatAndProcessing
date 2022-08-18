package skytheory.hap.util;

import defeatedcrow.hac.core.base.DCSidedBlock;
import defeatedcrow.hac.machine.MachineInit;
import defeatedcrow.hac.machine.block.BlockHopperFilter;
import defeatedcrow.hac.main.MainInit;
import defeatedcrow.hac.main.block.build.BlockSlabDC;
import skytheory.lib.util.WrenchRegistry;
import skytheory.lib.util.WrenchTypes;

public class WrenchRegister {

	public static void register() {

		WrenchRegistry.register(MachineInit.tankYard, WrenchTypes.NONE);
		WrenchRegistry.register(MachineInit.tankYardPart, WrenchTypes.NONE);
		WrenchRegistry.register(MachineInit.burner, WrenchTypes.NONE);
		WrenchRegistry.register(MainInit.mcClock_L, WrenchTypes.NONE);
		WrenchRegistry.register(MainInit.realtimeClock_L, WrenchTypes.NONE);

		WrenchRegistry.register(MainInit.bellow, WrenchTypesHaP.DC_ALL_FACINGS);
		WrenchRegistry.register(MainInit.geyser, WrenchTypesHaP.DC_ALL_FACINGS);
		WrenchRegistry.register(MainInit.lampCarbide, WrenchTypesHaP.DC_ALL_FACINGS);
		WrenchRegistry.register(MainInit.lampGas, WrenchTypesHaP.DC_ALL_FACINGS);
		WrenchRegistry.register(MachineInit.boilerTurbine, WrenchTypesHaP.DC_ALL_FACINGS);
		WrenchRegistry.register(MachineInit.dieselEngine, WrenchTypesHaP.DC_ALL_FACINGS);
		WrenchRegistry.register(MachineInit.fan, WrenchTypesHaP.DC_ALL_FACINGS);
		WrenchRegistry.register(MachineInit.gearbox, WrenchTypesHaP.DC_ALL_FACINGS);
		WrenchRegistry.register(MachineInit.gearbox2, WrenchTypesHaP.DC_ALL_FACINGS);
		WrenchRegistry.register(MachineInit.heatPump, WrenchTypesHaP.DC_ALL_FACINGS);
		WrenchRegistry.register(MachineInit.monitorCM, WrenchTypesHaP.DC_ALL_FACINGS);
		WrenchRegistry.register(MachineInit.monitorFluid, WrenchTypesHaP.DC_ALL_FACINGS);
		WrenchRegistry.register(MachineInit.monitorItem, WrenchTypesHaP.DC_ALL_FACINGS);
		WrenchRegistry.register(MachineInit.monitorRF, WrenchTypesHaP.DC_ALL_FACINGS);
		WrenchRegistry.register(MachineInit.monitorRS, WrenchTypesHaP.DC_ALL_FACINGS);
		WrenchRegistry.register(MachineInit.monitorTorque, WrenchTypesHaP.DC_ALL_FACINGS);
		WrenchRegistry.register(MachineInit.motor, WrenchTypesHaP.DC_ALL_FACINGS);
		WrenchRegistry.register(MachineInit.windmill, WrenchTypesHaP.DC_ALL_FACINGS);
		WrenchRegistry.register(MachineInit.windmill_ex, WrenchTypesHaP.DC_ALL_FACINGS);
		WrenchRegistry.register(MachineInit.windmill_l, WrenchTypesHaP.DC_ALL_FACINGS);
		WrenchRegistry.register(MachineInit.wirelessPortal, WrenchTypesHaP.DC_ALL_FACINGS);

		WrenchRegistry.register(MachineInit.creativeBox, WrenchTypesHaP.DC_TORQUE);
		WrenchRegistry.register(MachineInit.shaft_l, WrenchTypesHaP.DC_TORQUE);
		WrenchRegistry.register(MachineInit.shaft_s, WrenchTypesHaP.DC_TORQUE);
		WrenchRegistry.register(MachineInit.shaft_t_a, WrenchTypesHaP.DC_TORQUE);
		WrenchRegistry.register(MachineInit.shaft_t_b, WrenchTypesHaP.DC_TORQUE);
		WrenchRegistry.register(MachineInit.shaft_x, WrenchTypesHaP.DC_TORQUE);
		WrenchRegistry.register(MachineInit.shaft2_l, WrenchTypesHaP.DC_TORQUE);
		WrenchRegistry.register(MachineInit.shaft2_s, WrenchTypesHaP.DC_TORQUE);
		WrenchRegistry.register(MachineInit.shaft2_t_a, WrenchTypesHaP.DC_TORQUE);
		WrenchRegistry.register(MachineInit.shaft2_t_b, WrenchTypesHaP.DC_TORQUE);
		WrenchRegistry.register(MachineInit.shaft2_x, WrenchTypesHaP.DC_TORQUE);
		WrenchRegistry.register(MachineInit.shaft3_l, WrenchTypesHaP.DC_TORQUE);
		WrenchRegistry.register(MachineInit.shaft3_s, WrenchTypesHaP.DC_TORQUE);
		WrenchRegistry.register(MachineInit.shaft3_t_a, WrenchTypesHaP.DC_TORQUE);
		WrenchRegistry.register(MachineInit.shaft3_t_b, WrenchTypesHaP.DC_TORQUE);
		WrenchRegistry.register(MachineInit.shaft3_x, WrenchTypesHaP.DC_TORQUE);

		WrenchRegistry.register(MachineInit.shaft_switch, WrenchTypesHaP.DC_TORQUE);
		WrenchRegistry.register(MachineInit.shaft3_switch, WrenchTypesHaP.DC_TORQUE);

		WrenchRegistry.register(MachineInit.faucet, WrenchTypesHaP.DC_FAUCET);
		WrenchRegistry.register(MachineInit.faucet_sus, WrenchTypesHaP.DC_FAUCET);
		WrenchRegistry.register(MachineInit.faucet_r, WrenchTypesHaP.DC_FAUCET_REVERSED);

		WrenchRegistry.register(MachineInit.conveyor, WrenchTypesHaP.DC_HORIZONTAL);
		WrenchRegistry.register(MachineInit.freezer, WrenchTypesHaP.DC_HORIZONTAL);
		WrenchRegistry.register(MachineInit.hopperSUS, WrenchTypesHaP.DC_HORIZONTAL);
		WrenchRegistry.register(MachineInit.IBC_reactor, WrenchTypesHaP.DC_HORIZONTAL);
		WrenchRegistry.register(MachineInit.pressMachine, WrenchTypesHaP.DC_HORIZONTAL);
		WrenchRegistry.register(MachineInit.reactor, WrenchTypesHaP.DC_HORIZONTAL);
		WrenchRegistry.register(MachineInit.spinning, WrenchTypesHaP.DC_HORIZONTAL);
		WrenchRegistry.register(MachineInit.watermill, WrenchTypesHaP.DC_HORIZONTAL);
		WrenchRegistry.register(MachineInit.waterPump, WrenchTypesHaP.DC_HORIZONTAL);

		WrenchRegistry.register(MachineInit.catapult, WrenchTypesHaP.DC_CATAPULT);

		WrenchRegistry.register(MachineInit.playerPanel, WrenchTypesHaP.DC_PLAYER_PALEL);

		WrenchRegistry.registerType(DCSidedBlock.class, WrenchTypesHaP.DC_FLAG);
		WrenchRegistry.registerType(BlockSlabDC.class, WrenchTypesHaP.DC_SLAB);
		WrenchRegistry.registerType(BlockHopperFilter.class, WrenchTypesHaP.DC_ALL_FACINGS);
	}
}
