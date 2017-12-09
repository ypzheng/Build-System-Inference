import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyWriter {
	
	private BuildFileAnalyzerAdapter adapter;
	private static File file;
	private Properties propertyFile;

	public PropertyWriter(String type, File buildFile, File outputFile, String projectPath) throws IOException {

		adapter = new BuildFileAnalyzerAdapter(type, buildFile, projectPath);
		file = outputFile;
		propertyFile = new Properties();
	}
	
	
	public void setProperties() throws IOException {

		propertyFile.setProperty("src.compile", adapter.getCompileSrcTarget());
		propertyFile.setProperty("test.compile", adapter.getCompileTestTarget());
		propertyFile.setProperty("src.dir", adapter.getSrcDir());
		propertyFile.setProperty("src.comp.dir", adapter.getCompDir());
		propertyFile.setProperty("test.dir", adapter.getTestDir());
		propertyFile.setProperty("test.comp.dir", adapter.getCompTestDir());
		propertyFile.setProperty("src.compile.dependency", adapter.getSrcDep());
		propertyFile.setProperty("test.compile.dependency", adapter.getTestDep());
		propertyFile.setProperty("tests.all", adapter.getTestList());
//		
//		propertyFile.setProperty("src.compile", "");
//		propertyFile.setProperty("test.compile", "");
//		propertyFile.setProperty("src.dir", "");
//		propertyFile.setProperty("src.comp.dir", "");
//		propertyFile.setProperty("test.dir", "");
//		propertyFile.setProperty("test.comp.dir", "");
//		propertyFile.setProperty("src.compile.dependency","");
//		propertyFile.setProperty("test.compile.dependency", "");
//		propertyFile.setProperty("tests.all", "");
		
		saveProperties(propertyFile);
	
	}
	
	public static void saveProperties(Properties p)throws IOException{
		
		FileOutputStream fr=new FileOutputStream(file);	
		p.store(fr,"Properties");
	    fr.close();
	}
	
	public static void loadProperties(Properties p)throws IOException{
		
		FileInputStream fi=new FileInputStream("src/default.properties");
		p.load(fi);
		fi.close();
		System.out.println(p.getProperty("component.package"));
	}
}

