// Made with Blockbench 3.8.2
// Exported for Minecraft version 1.12
package skytheory.hap.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import skytheory.hap.tile.TileShaftLShaped;
import skytheory.lib.renderer.ITileModel;
import skytheory.lib.util.FloatUtils;

public class ModelShaftLShaped extends ModelBase implements ITileModel<TileShaftLShaped> {

	public static ModelShaftLShaped INSTANCE = new ModelShaftLShaped();

	private final ModelRenderer body;
	private final ModelRenderer input;
	private final ModelRenderer output;
	private final ModelRenderer input_shaft;
	private final ModelRenderer output_shaft;
	private final ModelRenderer gearbox;

	public ModelShaftLShaped() {
		textureWidth = 64;
		textureHeight = 64;

		body = new ModelRenderer(this);
		body.setRotationPoint(0.0f, 16.0f, 0.0f);

		input = new ModelRenderer(this);
		input.setRotationPoint(0.0f, 0.0f, 0.0f);
		body.addChild(input);
		input.cubeList.add(new ModelBox(input, 0, 0, -3.0f, -3.0f, 7.0f, 6, 6, 1, 0.0f, false));
		input.cubeList.add(new ModelBox(input, 0, 14, -3.0f, -3.0f, 6.25f, 6, 6, 1, -0.75f, false));

		output = new ModelRenderer(this);
		output.setRotationPoint(0.0f, 0.0f, 0.0f);
		body.addChild(output);
		output.cubeList.add(new ModelBox(output, 14, 0, -3.0f, -8.0f, -3.0f, 6, 1, 6, 0.0f, false));
		output.cubeList.add(new ModelBox(output, 14, 7, -3.0f, -7.25f, -3.0f, 6, 1, 6, -0.75f, false));

		input_shaft = new ModelRenderer(this);
		input_shaft.setRotationPoint(0.0f, 0.0f, 0.0f);
		body.addChild(input_shaft);
		input_shaft.cubeList.add(new ModelBox(input_shaft, 0, 41, -1.5f, -1.5f, 2.5f, 3, 3, 4, 0.0f, false));

		output_shaft = new ModelRenderer(this);
		output_shaft.setRotationPoint(0.0f, 0.0f, 0.0f);
		body.addChild(output_shaft);
		output_shaft.cubeList.add(new ModelBox(output_shaft, 0, 54, -1.5f, -6.5f, -1.5f, 3, 4, 3, 0.0f, false));

		gearbox = new ModelRenderer(this);
		gearbox.setRotationPoint(0.0f, 0.0f, 0.0f);
		body.addChild(gearbox);
		gearbox.cubeList.add(new ModelBox(gearbox, 32, 48, -4.0f, -4.0f, -4.0f, 8, 8, 8, -1.0f, false));
	}

	@Override
	public void render(TileShaftLShaped tile, float partialtick) {
		float scale = 1.0f / 16.0f;
		float angle = 0.0f;
		float delta = 0.0f;
		if (tile != null) {
			angle = tile.getShaftAngleInput();
			delta = tile.getShaftDeltaInput();
		}
		input_shaft.rotateAngleZ = FloatUtils.toRadian(angle + delta * partialtick);
		output_shaft.rotateAngleY = FloatUtils.toRadian(angle + delta * partialtick);
		GlStateManager.disableCull();
		body.render(scale);
		GlStateManager.enableCull();
	}
}
