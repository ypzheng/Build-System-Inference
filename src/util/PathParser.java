package util;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PathParser {
	
	private String path;
	private Properties properties;
	
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
			String temp_resolved = this.properties.getProperty(temp_key);
			
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
	
	
}
