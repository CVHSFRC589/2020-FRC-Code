package frc.robot.aiming;

import java.util.ArrayList;
import java.lang.Math;

public class ElevationAimer extends Aimer {
	
	// WARNING: IMPERIAL UNITS (INCHES) & RADIAN INPUTS AND OUTPUTS ARE USED, CONVERT AT YOUR OWN RISK
	
	final public static double angleIncr = 0.05; //inches/second
	final public static double minYAngle = Math.toRadians(5);
	final public static double maxYAngle = Math.toRadians(50);
	
	
	public double initV;
	
	public ElevationAimer(double initV) {
		this.allTrajectories = getTestTrajectories(initV);
		this.initV = initV;
	}
	
	// returns arraylist of all trajectories at some angle over a range of initial velocities, only run once during initialization (takes a second or two)!
	public static Trajectory[] getTestTrajectories(double initV) {
		ArrayList<Trajectory> allTrajectories = new ArrayList<>();
		for(double testYAngle = minYAngle; testYAngle <= maxYAngle; testYAngle += angleIncr) {
			double[][] testPoints = Trajectory.getPoints(initV, testYAngle);
			if(Trajectory.getPeak(testPoints)[1] >= portH-outerR+ballR) {
				double[] testCurve = Trajectory.getCurve(testPoints);
				allTrajectories.add(new Trajectory(testPoints, testCurve, initV, testYAngle));
			}
		}
		Trajectory[] format = new Trajectory[0];
		return allTrajectories.toArray(format);
	}
}