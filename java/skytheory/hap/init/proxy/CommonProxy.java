package skytheory.hap.init.proxy;

import java.util.Objects;

import defeatedcrow.hac.api.climate.ClimateAPI;
import defeatedcrow.hac.api.climate.DCHeatTier;
import defeatedcrow.hac.api.energy.capability.TorqueCapabilityHandler;
import defeatedcrow.hac.food.FoodInit;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.oredict.OreDictionary;
import skytheory.hap.HeatAndProcessing;
import skytheory.hap.config.HaPConfig;
import skytheory.hap.init.BlocksHaP;
import skytheory.hap.init.GuiHandler;
import skytheory.hap.init.TileEntitiesHaP;
import skytheory.hap.recipe.CokeOvenRecipes;
import skytheory.hap.recipe.ReactorRecipes;
import skytheory.hap.util.WrenchRegister;
import skytheory.lib.SkyTheoryLib;
import skytheory.lib.init.ResourceRegister;
import skytheory.lib.network.CapsSyncManager;

public class CommonProxy {

	public void registerModels(ModelRegistryEvent event) {
	}

	public void preInit(FMLPreInitializationEvent event) {
		if (Objects.isNull(TorqueCapabilityHandler.TORQUE_HANDLER_CAPABILITY)) {
			// カラスマンさん！ コレ忘れてますよーっ！
			// 覚書：向こうで追加されたら外す
			TorqueCapabilityHandler.register();
		} else {
			SkyTheoryLib.LOGGER.warn("Torque capability already registered.");
		}
		ResourceRegister.registerTiles(TileEntitiesHaP.class, HeatAndProcessing.MOD_ID);
		HaPConfig.read();
		NetworkRegistry.INSTANCE.registerGuiHandler(HeatAndProcessing.INSTANCE, GuiHandler.INSTANCE);
	}

	public void init(FMLInitializationEvent event) {
		CapsSyncManager.registerLookUp(TorqueCapabilityHandler.TORQUE_HANDLER_CAPABILITY);
		WrenchRegister.register();
		OreDictionary.registerOre("treeLeaves", new ItemStack(FoodInit.leavesDates, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("treeLeaves", new ItemStack(FoodInit.leavesLemon, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("treeLeaves", new ItemStack(FoodInit.leavesMorus, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("treeLeaves", new ItemStack(FoodInit.leavesOlive, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("treeLeaves", new ItemStack(FoodInit.leavesTea, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("treeLeaves", new ItemStack(FoodInit.leavesWalnut, 1, OreDictionary.WILDCARD_VALUE));
		ClimateAPI.registerBlock.registerHeatBlock(BlocksHaP.heat_block, 32767, DCHeatTier.KILN);
		ClimateAPI.registerBlock.registerHeatBlock(BlocksHaP.pumpkin_lantern, 32767, DCHeatTier.WARM);
		CokeOvenRecipes.register();
	}

	public void postInit(FMLPostInitializationEvent event) {
		ReactorRecipes.register();
	}
}
