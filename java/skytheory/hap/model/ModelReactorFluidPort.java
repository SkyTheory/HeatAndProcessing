package skytheory.hap.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import skytheory.hap.HeatAndProcessing;
import skytheory.hap.tile.EnumFluidMode;
import skytheory.hap.tile.TileReactorFluidPort;
import skytheory.lib.renderer.ITileModel;
import skytheory.lib.util.EnumSide;

public class ModelReactorFluidPort extends ModelBase implements ITileModel<TileReactorFluidPort> {

	public static final ModelReactorFluidPort INSTANCE = new ModelReactorFluidPort();
	public static final ResourceLocation IMPORT_LOCATION = new ResourceLocation(HeatAndProcessing.MOD_ID, "textures/tile/reactor_import.png");
	public static final ResourceLocation EXPORT_LOCATION = new ResourceLocation(HeatAndProcessing.MOD_ID, "textures/tile/reactor_export.png");

	private final ModelRenderer body;
	private final ModelRenderer top;
	private final ModelRenderer back;
	private final ModelRenderer bottom;

	public ModelReactorFluidPort() {
		textureWidth = 64;
		textureHeight = 64;

		body = new ModelRenderer(this);
		body.setRotationPoint(0.0f, 24.0f, 0.0f);
		body.cubeList.add(new ModelBox(body, 0, 34, -8.0F, -16.0F, -6.0F, 16, 16, 14, 0.0F, false));
		body.cubeList.add(new ModelBox(body, 0, 24, -8.0F, -8.0F, -8.0F, 16, 8, 2, 0.0F, false));
		body.cubeList.add(new ModelBox(body, 0, 21, -8.0F, -16.0F, -8.0F, 16, 1, 2, 0.0F, false));
		body.cubeList.add(new ModelBox(body, 60, 56, -8.0F, -15.0F, -7.0F, 1, 7, 1, 0.0F, false));
		body.cubeList.add(new ModelBox(body, 60, 56, 7.0F, -15.0F, -7.0F, 1, 7, 1, 0.0F, false));
		body.cubeList.add(new ModelBox(body, 0, 16, -7.0F, -12.0F, -7.0F, 14, 4, 1, 0.0F, false));

		top = new ModelRenderer(this);
		top.setTextureSize(16, 16);
		top.setRotationPoint(0.0F, 24.0F, 0.0F);
		top.cubeList.add(new ModelBox(top, 0, 0, -8.0F, -16.0005F, -8.0F, 16, 0, 16, 0.0F, false));

		back = new ModelRenderer(this);
		back.setTextureSize(16, 16);
		back.setRotationPoint(0.0F, 24.0F, 0.0F);
		back.cubeList.add(new ModelBox(back, 0, 0, -8.0F, -16.0F, 8.0005F, 16, 16, 0, 0.0F, false));

		bottom = new ModelRenderer(this);
		bottom.setTextureSize(16, 16);
		bottom.setRotationPoint(0.0F, 24.0F, 0.0F);
		bottom.cubeList.add(new ModelBox(bottom, 0, 0, -8.0F, 0.0005F, -8.0F, 16, 0, 16, 0.0F, false));
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	@Override
	public void render(TileReactorFluidPort tile, float partialtick) {
		float scale = 1.0f/16.0f;
		body.render(scale);
		if (tile != null) {
			EnumFluidMode topMode = tile.modeMap.get(EnumSide.TOP);
			if (topMode == EnumFluidMode.IMPORT) {
				Minecraft.getMinecraft().getTextureManager().bindTexture(IMPORT_LOCATION);
				top.render(scale);
			}
			if (topMode == EnumFluidMode.EXPORT) {
				Minecraft.getMinecraft().getTextureManager().bindTexture(EXPORT_LOCATION);
				top.render(scale);
			}
			EnumFluidMode backMode = tile.modeMap.get(EnumSide.BACK);
			if (backMode == EnumFluidMode.IMPORT) {
				Minecraft.getMinecraft().getTextureManager().bindTexture(IMPORT_LOCATION);
				back.render(scale);
			}
			if (backMode == EnumFluidMode.EXPORT) {
				Minecraft.getMinecraft().getTextureManager().bindTexture(EXPORT_LOCATION);
				back.render(scale);
			}
			EnumFluidMode bottomMode = tile.modeMap.get(EnumSide.BOTTOM);
			if (bottomMode == EnumFluidMode.IMPORT) {
				Minecraft.getMinecraft().getTextureManager().bindTexture(IMPORT_LOCATION);
				bottom.render(scale);
			}
			if (bottomMode == EnumFluidMode.EXPORT) {
				Minecraft.getMinecraft().getTextureManager().bindTexture(EXPORT_LOCATION);
				bottom.render(scale);
			}
		}
	}

}
