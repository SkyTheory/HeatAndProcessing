package skytheory.hap.renderer.tile;

import skytheory.hap.model.ModelShaftStraight;
import skytheory.hap.tile.TileShaftStraight;
import skytheory.lib.renderer.ITileModel;

public class RenderShaftStraight extends RenderShaftBase<TileShaftStraight> {

	@Override
	public ITileModel<TileShaftStraight> getModel(TileShaftStraight tile) {
		return ModelShaftStraight.INSTANCE;
	}

}
