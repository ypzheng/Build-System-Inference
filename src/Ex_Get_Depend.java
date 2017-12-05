import java.io.File;
import java.util.*;
import java.util.Hashtable;

import org.apache.tools.ant.*;
import org.apache.tools.ant.taskdefs.Javac;
import util.AttributeParser;
import util.PropertyParser;

public class Ex_Get_Depend {

    public static void printMap(Map<String, String> map) {
        Set<String> keys = map.keySet();
        for (String key:keys) {
            System.out.println("Key: " + key +" Value: " +map.get(key));
        }
        System.out.println("==========");
    }

    public static void printPath(String[] paths) {
        for (String path: paths) {
            System.out.println(path);
            Stack<File> folders = new Stack<File>();
            folders.add(new File(path));
            while (!folders.isEmpty()) {
                File folder = folders.pop();
                File[] listOfFiles = folder.listFiles();
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
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Project project = new Project();
        ProjectHelper helper = new ProjectHelper();
        project.init();
        File buildFile = project.resolveFile("test/TestBuildFile3.xml");
        helper.configureProject(project, buildFile);
        AttributeParser attributeParser = new AttributeParser(project);
        Hashtable<String, Target> target_table = project.getTargets();
        Vector<Target> sorted_target = project.topoSort("all",target_table);
        Enumeration<String > keys = target_table.keys();

        for (Target target:sorted_target) {
            Task[] tasks = target.getTasks();
//            target.execute();
            if (target.getName().equals("compile")) {
                for (Task tsk : tasks) {
                    System.out.println(tsk.getTaskType());
                    if (tsk.getTaskType().equals("javac")) {
                        String[] paths = attributeParser.parseClassPath(tsk.getRuntimeConfigurableWrapper());
//                        printPath(paths);
                    }
                }
                break;
            }
        }
    }

}
