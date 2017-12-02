import java.io.File;

public class BuildFileAnalyzerAdapter implements BuildAnalyzer{
	AntBuildAnalyzer aa;
	//Other kinds of build files can be added here.
	
	public BuildFileAnalyzerAdapter(String buildFileType, File buildFile) {
		if(buildFileType.equalsIgnoreCase("ant")) {
			aa = new AntBuildAnalyzer(buildFile);
		}
		//Add another kind of build file here
	}
	
	@Override
	public String getCompileSrcTarget() {
		// TODO Auto-generated method stub
		return aa.getCompileSrcTarget();
	}

	@Override
	public String getCompileTestTarget() {
		// TODO Auto-generated method stub
		return aa.getCompileTestTarget();
	}

	@Override
	public String getSrcDir() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTestDir() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCompDir() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCompTestDir() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSrcDep() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTestDep() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTestList() {
		// TODO Auto-generated method stub
		return null;
	}
}
