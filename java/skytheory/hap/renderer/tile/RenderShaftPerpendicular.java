package skytheory.hap.renderer.tile;

import skytheory.hap.model.ModelShaftPerpendicular;
import skytheory.hap.tile.TileShaftPerpendicular;
import skytheory.lib.renderer.ITileModel;

public class RenderShaftPerpendicular extends RenderShaftBase<TileShaftPerpendicular> {

	@Override
	public ITileModel<TileShaftPerpendicular> getModel(TileShaftPerpendicular tile) {
		return ModelShaftPerpendicular.INSTANCE;
	}

}
