package frc.robot.aiming;

import java.util.ArrayList;
import java.lang.Math;

public class SpeedAimer extends Aimer {
	
	// WARNING: IMPERIAL UNITS (INCHES) & RADIAN INPUTS AND OUTPUTS ARE USED, CONVERT AT YOUR OWN RISK
	
	final public static double vIncr = 0.0625; //inches/second
	final public static double maxV = 410.0; //inches/second
	
	
	public double yAngle;
	
	public SpeedAimer(double yAngle) {
		this.allTrajectories = getTestTrajectories(yAngle);
		this.yAngle = yAngle;
	}
	
	// returns arraylist of all trajectories at some angle over a range of initial velocities, only run once during initialization (takes a second or two)!
	public static Trajectory[] getTestTrajectories(double yAngle) {
		ArrayList<Trajectory> allTrajectories = new ArrayList<>();
		for(double testInitV = 0.0; testInitV <= maxV; testInitV += vIncr) {
			double[][] testPoints = Trajectory.getPoints(testInitV, yAngle);
			if(Trajectory.getPeak(testPoints)[1] >= portH-outerR+ballR) {
				double[] testCurve = Trajectory.getCurve(testPoints);
				allTrajectories.add(new Trajectory(testPoints, testCurve, testInitV, yAngle));
			}
		}
		Trajectory[] format = new Trajectory[0];
		return allTrajectories.toArray(format);
	}
}