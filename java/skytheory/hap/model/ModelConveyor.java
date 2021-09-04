package skytheory.hap.model;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import skytheory.hap.block.BlockConveyor;
import skytheory.hap.tile.TileConveyor;
import skytheory.lib.renderer.ITileModel;

public class ModelConveyor extends TileEntitySpecialRenderer<TileConveyor> implements ITileModel<TileConveyor> {

	public void render(TileConveyor tile, float partialTicks) {
		if (tile != null) {
			ItemStack stack = tile.getModelItem();
			IBlockState state = tile.getWorld().getBlockState(tile.getPos());
			float position = tile.getProgress(partialTicks);
			float height = -1.0f;
			if (state.getValue(BlockConveyor.SIDE)) height -= 0.1875f;
			EnumFacing facing = state.getValue(BlockHorizontal.FACING);
			if (!stack.isEmpty() && facing != null) {
				GlStateManager.pushMatrix();
				GlStateManager.enableRescaleNormal();
				GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
				float angle = 0.0f;
				switch (facing) {
				case NORTH:
					angle = 90.0f;
					break;
				case EAST:
					angle = 0.0f;
					break;
				case SOUTH:
					angle = 270.0f;
					break;
				case WEST:
					angle = 180.0f;
					break;
				default:
					break;
				}
				GlStateManager.rotate(angle, 0.0f, 1.0f, 0.0f);
				GlStateManager.translate(-0.5f + position, height, 0.0f);
				Minecraft.getMinecraft().getRenderItem().renderItem(stack, TransformType.GROUND);
				GlStateManager.disableRescaleNormal();
				GlStateManager.popMatrix();
			}
		}
	}
}
