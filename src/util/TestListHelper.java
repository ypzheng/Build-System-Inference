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
					include = include+pp.parse((String)temp.getAttributeMap().get("name"))+";";
				}
				if(temp.getElementTag().equals("exclude")){
					exclude = exclude+pp.parse((String)temp.getAttributeMap().get("name"))+";";
				}
				ret.replace("include", include);
				ret.replace("exclude", exclude);
			}
		}
	return ret;
}
}
