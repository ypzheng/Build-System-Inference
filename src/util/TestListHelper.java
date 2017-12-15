package util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.tools.ant.RuntimeConfigurable;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.Task;

/**
 * Helper class that finds the directory of tests, includes/excludes, and 
 * name of the tests to run, writing them onto a Map.
 *
 */
public class TestListHelper {
	private PathParser pp;
	private TaskHelper taskHelper;
	
	public TestListHelper(PathParser pp, TaskHelper taskHelper) {
		this.pp = pp;
		this.taskHelper = taskHelper;
	}
	/**
	 * Given a list of targets that contain Junit task, it will check if the targets contain
	 * "test" or "batchtest".  At last, write the test directory, includes, and excludes into 
	 * a map.
	 * @param junitTargets
	 * @return
	 */
	public Map<String, String> getTestList(List<Target> junitTargets ){
		Map<String, String> keyVal = new HashMap<String, String>();
		keyVal.put("include", "");
		keyVal.put("exclude", "");
		keyVal.put("dir", "");
		for(int i=0; i< junitTargets.size(); i++) {
			//get a list of tasks under junit
			List<Task> tasks = taskHelper.getTasks("junit", junitTargets.get(i));
			
	    		for(int j=0; j<tasks.size(); j++) {
	    			RuntimeConfigurable rt = tasks.get(j).getRuntimeConfigurableWrapper();
	    			Enumeration<RuntimeConfigurable> enumeration= rt.getChildren();
	    			while(enumeration.hasMoreElements()) {
	    				RuntimeConfigurable temp = enumeration.nextElement();
	    				if(temp.getElementTag().equals("batchtest")) {
	    					keyVal.putAll(batchtestHelper(temp));
	    				}
	    				if(temp.getElementTag().equals("classpath")) {
	    					
	//    					System.out.println("cp: "+this.junitTargets.get(j).getName()+ " pathelement exists");
	    				}
	    				if(temp.getElementTag().equals("test") || temp.getAttributeMap().containsKey("name")) {
	    					if(keyVal.get("include") == null)
	    						keyVal.put("include", (String)temp.getAttributeMap().get("name")+";");
	    					else
	    						keyVal.put("include", keyVal.get("include")+pp.parse((String) temp.getAttributeMap().get("name"))+";");
	    				}
	        		}
	    		}
		}
		return keyVal;
	}
	
	private Map<String, String> batchtestHelper(RuntimeConfigurable rt) {
		Enumeration<RuntimeConfigurable> filesets = rt.getChildren();
		Map<String, String> ret = new HashMap<>();

		
		while(filesets.hasMoreElements()) {
			RuntimeConfigurable fileset = filesets.nextElement();
			
			ret.put("dir", pp.parse(this.helper(fileset,"", "dir")));
			ret.put("include", pp.parse(this.helper(fileset, "","includes")));
			ret.put("exclude", pp.parse(this.helper(fileset, "","excludes")));
				
			Enumeration<RuntimeConfigurable> fileNamePattern = fileset.getChildren();
			String include = "";
			String exclude = "";
			while(fileNamePattern.hasMoreElements()) {
				RuntimeConfigurable fileset2 = fileNamePattern.nextElement();
				if(!this.helper(fileset2, "include",  "name").equals(""))
					include = include+pp.parse(this.helper(fileset2, "include",  "name"))+";";
				if(!this.helper(fileset2, "exclude",  "name").equals(""))
					exclude = exclude+pp.parse(this.helper(fileset2,"exclude" ,"name"))+";";
				ret.replace("include", include);
				ret.replace("exclude", exclude);
			}
		}
		
		return ret;
	}
	
	private String helper(RuntimeConfigurable filesets, String elem, String attr) {
	
			Hashtable attr_map = ((RuntimeConfigurable) filesets).getAttributeMap();
			if(elem.equals("")) {
				if(attr_map.containsKey(attr)) {
					return (String) attr_map.get(attr);
				}
			}
			else {
				if(filesets.getElementTag().equalsIgnoreCase(elem)) {
					if(attr_map.containsKey(attr)) {
						return (String) attr_map.get(attr);
					}
				}
			}
			
		
		return "";
	}
}
