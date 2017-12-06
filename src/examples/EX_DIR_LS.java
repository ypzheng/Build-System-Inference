package examples;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import util.Utility;
public class EX_DIR_LS {
	
	//equivalent to ls command
	public static void main(String[] args) {
		Project project = new Project();
        ProjectHelper helper = new ProjectHelper();
        project.init();
        File buildFile = project.resolveFile("src/build.xml");
        helper.configureProject(project, buildFile);
		// TODO Auto-generated method stub
//		Path dir = Paths.get(project.getBaseDir().toString());
//        System.out.println(project.getBaseDir().toString());
//        try(DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*")){
//        	for(Path file:stream) {
//        		System.out.println(file.getFileName());
//        	}
//        } catch(IOException e) {
//        	e.printStackTrace();
//        }
        String[] result = Utility.lsDirectory(project.getBaseDir().toString(), "*");
        for(String name : result)
        	System.out.println(name);
	}

}
