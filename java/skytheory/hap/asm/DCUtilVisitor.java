package skytheory.hap.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import defeatedcrow.hac.api.magic.CharmType;
import defeatedcrow.hac.api.magic.IJewelCharm;
import defeatedcrow.hac.core.plugin.baubles.DCPluginBaubles;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.Loader;
import skytheory.hap.config.HaPConfig;

public class DCUtilVisitor extends ClassVisitor implements Opcodes {

	public static final String TARGET_METHOD = "getPlayerCharm";
	public static final String TARGET_DUMMY = "getPlayerCharmDummy";
	public static final String TARGET_DESC = "(Lnet/minecraft/entity/player/EntityPlayer;Ldefeatedcrow/hac/api/magic/CharmType;)Lnet/minecraft/util/NonNullList;";
	public static final String TARGET_REPLACE = "skytheory/hap/asm/DCUtilVisitor";

	public DCUtilVisitor(ClassVisitor cv) {
		super(ASM4, cv);
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		MethodVisitor visitor = super.visitMethod(access, name, desc, signature, exceptions);
		if (name.equals(TARGET_METHOD) && desc.equals(TARGET_DESC)) {
			MethodVisitor replace = new getPlayerCharmTransformer(visitor);
			return replace;
		}
		return visitor;
	}

	public static class getPlayerCharmTransformer extends MethodVisitor {

		public getPlayerCharmTransformer(MethodVisitor visitor) {
			super(ASM4, visitor);
		}

		@Override
        public void visitCode() {
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKESTATIC, TARGET_REPLACE, TARGET_METHOD, TARGET_DESC, false);
            mv.visitInsn(ARETURN);
            mv.visitMaxs(1, 3);
            mv.visitEnd();
        }
	}

	/*
	 * 置き換え先のメソッド
	 * インベントリソート系のModがかなーり使いづらかったので……。
	 * 余談：OPって良く言われてるらしいけれど、装備コストは相応だと思うけれどなあ？
	 * 比較的強力な銀指輪が、他Modで銀をショートカットできる時の作成コストが低いのが原因だとは思う
	 */
	public static NonNullList<ItemStack> getPlayerCharm(EntityPlayer player, CharmType type) {
		NonNullList<ItemStack> charms = NonNullList.create();
		int range = HaPConfig.charm_extend ? 36 : 18;
		if (player == null) return charms;
		for (int index = 9, count = 0; index < range && count < HaPConfig.charm_max; index++) {
			ItemStack stack = player.inventory.getStackInSlot(index);
			if (!stack.isEmpty() && stack.getItem() instanceof IJewelCharm) {
				if (type == null || type.equals(((IJewelCharm) stack.getItem()).getCharmType(stack.getItemDamage()))) {
					addCharm(charms, stack);
					count += stack.getCount();
				}
			}
		}

		if (Loader.isModLoaded("baubles")) {
			NonNullList<ItemStack> baubles = DCPluginBaubles.getBaublesCharm(player, type);
			if (!baubles.isEmpty()) {
				baubles.stream().filter(s -> s.getItem() instanceof IJewelCharm).forEach(charm -> addCharm(charms, charm));
			}
		}

		return charms;
	}

	private static void addCharm(NonNullList<ItemStack> charms, ItemStack stack) {
		ItemStack charm = stack.copy();
		ItemStack match = charms.stream().filter(charm::isItemEqual).findFirst().orElse(ItemStack.EMPTY);
		if (!match.isEmpty()) {
			match.grow(charm.getCount());
		} else {
			charm = charm.copy();
			charms.add(charm);
		}
	}

}
