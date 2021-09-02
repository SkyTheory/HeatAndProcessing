package skytheory.hap.renderer.tile;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import skytheory.hap.HeatAndProcessing;
import skytheory.hap.block.BlockShaftBase;
import skytheory.hap.tile.TileShaftBase;
import skytheory.lib.renderer.TileRendererFromRotation;

public abstract class RenderShaftBase<T extends TileShaftBase> extends TileRendererFromRotation<T> {

	public static ResourceLocation TEXTURE_STEEL = new ResourceLocation(HeatAndProcessing.MOD_ID, "textures/tile/shaft_gearbox_steel.png");
	public static ResourceLocation TEXTURE_SUS = new ResourceLocation(HeatAndProcessing.MOD_ID, "textures/tile/shaft_gearbox_sus.png");

	@Override
	public ResourceLocation getTexture(T tile) {
		if (tile != null) {
			if (tile.getTorqueTier() == 2) {
				return TEXTURE_STEEL;
			}
			if (tile.getTorqueTier() == 3) {
				return TEXTURE_SUS;
			}
		}
		return TEXTURE_STEEL;
	}

	@Override
	public ResourceLocation getTexture(ItemStack stack) {
		Item item = stack.getItem();
		if (item instanceof ItemBlock) {
			Block block = ((ItemBlock) item).getBlock();
			if (block instanceof BlockShaftBase) {
				int tier = ((BlockShaftBase) block).torqueTier;
				if (tier == 2) {
					return TEXTURE_STEEL;
				}
				if (tier == 3) {
					return TEXTURE_SUS;
				}
			}
		}
		return TEXTURE_STEEL;
	}

}
