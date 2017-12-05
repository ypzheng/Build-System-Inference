package util;
import org.apache.tools.ant.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.tools.ant.types.Path;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

public class ClassPathParser {

    private PropertyParser propertyParser;
    private Project myProject;
    private final String regex = "\\$\\{[^\\$]*\\}";
    private final String regex2 = "[$\\{}]";
    private final Pattern pattern = Pattern.compile(regex);
    public ClassPathParser(Project project) {
        myProject = project;
        propertyParser = new PropertyParser(project);
    }

    /*
        Given a task return a dictionary of attribute:attribute value
        eg. input  <jar jarfile="${build.home}/${final.name}-sources.jar">
        return {jarfile:target/commons-lang-2.5-sources.jar}
     */

    private Map<String, String> getAttributeValues(RuntimeConfigurable runtimeConfigurable) {
        Hashtable<String, Object> cfg_table = runtimeConfigurable.getAttributeMap();
        Enumeration<String> cfgKeys = cfg_table.keys();
        Map<String,String> attrMap = new HashMap<>();
        while (cfgKeys.hasMoreElements()) {
            String cfgKey = cfgKeys.nextElement();
            String parsedValue = parseAttributeValue(cfg_table.get(cfgKey).toString());
            attrMap.put(cfgKey,parsedValue);
        }
        return attrMap;
    }

    /*
        Replace ${stuff} in the given string with value in the property
        eg. input ${build.home}/${final.name}-sources.jar
        return target/commons-lang-2.5-sources.jar

     */
    private String parseAttributeValue(String value) {
        String new_value = value;
        Map<String,String> valueMap = new HashMap<>();
        // Matcher for ${stuff}
        Matcher matcher = pattern.matcher(value);
        // Extract ${} from the value
        while (matcher.find()) {
            String matchResult = matcher.group();
            String property_value = propertyParser.get_property(matchResult.replaceAll(regex2,""));
            if (property_value != null) {
                new_value = new_value.replace(matchResult,property_value);
            }
        }
        return new_value;
    }

    /*
        Given a Javac RuntimeConfigurable file, return array of absolute path of then classpath that Javac needs
        If no classpath found, return null
        eg. input
        <javac srcdir="${source.home}" destdir="${build.home}/classes" debug="${compile.debug}" deprecation="${compile.deprecation}" target="${compile.target}" source="${compile.source}" excludes="${compile.excludes}" optimize="${compile.optimize}">
            <classpath refid="compile.classpath"/>
        </javac>

        return [abspath(compile.classpath)]
     */
    public String[] parseClassPath(RuntimeConfigurable cfg) {
        Enumeration<RuntimeConfigurable> children = cfg.getChildren();
        while (children.hasMoreElements()) {
            RuntimeConfigurable child = children.nextElement();
            if (child.getElementTag().equals("classpath")) {
                Set<String> keys = child.getAttributeMap().keySet();
                if (!keys.contains("refid")){
                    // handle the situation when classpath is not using reference
                    ArrayList<String> paths = classPathHelper(child);
                    return arrayList2Array(paths);
                } else {
                    System.out.println("Found classpath");
                    String ref = child.getAttributeMap().get("refid").toString();
                    Path path =  myProject.getReference(ref);
                    return path.list();
                }
            }
        }
        return null;
    }

    /*
        similar to the parseClassPath. But it looks for pathelement instead of the reference path.
     */
    private ArrayList<String> classPathHelper(RuntimeConfigurable cfg) {
        String baseDir = myProject.getBaseDir().getAbsolutePath();
        ArrayList<String> paths = new ArrayList<>();
        // Pathelement is child of Classpath
        Enumeration<RuntimeConfigurable> classpaths = cfg.getChildren();
        // Loop through all the path element and convert the path value to absolute path
        while (classpaths.hasMoreElements()) {
            RuntimeConfigurable child = classpaths.nextElement();
            if (child.getElementTag().equals("pathelement")) {
                String path = baseDir +"/"+getAttributeValues(child).get("location");
                paths.add(path);
            }
        }
        return paths;
    }

    /*
        Helper method that convert ArrayList<String> to String[]
     */
    private String[] arrayList2Array(ArrayList<String> al) {
        String[] array = new String[al.size()];
        for (int i = 0; i < al.size(); i ++) {
            array[i] = al.get(i);
        }
        return array;
    }

}
