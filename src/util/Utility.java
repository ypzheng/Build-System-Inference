package util;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Stack;

public class Utility {
	
	
	/**
	 * Recursively find all files that match the regular expression "rex"
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
		ArrayList<Path> dirs = new ArrayList<Path>();
		dirs.add(dir);
		
		
		
		while(dirs.size() > 0) {
			
	        try(DirectoryStream<Path> stream = Files.newDirectoryStream(dirs.get(0), rex)){
	        	for(Path entry:stream) {
	        		if(Files.isDirectory(entry)) {
	        			dirs.add(entry);
	        		} else {
	        			files.add(entry.getFileName().toString());
	        			
	        		}
	        	}
	        } catch(IOException e) {
	        	e.printStackTrace();
	        }
	        	
	        dirs.remove(0);
	      
	        
		}
        return (String[]) files.toArray(new String[files.size()]);
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
