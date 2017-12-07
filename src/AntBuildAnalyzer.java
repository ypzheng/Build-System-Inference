import java.io.File;
import java.io.FilenameFilter;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.Node;
import javax.annotation.Resource;
import javax.lang.model.element.Element;
import javax.xml.parsers.DocumentBuilder;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.RuntimeConfigurable;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.resources.FileResource;

import java.util.Iterator;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.types.FileSet;
import util.ClassPathParser;
import util.PathParser;
import util.Debugger;
import util.TaskHelper;
import util.FileUtility;

public class AntBuildAnalyzer implements BuildAnalyzer{
	private Vector sortedTargets;
	private ArrayList<Target> potentialSrcTargets, potentialTestTargets, junitTargets;
	private Target compileSrcTarget, compileTestTarget;
	private String projectName;

	private PathParser pp;
	private TaskHelper taskHelper;
    private ClassPathParser classPathParser;
    private Project project;

    public AntBuildAnalyzer(File f, String projectName) {
		//Initialize Variables
		compileSrcTarget = null;
		compileTestTarget = null;
		potentialSrcTargets = new ArrayList<Target>();
		potentialTestTargets = new ArrayList<Target>();
		junitTargets = new ArrayList<Target>();
		this.projectName = projectName;

		//Enable Console printing
		//Debugger.enable();

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
	 * target.
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

	private void enhanceSrcTargetFinding() {
		if(potentialSrcTargets.size() == 0) {
			Debugger.log("Cannot find target that compiles source.");
		}
		else{
			for(Target t : potentialSrcTargets) {
				if(t.getName().contains("compile") && potentialSrcTargets.size() == 1)
					this.compileSrcTarget = t;
				else if(t.getName().contains("compile") && potentialSrcTargets.size() > 1) {
					Debugger.log("Special case, may require manual inference.");
					if(t.getName().equals("compile"))
						this.compileSrcTarget = t;
				}
			}
			if(this.compileSrcTarget == null)
				this.compileSrcTarget = potentialSrcTargets.get(potentialSrcTargets.size()-1);
		}
	}
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
	 * Get compile-source target from the potential targets array.
	 * TODO: If the array is empty, we should check if the buid file is valid.
	 * If the array contains exactly one element, it is the target we are looking for.
	 * If the array contains more than one element, we can find compile-source target
	 * at the end of the list, since the list is sorted in execution order.
	 * @return
	 */
	public String getCompileSrcTarget() {
		return compileSrcTarget.getName();
	}

	/**
	 * Similar to the method above.
	 * TODO: Need to consider the case when the compile-test target doesn't contain
	 * the key word "test".  For example, TestBuildFile1 compiles test and source
	 * together in the target "compile".s
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
		// TODO Auto-generated method stub
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

	@Override
	public String getCompTestDir() {
		// TODO Auto-generated method stub
		return taskHelper.getDirectory("javac", "destdir", compileTestTarget);
	}

	@Override
	public String getSrcDep() {
		// TODO Auto-generated method stub
		Task[] tasks = compileSrcTarget.getTasks();
        findClassPath(tasks);
        return null;
	}

	@Override
	public String getTestDep() {
		// TODO Auto-generated method stub
        Task[] tasks = compileTestTarget.getTasks();
        findClassPath(tasks);
        return null;
	}

    private void findClassPath(Task[] tasks) {
        for (Task tsk : tasks) {
            if (tsk.getTaskType().equals("javac")) {
                String[] paths = classPathParser.parseClassPath(tsk.getRuntimeConfigurableWrapper());
                if (paths != null) {
                    FileUtility.printPath(paths);
                } else {
                    System.out.println("Can't find class path in Javac abort");
                }
            }
        }
    }

    @Override
	public String getTestList() {
		// TODO Auto-generated method stub
    		Map<String, String> keyVal = new HashMap<String, String>();
    		if(junitTargets.size() == 0) {
    			Debugger.log("No junit tasks, make sure this project contains unit tests.");
    			return "";
    		}
    		List<Task> tasks = taskHelper.getTasks("junit", junitTargets.get(junitTargets.size()-1));
    		for(int i=0; i<tasks.size(); i++) {
    			RuntimeConfigurable rt = tasks.get(i).getRuntimeConfigurableWrapper();
    			Enumeration<RuntimeConfigurable> enumeration= rt.getChildren();
    			while(enumeration.hasMoreElements()) {
    				RuntimeConfigurable temp = enumeration.nextElement();
    				if(temp.getElementTag().equals("batchtest")) {
    					keyVal = batchtestHelper(temp);
    				}
        		}
    		}

    		//TODO: Given a map of key values, for example {includes=**/*Test.class, dir=target/test-classes},
    		// 		use DirectoryScanner or Andy/Jucong's method to find test set.
    		//TODO: I REALIZED WE CAN JUST RUN THE COMPILE.TEST TARGET AND GET ALL TESTS FROM THE DIRECTORY. HMMMMMMMMMM.....
    		System.out.println(keyVal);
    		
    		String[] includes = keyVal.get("include").split(";");
    		String[] excludes = keyVal.get("exclude").split(";");
    		String[] str = this.getTests(includes, excludes, project.getBaseDir().getParent().toString()+"/"+projectName+"/"+keyVal.get("dir"));
    		for(int i = 0; i < str.length; i++) {
    			System.out.println(str[i]);
    		}
    		return "";
	}

    //TODO: If no batchtest found, we should return all available tests under specified test directory
    //TODO: If "test" instead of "batchtest" found, parse the path and list all tests
    //TODO: If neither "test" nor "batchtest" found, list all available tests under test.dir
    //TODO:
    private Map<String, String> batchtestHelper(RuntimeConfigurable rt) {
    		Enumeration<RuntimeConfigurable> filesets = rt.getChildren();
    		Map<String, String> ret = new HashMap<>();
			while(filesets.hasMoreElements()) {
				RuntimeConfigurable fileset = filesets.nextElement();
				Hashtable att_map_fs = ((RuntimeConfigurable) fileset).getAttributeMap();
				if(att_map_fs.containsKey("dir")) {
					ret.put("dir", pp.parse((String) att_map_fs.get("dir")));
				}
				if(att_map_fs.containsKey("includes")) {
					ret.put("includes", (String) att_map_fs.get("includes"));
				}
				if(att_map_fs.containsKey("excludes")) {
					ret.put("excludes", (String) att_map_fs.get("excludes"));
				}

				Enumeration<RuntimeConfigurable> fileNamePattern = fileset.getChildren();
				String include = "";
				String exclude = "";
				while(fileNamePattern.hasMoreElements()) {

					RuntimeConfigurable temp = fileNamePattern.nextElement();
					if(temp.getElementTag() == "include") {
						include = include+pp.parse((String)temp.getAttributeMap().get("name"))+"; ";
					}
					if(temp.getElementTag() == "exclude") {
						exclude = exclude+pp.parse((String)temp.getAttributeMap().get("name"))+"; ";
					}
					ret.put("include", include);
					ret.put("exclude", exclude);
				}
			}
		return ret;
    }

	private String[] getTests(String[] includes, String[] excludes, String baseDir) {
		DirectoryScanner ds = new DirectoryScanner();
		ds.setIncludes(includes);
		ds.setExcludes(excludes);
		ds.setBasedir(baseDir);
		ds.setCaseSensitive(true);
		ds.scan();
		return ds.getIncludedFiles();
	}




}
