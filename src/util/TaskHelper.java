package util;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;

import org.apache.tools.ant.RuntimeConfigurable;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.Task;

public class TaskHelper {
	private PathParser pp;
	
	public TaskHelper(PathParser pp) {
		this.pp = pp;
	}
	
	/**
	 * Given a target and task of interest, it returns a list of tasks.
	 * @param taskType
	 * @param target
	 * @return
	 */
	public List<Task> getTasks(String taskType, Target target) {
		if(target == null) {
			Debugger.log("target is null when trying to get task.");
			return null;
		}
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
	
	/**
	 * Given a task, it returns a hashtable of attributes
	 * @param t
	 * @return
	 */
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
	
	/**
	 * Given a task type (ex "javac"), a directory type ("srcdir" or "destdir"),
	 * and target, it returns the directory.
	 * @param taskType
	 * @param dirType
	 * @param target
	 * @return
	 */
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
		List<Task> tasks = this.getTasks(taskType, target);
		if(tasks != null) {
			for(Task t : tasks) {
				if(t.getTaskType().equals(taskType)) {
					String srcDirectory = (String)this.getAttr(t, dirType);
					if(srcDirectory == null) {
						Debugger.log("no "+dirType+" exists in "+target.getName());
					}else {
						javacTasks.add(srcDirectory);
					}
				}
			}
		}
		
		noDupList = javacTasks.stream().distinct().collect(Collectors.toList());
		
		Debugger.log("Show Directory list Before Parsing");
		Debugger.log(noDupList.toString());
		
		for(int i=0; i<noDupList.size()-1; i++) {
				ret+=pp.parse(noDupList.get(i)) + ", ";
		}
		
		if(noDupList.size()>0)
			ret+=pp.parse(noDupList.get(noDupList.size()-1));
		
		Debugger.log("Show Directory list After Parsing");
		Debugger.log(ret);
		
		
		return ret;
	}
	/**
	 * For each Task in array "tasks"
	 * Look for Task under with "subTaskOfInterest" type
	 * Look for "attrOfInterest" attr
	 * @param tasks
	 * @param subTaskOfInterest
	 * @param attrOfInterest
	 * @return
	 * 
	 * A list of attrOfInterest in tasks' subTaskOfInterest
	 */
	public String[] getSubTaskAttr(Task[] tasks, String subTaskOfInterest,String attrOfInterest) {
		
		Vector<String> result_attrs = new Vector(0,1);
		for(Task t : tasks) {
			RuntimeConfigurable rc = t.getRuntimeConfigurableWrapper();
			
			Enumeration<RuntimeConfigurable> sub_rc = rc.getChildren();
			while(sub_rc.hasMoreElements()) {
				RuntimeConfigurable curr_sub_rc = sub_rc.nextElement();
				if(curr_sub_rc.getElementTag().equals(subTaskOfInterest)) {
					Hashtable<String, Object> attr_map = curr_sub_rc.getAttributeMap();
					if(attr_map.containsKey(attrOfInterest)) {
						//Remove duplicate
						String temp_attri = attr_map.get(attrOfInterest).toString();
						if(!result_attrs.contains(temp_attri))
							result_attrs.addElement(temp_attri);
					}
				}
			}
		}
		
		return result_attrs.toArray(new String[result_attrs.size()]);
	}
}
