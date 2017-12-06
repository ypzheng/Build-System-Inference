import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.RuntimeConfigurable;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.util.FileUtils;
import util.ClassPathParser;
import util.PathParser;
import util.Debugger;
import util.TaskHelper;
import util.Utility;

public class AntBuildAnalyzer implements BuildAnalyzer{
	private Vector sortedTargets;
	private ArrayList<Target> potentialSrcTargets, potentialTestTargets, junitTargets;
	private Target compileSrcTarget, compileTestTarget;

	private PathParser pp;
	private TaskHelper taskHelper;
    private ClassPathParser classPathParser;

    public AntBuildAnalyzer(File f) {
		//Initialize Variables
		compileSrcTarget = null;
		compileTestTarget = null;
		potentialSrcTargets = new ArrayList<Target>();
		potentialTestTargets = new ArrayList<Target>();
		junitTargets = new ArrayList<Target>();

		//Enable Console printing
		//Debugger.enable();

		//Load in build.xml file
		Project project = new Project();
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
			else if(taskHelper.containsTask(t.getTasks(), "junit")) {
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
                    Utility.printPath(paths);
                } else {
                    System.out.println("Can't find class path in Javac abort");
                }
            }
        }
    }

    @Override
	public String getTestList() {
		// TODO Auto-generated method stub
		DirectoryScanner ds = new DirectoryScanner();
		String[] list = getTests(ds.getIncludedFiles(),ds.getExcludedFiles(), ds.getBasedir().getName());
		StringBuilder testList = new StringBuilder();
		for(String tests : list) testList.append(tests);
		return new String(testList);
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
