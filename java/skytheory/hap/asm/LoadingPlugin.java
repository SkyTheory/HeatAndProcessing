package skytheory.hap.asm;

import java.io.File;
import java.util.Map;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import skytheory.hap.config.HaPConfig;

@MCVersion("1.12.2")
public class LoadingPlugin implements IFMLLoadingPlugin {

	@Override
	public String[] getASMTransformerClass() {
		return new String[]{"skytheory.hap.asm.ClassTransformer"};
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Override
	public String getSetupClass() {
		return null;
	}


	@Override
	public void injectData(Map<String, Object> data) {
		File file = null;
		if (data.containsKey("mcLocation")) {
			file = (File) data.get("mcLocation");
		}
		if (file != null) {
			Configuration config = new Configuration(new File(file, "config/HeatAndProcessing.cfg"));
			HaPConfig.init(config);
		}
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}

}
