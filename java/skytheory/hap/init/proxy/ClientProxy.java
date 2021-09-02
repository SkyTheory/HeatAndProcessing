package skytheory.hap.init.proxy;

import defeatedcrow.hac.main.api.MainAPIManager;
import defeatedcrow.hac.main.recipes.DCInfoData;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import skytheory.hap.init.ItemsHaP;
import skytheory.hap.renderer.tile.RenderCompactFreezer;
import skytheory.hap.renderer.tile.RenderConveyor;
import skytheory.hap.renderer.tile.RenderCompactHeater;
import skytheory.hap.renderer.tile.RenderMistDispenser;
import skytheory.hap.renderer.tile.RenderReactorAdvanced;
import skytheory.hap.renderer.tile.RenderReactorFluidPort;
import skytheory.hap.renderer.tile.RenderReactorStorage;
import skytheory.hap.renderer.tile.RenderShaftBifurcated;
import skytheory.hap.renderer.tile.RenderShaftLShaped;
import skytheory.hap.renderer.tile.RenderShaftPerpendicular;
import skytheory.hap.renderer.tile.RenderShaftStraight;
import skytheory.hap.renderer.tile.RenderShaftTShaped;
import skytheory.hap.renderer.tile.RenderShaftXShaped;
import skytheory.hap.renderer.tile.RenderSmeltingPlate;
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
import skytheory.lib.init.ResourceRegister;
import skytheory.lib.renderer.RenderTileEntityItemStack;
import skytheory.lib.renderer.TileRendererBase;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerModels(ModelRegistryEvent event) {
		super.registerModels(event);
		ResourceRegister.registerItemModels(ItemsHaP.class);
	}

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		ClientRegistry.bindTileEntitySpecialRenderer(TileConveyor.class, new RenderConveyor());
		ClientRegistry.bindTileEntitySpecialRenderer(TileSmeltingPlate.class, new RenderSmeltingPlate());
		ClientRegistry.bindTileEntitySpecialRenderer(TileCompactFreezer.class, new RenderCompactFreezer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileCompactHeater.class, new RenderCompactHeater());
		ClientRegistry.bindTileEntitySpecialRenderer(TileMistDispenser.class, new RenderMistDispenser());
		ClientRegistry.bindTileEntitySpecialRenderer(TileReactorAdvanced.class, new RenderReactorAdvanced());
		ClientRegistry.bindTileEntitySpecialRenderer(TileReactorStorage.class, new RenderReactorStorage());
		ClientRegistry.bindTileEntitySpecialRenderer(TileReactorFluidPort.class, new RenderReactorFluidPort());
		ClientRegistry.bindTileEntitySpecialRenderer(TileShaftStraight.class, new RenderShaftStraight());
		ClientRegistry.bindTileEntitySpecialRenderer(TileShaftLShaped.class, new RenderShaftLShaped());
		ClientRegistry.bindTileEntitySpecialRenderer(TileShaftBifurcated.class, new RenderShaftBifurcated());
		ClientRegistry.bindTileEntitySpecialRenderer(TileShaftPerpendicular.class, new RenderShaftPerpendicular());
		ClientRegistry.bindTileEntitySpecialRenderer(TileShaftTShaped.class, new RenderShaftTShaped());
		ClientRegistry.bindTileEntitySpecialRenderer(TileShaftXShaped.class, new RenderShaftXShaped());
	}

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		setTEISR(ItemsHaP.compact_freezer, TileCompactFreezer.class);
		setTEISR(ItemsHaP.compact_heater, TileCompactHeater.class);
		setTEISR(ItemsHaP.mist_dispenser, TileMistDispenser.class);
		setTEISR(ItemsHaP.reactor_advanced, TileReactorAdvanced.class);
		setTEISR(ItemsHaP.reactor_storage, TileReactorStorage.class);
		setTEISR(ItemsHaP.reactor_fluid_port, TileReactorFluidPort.class);

		setTEISR(ItemsHaP.shaft_straight_steel, TileShaftStraight.class);
		setTEISR(ItemsHaP.shaft_l_shaped_steel, TileShaftLShaped.class);
		setTEISR(ItemsHaP.shaft_bifurcated_steel, TileShaftBifurcated.class);
		setTEISR(ItemsHaP.shaft_perpendicular_steel, TileShaftPerpendicular.class);
		setTEISR(ItemsHaP.shaft_t_shaped_steel, TileShaftTShaped.class);
		setTEISR(ItemsHaP.shaft_x_shaped_steel, TileShaftXShaped.class);

		setTEISR(ItemsHaP.shaft_straight_sus, TileShaftStraight.class);
		setTEISR(ItemsHaP.shaft_l_shaped_sus, TileShaftLShaped.class);
		setTEISR(ItemsHaP.shaft_bifurcated_sus, TileShaftBifurcated.class);
		setTEISR(ItemsHaP.shaft_perpendicular_sus, TileShaftPerpendicular.class);
		setTEISR(ItemsHaP.shaft_t_shaped_sus, TileShaftTShaped.class);
		setTEISR(ItemsHaP.shaft_x_shaped_sus, TileShaftXShaped.class);
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
		registerInfo();
	}

	private void registerInfo() {
		DCInfoData coat = new DCInfoData(
				ItemStack.EMPTY,
				ItemStack.EMPTY,
				new ItemStack(ItemsHaP.coating_molybdenum, 1, 32767),
				"dcs.info.title.coating",
				"dcs.info.desc.coating"
				);
		MainAPIManager.infoRegister.registerInfo(coat);
	}

	private static <T extends TileEntity> void setTEISR(Item item, Class<T> type) {
		TileEntitySpecialRenderer<? extends TileEntity> renderer = TileEntityRendererDispatcher.instance.renderers.get(type);
		if (renderer instanceof TileRendererBase) {
			@SuppressWarnings("unchecked")
			TileEntityItemStackRenderer teisr = new RenderTileEntityItemStack(item, (TileRendererBase<T>) renderer);
			item.setTileEntityItemStackRenderer(teisr);
		} else {
			throw new RuntimeException(String.format("Unexpected renderer type: %s", renderer.getClass().getName()));
		}
	}
}
