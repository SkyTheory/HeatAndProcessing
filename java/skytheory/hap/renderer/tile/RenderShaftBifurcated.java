package skytheory.hap.renderer.tile;

import skytheory.hap.model.ModelShaftBifurcated;
import skytheory.hap.tile.TileShaftBifurcated;
import skytheory.lib.renderer.ITileModel;

public class RenderShaftBifurcated extends RenderShaftBase<TileShaftBifurcated> {

	@Override
	public ITileModel<TileShaftBifurcated> getModel(TileShaftBifurcated tile) {
		return ModelShaftBifurcated.INSTANCE;
	}

}
