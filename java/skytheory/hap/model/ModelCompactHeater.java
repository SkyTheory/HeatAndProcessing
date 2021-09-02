// Made with Blockbench 3.8.2
// Exported for Minecraft version 1.12
package skytheory.hap.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import skytheory.hap.tile.TileCompactHeater;
import skytheory.lib.renderer.ITileModel;

public class ModelCompactHeater extends ModelBase implements ITileModel<TileCompactHeater> {

	public static ModelCompactHeater INSTANCE = new ModelCompactHeater();

	private final ModelRenderer body;
	private final ModelRenderer torque_tenon;
	private final ModelRenderer outer_frame;
	private final ModelRenderer inner_frame;
	private final ModelRenderer heat_pile;
	private final ModelRenderer filter;

	public ModelCompactHeater() {
		textureWidth = 64;
		textureHeight = 64;

		body = new ModelRenderer(this);
		body.setRotationPoint(0.0f, 16.0f, 0.0f);


		torque_tenon = new ModelRenderer(this);
		torque_tenon.setRotationPoint(0.0f, 0.0f, 0.0f);
		body.addChild(torque_tenon);
		torque_tenon.cubeList.add(new ModelBox(torque_tenon, 50, 36, -3.0f, -3.0f, 7.0f, 6, 6, 1, 0.0f, false));

		outer_frame = new ModelRenderer(this);
		outer_frame.setRotationPoint(0.0f, 0.0f, 0.0f);
		body.addChild(outer_frame);
		outer_frame.cubeList.add(new ModelBox(outer_frame, 0, 0, -8.0f, -8.0f, 2.0f, 16, 16, 5, 0.0f, false));

		inner_frame = new ModelRenderer(this);
		inner_frame.setRotationPoint(0.0f, 0.0f, 0.0f);
		body.addChild(inner_frame);
		inner_frame.cubeList.add(new ModelBox(inner_frame, 0, 46, -7.0f, 1.0f, -3.0f, 14, 6, 5, 0.0f, false));
		inner_frame.cubeList.add(new ModelBox(inner_frame, 42, 0, -8.0f, -4.0f, -8.0f, 1, 11, 10, 0.0f, false));
		inner_frame.cubeList.add(new ModelBox(inner_frame, 42, 43, 7.0f, -4.0f, -8.0f, 1, 11, 10, 0.0f, false));
		inner_frame.cubeList.add(new ModelBox(inner_frame, 0, 35, -8.0f, 7.0f, -8.0f, 16, 1, 10, 0.0f, false));
		inner_frame.cubeList.add(new ModelBox(inner_frame, 0, 21, -8.0f, -8.0f, -8.0f, 16, 4, 10, 0.0f, false));

		heat_pile = new ModelRenderer(this);
		heat_pile.setRotationPoint(0.0f, 0.0f, 0.0f);
		body.addChild(heat_pile);
		heat_pile.cubeList.add(new ModelBox(heat_pile, 0, 57, -7.0f, 4.0f, -6.0f, 14, 2, 2, 0.0f, false));
		heat_pile.cubeList.add(new ModelBox(heat_pile, 0, 57, -7.0f, 1.0f, -6.0f, 14, 2, 2, 0.0f, false));
		heat_pile.cubeList.add(new ModelBox(heat_pile, 0, 57, -7.0f, -2.0f, -6.0f, 14, 2, 2, 0.0f, false));

		filter = new ModelRenderer(this);
		filter.setRotationPoint(0.0f, 0.0f, 0.0f);
		body.addChild(filter);
		filter.cubeList.add(new ModelBox(filter, 38, 52, -6.0f, -4.0f, -8.0f, 0, 11, 1, 0.0f, false));
		filter.cubeList.add(new ModelBox(filter, 38, 52, -5.0f, -4.0f, -8.0f, 0, 11, 1, 0.0f, false));
		filter.cubeList.add(new ModelBox(filter, 38, 52, -4.0f, -4.0f, -8.0f, 0, 11, 1, 0.0f, false));
		filter.cubeList.add(new ModelBox(filter, 38, 52, -3.0f, -4.0f, -8.0f, 0, 11, 1, 0.0f, false));
		filter.cubeList.add(new ModelBox(filter, 38, 52, -2.0f, -4.0f, -8.0f, 0, 11, 1, 0.0f, false));
		filter.cubeList.add(new ModelBox(filter, 38, 52, -1.0f, -4.0f, -8.0f, 0, 11, 1, 0.0f, false));
		filter.cubeList.add(new ModelBox(filter, 38, 52, 0.0f, -4.0f, -8.0f, 0, 11, 1, 0.0f, false));
		filter.cubeList.add(new ModelBox(filter, 38, 52, 1.0f, -4.0f, -8.0f, 0, 11, 1, 0.0f, false));
		filter.cubeList.add(new ModelBox(filter, 38, 52, 2.0f, -4.0f, -8.0f, 0, 11, 1, 0.0f, false));
		filter.cubeList.add(new ModelBox(filter, 38, 52, 3.0f, -4.0f, -8.0f, 0, 11, 1, 0.0f, false));
		filter.cubeList.add(new ModelBox(filter, 38, 52, 4.0f, -4.0f, -8.0f, 0, 11, 1, 0.0f, false));
		filter.cubeList.add(new ModelBox(filter, 38, 52, 5.0f, -4.0f, -8.0f, 0, 11, 1, 0.0f, false));
		filter.cubeList.add(new ModelBox(filter, 38, 52, 6.0f, -4.0f, -8.0f, 0, 11, 1, 0.0f, false));
	}

	@Override
	public void render(TileCompactHeater tile, float partialtick) {
		float scale = 1.0f / 16.0f;
		body.render(scale);
	}

}
