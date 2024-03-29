package skytheory.hap.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import net.minecraft.launchwrapper.IClassTransformer;
import skytheory.hap.asm.config.HaPASMConfig;

public class ClassTransformer implements IClassTransformer {

	public static final String TARGET_ENDERMAN = "net.minecraft.entity.monster.EntityEnderman";
	public static final String TARGET_ENDERMAN_OBF = "acu";

	public static final String TARGET_DCUTIL = "defeatedcrow.hac.core.util.DCUtil";

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {

		if (transformedName.equals(TARGET_DCUTIL) && HaPASMConfig.asm_charm) {
			ClassReader reader = new ClassReader(basicClass);
			ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
			ClassVisitor visitor = new DCUtilVisitor(writer);
			reader.accept(visitor, ClassReader.EXPAND_FRAMES);
			return writer.toByteArray();
		}

		if (transformedName.equals(TARGET_ENDERMAN) || transformedName.equals(TARGET_ENDERMAN_OBF)) {
			if (HaPASMConfig.asm_enderman) {
				ClassReader reader = new ClassReader(basicClass);
				ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
				ClassVisitor visitor = new EndermanVisitor(name, writer);
				reader.accept(visitor, ClassReader.EXPAND_FRAMES);
				return writer.toByteArray();
			}
		}

		return basicClass;
	}

}