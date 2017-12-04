package util;
import org.apache.tools.ant.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AttributeParser {

    PropertyParser propertyParser;
    private final String regex = "\\$\\{[^\\$]*\\}";
    private final String regex2 = "[$\\{}]";
    private final Pattern pattern = Pattern.compile(regex);
    public AttributeParser(Project project) {
        propertyParser = new PropertyParser(project);
    }

    /*
        Given a task return a dictionary of attribute:attribute value
        eg. input  <jar jarfile="${build.home}/${final.name}-sources.jar">
        return {jarfile:target/commons-lang-2.5-sources.jar}
     */

    public Map<String, String> getAttributeValues(Task task) {
        RuntimeConfigurable runtimeConfigurable = task.getRuntimeConfigurableWrapper();
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

}
