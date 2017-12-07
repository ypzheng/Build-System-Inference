package util;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.RuntimeConfigurable;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.util.StringUtils;

public class PathParser {
	
	private String path;
	private Properties properties;
		
	private Map<String, String> properties_map;


	private Project project;
	
	
	public PathParser(Project project) {
		//Default tasks
		this.properties = new Properties();

		
		//Properties under target
		this.properties_map = new HashMap<String, String>();

		//Load in the build.xml
		this.project = project;
		this.loadPropertiesFromTarget();
		
	}
	
	

	
	public String parse(String path) {
		
		
		//String result stores the output string
		String result = "";
		//String temp stores the unparsed/worked section of String path
		
		String temp = Paths.get(path).toString();
		
		/**
		 * Try to find the first key in result
		 * key is in format ${key_name}
		 */
		int index = temp.indexOf('$');
		int end = temp.indexOf('}');
		
		/**
		 * Extract the key and get the property.
		 * Replace key in path with the property
		 * 
		 * Keep looking for more key in result of the path
		 */
		while(index >= 0 && end >=0 && end >= index+2 ) {
			String temp_key = temp.substring(index+2, end);
			String temp_resolved = this.getProperty(temp_key);
			
			//If key exists, replace the value in path
			if(temp_resolved != null) {
	
				result = temp.substring(0, index) + temp_resolved;
			
			} else {
				
				result = temp.substring(0,end+1);
			}
			
			//Reach end of path already
			if(end == temp.length()-1) {
				temp = "";
				break;
			}
			temp = temp.substring(end+1);
			index = temp.indexOf('$');
			end = temp.indexOf('}');
			
		}
		
		//Attach rest of the path that doesn't need parsing
		return Paths.get(result + temp).toString();
	
	}
	
	//a wrapper method for properties.getPorperty
	private String getProperty(String key) {
		
		String resolved = null;
		
		//Since project.getProject("basedir") will return an absolute path
		
		
		if(key.equals("basedir")) {
			//Absolute path of build file
			Path build_file_location = Paths.get(this.project.getBaseDir().toString());
			
			//Absolute path of basedir key
			Path basedir = Paths.get(this.project.getProperty("basedir"));
			
			resolved = build_file_location.relativize(basedir).toString();
			System.out.println(resolved);
			if(resolved!=null)
				return resolved;
		}
			
		
		//Look for property value using project.getProperty
		if(this.project != null) {
			resolved = this.project.getProperty(key);
			if(resolved != null)
				return resolved;
		}
		
		//Look for property value in .properties files
		resolved = this.properties_map.get(key);
		if(resolved != null)
			return resolved;
		
		
		
		return resolved;
		
		
	}
	
	/**
	 * Load Properties Under Target Such As Int
	 * 
	 * For Furture: Only load from Init Targets, or Targets that compile/build Target depends on.
	 */
	
	private void loadPropertiesFromTarget() {
		
		//Get ALL Target
		Hashtable target_map = project.getTargets();
		
		Enumeration names = target_map.keys();
		
		//Get all properties
		while(names.hasMoreElements()) {
			String str = (String) names.nextElement();
			Target t = (Target) target_map.get(str);
			Task[] tasks = t.getTasks();
			//Get all Tasks
			for(Task task : tasks) {
				//Only care about property tasks
				if(task.getTaskType() == "property") {
					RuntimeConfigurable rt = task.getRuntimeConfigurableWrapper();
					Hashtable att_map = rt.getAttributeMap();

					//Get attribute "name" & "value"
					String key = (String) att_map.get("name");
					String value = (String) att_map.get("value");
					if(key!=null && value != null)
						this.properties_map.put(key, value);
				}
			}
		}
		//Get all properties End
		
		
		//Resolve all properties in properties_map
		Iterator it = this.properties_map.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry pair =(Map.Entry)it.next();
			String key = (String) pair.getKey();
			String value = (String) pair.getValue();
			
			//Check if resolve is needed
			if(value.indexOf('$') >= 0)
				this.properties_map.put( key, this.parse(value));
			
		}
		//Resolve End
	}
	
	
	/**
	 * Add property files for parsing
	 * 
	 * No longer Necessary
	 */
	public void addProperties(String filename) {
		//Load the property file
		InputStream file = null;
		try{
		
		file = new FileInputStream(filename);
		
		this.properties.load(file);
		} catch(IOException ex) {
			//ex.printStackTrace();
		} finally {
			if(file != null) {
				try {
					file.close();
				} catch (IOException e) {
					//e.printStackTrace();
				}
			}
		}
	}
	
}
