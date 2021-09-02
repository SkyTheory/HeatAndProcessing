// Made with Blockbench 3.8.2
// Exported for Minecraft version 1.12
package skytheory.hap.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import skytheory.hap.tile.TileShaftStraight;
import skytheory.lib.renderer.ITileModel;
import skytheory.lib.util.FloatUtils;

public class ModelShaftStraight extends ModelBase implements ITileModel<TileShaftStraight> {

	public static ModelShaftStraight INSTANCE = new ModelShaftStraight();

	private final ModelRenderer body;
	private final ModelRenderer torque_tenon;
	private final ModelRenderer shaft;

	public ModelShaftStraight() {
		textureWidth = 64;
		textureHeight = 64;

		body = new ModelRenderer(this);
		body.setRotationPoint(0.0f, 16.0f, 0.0f);

		torque_tenon = new ModelRenderer(this);
		torque_tenon.setRotationPoint(0.0f, 0.0f, 0.0f);
		body.addChild(torque_tenon);
		torque_tenon.cubeList.add(new ModelBox(torque_tenon, 0, 0, -3.0f, -3.0f, 7.0f, 6, 6, 1, 0.0f, false));
		torque_tenon.cubeList.add(new ModelBox(torque_tenon, 0, 7, -3.0f, -3.0f, -8.0f, 6, 6, 1, 0.0f, false));
		torque_tenon.cubeList.add(new ModelBox(torque_tenon, 0, 14, -3.0f, -3.0f, -7.25f, 6, 6, 1, -0.75f, false));
		torque_tenon.cubeList.add(new ModelBox(torque_tenon, 0, 14, -3.0f, -3.0f, 6.25f, 6, 6, 1, -0.75f, false));

		shaft = new ModelRenderer(this);
		shaft.setRotationPoint(0.0f, 0.0f, 0.0f);
		body.addChild(shaft);
		shaft.cubeList.add(new ModelBox(shaft, 0, 48, -1.5f, -1.5f, -6.5f, 3, 3, 13, 0.0f, false));
	}

	@Override
	public void render(TileShaftStraight tile, float partialtick) {
		float scale = 1.0f / 16.0f;
		float angle = 0.0f;
		float delta = 0.0f;
		if (tile != null) {
			angle = tile.getShaftAngleInput();
			delta = tile.getShaftDeltaInput();
		}
		shaft.rotateAngleZ = FloatUtils.toRadian(angle + delta * partialtick);
		GlStateManager.disableCull();
		body.render(scale);
		GlStateManager.enableCull();
	}
}
