import java.io.File;


import java.nio.file.Paths;


import java.util.*;


import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Path;


import util.PathParser;
import util.TargetHelper;
import util.Debugger;
import util.FileUtility;
import util.TaskHelper;
import util.TestListHelper;
import util.WildCardResolver;



public class AntBuildAnalyzer implements BuildAnalyzer{
	private Vector sortedTargets;
	private ArrayList<Target> junitTargets;
	private Target compileSrcTarget, compileTestTarget;

	private PathParser pp;
	private TaskHelper taskHelper;
    private TestListHelper testHelper;
    private TargetHelper targetHelper;
    private Project project;

    public AntBuildAnalyzer(File f, String projectPath) {
		//Initialize Variables
		compileSrcTarget = null;
		compileTestTarget = null;
		junitTargets = new ArrayList<Target>();

		//Enable Console printing
//		Debugger.enable();

		//Load in build.xml file
		project = new Project();
		project.init();
		ProjectHelper helper = new ProjectHelper();
		helper.configureProject(project, f);
		
		if(project.getDefaultTarget() != null)
			sortedTargets = project.topoSort(project.getDefaultTarget(), project.getTargets());
		else
			sortedTargets = project.topoSort("", project.getTargets());
		
		
			
		//Print out all targets in execution order
		Enumeration vEnum = sortedTargets.elements();

		Debugger.log("Targets sorted in order of execution:");

	    while(vEnum.hasMoreElements())
	    		Debugger.log(vEnum.nextElement() + "\n");

	    //Path Parser
	    pp = new PathParser(project);

	    //taskHelper
	    taskHelper = new TaskHelper(pp);
	    
	    testHelper = new TestListHelper(pp, taskHelper);
	    
	    targetHelper = new TargetHelper(sortedTargets, taskHelper);
	    
	    //Initialize targets needed
	    this.compileSrcTarget = targetHelper.getCompileTarget();
	    this.compileTestTarget = targetHelper.getCompileTestTarget();
	    this.junitTargets = targetHelper.getTargets("junit");
	    
    }

	/**
	 * Get compile-source target
	 * @return
	 */
	public String getCompileSrcTarget() {
		
		if(compileSrcTarget == null)
			return "";
		
		return compileSrcTarget.getName();
	}

	/**
	 * Get compile-test target
	 * @return
	 */
	public String getCompileTestTarget() {
		if(compileTestTarget == null)
			return "";
		
		return compileTestTarget.getName();
	}

	/**
	 * Find directory of compiled classes
	 *
	 * IF directory is not found return an empty String
	 *
	 * @return String
	 */
	@Override
	public String getCompDir() {
		
		return taskHelper.getDirectory("javac", "destdir", compileSrcTarget);
	}

	@Override
	public String getTestDir() {
		
		String result="";
		result += taskHelper.getDirectory("javac", "srcdir", compileTestTarget);
		
		/*
		 * Handle Edge Case
		 * <src path> under <javac> 
		 */
		List<Task> tasks = taskHelper.getTasks("javac", this.compileSrcTarget);
		if(tasks != null) {
			String[] additional_dirs=taskHelper.getSubTaskAttr(tasks.toArray(new Task[tasks.size()]), "src", "path");
			if(additional_dirs.length > 0 && result.length()>0)
				result+=";";
			
			for(int i = 0; i < additional_dirs.length-1;i++){
				result+=pp.parse(additional_dirs[i]);
				result+=", ";
			}
			if(additional_dirs.length>0)
				result+=pp.parse(additional_dirs[additional_dirs.length-1]);
		}
			
		/*
		 * Handle Edge Case
		 * End
		 */
		
		
		return result;
	}

	/**
	 * Find source directory of compilation
	 *
	 * IF directory is not found return an empty String
	 *
	 * @return String
	 */
	@Override
	public String getSrcDir() {
		String result="";
		result += taskHelper.getDirectory("javac", "srcdir", compileSrcTarget);
		
		/*
		 * Handle Edge Case
		 * <src path> under <javac> 
		 */
		List<Task> tasks = taskHelper.getTasks("javac", this.compileSrcTarget);
		if(tasks != null) {
			String[] additional_dirs=taskHelper.getSubTaskAttr(tasks.toArray(new Task[tasks.size()]), "src", "path");
			if(additional_dirs.length > 0 && result.length()>0)
				result+=";";
			
			for(int i = 0; i < additional_dirs.length-1;i++){
				result+=pp.parse(additional_dirs[i]);
				result+=", ";
			}
			if(additional_dirs.length>0)
				result+=pp.parse(additional_dirs[additional_dirs.length-1]);
		}
			
		/*
		 * Handle Edge Case
		 * End
		 */
		
		
		return result;
	}

	/**
	 * Find compiled test directory
	 */
	@Override
	public String getCompTestDir() {
		return taskHelper.getDirectory("javac", "destdir", compileTestTarget);
		
	}

	/**
	 * Get dependencies that are required to compile source files
	 */
	@Override
	public String getSrcDep() {
		String ret = "";
		try {
			ret = this.dependencyHelper(this.compileSrcTarget);
		} catch (Exception e) {
			Debugger.log("Cannot find the exact source dependency files\n "
					+ "Possible cause: 1) Only build file is passed in or "
					+ "2) Cannot find jar file locally");
		}
		return ret;
	}

	/**
	 *  Get dependencies required to compile test files
	 */
	@Override
	public String getTestDep() {
		String ret = "";
		try {
			ret = this.dependencyHelper(this.compileTestTarget);
		} catch (Exception e) {
			Debugger.log("Cannot find the exact test dependency files\n "
					+ "Possible cause: 1) Only build file is passed in or "
					+ "2) Cannot find jar file locally");
		}
		return ret;
	}
	
	private String dependencyHelper(Target t) {
		String deps = "";
        
        //Get all tasks that contains javac task under compileSrcTarget
		List<Task> javac_tasks = taskHelper.getTasks("javac",t);
		//Get all classpath tasks' refid values in an array of String
		String[] classpath_refid_list = taskHelper.getSubTaskAttr(javac_tasks.toArray(new Task[javac_tasks.size()]), "classpath", "refid");
		
		for(String s : classpath_refid_list) {
			Path p = this.project.getReference(s);
			//Since we are only insterested in .jar files, filter them out and append to output string
			
			String[] filtered_deps = FileUtility.filterPath(p.list(), true,"(.*)[jar]");
			
			for(String filtered_dep : filtered_deps) {
				deps += pp.parse(filtered_dep) +";";
			}
		}
		
		// TODO Handle fileset tasks
		Path path = (Path) project.getReference("classpath");
		//Formating output string, remove last ","
		if(deps.length()>0 && deps.substring(deps.length()-1).equals(";"))
			deps = deps.substring(0, deps.length()-1);
		return deps;
	}

	/**
	 * Use directory scanner to get a list of tests with test dir, include, and exclude
	 * from map.
	 */
    @Override
	public String getTestList() {
    		Map<String, String> keyVal = new HashMap<String, String>();
    		String ret = "";
    		if(junitTargets.size() == 0) {
    			Debugger.log("No junit tasks, make sure this project contains unit tests.");
    			return "";
    		}
    		keyVal = testHelper.getTestList(junitTargets);
    		
    		if(keyVal.size() != 0) {
    			String[] includes = keyVal.get("include").split(";");
            	String[] excludes = keyVal.get("exclude").split(";");
            	String[] str = WildCardResolver.resolveWildCard(includes, excludes, this.getBaseDir()+Paths.get("/")+keyVal.get("dir"));
            	//no test dir found
            	if(str.length == 0) {
            		ret = ret + keyVal.toString();
            	}
            	else {
	            	for(int i = 0; i < str.length; i++) {
	            		ret = ret + str[i]+";";
	            	}
            	}
    		}
    		return ret;
	}
    
    @Override
    public String getBaseDir() {
    	return this.project.getBaseDir().toString();
    }
}
