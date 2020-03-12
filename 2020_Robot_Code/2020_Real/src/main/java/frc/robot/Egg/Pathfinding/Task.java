package frc.robot.Egg.Pathfinding;

import java.io.Serializable;

import frc.robot.Egg.Pathfinding.Path;

public class Task implements Serializable{
	
	//This is a class to hold the abstract idea of a task.  A task is a command programmed into
	//the robot, such a putting a hatch panel on the cargo ship in the 2019 game
	
	/**
	 *
	 */
	private static final long serialVersionUID = -5495842987829320073L;
	public Path path;
	public boolean backwards = false;

	public String command;
	public String method;

	public long time;

	public Task(Path P, boolean back) {
		path = P;
		backwards = back;
	}
	
	public Task(String Command, String Method) {
		method = Method;
		command = Command;
	}
	
	public Task(long Time) {
		time = Time;
	}
}
