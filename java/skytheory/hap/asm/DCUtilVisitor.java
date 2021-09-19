package skytheory.hap.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class DCUtilVisitor extends ClassVisitor implements Opcodes {

	public static final String TARGET_METHOD = "getPlayerCharm";
	public static final String TARGET_DESC = "(Lnet/minecraft/entity/player/EntityPlayer;Ldefeatedcrow/hac/api/magic/CharmType;)Lnet/minecraft/util/NonNullList;";
	public static final String HOOK_OWNER = "skytheory/hap/asm/DCUtilHook";
	public static final String HOOK_NAME = "playerCharmHook";
	public static final String HOOK_DESC = "(Lnet/minecraft/entity/player/EntityPlayer;Ldefeatedcrow/hac/api/magic/CharmType;)Lnet/minecraft/util/NonNullList;";

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
            mv.visitMethodInsn(INVOKESTATIC, HOOK_OWNER, HOOK_NAME, HOOK_DESC, false);
            mv.visitInsn(ARETURN);
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }
	}

}
