package util;

import org.apache.tools.ant.Project;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class PropertyParser {
    private Project myProject;
    private Map<String,String> property_map;
    public PropertyParser(Project p) {
        myProject = p;
        property_map = convert2String(myProject.getProperties());
    }

    public String get_property(String name) {
        if (property_map.containsKey(name)) {
            return property_map.get(name);
        } else {
            return null;
        }
    }

    private Map<String,String> convert2String(Hashtable<String,Object> property_table) {
        Enumeration<String> keys = property_table.keys();
        Map<String,String> tempMap = new HashMap<>();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            tempMap.put(key,property_table.get(key).toString());
        }
        return tempMap;
    }
}
