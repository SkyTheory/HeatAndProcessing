package skytheory.hap.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import skytheory.hap.tile.TileReactorStorage;
import skytheory.lib.renderer.ITileModel;

public class ModelReactorStorage extends ModelBase implements ITileModel<TileReactorStorage> {

	public static final ModelReactorStorage INSTANCE = new ModelReactorStorage();

	private final ModelRenderer bone;

	public ModelReactorStorage() {
		textureWidth = 64;
		textureHeight = 64;

		bone = new ModelRenderer(this);
		bone.setRotationPoint(0.0f, 16.0f, 0.0f);
		bone.cubeList.add(new ModelBox(bone, 8, 18, -4.0f, 0.0f, -8.0f, 12, 8, 16, 0.0f, false));
		bone.cubeList.add(new ModelBox(bone, 0, 8, -8.0f, 0.0f, -6.0f, 4, 8, 14, 0.0f, false));
		bone.cubeList.add(new ModelBox(bone, 24, 2, -8.0f, -4.0f, -4.0f, 4, 4, 12, 0.0f, false));
		bone.cubeList.add(new ModelBox(bone, 0, 42, -4.0f, -8.0f, -6.0f, 12, 8, 14, 0.0f, false));
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	@Override
	public void render(TileReactorStorage tile, float partialtick) {
		float scale = 1.0f/16.0f;
		bone.render(scale);
	}

}
