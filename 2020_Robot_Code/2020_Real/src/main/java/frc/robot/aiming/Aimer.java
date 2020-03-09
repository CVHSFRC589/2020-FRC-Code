package frc.robot.aiming;

import java.util.ArrayList;
import java.lang.Math;

public abstract class Aimer {
	
	// WARNING: IMPERIAL UNITS (INCHES) & RADIAN INPUTS AND OUTPUTS ARE USED, CONVERT AT YOUR OWN RISK
	
	final public static double portH = 98.25;
	final public static double portD = 29.25;
	final public static double ballR = 3.5;
	final public static double outerR = 15.0;
	final public static double innerR = 6.5;
	final public static double bestInner = 400.0;
	final public static double bestOuter = 100.0;
	
	public Trajectory[] allTrajectories;
	
	public Aimer() {
		this.allTrajectories = new Trajectory[1];
		allTrajectories[0] = new Trajectory(new double[][] {{0}, {0}}, new double[] {0}, 0, 0);
	}
	
	// tests all possible trajectories to find the optimal initial velocity at some distance and angle
	public Trajectory getOptimalTrajectory(double dist, double xAngle) {
		double optimal = 0.0;
		double targetXAngle = 0.0;
		int optTrajectoryNum = 0;
		for(int testTrajectoryNum = 0; testTrajectoryNum < allTrajectories.length; testTrajectoryNum++) {
			Trajectory traj = allTrajectories[testTrajectoryNum];
			double[] testPeak = traj.getPeak();
			double[] inner = getInner(dist, xAngle);
			double outerOffset = Math.pow(traj.getY(inner[3]) - portH, 2);
			outerOffset += Math.pow(inner[1], 2);
			outerOffset = Math.sqrt(outerOffset);
			if(outerOffset < outerR-ballR-1) {
				double innerOffset = Math.abs(traj.getY(inner[0]) - portH);
				if(innerOffset < innerR-ballR-1) {
					if(testPeak[0] > inner[3] && testPeak[0] < inner[0]) {
						if(Math.abs(testPeak[1]-portH) < outerR-ballR) {
							double innerOpt = (1-(innerOffset/(innerR-ballR))) * bestInner;
							double outerOpt = (1-(outerOffset/(outerR-ballR))) * bestOuter;
							double testOptimal = innerOpt+outerOpt;
							if(testOptimal > optimal) {
								targetXAngle = inner[2];
								optimal = testOptimal;
								optTrajectoryNum = testTrajectoryNum;
							}
						}
					}
					else {
						double innerOpt = (1-(innerOffset/(innerR-ballR))) * bestInner;
						double outerOpt = (1-(outerOffset/(outerR-ballR))) * bestOuter;
						double testOptimal = innerOpt+outerOpt;
						if(testOptimal > optimal) {
							targetXAngle = inner[2];
							optimal = testOptimal;
							optTrajectoryNum = testTrajectoryNum;
						}
					}
				}
				else {
					double outerOpt = (1-(outerOffset/(outerR-ballR))) * bestOuter;
					if(outerOpt > optimal) {
						targetXAngle = inner[2];
						optimal = outerOpt;
						optTrajectoryNum = testTrajectoryNum;
					}
				}
			}
		}
		Trajectory targetTrajectory = allTrajectories[optTrajectoryNum].clone();
		targetTrajectory.setDetails(targetXAngle, dist, optimal);
		return targetTrajectory;
	}
	
	// uses getAngledInitV to find the optimal initial velocity at a range of distances
	public Trajectory[] getOptimalTrajectories(double distIncr, double maxDist, double xAngle) {
		ArrayList<Trajectory> returned = new ArrayList<>();
		for(double testDist = distIncr; testDist <= maxDist; testDist += distIncr) {
			Trajectory optTrajectory = this.getOptimalTrajectory(testDist, xAngle);
			if(optTrajectory.optimal > 0) {
				returned.add(optTrajectory);
			}
		}
		Trajectory[] format = new Trajectory[0];
		return returned.toArray(format);
	}
	
	// tests all possible trajectories to find the optimal initial velocity at some distance for an outer port shot
	public Trajectory getOuterTrajectory(double dist) {
		double optimal = 0.0;
		int optTrajectoryNum = 0;
		for(int testTrajectoryNum = 0; testTrajectoryNum < allTrajectories.length; testTrajectoryNum++) {
			double outerOffset = Math.abs(allTrajectories[testTrajectoryNum].getY(dist) - portH);
			if(outerOffset < outerR-ballR-1) {
				double outerOpt = (1-(outerOffset/(outerR-ballR)));
				if(outerOpt > optimal) {
					optimal = outerOpt;
					optTrajectoryNum = testTrajectoryNum;
				}
			}
		}
		Trajectory targetTrajectory = allTrajectories[optTrajectoryNum].clone();
		targetTrajectory.setDetails(0.0, dist, optimal);
		return targetTrajectory;
	}
	
	// uses getOuterInitV to find the optimal initial velocity at a range of distances for an outer port shot
	public Trajectory[] getOuterTrajectories(double distIncr, double maxDist) {
		ArrayList<Trajectory> returned = new ArrayList<>();
		for(double testDist = distIncr; testDist <= maxDist; testDist += distIncr) {
			Trajectory optTrajectory = this.getOuterTrajectory(testDist);
			if(optTrajectory.optimal > 0) {
				returned.add(optTrajectory);
			}
		}
		Trajectory[] format = new Trajectory[0];
		return returned.toArray(format);
	}
	
	public Trajectory getInnerTrajectory(double dist, double xAngle) {
		double optimal = 0.0;
		double targetXAngle = 0.0;
		int optTrajectoryNum = 0;
		for(int testTrajectoryNum = 0; testTrajectoryNum < allTrajectories.length; testTrajectoryNum++) {
			Trajectory traj = allTrajectories[testTrajectoryNum];
			double[] testPeak = traj.getPeak();
			double[] inner = getInner(dist, xAngle);
			double outerOffset = Math.pow(traj.getY(inner[3]) - portH, 2);
			outerOffset += Math.pow(inner[1], 2);
			outerOffset = Math.sqrt(outerOffset);
			if(outerOffset < outerR-ballR-1) {
				double innerOffset = Math.abs(traj.getY(inner[0]) - portH);
				if(innerOffset < innerR-ballR-1) {
					if(testPeak[0] > inner[3] && testPeak[0] < inner[0]) {
						if(Math.abs(testPeak[1]-portH) < outerR-ballR) {
							double innerOpt = (1-(innerOffset/(innerR-ballR))) * bestInner;
							double outerOpt = (1-(outerOffset/(outerR-ballR))) * bestOuter;
							double testOptimal = innerOpt+outerOpt;
							if(testOptimal > optimal) {
								targetXAngle = inner[2];
								optimal = testOptimal;
								optTrajectoryNum = testTrajectoryNum;
							}
						}
					}
					else {
						double innerOpt = (1-(innerOffset/(innerR-ballR))) * bestInner;
						double outerOpt = (1-(outerOffset/(outerR-ballR))) * bestOuter;
						double testOptimal = innerOpt+outerOpt;
						if(testOptimal > optimal) {
							targetXAngle = inner[2];
							optimal = testOptimal;
							optTrajectoryNum = testTrajectoryNum;
						}
					}
				}
			}
		}
		Trajectory targetTrajectory = allTrajectories[optTrajectoryNum].clone();
		targetTrajectory.setDetails(targetXAngle, dist, optimal);
		return targetTrajectory;
	}
	
	public Trajectory[] getInnerTrajectories(double distIncr, double maxDist, double xAngle) {
		ArrayList<Trajectory> returned = new ArrayList<>();
		for(double testDist = distIncr; testDist <= maxDist; testDist += distIncr) {
			Trajectory optTrajectory = this.getInnerTrajectory(testDist, xAngle);
			if(optTrajectory.optimal > 0) {
				returned.add(optTrajectory);
			}
		}
		Trajectory[] format = new Trajectory[0];
		return returned.toArray(format);
	}
	
	public Trajectory getMatch(double xCoord, double yCoord) {
		double optimal = Double.MAX_VALUE;
		int optTrajectoryNum = 0;
		for(int testTrajectoryNum = 0; testTrajectoryNum < allTrajectories.length; testTrajectoryNum++) {
			double testOptimal = Math.abs(allTrajectories[testTrajectoryNum].getY(xCoord) - yCoord);
			if(testOptimal < optimal) {
				optimal = testOptimal;
				optTrajectoryNum = testTrajectoryNum;
			}
		}
		return allTrajectories[optTrajectoryNum];
	}
	
	// translates initial velocity to motor power from 0 to 1, incompleted
	public static double getMotorPower(double initV) {
		return 1.0;
	}
	
	// subtracts a velocity for a moving shot and gives the new inital velocity and angle
	public static double[] subtractVector(double targetInitV, double targetXAngle, double removedInitV, double removedXAngle) {
		double xInitV = targetInitV * Math.sin(targetXAngle);
		double yInitV = targetInitV * Math.cos(targetXAngle);
		xInitV -= removedInitV * Math.sin(targetXAngle);
		yInitV -= removedInitV * Math.cos(targetXAngle);
		return new double[] {Math.sqrt(Math.pow(xInitV, 2) + Math.pow(yInitV, 2)), Math.atan(xInitV/yInitV)};
	}
	
	// finds information about the inner port from outer port information
	public static double[] getInner(double dist, double xAngle) {
		double a = dist*Math.cos(xAngle)+portD;
		double b = dist*Math.sin(xAngle);
		double innerDist = Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
		double targetXAngle = Math.atan(b/a);
		return new double[] {innerDist, b/a*portD, targetXAngle, innerDist - (portD/Math.cos(targetXAngle))};
	}
}