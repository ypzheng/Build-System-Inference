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

public class AntBuildAnalyzer implements BuildAnalyzer{
	private Vector sortedTargets;
	private ArrayList<Target> potentialSrcTargets, potentialTestTargets;
	private Target compileSrcTarget, compileTestTarget;
	
	private PathParser pp;
	private TaskHelper taskHelper;
    private ClassPathParser classPathParser;
    private Project project;

    public AntBuildAnalyzer(File f) {
		//Initialize Variables
		compileSrcTarget = null;
		compileTestTarget = null;
		potentialSrcTargets = new ArrayList<Target>();
		potentialTestTargets = new ArrayList<Target>();

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
                    printPath(paths);
                } else {
                    System.out.println("Can't find class path in Javac abort");
                }
            }
        }
    }

    @Override
	public String getTestList() {
		// TODO Auto-generated method stub
    	Hashtable<String,Object>hash = project.getReferences();
//    	System.out.println(hash.get("test.classpath"));
    	
        FileSet fs = new FileSet();
    	fs.setDir(new File(project.getProperty("test.home")));
    	DirectoryScanner ds = fs.getDirectoryScanner(project);
    	ds.setBasedir(new File(project.getProperty("test.home")));
		ds.scan();
		String[] list = getTests(ds.getIncludedFiles(),ds.getExcludedFiles(), ds.getBasedir().getName());
		
		for(String tests : list) System.out.println(tests);
//		StringBuilder testList = new StringBuilder();
//		for(String tests : list) testList.append(tests + ", ");
//		return new String(testList);
		
    	
    	
		return null;
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
	

    /*
    Perform a DFS recursively find all the file in the given root folder
 */
    private void printPath(String[] paths) {
        for (String path: paths) {
            Stack<File> folders = new Stack<>();
            folders.add(new File(path));
            while (!folders.isEmpty()) {
                File folder = folders.pop();
                if (folder.isFile()) {
                    System.out.println(folder.getName());
                } else {
                    File[] listOfFiles = folder.listFiles();
                    if (listOfFiles != null){
                        for (int i = 0; i < listOfFiles.length; i++) {
                            if (listOfFiles[i].isFile()) {
                                String fileName = listOfFiles[i].getName();
                                if (fileName.endsWith(".class")) {
                                    System.out.println("File " + listOfFiles[i].getName());
                                }
                            } else if (listOfFiles[i].isDirectory()) {
                                String absPath = listOfFiles[i].getAbsolutePath();
                                File newFolder = new File(absPath);
                                folders.add(newFolder);
                            }
                        }
                    }
                }

            }
        }
    }
}
