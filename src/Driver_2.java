import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

import util.WildCardResolver;

public class Driver_2 {

	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		String path = "";
		int count = 0;
		String projectPath = "";
		File buildFile;
		
		System.out.println("Please input a Dir contain projects: ");
		Scanner scanner = new Scanner(System.in);
		path = scanner.nextLine();
		
		File folder = new File(path);
		if(!folder.isDirectory()) {
			System.out.println("Not a dir");
		}
		else {
			for(final File dirs:folder.listFiles()) {
				if(dirs.isDirectory()) {
					String[] includes = {"*build**.xml","*Build**.xml"};
					String[] excludes = {};
					String str[] = WildCardResolver.resolveWildCard(includes, excludes, dirs.toString());
					System.out.println(str.length);
					if(str.length == 1) {
						buildFile = new File(dirs.getPath() + Paths.get("/") + str[0]);
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
					projectPath = buildFile.getParent();
					String[] name = projectPath.split(Paths.get("/").toString());
					File outputFile = new File("build-"+name[name.length-1]+".properties");
					PropertyWriter pw = new PropertyWriter("ant", buildFile, outputFile, Paths.get(path).toString());
					pw.setProperties();
				}
				
				else {
					buildFile = new File(dirs.toString());
					File outputFile = new File("build-"+count+".properties");
					count++;
					PropertyWriter pw = new PropertyWriter("ant", buildFile, outputFile, Paths.get(path).toString());
					pw.setProperties();
				}
				
			}
			
		}
		
	}

}
