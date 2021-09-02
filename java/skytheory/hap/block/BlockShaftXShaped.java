package skytheory.hap.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import skytheory.hap.tile.TileShaftXShaped;

public class BlockShaftXShaped extends BlockShaftBase {

	public BlockShaftXShaped(int torqueTier) {
		super(torqueTier);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileShaftXShaped();
	}

}
