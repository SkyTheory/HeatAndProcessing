package skytheory.hap.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;

public class EndermanVisitor extends ClassVisitor implements Opcodes {

	public static final String TARGET_METHOD = "shouldAttackPlayer";
	public static final String TARGET_METHOD_SRG = "func_70821_d";
	public static final String TARGET_DESC = "(Lnet/minecraft/entity/player/EntityPlayer;)Z";
	public static final String HOOK_OWNER = "skytheory/hap/asm/EndermanHook";
	public static final String HOOK_NAME = "cancelHostility";
	public static final String HOOK_DESC = "(Lnet/minecraft/entity/monster/EntityEnderman;Lnet/minecraft/entity/player/EntityPlayer;)Z";

	public final String owner;

	public EndermanVisitor(String owner, ClassVisitor cv) {
		super(ASM4, cv);
		this.owner = owner;
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		MethodVisitor visitor = super.visitMethod(access, name, desc, signature, exceptions);
		String methodName = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(owner, name, desc);
		String methodDesc = FMLDeobfuscatingRemapper.INSTANCE.mapMethodDesc(desc);
		if ((methodName.equals(TARGET_METHOD) || methodName.equals(TARGET_METHOD_SRG)) && methodDesc.equals(TARGET_DESC)) {
			MethodVisitor mv = new ShouldAttackPlayerTransformer(visitor);
			return mv;
		}
		return visitor;
	}

	public static class ShouldAttackPlayerTransformer extends MethodVisitor {

		public ShouldAttackPlayerTransformer(MethodVisitor visitor) {
			super(ASM4, visitor);
		}

		@Override
		public void visitCode() {
			Label label = new Label();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKESTATIC, HOOK_OWNER, HOOK_NAME, HOOK_DESC, false);
			mv.visitJumpInsn(IFEQ, label);
			mv.visitLdcInsn(0);
			mv.visitInsn(IRETURN);
			mv.visitLabel(label);
			mv.visitCode();
		}

	}

}
