import java.io.File;
import java.util.*;
import java.util.Hashtable;

import org.apache.tools.ant.*;
import util.ClassPathParser;

public class Ex_Get_Depend {

    public static void printMap(Map<String, String> map) {
        Set<String> keys = map.keySet();
        for (String key:keys) {
            System.out.println("Key: " + key +" Value: " +map.get(key));
        }
        System.out.println("==========");
    }

    private static void printPath(String[] paths) {
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
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Project project = new Project();
        ProjectHelper helper = new ProjectHelper();
        project.init();
        File buildFile = project.resolveFile("/Users/Jucong/Downloads/commons-lang-LANG_2_5/build.xml");
        helper.configureProject(project, buildFile);
        ClassPathParser classPathParser = new ClassPathParser(project);
        Hashtable<String, Target> target_table = project.getTargets();
        Vector<Target> sorted_target = project.topoSort("init",target_table);
        Enumeration<String > keys = target_table.keys();

        for (Target target:sorted_target) {
            Task[] tasks = target.getTasks();
            target.execute();
            if (target.getName().equals("compile")) {
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
        }
    }

}
