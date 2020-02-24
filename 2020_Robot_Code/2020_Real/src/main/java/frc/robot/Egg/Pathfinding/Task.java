package frc.robot.Egg.Pathfinding;

import java.io.Serializable;

import frc.robot.Egg.Pathfinding.Path;

public class Task implements Serializable{
	
	//This is a class to hold the abstract idea of a task.  A task is a command programmed into
	//the robot, such a putting a hatch panel on the cargo ship in the 2019 game
	
	public Path path;
	public boolean backwards;

	public String command;
	public String method;

	public double time;

	public Task(Path P, boolean back) {
		path = P;
		backwards = back;
	}
	
	public Task(String Command, String Method) {
		method = Method;
		command = Command;
	}
	
	public Task(double Time) {
		time = Time;
	}
}
