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
				if(t.getName().contains("compile") && potentialSrcTargets.size() == 1) {
					this.compileSrcTarget = t;
				}
				else if(t.getName().contains("compile") && potentialSrcTargets.size() > 1) {
					System.out.println("Special case, may require manual inference.");
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
	
		//Output String
		String deps = "";
		
		//Get all tasks that contains javac task under compileSrcTarget
		List<Task> javac_tasks = taskHelper.getTasks("javac",this.compileSrcTarget);
		
		//Get all classpath tasks' refid values in an array of String
		String[] classpath_refid_list = taskHelper.getSubTaskAttr(javac_tasks.toArray(new Task[javac_tasks.size()]), "classpath", "refid");
		
		
		for(String s : classpath_refid_list) {
			Path p = this.project.getReference(s);
			
			//Since we are only insterested in .jar files, filter them out and append to output string
			String[] filtered_deps = FileUtility.filterPath(p.list(), true,"(.*)[.jar]");
			
			for(String filtered_dep : filtered_deps) {
				deps += pp.parse(filtered_dep) +",";
			}
		}
		
		// TODO Handle fileset tasks
		
		//Formating output string, remove last ","
		if(deps.substring(deps.length()-1).equals(","))
			deps = deps.substring(0, deps.length()-1);
		
		return deps;
	}

	@Override
	public String getTestDep() {
               
        String deps = "";
        
        //Get all tasks that contains javac task under compileSrcTarget
		List<Task> javac_tasks = taskHelper.getTasks("javac",this.compileTestTarget);
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
		if(deps.substring(deps.length()-1).equals(","))
			deps = deps.substring(0, deps.length()-1);
		return deps;
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
    
    private Target getTopLevelTestTarget(List<Target> targets) {
    		for(Target t: targets) {
    			if(t.dependsOn(this.compileTestTarget.getName())) {
    				return t;
    			}
    		}
    		return null;
    }

    @Override
	public String getTestList() {
		// TODO Auto-generated method stub
    		Map<String, String> keyVal = new HashMap<String, String>();
    		if(junitTargets.size() == 0) {
    			Debugger.log("No junit tasks, make sure this project contains unit tests.");
    			return "";
    		}
    		
    		List<Task> tasks = taskHelper.getTasks("junit", this.getTopLevelTestTarget(junitTargets));
    		for(int i=0; i<tasks.size(); i++) {
    			RuntimeConfigurable rt = tasks.get(i).getRuntimeConfigurableWrapper();
    			Enumeration<RuntimeConfigurable> enumeration= rt.getChildren();
    			while(enumeration.hasMoreElements()) {
    				RuntimeConfigurable temp = enumeration.nextElement();
    				if(temp.getElementTag().equals("batchtest")) {
    					keyVal = batchtestHelper(temp);
    				}
    				if(temp.getElementTag().equals("classpath")) {
    					System.out.println(this.compileTestTarget.getName()+ " pathelement exists");
    				}
        		}
    		}

    		//TODO: Given a map of key values, for example {includes=**/*Test.class, dir=target/test-classes},
    		// 		use DirectoryScanner or Andy/Jucong's method to find test set.
    		//TODO: I REALIZED WE CAN JUST RUN THE COMPILE.TEST TARGET AND GET ALL TESTS FROM THE DIRECTORY. HMMMMMMMMMM.....
    		System.out.println("keyval: "+keyVal);
    		
    		if(keyVal.size() != 0) {
    			String[] includes = keyVal.get("include").split(";");
            	String[] excludes = keyVal.get("exclude").split(";");
            	String[] str = this.resolvedWildCardFiles(includes, excludes, project.getBaseDir().getParent().toString()+Paths.get("/")+projectName+Paths.get("/")+keyVal.get("dir"));
            	for(int i = 0; i < str.length; i++) {
            		System.out.println(str[i]);
            	}
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
						include = include+pp.parse((String)temp.getAttributeMap().get("name"))+"; ";
					}
					if(temp.getElementTag().equals("exclude")){
						exclude = exclude+pp.parse((String)temp.getAttributeMap().get("name"))+"; ";
					}
					ret.replace("include", include);
					ret.replace("exclude", exclude);
				}
			}
		return ret;
    }

	private String[] resolvedWildCardFiles(String[] includes, String[] excludes, String baseDir) {
		DirectoryScanner ds = new DirectoryScanner();
		ds.setIncludes(includes);
		ds.setExcludes(excludes);
		ds.setBasedir(baseDir);
		ds.setCaseSensitive(true);
		try {
			ds.scan();
		}catch(IllegalStateException e){
			Debugger.log("Illegal State Exception found, basedir does not exist");
			System.out.println("Directory that contains tests cannot be found. \nPlease try to compile the project: "+projectName+" first in order to get a test list");
		}
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
