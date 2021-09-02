package skytheory.hap.tile;

import java.util.List;

import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import skytheory.lib.tile.ISidedTileDirectional;
import skytheory.lib.util.FacingHelper;
import skytheory.lib.util.STLibConstants;
import skytheory.lib.util.TextUtils;

public abstract class TileTorqueDirectional extends TileTorque implements ISidedTileDirectional {

	@Override
	public EnumFacing getFacing() {
		IBlockState state = world.getBlockState(pos);
		return FacingHelper.getFacingFromState(state);
	}

	@Override
	public void getWailaTips(ItemStack stack, List<String> tips, IWailaDataAccessor accessor) {
		super.getWailaTips(stack, tips, accessor);
		tips.add(TextUtils.format(STLibConstants.TIP_FACING, this.getFacing().getName()));
		tips.add(TextUtils.format(STLibConstants.TIP_SIDE, this.getSide(accessor.getSide()).getName()));
	}

}
