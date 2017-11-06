import java.io.File;
import java.io.IOException;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.types.Path;

public class Testing {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Project project = new Project();
		ProjectHelper helper = new ProjectHelper();
		project.init();
		File buildFile = project.resolveFile("src/build.xml");
//		System.out.println(helper.getDefaultBuildFile());
		helper.configureProject(project, buildFile);
		Path path = (Path) project.getReference("test.classpath");
		System.out.println(path);
	}

}
