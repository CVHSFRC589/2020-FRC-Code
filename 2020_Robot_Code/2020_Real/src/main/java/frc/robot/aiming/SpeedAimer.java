package frc.robot.aiming;

import java.util.ArrayList;

public class SpeedAimer extends Aimer {
	
	// WARNING: IMPERIAL UNITS (INCHES) & RADIAN INPUTS AND OUTPUTS ARE USED, CONVERT AT YOUR OWN RISK
	
	public static double vIncr = 0.5;
	public static double maxV = 410.0;
	
	
	public double yAngle;
	
	public SpeedAimer(double yAngle) {
		this.allTrajectories = getTestTrajectories(yAngle);
		this.yAngle = yAngle;
	}
	
	public SpeedAimer(double yAngle, double vIncr, double maxV) {
		SpeedAimer.vIncr = vIncr;
		SpeedAimer.maxV = maxV;
		this.allTrajectories = getTestTrajectories(yAngle);
		this.yAngle = yAngle;
	}
	
	// returns arraylist of all trajectories at some angle over a range of initial velocities, only run once during initialization (takes a second or two)!
	public static Trajectory[] getTestTrajectories(double yAngle) {
		ArrayList<Trajectory> allTrajectories = new ArrayList<>();
		allTrajectories.add(new Trajectory(new double[][] {{0}, {0}}, new double[] {0}, 0, 0));
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