package skytheory.hap.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * 覚書：これ自体は無機能なので、別個呼び出しメソッドを実装すること
 * @author SkyTheory
 *
 */
public interface ITileInteract {

	public boolean onRightClick(World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing facing);
}
