import java.io.File;
import java.util.List;

public class BuildFileAnalyzerAdapter implements BuildAnalyzer{
	BuildAnalyzer analyzer;
	//Other kinds of build files can be added here.
	
	public BuildFileAnalyzerAdapter(String buildFileType, File buildFile, String projectName) {
		if(buildFileType.equalsIgnoreCase("ant")) {
			analyzer = new AntBuildAnalyzer(buildFile, projectName);
		}
		//Add another kind of build file here
	}
	
	@Override
	public String getCompileSrcTarget() {
		return analyzer.getCompileSrcTarget();
	}

	@Override
	public String getCompileTestTarget() {
		return analyzer.getCompileTestTarget();
	}

	@Override
	public String getSrcDir() {
		return analyzer.getSrcDir();
	}

	@Override
	public String getTestDir() {
		return analyzer.getTestDir();
	}

	@Override
	public String getCompDir() {
		return analyzer.getCompDir();
	}

	@Override
	public String getCompTestDir() {
		return analyzer.getCompTestDir();
	}

	@Override
	public String getSrcDep() {
		return analyzer.getSrcDep();
	}

	@Override
	public String getTestDep() {
		return analyzer.getTestDep();
	}

	@Override
	public String getTestList() {
		return analyzer.getTestList();
	}
	
	@Override
	public String getBaseDir() {
		return analyzer.getBaseDir();
	}
}
