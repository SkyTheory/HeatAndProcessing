// Made with Blockbench 3.8.2
// Exported for Minecraft version 1.12
package skytheory.hap.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import skytheory.hap.tile.TileShaftPerpendicular;
import skytheory.lib.renderer.ITileModel;
import skytheory.lib.util.FloatUtils;

public class ModelShaftPerpendicular extends ModelBase implements ITileModel<TileShaftPerpendicular> {

	public static ModelShaftPerpendicular INSTANCE = new ModelShaftPerpendicular();

	private final ModelRenderer body;
	private final ModelRenderer input;
	private final ModelRenderer output1;
	private final ModelRenderer output2;
	private final ModelRenderer input_shaft;
	private final ModelRenderer output1_shaft;
	private final ModelRenderer output2_shaft;
	private final ModelRenderer gearbox;

	public ModelShaftPerpendicular() {
		textureWidth = 64;
		textureHeight = 64;

		body = new ModelRenderer(this);
		body.setRotationPoint(0.0f, 16.0f, 0.0f);


		input = new ModelRenderer(this);
		input.setRotationPoint(0.0f, 0.0f, 0.0f);
		body.addChild(input);
		input.cubeList.add(new ModelBox(input, 0, 0, -3.0f, -3.0f, 7.0f, 6, 6, 1, 0.0f, false));
		input.cubeList.add(new ModelBox(input, 0, 14, -3.0f, -3.0f, 6.25f, 6, 6, 1, -0.75f, false));

		output1 = new ModelRenderer(this);
		output1.setRotationPoint(0.0f, 0.0f, 0.0f);
		body.addChild(output1);
		output1.cubeList.add(new ModelBox(output1, 14, 0, -3.0f, -8.0f, -3.0f, 6, 1, 6, 0.0f, false));
		output1.cubeList.add(new ModelBox(output1, 14, 7, -3.0f, -7.25f, -3.0f, 6, 1, 6, -0.75f, false));

		output2 = new ModelRenderer(this);
		output2.setRotationPoint(0.0f, 0.0f, 0.0f);
		body.addChild(output2);
		output2.cubeList.add(new ModelBox(output2, 38, 0, 7.0f, -3.0f, -3.0f, 1, 6, 6, 0.0f, false));
		output2.cubeList.add(new ModelBox(output2, 38, 12, 6.25f, -3.0f, -3.0f, 1, 6, 6, -0.75f, false));

		input_shaft = new ModelRenderer(this);
		input_shaft.setRotationPoint(0.0f, 0.0f, 0.0f);
		body.addChild(input_shaft);
		input_shaft.cubeList.add(new ModelBox(input_shaft, 0, 41, -1.5f, -1.5f, 2.5f, 3, 3, 4, 0.0f, false));

		output1_shaft = new ModelRenderer(this);
		output1_shaft.setRotationPoint(0.0f, 0.0f, 0.0f);
		body.addChild(output1_shaft);
		output1_shaft.cubeList.add(new ModelBox(output1_shaft, 0, 54, -1.5f, -6.5f, -1.5f, 3, 4, 3, 0.0f, false));

		output2_shaft = new ModelRenderer(this);
		output2_shaft.setRotationPoint(0.0f, 0.0f, 0.0f);
		body.addChild(output2_shaft);
		output2_shaft.cubeList.add(new ModelBox(output2_shaft, 0, 35, 2.5f, -1.5f, -1.5f, 4, 3, 3, 0.0f, false));

		gearbox = new ModelRenderer(this);
		gearbox.setRotationPoint(0.0f, 0.0f, 0.0f);
		body.addChild(gearbox);
		gearbox.cubeList.add(new ModelBox(gearbox, 32, 48, -4.0f, -4.0f, -4.0f, 8, 8, 8, -1.0f, false));
	}

	@Override
	public void render(TileShaftPerpendicular tile, float partialtick) {
		float scale = 1.0f / 16.0f;
		float angle1 = 0.0f;
		float delta1 = 0.0f;
		float angle2 = 0.0f;
		float delta2 = 0.0f;
		if (tile != null) {
			angle1 = tile.getShaftAngleInput();
			delta1 = tile.getShaftDeltaInput();
			angle2 = tile.getShaftAngleOutput();
			delta2 = tile.getShaftDeltaOutput();
		}
		input_shaft.rotateAngleZ = FloatUtils.toRadian(angle1 + delta1 * partialtick);
		output1_shaft.rotateAngleY = FloatUtils.toRadian(angle2 + delta2 * partialtick);
		output2_shaft.rotateAngleX = -FloatUtils.toRadian(angle2 + delta2 * partialtick);
		GlStateManager.disableCull();
		body.render(scale);
		GlStateManager.enableCull();
	}
}
