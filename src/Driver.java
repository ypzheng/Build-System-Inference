import java.io.File;

public class Driver {

	public static void main(String[] args) {
		File buildFile = new File("src/build.xml");
		BuildFileAnalyzerAdapter adapter = new BuildFileAnalyzerAdapter("ant", buildFile);
	}

}
