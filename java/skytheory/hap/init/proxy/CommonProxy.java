package skytheory.hap.init.proxy;

import java.io.File;
import java.util.Objects;

import defeatedcrow.hac.api.climate.ClimateAPI;
import defeatedcrow.hac.api.climate.DCHeatTier;
import defeatedcrow.hac.api.energy.capability.TorqueCapabilityHandler;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import skytheory.hap.HeatAndProcessing;
import skytheory.hap.config.HaPConfig;
import skytheory.hap.init.BlocksHaP;
import skytheory.hap.init.GuiHandler;
import skytheory.hap.init.TileEntitiesHaP;
import skytheory.hap.recipe.ReactorRecipes;
import skytheory.hap.util.WrenchRegister;
import skytheory.lib.SkyTheoryLib;
import skytheory.lib.init.ResourceRegister;
import skytheory.lib.network.CapsSyncManager;

public class CommonProxy {

	public Configuration config;

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
		File directory = event.getModConfigurationDirectory();
		config = new Configuration(new File(directory.getPath(), "HeatAndProcessing.cfg"));
		HaPConfig.readConfig();
		NetworkRegistry.INSTANCE.registerGuiHandler(HeatAndProcessing.INSTANCE, GuiHandler.INSTANCE);
	}

	public void init(FMLInitializationEvent event) {
		CapsSyncManager.registerLookUp(TorqueCapabilityHandler.TORQUE_HANDLER_CAPABILITY);
		WrenchRegister.register();
	}

	public void postInit(FMLPostInitializationEvent event) {
		ClimateAPI.registerBlock.registerHeatBlock(BlocksHaP.heat_block, 32767, DCHeatTier.KILN);
		ClimateAPI.registerBlock.registerHeatBlock(BlocksHaP.pumpkin_lantern, 32767, DCHeatTier.WARM);
		if (config.hasChanged()) config.save();
		ReactorRecipes.register();
	}
}
