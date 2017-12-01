import java.io.File;
import java.util.*;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.taskdefs.Tar;

public class Ex_Get_Depend {
    private Project project;
    private ProjectHelper helper;
    private File buildFile;
    private String COMPILE = "compile";
    private String TEST = "test";

    public Ex_Get_Depend() {
        project = new Project();
        project.init();
        helper = new ProjectHelper();
        buildFile = project.resolveFile("src/build.xml");
        helper.configureProject(project, buildFile);
    }

    private Stack<Target> getCompileTarget(String type) {
        Stack<Target> return_targets = new Stack<>();
        Hashtable<String, Target> targets_table = project.getTargets();
        Enumeration<Target> targets = targets_table.elements();
        while (targets.hasMoreElements()) {
            Target target = targets.nextElement();
            String t_name = target.getName();
            if (type.equals(COMPILE)) {
                if (t_name.contains(COMPILE) && !t_name.contains(TEST)) {
                    return_targets.add(target);
                }
            } else {
                if (t_name.contains(TEST)) {
                    return_targets.add(target);
                }
            }
        }
        return return_targets;
    }

    private Target getTarget(String name) {
        Hashtable<String, Target> targets_table = project.getTargets();
        Enumeration<Target> targets = targets_table.elements();
        while (targets.hasMoreElements()) {
            Target target = targets.nextElement();
            String t_name = target.getName();
            if (t_name.equals(name)) {
                return target;
            }
        }
        return null;
    }

    public Map<String, ArrayList<String>> getDependencies() {
        Stack<Target> targets = getCompileTarget(TEST);
        Map<String,ArrayList<String>> dependencies = new HashMap<>();
        while (!targets.isEmpty()) {
            Target t = targets.pop();
            Enumeration<String> targetDependencies = t.getDependencies();
            ArrayList<String> temp = new ArrayList<>();
            while (targetDependencies.hasMoreElements()) {
                String tname = targetDependencies.nextElement();
                temp.add(tname);
                Target newTarget = getTarget(tname);
                targets.add(newTarget);
            }
            dependencies.put(t.getName(), temp);
        }
        return dependencies;
    }
}
