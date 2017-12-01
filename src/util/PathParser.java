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
	
		this.path = path;
		
		int index = path.indexOf('$');
		int end = path.indexOf('}');
		if(index >= 0 && end >=0 && end >= index) {
			System.out.println(path.substring(index, end+1));
		}
		return "";
	}
	
}
