package skytheory.hap.model;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import skytheory.hap.block.BlockSmeltingPlate;
import skytheory.hap.tile.TileSmeltingPlate;
import skytheory.lib.renderer.ITileModel;

public class ModelSmeltingPlate extends ModelBase implements ITileModel<TileSmeltingPlate> {

	public void render(TileSmeltingPlate tile, float partialtick) {
		if (tile != null) {
			ItemStack stack = tile.getModelItem();
			IBlockState state = tile.getWorld().getBlockState(tile.getPos());
			boolean side = state.getValue(BlockSmeltingPlate.SIDE);
			EnumFacing facing = state.getValue(BlockHorizontal.FACING);
			if (!stack.isEmpty() && facing != null) {
				GlStateManager.pushMatrix();
				GlStateManager.enableRescaleNormal();
				GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
				float angle = 0.0f;
				switch (facing) {
				case NORTH:
					angle = 180.0f;
					break;
				case EAST:
					angle = 90.0f;
					break;
				case SOUTH:
					angle = 0.0f;
					break;
				case WEST:
					angle = 270.0f;
					break;
				default:
					break;
				}
				GlStateManager.rotate(angle, 0.0f, 1.0f, 0.0f);
				if (side) {
					GlStateManager.translate(0.0f, -2f, 0.0f);
				} else {
					GlStateManager.translate(0.0f, -1.0625f, 0.0f);
				}
				GlStateManager.translate(0.0f, 0.6875f, 0.0f);
				Minecraft.getMinecraft().getRenderItem().renderItem(stack, TransformType.GROUND);
				GlStateManager.disableRescaleNormal();
				GlStateManager.popMatrix();
			}
		}
	}

}
