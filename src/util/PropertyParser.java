package util;

import org.apache.tools.ant.Project;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class PropertyParser {
    private Project myProject;
    private Hashtable<String, Object> property_map;
    public PropertyParser(Project p) {
        myProject = p;
        property_map = myProject.getProperties();
    }

    public String get_property(String name) {
        if (property_map.containsKey(name)) {
            return myProject.getProperty(name);
        } else {
            return null;
        }
    }

}
