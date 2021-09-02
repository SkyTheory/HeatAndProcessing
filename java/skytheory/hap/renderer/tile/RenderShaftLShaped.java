package skytheory.hap.renderer.tile;

import skytheory.hap.model.ModelShaftLShaped;
import skytheory.hap.tile.TileShaftLShaped;
import skytheory.lib.renderer.ITileModel;

public class RenderShaftLShaped extends RenderShaftBase<TileShaftLShaped> {

	@Override
	public ITileModel<TileShaftLShaped> getModel(TileShaftLShaped tile) {
		return ModelShaftLShaped.INSTANCE;
	}

}
