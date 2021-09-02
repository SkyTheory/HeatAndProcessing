package skytheory.hap.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import skytheory.hap.tile.TileShaftTShaped;

public class BlockShaftTShaped extends BlockShaftBase {

	public BlockShaftTShaped(int torqueTier) {
		super(torqueTier);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileShaftTShaped();
	}

}
