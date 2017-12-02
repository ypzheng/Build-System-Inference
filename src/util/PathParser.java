package util;


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
		
		String result = path;
		
		/**
		 * Try to find the first key in result
		 * key is in format ${key_name}
		 */
		int index = result.indexOf('$');
		int last_index = index;
		int end = result.indexOf('}');
		
		/**
		 * Extract the key and get the property.
		 * Replace key in result with the property
		 * 
		 * Keep looking for more key in result
		 */
		while(index >= 0 && end >=0 && end >= index+2 ) {
			String temp_key = result.substring(index+2, end);
			String temp_resolved = this.properties.getProperty(temp_key);
			
			if(temp_resolved != null) {
				if(end == result.length()-1)
					result = result.substring(0, index) + temp_resolved;
				else
					result= result.substring(0, index) + temp_resolved +result.substring(end+1);
			}
			
			
			index = result.indexOf('$');
			end = result.indexOf('}');
			
			//Prevent infinite loop
			if(index == last_index)
				break;
		}
		return result;
	}
	
	
}
