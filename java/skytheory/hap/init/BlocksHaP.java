package skytheory.hap.init;

import net.minecraft.block.Block;
import skytheory.hap.block.BlockCokeOven;
import skytheory.hap.block.BlockCompactFreezer;
import skytheory.hap.block.BlockCompactHeater;
import skytheory.hap.block.BlockConveyor;
import skytheory.hap.block.BlockHeatSource;
import skytheory.hap.block.BlockMistDispenser;
import skytheory.hap.block.BlockPumpkinLantern;
import skytheory.hap.block.BlockReactorAdvanced;
import skytheory.hap.block.BlockReactorFluidPort;
import skytheory.hap.block.BlockReactorStorage;
import skytheory.hap.block.BlockShaftBifurcated;
import skytheory.hap.block.BlockShaftLShaped;
import skytheory.hap.block.BlockShaftPerpendicular;
import skytheory.hap.block.BlockShaftStraight;
import skytheory.hap.block.BlockShaftTShaped;
import skytheory.hap.block.BlockShaftXShaped;
import skytheory.hap.block.BlockSmeltingPlate;
import skytheory.hap.block.BlockWaterCollector;

public class BlocksHaP {

	public static Block coke_oven = new BlockCokeOven();
	public static Block conveyor = new BlockConveyor();
	public static Block heat_block = new BlockHeatSource();
	public static Block pumpkin_lantern = new BlockPumpkinLantern();
	public static Block water_collector = new BlockWaterCollector();

	public static Block compact_freezer = new BlockCompactFreezer();
	public static Block compact_heater = new BlockCompactHeater();
	public static Block mist_dispenser = new BlockMistDispenser();
	public static Block reactor_advanced = new BlockReactorAdvanced();
	public static Block reactor_storage = new BlockReactorStorage();
	public static Block reactor_fluid_port = new BlockReactorFluidPort();

	public static Block shaft_straight_steel = new BlockShaftStraight(2);
	public static Block shaft_l_shaped_steel = new BlockShaftLShaped(2);
	public static Block shaft_bifurcated_steel = new BlockShaftBifurcated(2);
	public static Block shaft_perpendicular_steel = new BlockShaftPerpendicular(2);
	public static Block shaft_t_shaped_steel = new BlockShaftTShaped(2);
	public static Block shaft_x_shaped_steel = new BlockShaftXShaped(2);

	public static Block shaft_straight_sus = new BlockShaftStraight(3);
	public static Block shaft_l_shaped_sus = new BlockShaftLShaped(3);
	public static Block shaft_bifurcated_sus = new BlockShaftBifurcated(3);
	public static Block shaft_perpendicular_sus = new BlockShaftPerpendicular(3);
	public static Block shaft_t_shaped_sus = new BlockShaftTShaped(3);
	public static Block shaft_x_shaped_sus = new BlockShaftXShaped(3);

	public static Block smelting_plate = new BlockSmeltingPlate();
}
