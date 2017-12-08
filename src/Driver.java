import java.io.File;
import java.io.IOException;import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.nio.file.Paths;

public class Driver {

	public static void main(String[] args) throws IOException {
		File buildFile = new File("commons-math-build.xml");
		File outputFile = new File("build.properties");
		PropertyWriter pw = new PropertyWriter("ant", buildFile, outputFile, "commons-math");

        AntBuildAnalyzer aba = new AntBuildAnalyzer(buildFile, "commons-math");
        System.out.println("compile target: " +aba.getCompileSrcTarget());
        System.out.println("compile test target: " +aba.getCompileTestTarget());
        System.out.println("test source: "+aba.getTestDir());
        System.out.println("source dir: "+aba.getSrcDir());
        System.out.println("compiled test classes: "+aba.getCompTestDir());
        System.out.println("compiled source classes: "+aba.getCompDir());
        System.out.println("comp src dep: "+aba.getSrcDep());
        System.out.println("comp test dep: "+aba.getTestDep());
        System.out.println(aba.getTestList());

	}

}
