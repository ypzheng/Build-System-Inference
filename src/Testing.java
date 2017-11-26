import java.io.File;
import java.io.IOException;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.RuntimeConfigurable;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Path;
import java.util.*;
public class Testing {

	public static void main(String[] args) throws IOException {
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
			Target target = (Target) target_map.get(str);
			System.out.println("Name: " + str + ", Description: " + target.getDescription());
			Task[] tasks = target.getTasks();
			for(int i =0; i < tasks.length; i++) {
				System.out.println("Task "+i+": "+tasks[i].getTaskName());
				RuntimeConfigurable rt =tasks[i].getRuntimeConfigurableWrapper();
				Hashtable att_map = rt.getAttributeMap();
				Enumeration att_names = att_map.keys();
				
				while(att_names.hasMoreElements()) {
					String att_name = (String) att_names.nextElement();
					
					System.out.println("Attribute: "+att_name+" "+att_map.get(att_name));
				}
				System.out.println(".....................................");
			}
		}
		
	}

}
