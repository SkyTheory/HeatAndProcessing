package skytheory.hap.init;

import skytheory.hap.tile.TileCompactFreezer;
import skytheory.hap.tile.TileConveyor;
import skytheory.hap.tile.TileCompactHeater;
import skytheory.hap.tile.TileMistDispenser;
import skytheory.hap.tile.TileReactorAdvanced;
import skytheory.hap.tile.TileReactorFluidPort;
import skytheory.hap.tile.TileReactorStorage;
import skytheory.hap.tile.TileShaftBifurcated;
import skytheory.hap.tile.TileShaftLShaped;
import skytheory.hap.tile.TileShaftPerpendicular;
import skytheory.hap.tile.TileShaftStraight;
import skytheory.hap.tile.TileShaftTShaped;
import skytheory.hap.tile.TileShaftXShaped;
import skytheory.hap.tile.TileSmeltingPlate;

public class TileEntitiesHaP {

	public static Class<TileConveyor> conveyor = TileConveyor.class;
	public static Class<TileSmeltingPlate> smelting_plate = TileSmeltingPlate.class;

	public static Class<TileCompactFreezer> compact_freezer = TileCompactFreezer.class;
	public static Class<TileCompactHeater> compact_heater = TileCompactHeater.class;
	public static Class<TileMistDispenser> mist_dispenser = TileMistDispenser.class;
	public static Class<TileReactorAdvanced> reactor_advanced = TileReactorAdvanced.class;
	public static Class<TileReactorStorage> reactor_storage = TileReactorStorage.class;
	public static Class<TileReactorFluidPort> reactor_fluid_port = TileReactorFluidPort.class;

	public static Class<TileShaftStraight> shaft_straight = TileShaftStraight.class;
	public static Class<TileShaftLShaped> shaft_l_shaped = TileShaftLShaped.class;
	public static Class<TileShaftBifurcated> shaft_bifurcated = TileShaftBifurcated.class;
	public static Class<TileShaftPerpendicular> shaft_perpendicular = TileShaftPerpendicular.class;
	public static Class<TileShaftTShaped> shaft_t_shaped = TileShaftTShaped.class;
	public static Class<TileShaftXShaped> shaft_x_shaped = TileShaftXShaped.class;
}
