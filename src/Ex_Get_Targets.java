import java.io.File;
import java.util.*;
import java.util.Hashtable;

import org.apache.tools.ant.*;
import org.apache.tools.ant.taskdefs.Javac;
import util.AttributeParser;
import util.PropertyParser;

public class Ex_Get_Targets {

    public static void printMap(Map<String, String> map) {
        Set<String> keys = map.keySet();
        for (String key:keys) {
            System.out.println("Key: " + key +" Value: " +map.get(key));
        }
        System.out.println("==========");
    }

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Project project = new Project();
		ProjectHelper helper = new ProjectHelper();
		project.init();
		File buildFile = project.resolveFile("/Users/Jucong/Downloads/commons-lang-LANG_2_5/build.xml");
		helper.configureProject(project, buildFile);
        AttributeParser attributeParser = new AttributeParser(project);
        Hashtable<String, Target> target_table = project.getTargets();
		Enumeration<String > keys = target_table.keys();
		while (keys.hasMoreElements()) {
		    Target target = target_table.get(keys.nextElement());
		    Task[] tasks = target.getTasks();
		    for (Task tsk:tasks) {
                System.out.println(tsk.getTaskName());
                Map<String,String > attrMap = attributeParser.getAttributeValues(tsk);
               printMap(attrMap);
            }
        }
	}

}
