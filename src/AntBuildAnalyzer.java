import java.io.File;
import java.io.FilenameFilter;

import java.nio.file.Paths;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.Node;
import javax.annotation.Resource;
import javax.lang.model.element.Element;
import javax.xml.parsers.DocumentBuilder;
import java.util.*;

import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Path;

import util.ClassPathParser;
import util.PathParser;
import util.Debugger;
import util.FileUtility;
import util.TaskHelper;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.tools.ant.RuntimeConfigurable;


public class AntBuildAnalyzer implements BuildAnalyzer{
	private Vector sortedTargets;
	private ArrayList<Target> potentialSrcTargets, potentialTestTargets, junitTargets;
	private Target compileSrcTarget, compileTestTarget;
	private String projectPath;

	private PathParser pp;
	private TaskHelper taskHelper;
    private ClassPathParser classPathParser;
    private Project project;

    public AntBuildAnalyzer(File f, String projectPath) {
		//Initialize Variables
		compileSrcTarget = null;
		compileTestTarget = null;
		potentialSrcTargets = new ArrayList<Target>();
		potentialTestTargets = new ArrayList<Target>();
		junitTargets = new ArrayList<Target>();
		this.projectPath = projectPath;

		//Enable Console printing
//		Debugger.enable();

		//Load in build.xml file
		project = new Project();
		project.init();
		ProjectHelper helper = new ProjectHelper();
		helper.configureProject(project, f);
		sortedTargets = project.topoSort(project.getDefaultTarget(), project.getTargets());

		//Print out all targets in execution order
		Enumeration vEnum = sortedTargets.elements();

		Debugger.log("Targets sorted in order of execution:");

	    while(vEnum.hasMoreElements())
	    		Debugger.log(vEnum.nextElement() + "\n");

	    //Path Parser
	    pp = new PathParser(project);

	    //taskHelper
	    taskHelper = new TaskHelper(pp);
	    this.getPotentialCompileTargets();
	    classPathParser = new ClassPathParser(project);
    }

	/**
	 * If a target contains javac: check if the target name contains "test",
	 * add it to the list that contains potential compile-test targets; if it does
	 * not contain "test", add it to the list that contains potential compile-source
	 * target.  This reduces returning false positives of special naming on targets.
	 *
	 */
	private void getPotentialCompileTargets() {
		Enumeration vEnum = sortedTargets.elements();
		while(vEnum.hasMoreElements()) {
			Target t = (Target) vEnum.nextElement();
			if(taskHelper.containsTask(t.getTasks(),"javac")) {
				if(t.getName().contains("test")) {
					potentialTestTargets.add(t);
					Debugger.log("test: "+t.getName());
				}
				else{
					potentialSrcTargets.add(t);
					Debugger.log("src: "+t.getName());
				}
			}
			if(taskHelper.containsTask(t.getTasks(), "junit")) {
				junitTargets.add(t);
			}
		}
		enhanceSrcTargetFinding();
		enhanceTestTargetFinding();
	}

	/**
	 * Helper method to find compile target
	 */
	private void enhanceSrcTargetFinding() {
		Map<String, Target> targets = new HashMap<String, Target>();
		if(potentialSrcTargets.size() == 0) {
			Debugger.log("Cannot find target that compiles source.");
		}
		else{
			for(Target t : potentialSrcTargets) {
				if(potentialSrcTargets.size() == 1) {
					this.compileSrcTarget = t;
				}
				else if(t.getName().contains("compile") && potentialSrcTargets.size() > 1) {
					System.out.println("Special case, there might be a top-level compile target.  Requires manual inference.");
					if(t.getName().equals("compile") || t.getName().contains("all"))
						this.compileSrcTarget = t;
				}
			}
			if(this.compileSrcTarget == null)
				this.compileSrcTarget = potentialSrcTargets.get(potentialSrcTargets.size()-1);
		}
	}
	
	/**
	 * Helper method to find test target
	 */
	private void enhanceTestTargetFinding() {
		// if no target name that contains "test" and there is exactly one compile source target,
		// check if the compile source target contains multiple javac.  If true, test.compile = src.compile
		if(potentialTestTargets.size() == 0 && potentialSrcTargets.size() == 1) {
			Target target = potentialSrcTargets.get(0);
			if(taskHelper.getTasks("javac", target).size()>1) {
				this.compileTestTarget = target;
			}
		}
		else{
			this.compileTestTarget = potentialTestTargets.get(potentialTestTargets.size()-1);
		}

	}


	/**
	 * Get compile-source target
	 * @return
	 */
	public String getCompileSrcTarget() {
		return compileSrcTarget.getName();
	}

	/**
	 * Get compile-test target
	 * @return
	 */
	public String getCompileTestTarget() {
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
		return taskHelper.getDirectory("javac", "srcdir", compileTestTarget);
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
		return taskHelper.getDirectory("javac", "srcdir", compileSrcTarget);
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
			System.out.println("Cannot find the exact dependency files\n "
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
			System.out.println("Cannot find the exact dependency files\n "
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
				deps += pp.parse(filtered_dep) +",";
				
			}
		}
		
		// TODO Handle fileset tasks
		
		//Formating output string, remove last ","
		if(deps.length()>0 && deps.substring(deps.length()-1).equals(","))
			deps = deps.substring(0, deps.length()-1);
		return deps;
	}


    @Override
	public String getTestList() {
    		Map<String, String> keyVal = new HashMap<String, String>();
    		String ret = "";
    		if(junitTargets.size() == 0) {
    			System.out.println("No junit tasks, make sure this project contains unit tests.");
    			return "";
    		}
    		for(int i=0; i< junitTargets.size(); i++) {
    			List<Task> tasks = taskHelper.getTasks("junit", junitTargets.get(i));
	    		for(int j=0; j<tasks.size(); j++) {
	    			RuntimeConfigurable rt = tasks.get(j).getRuntimeConfigurableWrapper();
	    			Enumeration<RuntimeConfigurable> enumeration= rt.getChildren();
	    			while(enumeration.hasMoreElements()) {
	    				RuntimeConfigurable temp = enumeration.nextElement();
	    				if(temp.getElementTag().equals("batchtest")) {
	    					keyVal.putAll(batchtestHelper(temp));
	    				}
	    				if(temp.getElementTag().equals("classpath")) {
	    					
//	    					System.out.println("cp: "+this.junitTargets.get(j).getName()+ " pathelement exists");
	    				}
	    				if(temp.getElementTag().equals("test") && temp.getAttributeMap().containsKey("name")) {
	    					keyVal.replace("include", keyVal.get("include")+", "+temp.getAttributeMap().get("name"));
//	    					System.out.println("zzz: "+temp.getAttributeMap().get("name"));
	    				}
	        		}
	    		}
    		}

    		
//    		System.out.println("keyval: "+keyVal);
    		
    		if(keyVal.size() != 0) {
    			String[] includes = keyVal.get("include").split(";");
            	String[] excludes = keyVal.get("exclude").split(";");
            	String[] str = WildCardResolver.resolveWildCard(includes, excludes, projectPath+Paths.get("/")+keyVal.get("dir"));
//            	System.out.println(projectPath+Paths.get("/")+keyVal.get("dir"));
            	//no test dir found
            	if(str.length == 0) {
            		ret = ret + keyVal.toString();
            	}
            	else {
	            	for(int i = 0; i < str.length; i++) {
	            		ret = ret + str[i];
	            	}
            	}
            	
    		}
//    		System.out.println("ret: "+ret);
    		return ret;
	}

    //TODO: If no batchtest found, we should return all available tests under specified test directory
    //TODO: If neither "test" nor "batchtest" found, list all available tests under test.dir
    //TODO:
    private Map<String, String> batchtestHelper(RuntimeConfigurable rt) {
    		Enumeration<RuntimeConfigurable> filesets = rt.getChildren();
    		Map<String, String> ret = new HashMap<>();

    		ret.put("include", "");
    		ret.put("exclude", "");
    		ret.put("dir", "");
			while(filesets.hasMoreElements()) {
				RuntimeConfigurable fileset = filesets.nextElement();
				Hashtable att_map_fs = ((RuntimeConfigurable) fileset).getAttributeMap();
				if(att_map_fs.containsKey("dir")) {
					ret.put("dir", pp.parse((String) att_map_fs.get("dir")));
				}
				if(att_map_fs.containsKey("includes")) {
					ret.put("include", (String) att_map_fs.get("includes"));
				}
				if(att_map_fs.containsKey("excludes")) {
					ret.put("exclude", (String) att_map_fs.get("excludes"));
				}

				Enumeration<RuntimeConfigurable> fileNamePattern = fileset.getChildren();
				String include = "";
				String exclude = "";
				while(fileNamePattern.hasMoreElements()) {

					RuntimeConfigurable temp = fileNamePattern.nextElement();
					if(temp.getElementTag().equals("include")){
						include = include+pp.parse((String)temp.getAttributeMap().get("name"))+";";
					}
					if(temp.getElementTag().equals("exclude")){
						exclude = exclude+pp.parse((String)temp.getAttributeMap().get("name"))+";";
					}
					ret.replace("include", include);
					ret.replace("exclude", exclude);
				}
			}
		return ret;
    }

}
