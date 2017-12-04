package util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.tools.ant.RuntimeConfigurable;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.Task;

public class DirectoryHelper {
	private PathParser pp;
	
	public DirectoryHelper(PathParser pp) {
		this.pp = pp;
	}
	
	public String getDirectory(String taskType, String dirType, Target target) {
		//Compile Target is not found yet
				Task[] tasks = target.getTasks();
				List<String> javacTasks = new ArrayList<String>();
				List<String> noDupList;
				String ret = "";
				
				//Infer Src Directory from Compile Target
				/**
				 * Find taskType Task
				 * Looks for dirType attribute
				 */
				for(Task t : tasks) {
					if(t.getTaskType().equals(taskType)) {
						RuntimeConfigurable rt =t.getRuntimeConfigurableWrapper();
						Hashtable att_map = rt.getAttributeMap();

						String srcDirectory = (String) att_map.get(dirType);
						if(srcDirectory == null) {
							Debugger.log("no "+dirType+" exists in "+target.getName());
						}else {
							javacTasks.add(srcDirectory);
						}
					}
				}
				noDupList = javacTasks.stream().distinct().collect(Collectors.toList());
				
				Debugger.log("Show Directory list Before Parsing");
				Debugger.log(noDupList.toString());
				
				for(int i=0; i<noDupList.size()-1; i++) {
						ret+=pp.parse(noDupList.get(i)) + ", ";
				}
				ret+=pp.parse(noDupList.get(noDupList.size()-1));
				
				Debugger.log("Show Directory list After Parsing");
				Debugger.log(ret);
				
				return ret;
	}
}
