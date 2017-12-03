import java.io.File;
import java.util.List;

public class BuildFileAnalyzerAdapter implements BuildAnalyzer{
	BuildAnalyzer analyzer;
	//Other kinds of build files can be added here.
	
	public BuildFileAnalyzerAdapter(String buildFileType, File buildFile) {
		if(buildFileType.equalsIgnoreCase("ant")) {
			analyzer = new AntBuildAnalyzer(buildFile);
		}
		//Add another kind of build file here
	}
	
	@Override
	public String getCompileSrcTarget() {
		// TODO Auto-generated method stub
		return analyzer.getCompileSrcTarget();
	}

	@Override
	public String getCompileTestTarget() {
		// TODO Auto-generated method stub
		return analyzer.getCompileTestTarget();
	}

	@Override
	public String getSrcDir() {
		// TODO Auto-generated method stub
		return analyzer.getSrcDir();
	}

	@Override
	public String getTestDir() {
		// TODO Auto-generated method stub
		return analyzer.getTestDir();
	}

	@Override
	public String getCompDir() {
		// TODO Auto-generated method stub
		return analyzer.getCompDir();
	}

	@Override
	public String getCompTestDir() {
		// TODO Auto-generated method stub
		return analyzer.getCompTestDir();
	}

	@Override
	public String getSrcDep() {
		// TODO Auto-generated method stub
		return analyzer.getSrcDep();
	}

	@Override
	public String getTestDep() {
		// TODO Auto-generated method stub
		return analyzer.getTestDep();
	}

	@Override
	public String getTestList() {
		// TODO Auto-generated method stub
		return analyzer.getTestList();
	}
}
