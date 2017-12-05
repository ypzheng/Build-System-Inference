import java.io.File;
import java.lang.reflect.Array;
import java.util.*;

import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.Task;
import util.ClassPathParser;
import util.PathParser;
import util.Debugger;
import util.TaskHelper;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AntBuildAnalyzer implements BuildAnalyzer{
	private Vector sortedTargets;
	private ArrayList<Target> potentialSrcTargets, potentialTestTargets;
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
	    this.getPotentialCompileTargets();
	    
	    //Path Parser
	    pp = new PathParser(project);
	    
	    //DirectoryHelper
	    taskHelper = new TaskHelper(pp);
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
			if(containsJavac(t.getTasks())) {
				if(t.getName().contains("test")) {
					potentialTestTargets.add(t);
					Debugger.log("test: "+t.getName());
				}
				else{
					potentialSrcTargets.add(t);
					Debugger.log("src: "+t.getName());
				}
			}
		}
		getCompileSrcTarget();
		getCompileTestTarget();
	}
	
	/**
	 * Helpter method that checks if a task contains javac.
	 * @param tasks
	 * @return
	 */
	private boolean containsJavac(Task[] tasks) {
		for(Task t : tasks) {
			if(t.getTaskType().equals("javac")) {
				return true;
			}
		}
		return false;
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
		int size = potentialSrcTargets.size();
		if(size == 0) {
			Debugger.log("Cannot find target that compiles source");
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
	 * Similar to the method above.
	 * TODO: Need to consider the case when the compile-test target doesn't contain
	 * the key word "test".  For example, TestBuildFile1 compiles test and source 
	 * together in the target "compile".s
	 * @return
	 */
	public String getCompileTestTarget() {
		int size = potentialTestTargets.size();
		if(size ==0 && this.getCompileSrcTarget()!=null) {
			compileTestTarget = this.compileSrcTarget;
		}
		else if(size == 1) {
			compileTestTarget = potentialTestTargets.get(0);
		}
		else if(size > 1) {
			compileTestTarget = potentialTestTargets.get(size - 1);
		}

		Debugger.log("test target: "+compileTestTarget.getName());

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
		String[] paths = findClassPath(tasks);
        if (paths != null) {
            return convert2String(getDependFile(paths));
        } else {
            System.out.println("Can't find class path in Javac abort");
        }
        return null;
	}

	@Override
	public String getTestDep() {
		// TODO Auto-generated method stub
        Task[] tasks = compileTestTarget.getTasks();
        String[] paths = findClassPath(tasks);
        if (paths != null) {
            return convert2String(getDependFile(paths));
        } else {
            System.out.println("Can't find class path in Javac abort");
        }
        return null;
	}

    private String[] findClassPath(Task[] tasks) {
        for (Task tsk : tasks) {
            if (tsk.getTaskType().equals("javac")) {
                String[] paths = classPathParser.parseClassPath(tsk.getRuntimeConfigurableWrapper());
                return paths;
            }
        }
        return null;
    }

    private String convert2String(ArrayList<String> al) {
	    String temp = "";
	    for (String w: al) {
	        temp += w+", ";
        }
        return temp;
    }

    @Override
	public String getTestList() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private String[] resolvedWildCardFiles(String[] includes, String[] excludes, String baseDir) {
		DirectoryScanner ds = new DirectoryScanner();
		ds.setIncludes(includes);
		ds.setExcludes(excludes);
		ds.setBasedir(baseDir);
		ds.setCaseSensitive(true);
		ds.scan();
		return ds.getIncludedFiles();
	}

    /*
    Perform a DFS recursively find all the file in the given root folder
 */
    private ArrayList<String> getDependFile(String[] paths) {
        ArrayList<String> files = new ArrayList<>();
        String[] excludes = new String[]{""};
        for (String path:paths) {
            System.out.println(path);
            String[] wildcard = new String[]{isWildCard(path)};
            if (!wildcard[0].equals("")) {
                System.out.println("It's a wild card");
                String baseDir = path.replace(wildcard[0],"");
                String[] resolvedFiles = resolvedWildCardFiles(wildcard,excludes,baseDir);
                files.addAll(Arrays.asList(resolvedFiles));
            } else {
                System.out.println("It's not a wild card");
                File potentialFile = new File(path);
                if (potentialFile.isFile()) {
                    files.add(potentialFile.getName());
                } else {
                    String [] includes = new String[]{"*.class"};
                    String [] resolvedFiles = resolvedWildCardFiles(includes,excludes,path);
                    for (String f:resolvedFiles) {
                        System.out.println(f);
                    }
                    files.addAll(Arrays.asList(resolvedFiles));
                }
            }
        }
        return files;
    }

    private String isWildCard(String path) {
        String regex = "\\*\\w*\\.\\w*";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(path);

        if (matcher.find()) {
            return matcher.group();
        }else {
            return "";
        }

    }
}
