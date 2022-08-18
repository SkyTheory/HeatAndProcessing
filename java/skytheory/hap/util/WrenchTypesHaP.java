package skytheory.hap.util;

import defeatedcrow.hac.api.blockstate.DCState;
import defeatedcrow.hac.main.block.build.BlockSlabDC;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import skytheory.lib.util.IWrenchType;
import skytheory.lib.util.WrenchHelper;
import skytheory.lib.util.WrenchType;

public class WrenchTypesHaP {

	/** DCSideに対応するALL_FACING */
	public static final IWrenchType DC_ALL_FACINGS = new WrenchType("DC_All_Facings") {
		@Override
		public void interact(EntityPlayer player, World world, BlockPos pos, EnumHand hand,
				EnumFacing facing) {
			if (player.isSneaking()) WrenchHelperHaP.invertDCSide(world, pos);
			else WrenchHelperHaP.rotateDCSide(world, pos, facing);
		}
	};

	/** DCSideに対応するHORIZONTAL */
	public static final IWrenchType DC_HORIZONTAL = new WrenchType("DC_Horizontal") {
		@Override
		public void interact(EntityPlayer player, World world, BlockPos pos, EnumHand hand,
				EnumFacing facing) {
			if (player.isSneaking()) WrenchHelperHaP.invertDCSide(world, pos);
			else WrenchHelperHaP.rotateDCSideY(world, pos, player);
		}
	};

	/** HaCの自由回転可能なTorqueMachineに対応するWrenchType */
	public static final IWrenchType DC_TORQUE = new WrenchType("DC_Torque") {
		@Override
		public void interact(EntityPlayer player, World world, BlockPos pos, EnumHand hand,
				EnumFacing facing) {
			if (player.isSneaking()) WrenchHelperHaP.invertDCSideAngle(player, world, pos);
			else WrenchHelperHaP.rotateDCSideAngle(player, world, pos, facing);
		}
	};

	/** HaCの蛇口に対応するWrenchType */
	public static final IWrenchType DC_FAUCET = new WrenchType("DC_Faucet") {
		@Override
		public void interact(EntityPlayer player, World world, BlockPos pos, EnumHand hand,
				EnumFacing facing) {
			WrenchHelperHaP.rotateFaucet(world, pos, facing);
		}
	};

	/** HaCの蛇口に対応するWrenchType */
	public static final IWrenchType DC_FAUCET_REVERSED = new WrenchType("DC_Faucet_Reversed") {
		@Override
		public void interact(EntityPlayer player, World world, BlockPos pos, EnumHand hand,
				EnumFacing facing) {
			WrenchHelperHaP.rotateFaucetReversed(world, pos, facing);
		}
	};

	/** DCState.FLAGに対応するWrenchType */
	public static final IWrenchType DC_FLAG = new WrenchType("DC_Flag") {
		@Override
		public void interact(EntityPlayer player, World world, BlockPos pos, EnumHand hand,
				EnumFacing facing) {
			WrenchHelper.cycleProperty(world, pos, DCState.FLAG);
		}
	};

	/** BlockSlabDC.SIDEに対応するWrenchType */
	public static final IWrenchType DC_SLAB = new WrenchType("DC_Slab") {
		@Override
		public void interact(EntityPlayer player, World world, BlockPos pos, EnumHand hand,
				EnumFacing facing) {
			WrenchHelper.cycleProperty(world, pos, BlockSlabDC.SIDE);
		}
	};

	public static final IWrenchType DC_CATAPULT = new WrenchType("DC_Catapult") {
		@Override
		public void interact(EntityPlayer player, World world, BlockPos pos, EnumHand hand,
				EnumFacing facing) {
			if (player.isSneaking()) {
				WrenchHelperHaP.rotateDCSideY(world, pos, player);
			}
		}
		@Override
		public boolean skipActivateBlock(EntityPlayer player, World world, BlockPos pos, EnumFacing side) {
			return false;
		}
	};

	public static final IWrenchType DC_PLAYER_PALEL = new WrenchType("DC_Player_Panel") {
		@Override
		public void interact(EntityPlayer player, World world, BlockPos pos, EnumHand hand,
				EnumFacing facing) {
			if (player.isSneaking()) {
				WrenchHelper.rotateFacing(world, pos, EnumFacing.UP);
			}
		}
		@Override
		public boolean skipActivateBlock(EntityPlayer player, World world, BlockPos pos, EnumFacing side) {
			return false;
		}
	};
}
