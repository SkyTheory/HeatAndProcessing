package skytheory.hap.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import skytheory.hap.tile.TileShaftPerpendicular;

public class BlockShaftPerpendicular extends BlockShaftBase {

	public BlockShaftPerpendicular(int torqueTier) {
		super(torqueTier);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileShaftPerpendicular();
	}

}
