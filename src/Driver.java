import java.io.File;

public class Driver {

	public static void main(String[] args) {
		File buildFile = new File("src/TestBuildFile1.xml");
		BuildFileAnalyzerAdapter adapter = new BuildFileAnalyzerAdapter("ant", buildFile);
		System.out.println("test target: "+adapter.getCompileTestTarget());
		
	}

}
