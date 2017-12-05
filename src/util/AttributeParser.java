package util;
import org.apache.tools.ant.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.tools.ant.types.Path;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

public class AttributeParser {

    private PropertyParser propertyParser;
    private Project myProject;
    private final String regex = "\\$\\{[^\\$]*\\}";
    private final String regex2 = "[$\\{}]";
    private final Pattern pattern = Pattern.compile(regex);
    public AttributeParser(Project project) {
        myProject = project;
        propertyParser = new PropertyParser(project);
    }

    /*
        Given a task return a dictionary of attribute:attribute value
        eg. input  <jar jarfile="${build.home}/${final.name}-sources.jar">
        return {jarfile:target/commons-lang-2.5-sources.jar}
     */

    public Map<String, String> getAttributeValues(RuntimeConfigurable runtimeConfigurable) {
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

    public String[] parseClassPath(RuntimeConfigurable cfg) {
        Enumeration<RuntimeConfigurable> children = cfg.getChildren();
        while (children.hasMoreElements()) {
            RuntimeConfigurable child = children.nextElement();
            if (child.getElementTag().equals("classpath")) {
                if (!child.getAttributeMap().contains("refid")) {
                    ArrayList<String> paths = classPathHelper(child);
                    return arrayList2Array(paths);
                } else {
                    String ref = child.getAttributeMap().get("refid").toString();
                    Path path =  myProject.getReference(ref);
                    return path.list();
                }
            }
        }
        return null;
    }

    private ArrayList<String> classPathHelper(RuntimeConfigurable cfg) {
        String baseDir = myProject.getBaseDir().getAbsolutePath();
        ArrayList<String> paths = new ArrayList<>();
        Enumeration<RuntimeConfigurable> classpaths = cfg.getChildren();
        while (classpaths.hasMoreElements()) {
            RuntimeConfigurable child = classpaths.nextElement();
            if (child.getElementTag().equals("pathelement")) {
                String path = baseDir +"/"+getAttributeValues(child).get("location");
                paths.add(path);
            }
        }
        return paths;
    }

    private String[] arrayList2Array(ArrayList<String> al) {
        String[] array = new String[al.size()];
        for (int i = 0; i < al.size(); i ++) {
            array[i] = al.get(i);
        }
        return array;
    }

}
