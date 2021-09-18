package skytheory.hap;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import skytheory.hap.creativetab.CreativeTabsHaP;
import skytheory.hap.event.CapabilityEvent;
import skytheory.hap.event.CharmEvent;
import skytheory.hap.event.ClimateHUDEvent;
import skytheory.hap.event.TextureEvent;
import skytheory.hap.event.WrenchEvent;
import skytheory.hap.init.BlocksHaP;
import skytheory.hap.init.ItemsHaP;
import skytheory.hap.init.proxy.CommonProxy;
import skytheory.lib.init.ResourceRegister;

@Mod(
		modid=HeatAndProcessing.MOD_ID,
		name=HeatAndProcessing.MOD_NAME,
		version=HeatAndProcessing.VERSION,
		guiFactory = "skytheory.hap.config.HaPConfigGuiFactory",
		dependencies = "required-after:dcs_climate;required-after:stlib@[1.12.2-1.2.0,)"
	)

public class HeatAndProcessing {

	public static final String MOD_ID = "hap";
	public static final String MOD_NAME = "HeatAndProcessing";
	public static final String MC_VERSION = "1.12.2";
	public static final String MOD_VERSION = "1.2.3a";
	public static final String VERSION = MC_VERSION + "-" + MOD_VERSION;

	@Mod.Instance
	public static HeatAndProcessing INSTANCE;

	public static final String PROXY_CLIENT = "skytheory.hap.init.proxy.ClientProxy";
	public static final String PROXY_SERVER = "skytheory.hap.init.proxy.ServerProxy";

	@SidedProxy(clientSide = PROXY_CLIENT, serverSide = PROXY_SERVER)
	public static CommonProxy proxy;

	@Mod.EventHandler
	public void construct(FMLConstructionEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(CapabilityEvent.class);
		MinecraftForge.EVENT_BUS.register(CharmEvent.class);
		MinecraftForge.EVENT_BUS.register(ClimateHUDEvent.class);
		MinecraftForge.EVENT_BUS.register(TextureEvent.class);
		MinecraftForge.EVENT_BUS.register(WrenchEvent.class);
	}

	@SubscribeEvent
	public void registerBlocks(RegistryEvent.Register<Block> event) {
		ResourceRegister.registerAll(event.getRegistry(), BlocksHaP.class, HeatAndProcessing.MOD_ID, CreativeTabsHaP.MAIN);
	}

	@SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> event) {
		ResourceRegister.registerAll(event.getRegistry(), ItemsHaP.class, HeatAndProcessing.MOD_ID, CreativeTabsHaP.MAIN);
	}

	@SubscribeEvent
	public void registerModels(ModelRegistryEvent event) {
		proxy.registerModels(event);
	}

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}
}
