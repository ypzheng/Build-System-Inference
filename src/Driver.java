import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import util.WildCardResolver;

public class Driver {

	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		String path = "";
		int count = 0;
		String projectPath = "";
		File buildFile;
		
		System.out.println("Please input a Dir containing build files: ");
		Scanner scanner = new Scanner(System.in);
		path = scanner.nextLine();
		
		File folder = new File(path);
		if(!folder.isDirectory()) {
			System.out.println("Not a dir");
		}
		else {
			for(final File dirs:folder.listFiles()) {
				if(!dirs.isDirectory()) {
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
