import java.io.File;

public class BuildFileAnalyzerAdapter implements BuildFileAnalyzer{
	AntBuildAnalyzer aa;
	//Other kinds of build files can be added here.
	
	public BuildFileAnalyzerAdapter(String buildFileType, File buildFile) {
		if(buildFileType.equalsIgnoreCase("ant")) {
			aa = new AntBuildAnalyzer(buildFile);
		}
		//Add another kind of build file here
	}
	
	public File infer() {
		// TODO Auto-generated method stub
		return null;
	}
}
