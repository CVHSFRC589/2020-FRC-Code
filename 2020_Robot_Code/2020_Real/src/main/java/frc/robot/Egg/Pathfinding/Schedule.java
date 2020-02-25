package frc.robot.Egg.Pathfinding;

import java.io.Serializable;
import java.util.ArrayList;

import frc.robot.Egg.Pathfinding.*;

public class Schedule implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4726394004929100537L;
	public ArrayList<Task> Tasks = new ArrayList<Task>();
	public int spot = -1;
	
	public Schedule(ArrayList<Task> tasks) {	
		Tasks = tasks;
	}
	
	public void addTask(Task T) {		
		Tasks.add(T);
	}
	
	public Task getNext() {
		spot++;
		if (spot < Tasks.size()) {
			return Tasks.get(spot);
		} else {
			return new Task(null, "Null");
		}
	}
}
