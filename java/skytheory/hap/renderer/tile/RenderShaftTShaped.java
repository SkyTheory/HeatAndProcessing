package skytheory.hap.renderer.tile;

import skytheory.hap.model.ModelShaftTShaped;
import skytheory.hap.tile.TileShaftTShaped;
import skytheory.lib.renderer.ITileModel;

public class RenderShaftTShaped extends RenderShaftBase<TileShaftTShaped> {

	@Override
	public ITileModel<TileShaftTShaped> getModel(TileShaftTShaped tile) {
		return ModelShaftTShaped.INSTANCE;
	}

}
