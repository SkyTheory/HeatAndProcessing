// Made with Blockbench 3.8.2
// Exported for Minecraft version 1.12
package skytheory.hap.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import skytheory.hap.tile.TileMistDispenser;
import skytheory.lib.renderer.ITileModel;

public class ModelMistDispenser extends ModelBase implements ITileModel<TileMistDispenser> {

	public static ModelMistDispenser INSTANCE = new ModelMistDispenser();

	private final ModelRenderer body;

	public ModelMistDispenser() {
		textureWidth = 64;
		textureHeight = 64;

		body = new ModelRenderer(this);
		body.setRotationPoint(0.0f, 24.0f, 0.0f);
		body.cubeList.add(new ModelBox(body, 0, 29, -8.0f, -16.0f, -8.0f, 16, 4, 2, 0.0f, false));
		body.cubeList.add(new ModelBox(body, 0, 35, -8.0f, -1.0f, -8.0f, 16, 1, 2, 0.0f, false));
		body.cubeList.add(new ModelBox(body, 0, 38, -8.0f, -12.0f, -8.0f, 1, 11, 2, 0.0f, false));
		body.cubeList.add(new ModelBox(body, 6, 38, 7.0f, -12.0f, -8.0f, 1, 11, 2, 0.0f, false));
		body.cubeList.add(new ModelBox(body, 0, 0, -3.0f, -11.0f, 7.0f, 6, 6, 1, 0.0f, false));
		body.cubeList.add(new ModelBox(body, 1, 0, -8.0f, -16.0f, -6.0f, 16, 16, 13, 0.0f, false));
	}

	@Override
	public void render(TileMistDispenser tile, float partialtick) {
		float scale = 1.0f / 16.0f;
		body.render(scale);
	}

}
