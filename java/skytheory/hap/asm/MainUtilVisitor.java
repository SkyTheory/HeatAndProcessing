package skytheory.hap.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class MainUtilVisitor extends ClassVisitor implements Opcodes {

	public static final String TARGET_OWNER = "defeatedcrow/hac/main/util/MainUtil";
	public static final String TARGET_METHOD = "getLumberTargetList";
	public static final String TARGET_DESC = "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;I)Ljava/util/Set;";
	public static final String HOOK_OWNER = "skytheory/hap/asm/MainUtilHook";
	public static final String HOOK_NAME = "hookLumberjack";
	public static final String HOOK1_DESC = "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;)Z;";
	public static final String HOOK2_DESC = "(Lnet/minecraft/util/math/BlockPos;)Ljava/util/Set;";

	public MainUtilVisitor(ClassVisitor cv) {
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

		public getLumberTargetListTransformer(MethodVisitor mv) {
			super(ASM4, mv);
		}

		@Override
		public void visitCode() {
			Label label = new Label();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKESTATIC, HOOK_OWNER, HOOK_NAME, HOOK1_DESC, false);
			mv.visitJumpInsn(IFEQ, label);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKESTATIC, HOOK_OWNER, HOOK_NAME, HOOK2_DESC, false);
			mv.visitInsn(ARETURN);
			mv.visitLabel(label);
			mv.visitCode();
		}
	}
}
