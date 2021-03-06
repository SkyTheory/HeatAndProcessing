package skytheory.hap.init;

import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import skytheory.hap.item.ItemAmuletBR;
import skytheory.hap.item.ItemAmuletGW;
import skytheory.hap.item.ItemAmuletRG;
import skytheory.hap.item.ItemAmuletUB;
import skytheory.hap.item.ItemAmuletWU;
import skytheory.hap.item.ItemCoatingTool;
import skytheory.hap.item.ItemWrench;
import skytheory.lib.item.ItemBlockHorizontal;
import skytheory.lib.item.ItemBlockRotational;

public class ItemsHaP {

	// Item
	public static Item wrench = new ItemWrench();
	public static Item coating_molybdenum = new ItemCoatingTool(Enchantments.SWEEPING, Enchantments.FROST_WALKER);

	public static Item amulet_wu = new ItemAmuletWU();
	public static Item amulet_ub = new ItemAmuletUB();
	public static Item amulet_br = new ItemAmuletBR();
	public static Item amulet_rg = new ItemAmuletRG();
	public static Item amulet_gw = new ItemAmuletGW();

	// Block
	public static Item coke_oven = new ItemBlock(BlocksHaP.coke_oven);
	public static Item conveyor = new ItemBlock(BlocksHaP.conveyor);
	public static Item heat_block = new ItemBlock(BlocksHaP.heat_block);
	public static Item pumpkin_lantern = new ItemBlock(BlocksHaP.pumpkin_lantern);
	public static Item water_collector = new ItemBlock(BlocksHaP.water_collector);

	public static Item compact_freezer = new ItemBlockRotational(BlocksHaP.compact_freezer, true);
	public static Item compact_heater = new ItemBlockRotational(BlocksHaP.compact_heater, true);
	public static Item mist_dispenser = new ItemBlockRotational(BlocksHaP.mist_dispenser, true);
	public static Item reactor_advanced = new ItemBlockHorizontal(BlocksHaP.reactor_advanced);
	public static Item reactor_storage = new ItemBlockHorizontal(BlocksHaP.reactor_storage);
	public static Item reactor_fluid_port = new ItemBlockHorizontal(BlocksHaP.reactor_fluid_port);

	public static Item shaft_straight_steel = new ItemBlockRotational(BlocksHaP.shaft_straight_steel);
	public static Item shaft_l_shaped_steel = new ItemBlockRotational(BlocksHaP.shaft_l_shaped_steel);
	public static Item shaft_bifurcated_steel = new ItemBlockRotational(BlocksHaP.shaft_bifurcated_steel);
	public static Item shaft_perpendicular_steel = new ItemBlockRotational(BlocksHaP.shaft_perpendicular_steel);
	public static Item shaft_t_shaped_steel = new ItemBlockRotational(BlocksHaP.shaft_t_shaped_steel);
	public static Item shaft_x_shaped_steel = new ItemBlockRotational(BlocksHaP.shaft_x_shaped_steel);

	public static Item shaft_straight_sus = new ItemBlockRotational(BlocksHaP.shaft_straight_sus);
	public static Item shaft_l_shaped_sus = new ItemBlockRotational(BlocksHaP.shaft_l_shaped_sus);
	public static Item shaft_bifurcated_sus = new ItemBlockRotational(BlocksHaP.shaft_bifurcated_sus);
	public static Item shaft_perpendicular_sus = new ItemBlockRotational(BlocksHaP.shaft_perpendicular_sus);
	public static Item shaft_t_shaped_sus = new ItemBlockRotational(BlocksHaP.shaft_t_shaped_sus);
	public static Item shaft_x_shaped_sus = new ItemBlockRotational(BlocksHaP.shaft_x_shaped_sus);

	public static Item smelting_plate = new ItemBlock(BlocksHaP.smelting_plate);
}
