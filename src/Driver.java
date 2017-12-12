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
//		String path = "/Users/caitlynzheng/eclipse-workspace/closure-compiler";
		String path = "/Users/caitlynzheng/eclipse-workspace/BSI/test/";
//		String path = "";
		File buildFile;
		
		System.out.println("Please input your project path: ");
		Scanner scanner = new Scanner(System.in);
//		path = scanner.nextLine();
		
		String[] includes = {"*build*.xml","*Build*.xml"};
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
		
	}

}
