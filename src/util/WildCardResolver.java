package util;


import java.io.File;
import java.io.IOException;

import org.apache.tools.ant.DirectoryScanner;

public class WildCardResolver {
	private static DirectoryScanner ds;
	
	public static String[] resolveWildCard(String[] includes, String[] excludes, String baseDir) {
		ds = new DirectoryScanner();
		ds.setIncludes(includes);
		ds.setExcludes(excludes);
		ds.setBasedir(baseDir);
		ds.setCaseSensitive(true);
		try {
			ds.scan();
		}catch(IllegalStateException e){
			System.out.println("Illegal State Exception found resolving wild cards, basedir does not exist.\n"
					+ "Instead, a list of wildcards that matches test file pattern is written to property file.");
		}
		return ds.getIncludedFiles();
	
	}
}
