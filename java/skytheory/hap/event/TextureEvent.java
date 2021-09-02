package skytheory.hap.event;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import skytheory.hap.HeatAndProcessing;

/**
 * GUIで使用するためにItem化しないテクスチャを登録しておく
 * @author skytheory
 *
 */
public class TextureEvent {

	public static final ResourceLocation TEXTURE_CATALYST = new ResourceLocation(HeatAndProcessing.MOD_ID, "items/ghost_catalyst");
	public static final ResourceLocation TEXTURE_BUCKET = new ResourceLocation(HeatAndProcessing.MOD_ID, "items/ghost_bucket");

	@SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void textureStitch(TextureStitchEvent.Pre event) {
        TextureMap textureMap = event.getMap();
        textureMap.registerSprite(TEXTURE_CATALYST);
        textureMap.registerSprite(TEXTURE_BUCKET);
    }
}
