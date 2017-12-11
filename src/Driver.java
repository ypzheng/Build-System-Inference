import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

import util.WildCardResolver;

import java.nio.file.Paths;

public class Driver {

	public static void main(String[] args) throws IOException {
		String path = "/Users/caitlynzheng/eclipse-workspace/closure-compiler";
//		String path = "/Users/caitlynzheng/eclipse-workspace/BSI/test/";
//		String path = "";
		File buildFile;
		
		System.out.println("Please input your project path: ");
		Scanner scanner = new Scanner(System.in);
//		path = scanner.nextLine();
		
		String[] includes = {"*build**.xml"};
		String[] excludes = {};
		String str[] = WildCardResolver.resolveWildCard(includes, excludes, path);
		if(str.length == 1) {
			buildFile = new File(Paths.get(path).toString() + Paths.get("/") + str[0]);
		}
		else if(str.length == 0) {
			System.out.println("No build file found, please manually input your build file name: ");
			int index = path.lastIndexOf(Paths.get("/").toString());
			buildFile = new File(Paths.get(path.substring(0,index+1)) + Paths.get("/").toString() + scanner.nextLine());
			
		}
		else{
			System.out.println("More than 1 *build.xml files found, please manually input your build file name:");
			int index = path.lastIndexOf(Paths.get("/").toString());
			buildFile = new File(Paths.get(path.substring(0,index+1)) + Paths.get("/").toString() + scanner.nextLine());
		}
			
		File outputFile = new File("build.properties");
		PropertyWriter pw = new PropertyWriter("ant", buildFile, outputFile, Paths.get(path).toString());
		pw.setProperties();
			
//			File buildFile1 = new File("test/TestBuildFile2.xml");
//	        AntBuildAnalyzer aba = new AntBuildAnalyzer(buildFile1, "/Users/caitlynzheng/eclipse-workspace/");
//	        System.out.println("compile target: " +aba.getCompileSrcTarget());
//	        System.out.println("compile test target: " +aba.getCompileTestTarget());
//	        System.out.println("test source: "+aba.getTestDir());
//	        System.out.println("source dir: "+aba.getSrcDir());
//	        System.out.println("compiled test classes: "+aba.getCompTestDir());
//	        System.out.println("compiled source classes: "+aba.getCompDir());
//	        System.out.println("comp src dep: "+aba.getSrcDep());
//	        System.out.println("comp test dep: "+aba.getTestDep());
//	        System.out.println(aba.getTestList());
	}

}
