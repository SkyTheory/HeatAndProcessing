package skytheory.hap.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import skytheory.hap.tile.TileShaftBifurcated;

public class BlockShaftBifurcated extends BlockShaftBase {

	public BlockShaftBifurcated(int torqueTier) {
		super(torqueTier);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileShaftBifurcated();
	}

}
