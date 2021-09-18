package skytheory.hap.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ColorPendant2Visitor extends ClassVisitor implements Opcodes {

	public static final String TARGET_OWNER = "defeatedcrow/hac/main/util/MainUtil";
	public static final String TARGET_METHOD = "onToolUsing";
	public static final String TARGET_DESC = "(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/item/ItemStack;)Z";
	public static final String HOOK_OWNER = "skytheory/hap/asm/ColorPendant2Hook";
	public static final String HOOK_NAME = "cancelLumberjack";
	public static final String HOOK_DESC = "(Ldefeatedcrow/hac/magic/item/ItemColorPendant2;Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/item/ItemStack;)Z";

	public ColorPendant2Visitor(ClassVisitor cv) {
		super(ASM4, cv);
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		MethodVisitor visitor = super.visitMethod(access, name, desc, signature, exceptions);
		if (name.equals(TARGET_METHOD) && desc.equals(TARGET_DESC)) {
			MethodVisitor replace = new getLumberTargetListTransformer(visitor);
			return replace;
		}
		return visitor;
	}

	public static class getLumberTargetListTransformer extends MethodVisitor {

		public getLumberTargetListTransformer(MethodVisitor visitor) {
			super(ASM4, visitor);
		}

		@Override
		public void visitCode() {
			Label label = new Label();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitVarInsn(ALOAD, 4);
			mv.visitMethodInsn(INVOKESTATIC, HOOK_OWNER, HOOK_NAME, HOOK_DESC, false);
			mv.visitJumpInsn(IFEQ, label);
			mv.visitLdcInsn(0);
			mv.visitInsn(IRETURN);
			mv.visitLabel(label);
			mv.visitCode();
		}

	}

}
