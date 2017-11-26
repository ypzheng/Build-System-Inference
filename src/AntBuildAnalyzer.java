import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.RuntimeConfigurable;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Path;

public class AntBuildAnalyzer implements BuildFileAnalyzer{
	private Vector sortedTargets,potentialTargets;
	private ArrayList<Target> potentialSrcTargets, potentialTestTargets;
	private Target compileSrcTarget, compileTestTarget;
	
	
	public AntBuildAnalyzer(File f) {
		//Initialize Variables
		compileSrcTarget = null;
		compileTestTarget = null;
		potentialSrcTargets = new ArrayList<Target>();
		potentialTestTargets = new ArrayList<Target>();
		
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
				if(t.getName().contains("test")) {
					potentialTestTargets.add(t);
					System.out.println("test: "+t.getName());
				}
				else{
					potentialSrcTargets.add(t);
					System.out.println("src: "+t.getName());
				}
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
		int size = potentialSrcTargets.size();
		if(size == 0) {
			System.out.println("Cannot find target that compiles source");
			return "";
		}
		else if(size == 1) {
			compileSrcTarget = potentialSrcTargets.get(0);
		}
		else if(size > 1) {
			compileSrcTarget = potentialSrcTargets.get(size - 1);
		}
		
		return compileSrcTarget.getName();
	}
	
	/**
	 * Find source directory of compilation
	 * 
	 * IF directory is not found return an empty String
	 * 
	 * @return String
	 */
	public String getCompileSrcDirectory() {
		
		//Compile Target is not found yet
		if(compileSrcTarget == null) {
			
			//Cannot find Compile Target
			if(getCompileSrcTarget().equals("")) {
				return "";
			}
		} 
		
		//Infer Src Directory from Compile Target
		/**
		 * Find "javac" Task
		 * Looks for "srcdir" attribute
		 */
		Task[] tasks = compileSrcTarget.getTasks();
		for(Task t : tasks) {
			if(t.getTaskType().equals("javac")) {
				RuntimeConfigurable rt =t.getRuntimeConfigurableWrapper();
				Hashtable att_map = rt.getAttributeMap();
				
				String srcDirectory = (String) att_map.get("srcdir");
				if(srcDirectory == null) {
					return "";
				}else {
					return srcDirectory;
				}
			}
		}
		return "";
		
		
	}
}
