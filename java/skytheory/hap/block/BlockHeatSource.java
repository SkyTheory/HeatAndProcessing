package skytheory.hap.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockHeatSource extends Block {

	public BlockHeatSource() {
		super(Material.IRON);
		this.setHardness(5.0F);
		this.setResistance(10.0F);
		this.setSoundType(SoundType.METAL);
		this.setLightLevel(1.0F);
		this.setHarvestLevel("pickaxe", 1);
	}
}
