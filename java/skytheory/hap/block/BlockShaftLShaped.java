package skytheory.hap.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import skytheory.hap.tile.TileShaftLShaped;

public class BlockShaftLShaped extends BlockShaftBase {

	public BlockShaftLShaped(int torqueTier) {
		super(torqueTier);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileShaftLShaped();
	}

}
