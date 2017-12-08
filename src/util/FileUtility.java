package util;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtility {
	
	
	/**
	 * Recursively find all files that match the regular expression "include" but not match the regular expression "exclude"
	 * Under "path" directory
	 * 
	 * @param path
	 * Path of Directory to Search
	 * @param rex
	 * Regular Expression of filename
	 * 
	 * @return
	 * A list of files that match the criteria
	 */
	public static String[] lsDirectoryRS(String path, String include, String exclude) {
		
		Path dir = Paths.get(path);
		ArrayList<String> files = new ArrayList<String>();
		ArrayList<Path> dirs = new ArrayList<Path>();
		dirs.add(dir);
		
		Pattern include_r = Pattern.compile(include);
		Pattern exclude_r = Pattern.compile(exclude);
		while(dirs.size() > 0) {
			
			//Only return files that 
	        try(DirectoryStream<Path> stream = Files.newDirectoryStream(dirs.get(0), "*")){
	        	for(Path entry:stream) {
	        		if(Files.isDirectory(entry)) {
	        			dirs.add(entry);
	        		} else {
	        			String filename = entry.getFileName().toString();
	        			Matcher include_m = include_r.matcher(filename);
	        			Matcher exclude_m = exclude_r.matcher(filename);
	        			
	        			//Exclude filenames base on rex exclude
	        			if(exclude_m.matches())
	        				continue;
	        			
	        			//Include filenames base on rex include
	        			if(include_m.matches())
	        				files.add(filename);
	        			
	        		}
	        	}
	        } catch(IOException e) {
	        	e.printStackTrace();
	        }
	        	
	        dirs.remove(0);
	        
		}
        return (String[]) files.toArray(new String[files.size()]);
	}
	
	/**
	 * find all files that match the regular expression "rex"
	 * Under "path" directory
	 * 
	 * @param path
	 * Path of Directory to Search
	 * @param rex
	 * Regular Expression of filename
	 * 
	 * @return
	 * A list of files that match the criteria
	 */
	public static String[] lsDirectory(String path, String rex) {
		
		Path dir = Paths.get(path);
		ArrayList<String> files = new ArrayList<String>();
				
        try(DirectoryStream<Path> stream = Files.newDirectoryStream(dir, rex)){
        	for(Path entry:stream) {
        		if(!Files.isDirectory(entry)) {
        			files.add(entry.getFileName().toString());
        			
        		}
        	}
        } catch(IOException e) {
        	e.printStackTrace();
        }
        	
        
		
        return (String[]) files.toArray(new String[files.size()]);
	}
	
	/**
	 * "absolute" is expected to be the base directory in absolute path
	 * "path" can be relative or aboslute
	 * 
	 * @param String absolute
	 * @param String path
	 * @return
	 * A relative path version of "path" in relation to "absolute" 
	 */
	public static String absoluteToRelative(String absolute, String path) {
		
		
		/**
		 * Since MAC don't have Drive likes Windows
		 * getRoot or isAsolute can't distinguish relative path from absolute
		 * 
		 */
		
		if(!path.contains(absolute))
			if(!absolute.contains(path))
				return path;
		
		//Initialize "path" and "absolute" as Path object to take advantage of the library
		Path original_path = Paths.get(path);
		Path absolute_path = Paths.get(absolute);
		
		/**
		 * Check if original_path is a in absolute path form 
		 * and has the same type/root as absolute_path
		 */
		if(!original_path.isAbsolute())
			return path;
		
		//Find the relative path from absolute_path to original_path 
		String relative_path = absolute_path.relativize(original_path).toString();
		
		//Final null checkpoint
		if(relative_path!=null) {
			return relative_path;
		}
		
		//Return the original "path" if there isn't a solution
		return path;
			
	}
	
	/**
	 * List of paths in "paths"
	 * 
	 * Filter with "rex" as regular expression
	 * 
	 * Boolean "include" define whether to keep or discard that matches "rex"
	 * In other words, whether "rex" is include or exclude
	 * 
	 * @param paths
	 * @param include
	 * @param rex
	 * @return
	 * 
	 * a list of filtered path
	 */
	public static String[] filterPath(String[] paths, boolean include, String rex) {
		
		Vector<String> result_paths = new Vector(0,1);
		for(String path : paths) {
			if(rex != null) {
				if(rex.length()>0) {
					Pattern include_r = Pattern.compile(rex);
					Matcher include_m = include_r.matcher(path);
					if(include_m.matches() && include)
						result_paths.addElement(path);
				}
			}
		}
		
		
		return result_paths.toArray(new String[result_paths.size()]);
		
	
	}
    /*
    Perform a DFS recursively find all the file in the given root folder
     */
    public static void printPath(String[] paths) {
        for (String path: paths) {
            Stack<File> folders = new Stack<>();
            folders.add(new File(path));
            while (!folders.isEmpty()) {
                File folder = folders.pop();
                if (folder.isFile()) {
                    System.out.println(folder.getName());
                } else {
                    File[] listOfFiles = folder.listFiles();
                    if (listOfFiles != null){
                        for (int i = 0; i < listOfFiles.length; i++) {
                            if (listOfFiles[i].isFile()) {
                                String fileName = listOfFiles[i].getName();
                                if (fileName.endsWith(".class")) {
                                    System.out.println("File " + listOfFiles[i].getName());
                                }
                            } else if (listOfFiles[i].isDirectory()) {
                                String absPath = listOfFiles[i].getAbsolutePath();
                                File newFolder = new File(absPath);
                                folders.add(newFolder);
                            }
                        }
                    }
                }

            }
        }
    }
    
}
