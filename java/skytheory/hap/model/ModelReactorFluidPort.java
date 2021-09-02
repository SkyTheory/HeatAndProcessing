package skytheory.hap.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import skytheory.hap.tile.TileReactorFluidPort;
import skytheory.lib.renderer.ITileModel;

public class ModelReactorFluidPort extends ModelBase implements ITileModel<TileReactorFluidPort> {

	public static final ModelReactorFluidPort INSTANCE = new ModelReactorFluidPort();

	private final ModelRenderer body;

	public ModelReactorFluidPort() {
		textureWidth = 64;
		textureHeight = 64;

		body = new ModelRenderer(this);
		body.setRotationPoint(0.0f, 16.0f, 0.0f);
		body.setRotationPoint(0.0f, 24.0f, 0.0f);
		body.cubeList.add(new ModelBox(body, 0, 34, -8.0f, -16.0f, -6.0f, 16, 16, 14, 0.0f, false));
		body.cubeList.add(new ModelBox(body, 0, 24, -8.0f, -8.0f, -8.0f, 16, 8, 2, 0.0f, false));
		body.cubeList.add(new ModelBox(body, 0, 21, -8.0f, -16.0f, -8.0f, 16, 1, 2, 0.0f, false));
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
	}

}
