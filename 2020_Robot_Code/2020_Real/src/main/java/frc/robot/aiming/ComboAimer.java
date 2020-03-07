package frc.robot.aiming;

import java.util.ArrayList;

public class ComboAimer extends Aimer {
	
	// WARNING: IMPERIAL UNITS (INCHES) & RADIAN INPUTS AND OUTPUTS ARE USED, CONVERT AT YOUR OWN RISK
	
	public static double vIncr = SpeedAimer.vIncr;
	public static double maxV = SpeedAimer.maxV;
	public static double angleIncr = ElevationAimer.angleIncr;
	public static double minYAngle = ElevationAimer.minYAngle;
	public static double maxYAngle = ElevationAimer.maxYAngle;
	
	
	public ComboAimer() {
		this.allTrajectories = getTestTrajectories();
	}
	
	public ComboAimer(double vIncr, double maxV, double angleIncr, double minYAngle, double maxYAngle) {
		ComboAimer.vIncr = vIncr;
		ComboAimer.maxV = maxV;
		ComboAimer.angleIncr = angleIncr;
		ComboAimer.minYAngle = minYAngle;
		ComboAimer.maxYAngle = maxYAngle;
		this.allTrajectories = getTestTrajectories();
	}
	
	// returns arraylist of all trajectories at some angle over a range of initial velocities, only run once during initialization (takes a second or two)!
	public static Trajectory[] getTestTrajectories() {
		ArrayList<Trajectory> allTrajectories = new ArrayList<>();
		allTrajectories.add(new Trajectory(new double[][] {{0}, {0}}, new double[] {0}, 0, 0));
		for(double testInitV = 0.0; testInitV <= maxV; testInitV += vIncr) {
			for(double testYAngle = minYAngle; testYAngle <= maxYAngle; testYAngle += angleIncr) {
				double[][] testPoints = Trajectory.getPoints(testInitV, testYAngle);
				if(Trajectory.getPeak(testPoints)[1] >= portH-outerR+ballR) {
					double[] testCurve = Trajectory.getCurve(testPoints);
					allTrajectories.add(new Trajectory(testPoints, testCurve, testInitV, testYAngle));
				}
			}
		}
		Trajectory[] format = new Trajectory[0];
		return allTrajectories.toArray(format);
	}
}