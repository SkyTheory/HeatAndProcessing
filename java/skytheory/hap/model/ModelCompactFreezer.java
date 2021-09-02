// Made with Blockbench 3.8.2
// Exported for Minecraft version 1.12
package skytheory.hap.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import skytheory.hap.tile.TileCompactFreezer;
import skytheory.lib.renderer.ITileModel;


public class ModelCompactFreezer extends ModelBase implements ITileModel<TileCompactFreezer> {
	private final ModelRenderer body;
	private final ModelRenderer torque_tenon;
	private final ModelRenderer rear;
	private final ModelRenderer front_frame;
	private final ModelRenderer pillar_horizontal;
	private final ModelRenderer pillar_vertical;

	public ModelCompactFreezer() {
		textureWidth = 64;
		textureHeight = 64;

		body = new ModelRenderer(this);
		body.setRotationPoint(0.0f, 16.0f, 0.0f);


		torque_tenon = new ModelRenderer(this);
		torque_tenon.setRotationPoint(0.0f, 0.0f, 0.0f);
		body.addChild(torque_tenon);
		torque_tenon.cubeList.add(new ModelBox(torque_tenon, 0, 0, -3.0f, -3.0f, 7.0f, 6, 6, 1, 0.0f, false));

		rear = new ModelRenderer(this);
		rear.setRotationPoint(0.0f, 0.0f, 0.0f);
		body.addChild(rear);
		rear.cubeList.add(new ModelBox(rear, 0, 0, -8.0f, -8.0f, -7.0f, 16, 16, 14, 0.0f, false));

		front_frame = new ModelRenderer(this);
		front_frame.setRotationPoint(0.0f, 0.0f, 0.0f);
		body.addChild(front_frame);
		front_frame.cubeList.add(new ModelBox(front_frame, 0, 30, -8.0f, -8.0f, -8.0f, 16, 4, 1, 0.0f, false));
		front_frame.cubeList.add(new ModelBox(front_frame, 0, 35, -8.0f, 7.0f, -8.0f, 16, 1, 1, 0.0f, false));
		front_frame.cubeList.add(new ModelBox(front_frame, 46, 2, -8.0f, -4.0f, -8.0f, 1, 11, 1, 0.0f, false));
		front_frame.cubeList.add(new ModelBox(front_frame, 50, 2, 7.0f, -4.0f, -8.0f, 1, 11, 1, 0.0f, false));

		pillar_horizontal = new ModelRenderer(this);
		pillar_horizontal.setRotationPoint(0.0f, 0.0f, 0.0f);
		body.addChild(pillar_horizontal);
		pillar_horizontal.cubeList.add(new ModelBox(pillar_horizontal, 0, 37, -7.0f, -3.0f, -8.0f, 14, 1, 1, 0.0f, false));
		pillar_horizontal.cubeList.add(new ModelBox(pillar_horizontal, 0, 37, -7.0f, -1.0f, -8.0f, 14, 1, 1, 0.0f, false));
		pillar_horizontal.cubeList.add(new ModelBox(pillar_horizontal, 0, 37, -7.0f, 1.0f, -8.0f, 14, 1, 1, 0.0f, false));
		pillar_horizontal.cubeList.add(new ModelBox(pillar_horizontal, 0, 37, -7.0f, 3.0f, -8.0f, 14, 1, 1, 0.0f, false));
		pillar_horizontal.cubeList.add(new ModelBox(pillar_horizontal, 0, 37, -7.0f, 5.0f, -8.0f, 14, 1, 1, 0.0f, false));

		pillar_vertical = new ModelRenderer(this);
		pillar_vertical.setRotationPoint(0.0f, 0.0f, 0.0f);
		body.addChild(pillar_vertical);
		pillar_vertical.cubeList.add(new ModelBox(pillar_vertical, 30, 37, -5.0f, -4.0f, -8.0f, 1, 1, 1, 0.0f, false));
		pillar_vertical.cubeList.add(new ModelBox(pillar_vertical, 30, 37, -2.0f, -4.0f, -8.0f, 1, 1, 1, 0.0f, false));
		pillar_vertical.cubeList.add(new ModelBox(pillar_vertical, 30, 37, 1.0f, -4.0f, -8.0f, 1, 1, 1, 0.0f, false));
		pillar_vertical.cubeList.add(new ModelBox(pillar_vertical, 30, 37, 4.0f, -4.0f, -8.0f, 1, 1, 1, 0.0f, false));
		pillar_vertical.cubeList.add(new ModelBox(pillar_vertical, 30, 37, -5.0f, -2.0f, -8.0f, 1, 1, 1, 0.0f, false));
		pillar_vertical.cubeList.add(new ModelBox(pillar_vertical, 30, 37, -2.0f, -2.0f, -8.0f, 1, 1, 1, 0.0f, false));
		pillar_vertical.cubeList.add(new ModelBox(pillar_vertical, 30, 37, 1.0f, -2.0f, -8.0f, 1, 1, 1, 0.0f, false));
		pillar_vertical.cubeList.add(new ModelBox(pillar_vertical, 30, 37, 4.0f, -2.0f, -8.0f, 1, 1, 1, 0.0f, false));
		pillar_vertical.cubeList.add(new ModelBox(pillar_vertical, 30, 37, -5.0f, 0.0f, -8.0f, 1, 1, 1, 0.0f, false));
		pillar_vertical.cubeList.add(new ModelBox(pillar_vertical, 30, 37, -2.0f, 0.0f, -8.0f, 1, 1, 1, 0.0f, false));
		pillar_vertical.cubeList.add(new ModelBox(pillar_vertical, 30, 37, 1.0f, 0.0f, -8.0f, 1, 1, 1, 0.0f, false));
		pillar_vertical.cubeList.add(new ModelBox(pillar_vertical, 30, 37, 4.0f, 0.0f, -8.0f, 1, 1, 1, 0.0f, false));
		pillar_vertical.cubeList.add(new ModelBox(pillar_vertical, 30, 37, -5.0f, 2.0f, -8.0f, 1, 1, 1, 0.0f, false));
		pillar_vertical.cubeList.add(new ModelBox(pillar_vertical, 30, 37, -2.0f, 2.0f, -8.0f, 1, 1, 1, 0.0f, false));
		pillar_vertical.cubeList.add(new ModelBox(pillar_vertical, 30, 37, 1.0f, 2.0f, -8.0f, 1, 1, 1, 0.0f, false));
		pillar_vertical.cubeList.add(new ModelBox(pillar_vertical, 30, 37, 4.0f, 2.0f, -8.0f, 1, 1, 1, 0.0f, false));
		pillar_vertical.cubeList.add(new ModelBox(pillar_vertical, 30, 37, -5.0f, 4.0f, -8.0f, 1, 1, 1, 0.0f, false));
		pillar_vertical.cubeList.add(new ModelBox(pillar_vertical, 30, 37, -2.0f, 4.0f, -8.0f, 1, 1, 1, 0.0f, false));
		pillar_vertical.cubeList.add(new ModelBox(pillar_vertical, 30, 37, 1.0f, 4.0f, -8.0f, 1, 1, 1, 0.0f, false));
		pillar_vertical.cubeList.add(new ModelBox(pillar_vertical, 30, 37, 4.0f, 4.0f, -8.0f, 1, 1, 1, 0.0f, false));
		pillar_vertical.cubeList.add(new ModelBox(pillar_vertical, 30, 37, -5.0f, 6.0f, -8.0f, 1, 1, 1, 0.0f, false));
		pillar_vertical.cubeList.add(new ModelBox(pillar_vertical, 30, 37, -2.0f, 6.0f, -8.0f, 1, 1, 1, 0.0f, false));
		pillar_vertical.cubeList.add(new ModelBox(pillar_vertical, 30, 37, 1.0f, 6.0f, -8.0f, 1, 1, 1, 0.0f, false));
		pillar_vertical.cubeList.add(new ModelBox(pillar_vertical, 30, 37, 4.0f, 6.0f, -8.0f, 1, 1, 1, 0.0f, false));
	}

	@Override
	public void render(TileCompactFreezer tile, float partialtick) {
		float scale = 1.0f / 16.0f;
		body.render(scale);
	}

}