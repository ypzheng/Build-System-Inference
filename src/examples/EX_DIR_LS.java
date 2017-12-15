package examples;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;

import util.PathParser;
import util.FileUtility;
public class EX_DIR_LS {
	
	//equivalent to ls command
	public static void main(String[] args) {
		Project project = new Project();
        ProjectHelper helper = new ProjectHelper();
        project.init();
        File buildFile = project.resolveFile("closure-compiler-build.xml");
        helper.configureProject(project, buildFile);

        String[] result = FileUtility.lsDirectoryRS(project.getBaseDir().toString(), "(.*)[.java]","[Ant](.*)");

        for(String name : result)
        	System.out.println(name);
	}

}
