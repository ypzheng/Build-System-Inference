import java.io.File;
import java.io.IOException;import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.nio.file.Paths;

public class Driver {

	public static void main(String[] args) throws IOException {
//		Ex_Get_Depend parser = new Ex_Get_Depend();
//        Map<String, ArrayList<String>> dependencies = parser.getDependencies();
//        for(String key : dependencies.keySet()) {
//            ArrayList<String> values = dependencies.get(key);
//            System.out.print(key +" is dependent on ");
//            for (String d: values) {
//                System.out.print(d+", ");
//            }
//            System.out.println();
//        }

		//"/Users/juconghe/Downloads/commons-lang-LANG_2_5/build.xml"
		File buildFile = new File("closure-compiler-build.xml");
		File outputFile = new File("build.properties");
		PropertyWriter pw = new PropertyWriter("ant", buildFile, outputFile);

        AntBuildAnalyzer aba = new AntBuildAnalyzer(buildFile);
        System.out.println("compiled classes: " +aba.getCompileSrcTarget());
        System.out.println("compiled classes: " +aba.getCompDir());
        System.out.println("test source: "+aba.getTestDir());
        System.out.println("source dir: "+aba.getSrcDir());
        System.out.println("compiled test classes: "+aba.getCompTestDir());
        System.out.println(aba.getTestList());

	}

}
