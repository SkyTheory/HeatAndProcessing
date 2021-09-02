// Made with Blockbench 3.8.2
// Exported for Minecraft version 1.12
package skytheory.hap.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import skytheory.hap.tile.TileReactorAdvanced;
import skytheory.lib.renderer.ITileModel;

public class ModelReactorAdvanced extends ModelBase implements ITileModel<TileReactorAdvanced> {

	public static final ModelReactorAdvanced INSTANCE = new ModelReactorAdvanced();

	private final ModelRenderer body;

	public ModelReactorAdvanced() {
		textureWidth = 64;
		textureHeight = 64;

		body = new ModelRenderer(this);
		body.setRotationPoint(0.0f, 16.0f, 0.0f);
		body.cubeList.add(new ModelBox(body, 30, 35, -8.0f, -8.0f, -6.0f, 4, 16, 13, 0.0f, false));
		body.cubeList.add(new ModelBox(body, 0, 24, -4.0f, -5.0f, -1.0f, 12, 13, 8, 0.0f, false));
		body.cubeList.add(new ModelBox(body, 20, 0, -4.0f, 7.0f, -8.0f, 12, 1, 7, 0.0f, false));
		body.cubeList.add(new ModelBox(body, 0, 9, -4.0f, -2.0f, -7.0f, 12, 9, 6, 0.0f, false));
		body.cubeList.add(new ModelBox(body, 10, 7, 3.0f, -3.0f, -2.0f, 4, 2, 1, 0.0f, false));
		body.cubeList.add(new ModelBox(body, 0, 7, -3.0f, -3.0f, -2.0f, 4, 2, 1, 0.0f, false));
		body.cubeList.add(new ModelBox(body, 0, 0, -3.0f, -3.0f, 7.0f, 6, 6, 1, 0.0f, false));
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	@Override
	public void render(TileReactorAdvanced tile, float partialtick) {
		float scale = 1.0f/16.0f;
		body.render(scale);
	}

}
