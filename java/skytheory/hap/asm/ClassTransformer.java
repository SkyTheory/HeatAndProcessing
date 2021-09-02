package skytheory.hap.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import net.minecraft.launchwrapper.IClassTransformer;

public class ClassTransformer implements IClassTransformer {

	public static final String TARGET_DCUTIL = "defeatedcrow.hac.core.util.DCUtil";

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if (name.equals(TARGET_DCUTIL)) {
			ClassReader reader = new ClassReader(basicClass);
			ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
			ClassVisitor visitor = new DCUtilVisitor(writer);
			reader.accept(visitor, ClassReader.EXPAND_FRAMES);
			return writer.toByteArray();
		}
		return basicClass;
	}

}
