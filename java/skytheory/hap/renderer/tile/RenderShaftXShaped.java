package skytheory.hap.renderer.tile;

import skytheory.hap.model.ModelShaftXShaped;
import skytheory.hap.tile.TileShaftXShaped;
import skytheory.lib.renderer.ITileModel;

public class RenderShaftXShaped extends RenderShaftBase<TileShaftXShaped> {

	@Override
	public ITileModel<TileShaftXShaped> getModel(TileShaftXShaped tile) {
		return ModelShaftXShaped.INSTANCE;
	}

}
