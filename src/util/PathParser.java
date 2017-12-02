package util;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Target;

public class PathParser {
	
	private String path;
	private Properties properties;
	

	private Project project;
	
	public PathParser() {
		
		this.properties = new Properties();
		
		//For Testing only *******************
		//Load the property file
		InputStream file = null;
		try{
		file = new FileInputStream("src/default.properties");
		this.properties.load(file);
		} catch(IOException ex) {
			ex.printStackTrace();
		} finally {
			if(file != null) {
				try {
					file.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		//For Testing only ends *******************

		this.project = null;
	}
	
	public PathParser(Project project) {
		//Default tasks
		this.properties = new Properties();

		
		//Load in the build.xml
		this.project = project;
		this.loadProperties();
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
			ex.printStackTrace();
		} finally {
			if(file != null) {
				try {
					file.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public String parse(String path) {
		
		//String result stores the output string
		String result = "";
		//String temp stores the unparsed/worked section of String path
		String temp = path;
		
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
		return result + temp;
	}
	
	//a wrapper method for properties.getPorperty
	private String getProperty(String key) {
		
		//Look for property value using project.getProperty
		if(this.project != null) {
			String resolved = this.project.getProperty(key);
			if(resolved != null)
				return resolved;
		}
		
		//Look for property value in .properties files
		resolved = this.properties.getProperty(key);
		if(resolved != null)
			return resolved;
		
		if(key.equals("basedir")) {
			if(this.project != null) {
				return this.project.getBaseDir().toString();
			}
		}
		
		return null;
		
		
	}
	
	private void loadProperties() {
		Hashtable property_map = this.project.getProperties();
		
		Enumeration names = property_map.keys();
		
		while(names.hasMoreElements()) {
			String str = (String) names.nextElement();
			String t =  property_map.get(str).toString();
			System.out.println("Name: " + str + ", Description: " + t);
		}
		System.out.println("............................................................");
	}
	
	
}
