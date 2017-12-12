package util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.tools.ant.RuntimeConfigurable;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.Task;

public class TestListHelper {
	private PathParser pp;
	private TaskHelper taskHelper;
	
	public TestListHelper(PathParser pp, TaskHelper taskHelper) {
		this.pp = pp;
		this.taskHelper = taskHelper;
	}
	
	public Map<String, String> getTestList(List<Target> junitTargets ){
		Map<String, String> keyVal = new HashMap<String, String>();
		for(int i=0; i< junitTargets.size(); i++) {
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
	    				if(temp.getElementTag().equals("test") && temp.getAttributeMap().containsKey("name")) {
	    					keyVal.replace("include", keyVal.get("include")+", "+temp.getAttributeMap().get("name"));
	//    					System.out.println("zzz: "+temp.getAttributeMap().get("name"));
	    				}
	        		}
	    		}
		}
		System.out.println(keyVal);
		return keyVal;
	}
	
	private Map<String, String> batchtestHelper(RuntimeConfigurable rt) {
		Enumeration<RuntimeConfigurable> filesets = rt.getChildren();
		Map<String, String> ret = new HashMap<>();

		ret.put("include", "");
		ret.put("exclude", "");
		ret.put("dir", "");
		RuntimeConfigurable fileset = filesets.nextElement();
			
		ret.put("dir", pp.parse(this.helper(filesets,"", "dir")));
		ret.put("include", pp.parse(this.helper(filesets, "","includes")));
		ret.put("exclude", pp.parse(this.helper(filesets, "","excludes")));
			
		Enumeration<RuntimeConfigurable> fileNamePattern = fileset.getChildren();
		String include = "";
		String exclude = "";
		include = include+pp.parse(this.helper(fileNamePattern, "include",  "name"));
		exclude = exclude+pp.parse(this.helper(fileNamePattern,"exclude" ,"name"));
		ret.replace("include", include);
		ret.replace("exclude", exclude);
			
			
		return ret;
	}
	private String helper(Enumeration<RuntimeConfigurable> filesets, String elem, String attr) {
		while(filesets.hasMoreElements()) {
			RuntimeConfigurable next = filesets.nextElement();
			Hashtable attr_map = ((RuntimeConfigurable) next).getAttributeMap();
			if(elem.equals("")) {
				if(attr_map.containsKey(attr)) {
					return (String) attr_map.get(attr);
				}
			}
			else {
				if(next.getElementTag().equals(elem)) {
					if(attr_map.containsKey(attr)) {
						return (String) attr_map.get(attr);
					}
				}
			}
			
		}
		return "";
	}
}
