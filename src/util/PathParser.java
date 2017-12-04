package util;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.RuntimeConfigurable;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.Task;

public class PathParser {
	
	private String path;
	private Properties properties;
		
	private Map<String, String> properties_map;
	private File build_file;

	private Project project;
	
	
	public PathParser(Project project, File f) {
		//Default tasks
		this.properties = new Properties();

		
		//Properties under target
		this.properties_map = new HashMap<String, String>();

		//Load in the build.xml
		this.project = project;
		this.build_file = f;
		this.loadPropertiesFromTarget();
		
	}
	
	
	/**
	 * Add property files for parsing
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
	
	private void loadProperties() {
		Vector<String> files = XmlParser.getPropertiesFiles(this.build_file);
		boolean changed = true;
		while(changed) {
			changed = false;
			
			for(String file : files) {
				String parsed_file = this.parse(file);
				if(parsed_file.indexOf('$') < 0) {
					this.addProperties(file);
					
					changed = true;
					files.remove(file);
					break;
				}
			}//End for loop
			
		}//End while loop
	}
	
	private void loadPropertiesFromTarget() {
		
		Hashtable target_map = project.getTargets();
		
		Enumeration names = target_map.keys();
		
		while(names.hasMoreElements()) {
			String str = (String) names.nextElement();
			Target t = (Target) target_map.get(str);
			Task[] tasks = t.getTasks();
			for(Task task : tasks) {
				if(task.getTaskType() == "property") {
					RuntimeConfigurable rt = task.getRuntimeConfigurableWrapper();
					Hashtable att_map = rt.getAttributeMap();

					String key = (String) att_map.get("name");
					String value = (String) att_map.get("value");
					this.properties_map.put(key, value);
				}
			}
		}

	}
	
	
	
}
