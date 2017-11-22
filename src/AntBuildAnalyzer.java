import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Path;

public class AntBuildAnalyzer implements BuildFileAnalyzer{
	private Vector sortedTargets,potentialTargets;
	private Target compileSrcTarget, compileTestTarget;
	
	public AntBuildAnalyzer(File f) {
		Project project = new Project();
		project.init();
		ProjectHelper helper = new ProjectHelper();
		helper.configureProject(project, f);
		sortedTargets = project.topoSort(project.getDefaultTarget(), project.getTargets());
		potentialTargets = new Vector();
		Enumeration vEnum = sortedTargets.elements();
	    System.out.println("Targets sorted in order of execution:");
	    while(vEnum.hasMoreElements())
	    		System.out.print(vEnum.nextElement() + "\n");
	    this.getPotentialCompileTargets();
	}

	@Override
	public File infer() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void getPotentialCompileTargets() {
		Enumeration vEnum = sortedTargets.elements();
		while(vEnum.hasMoreElements()) {
			Target t = (Target) vEnum.nextElement();
			if(containsJavac(t.getTasks())) {
				potentialTargets.add(t);
				System.out.println(t.getName());
			}
		}
	}
	
	private boolean containsJavac(Task[] tasks) {
		for(Task t : tasks) {
			if(t.getTaskType().equals("javac")) {
				return true;
			}
		}
		return false;
	}
	
	public String getCompileSrcTarget() {
		Enumeration vEnum = potentialTargets.elements();
		while(vEnum.hasMoreElements()) {
			Target t = (Target) vEnum.nextElement();
			if(t.getName().equalsIgnoreCase("build")||
					(t.getName().equalsIgnoreCase("compile")||
							(t.getName().equalsIgnoreCase("prepare")))) {
				return t.getName();
			}
		}
		//TODO: other target names
		return "";
	}
}
