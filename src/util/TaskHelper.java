package util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.tools.ant.RuntimeConfigurable;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.Task;

public class TaskHelper {
	private PathParser pp;
	
	public TaskHelper(PathParser pp) {
		this.pp = pp;
	}
	
	public List<Task> getTasks(String taskType, Target target) {
		Task[] tasks = target.getTasks();
		List<Task> tasksOfInterest = new ArrayList<Task>();
		if(taskType == "") {
			Debugger.log("taskType is empty, please input valid taskType");
		}
		for(Task t : tasks) {
			if(t.getTaskName().equals(taskType)) {
				tasksOfInterest.add(t);
			}
		}
		if(tasksOfInterest.size() == 0)
			Debugger.log("No task: "+taskType+" found under "+target.getName()+", returning null");
		return tasksOfInterest;
	}
	
	public Hashtable getAttributes(Task t){
		RuntimeConfigurable rt =t.getRuntimeConfigurableWrapper();
		Hashtable att_map = rt.getAttributeMap();
		return att_map;
	}
	
	public String getAttr(Task t, String attrOfInterest){
		return (String)this.getAttributes(t).get(attrOfInterest);
	}
	
	public boolean containsTask(Task[] tasks, String taskName) {
		for(Task t : tasks) {
			if(t.getTaskType().equals(taskName)) {
				return true;
			}
		}
		return false;
	}
	
	public String getDirectory(String taskType, String dirType, Target target) {
		//Compile Target is not found yet
				List<String> javacTasks = new ArrayList<String>();
				List<String> noDupList;
				String ret = "";
				
				//Infer Src Directory from Compile Target
				/**
				 * Find taskType Task
				 * Looks for dirType attribute
				 */
				for(Task t : this.getTasks(taskType, target)) {
					if(t.getTaskType().equals(taskType)) {
						String srcDirectory = (String)this.getAttr(t, dirType);
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
