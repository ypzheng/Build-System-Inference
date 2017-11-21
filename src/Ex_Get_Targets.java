import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.Target;

public class Ex_Get_Targets {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Project project = new Project();
		ProjectHelper helper = new ProjectHelper();
		project.init();
		File buildFile = project.resolveFile("src/build.xml");
		helper.configureProject(project, buildFile);
		Hashtable target_map = project.getTargets();
		
		Enumeration names = target_map.keys();
		
		while(names.hasMoreElements()) {
			String str = (String) names.nextElement();
			Target t = (Target) target_map.get(str);
			System.out.println("Name: " + str + ", Description: " + t.getDescription());
		}
		
	}

}
