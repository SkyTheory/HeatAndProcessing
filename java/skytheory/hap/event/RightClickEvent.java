package skytheory.hap.event;

import defeatedcrow.hac.magic.MagicInit;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import skytheory.hap.config.HaPConfig;

public class RightClickEvent {

	@SubscribeEvent
	public static void onRightClick(PlayerInteractEvent.RightClickBlock event) {
		if (HaPConfig.vein_beacon) {
			World world = event.getWorld();
			if (!world.isRemote) {
				BlockPos pos = event.getPos();
				if (world.getBlockState(pos).getBlock() == MagicInit.veinBeacon) {
					world.setBlockToAir(pos);
				}
			}
		}
	}
}
