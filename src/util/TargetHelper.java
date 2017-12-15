package util;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import org.apache.tools.ant.Target;

/**
 * A helper class that contains methods to get important targets.
 * 
 */

public class TargetHelper {
	private Vector sortedTargets;
	private TaskHelper taskHelper;
	private ArrayList<Target> potentialSrcTargets, potentialTestTargets, junitTargets;
	
	public TargetHelper(Vector sortedTargets, TaskHelper taskHelper) {
		this.sortedTargets = sortedTargets;
		this.taskHelper = taskHelper;
		this.potentialSrcTargets = new ArrayList<Target>();
		this.potentialTestTargets = new ArrayList<Target>();
		this.getCompileTargets();
	}
	
	/**
	 * returns an array of targets that contains certain tasks
	 * @param targetOfInterest
	 * @return
	 */
	public ArrayList<Target> getTargets(String targetOfInterest){
		ArrayList<Target> list = new ArrayList<Target>();
		Enumeration vEnum = sortedTargets.elements();
		while(vEnum.hasMoreElements()) {
			Target t = (Target) vEnum.nextElement();
			if(taskHelper.containsTask(t.getTasks(),targetOfInterest)) {
				list.add(t);
			}
		}
		return list;
	}
	
	/**
	 * helper method that differentiates compile targets and test targets.
	 * 
	 */
	private void getCompileTargets(){
		List<Target> javacTargets = this.getTargets("javac");
		for(Target t : javacTargets) {
			if(t.getName().contains("test")) {
				this.potentialTestTargets.add(t);
			}
			else {
				this.potentialSrcTargets.add(t);
			}
		}
	}
	
	/**
	 * get compile source target
	 * @return
	 */
	public Target getCompileTarget() {
		if(potentialSrcTargets.size() == 0) {
			Debugger.log("Cannot find target that compiles source.");
			return null;
		}
		else if(potentialSrcTargets.size() > 1) {
			Debugger.log("Special case, there might be a top-level compile target.  Requires manual inference.");
			for(Target t : potentialSrcTargets) {
				if(t.getName().contains("compile")) {
					if(t.getName().equals("compile") || t.getName().contains("all"))
						return t;
				}
			}
		}
		else {
			return potentialSrcTargets.get(0);
		}
		return potentialSrcTargets.get(potentialSrcTargets.size()-1);
	}
	
	/**
	 * get compile test target
	 * @return
	 */
	public Target getCompileTestTarget() {
		if(potentialTestTargets.size() == 0 && potentialSrcTargets.size() == 1) {
			Target target = potentialSrcTargets.get(0);
			if(taskHelper.getTasks("javac", target).size()>1) {
				return target;
			}
		}
		else if(potentialTestTargets.size()>0)
			return potentialTestTargets.get(potentialTestTargets.size()-1);
		
		Debugger.log("No test target found.");
		return null;
	}
	
	

}
