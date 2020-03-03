package frc.robot;

import java.util.ArrayList;
import java.lang.Math;

import org.apache.commons.math3.fitting.*;

public class Trajectory {
	
	// WARNING: IMPERIAL UNITS (INCHES) & RADIAN INPUTS AND OUTPUTS ARE USED, CONVERT AT YOUR OWN RISK
	
	final public static Double g = 386.0885827; //inches/second^2
	final public static Double portH = 98.25; //inches
	final public static Double portD = 29.25; //inches
	final public static Double robotH = 24.0; //inches
	final public static Double ballR = 3.5; //inches
	final public static Double outerR = 15.0; //inches
	final public static Double innerR = 6.5; //inches
	final public static Double vIncr = 0.0625; //inches/second
	final public static Double maxV = 410.0; //inches/second
	final public static Double timeIncr = 0.01; //seconds
	final public static Double density = 0.00004317211; //pounds/inches^3
	final public static Double ballM = 0.308647; //pounds
	final public static Double dragC = 0.55; //unitless
	final public static Double bestInner = 400.0; //unitless
	final public static Double bestOuter = 100.0; //unitless
	final public static int defaultDegree = 4; //polynomial degree
	final public static Double defaultYAngle = Math.toRadians(45); //rad
	// allTrajectories is an arraylist of all tested trajectories
	// dist is in inches
	// distIncr is in inches
	// maxDist is in inches
	// initV is in inches/second
	// xAngle is in rad
	// yAngle is in rad
	// spin is in revolutions/second
	// curve is array of polynomial factors
	// degree is the polynomial degree of the regression
	
	// constructs a trajectory containing points, the polynomial curve, and the initial velocity
	public ArrayList<Double>[] points;
	public Double[] curve;
	public Double initV;
	
	public Trajectory(Double initV, Double yAngle, int degree) {
		this.points = getPoints(initV, yAngle);
		this.curve = getCurve(points, degree);
		this.initV = initV;
	}
	
	public Trajectory(ArrayList<Double>[] points, Double[] curve, Double initV) {
		this.points = points;
		this.curve = curve;
		this.initV = initV;
	}
	
	// translates initial velocity to spin
	public static Double getSpin(Double initV) {
		return getY(initV, new Double[] {2.2332*Math.pow(10, -8), -0.000043438, 0.0562369, 0.0});
	}
	
	// translates initial velocity and original angle to warped yAngle, incompleted
	public static Double getWarpedYAngle(Double initV, Double yAngle) {
		return yAngle;
	}
	
	// translates initial velocity to motor power from 0 to 1, incompleted
	public static Double getMotorPower(Double initV) {
		return 1.0;
	}
	
	// evaluates a flight path using incremental points of the ball including gravity, lift, and drag
	@SuppressWarnings("unchecked")
	private static ArrayList<Double>[] getPoints(Double initV, Double yAngle) {
		Double spin = getSpin(initV);
		ArrayList<Double> x = new ArrayList<>();
		ArrayList<Double> y = new ArrayList<>();
		// ArrayList<Double> t = new ArrayList<>();
		ArrayList<Double> v = new ArrayList<>();
		ArrayList<Double> Vx = new ArrayList<>();
		ArrayList<Double> Vy = new ArrayList<>();
		ArrayList<Double> Cl = new ArrayList<>();
		ArrayList<Double> Cd = new ArrayList<>();
		// ArrayList<Double> Fl = new ArrayList<>();
		// ArrayList<Double> Fd = new ArrayList<>();
		// ArrayList<Double> angle = new ArrayList<>();
		Double k = (density*Math.PI*Math.pow(ballR, 2)) / (2*ballM);
		
		x.add(0.0);
		y.add(robotH);
		// t.add(0.0);
		v.add(initV);
		Vx.add(initV * Math.cos(yAngle));
		Vy.add(initV * Math.sin(yAngle));
		Cl.add(1/(2 + v.get(0)/(spin*2*Math.PI*ballR)));
		Cd.add(dragC + 1/Math.pow(22.5 + 4.2*Math.pow(v.get(0)/(spin*2*Math.PI*ballR), 2.5), 0.4));
		// Fl.add(Cl.get(0) * Math.pow(ballR, 2)*Math.PI*density * (Math.pow(v.get(0), 2)/2));
		// Fd.add(Cd.get(0) * Math.pow(ballR, 2)*Math.PI*density * (Math.pow(v.get(0), 2)/2));
		// angle.add(yAngle);
		
		int index = 1;
		while(y.get(index-1) >= 0 || index <= 10) {
			Vx.add(Vx.get(index-1) - (k*v.get(index-1)*(Cd.get(index-1)*Vx.get(index-1) + Cl.get(index-1)*Vy.get(index-1)))*timeIncr);
			Vy.add(Vy.get(index-1) + (k*v.get(index-1)*(Cl.get(index-1)*Vx.get(index-1) - Cd.get(index-1)*Vy.get(index-1))-g)*timeIncr);
			v.add(Math.sqrt(Math.pow(Vx.get(index), 2) + Math.pow(Vy.get(index), 2)));
			x.add(x.get(index-1) + (Vx.get(index-1) + Vx.get(index))/2*timeIncr);
			y.add(y.get(index-1) + (Vy.get(index-1) + Vy.get(index))/2*timeIncr);
			// t.add(t.get(index-1) + timeIncr);
			Cl.add(1/(2 + v.get(index)/(spin*2*Math.PI*ballR)));
			Cd.add(dragC + 1/Math.pow(22.5 + 4.2*Math.pow(v.get(index)/(spin*2*Math.PI*ballR), 2.5), 0.4));
			// Fl.add(Cl.get(index) * Math.pow(ballR, 2)*Math.PI*density * (Math.pow(v.get(index), 2)/2));
			// Fd.add(Cd.get(index) * Math.pow(ballR, 2)*Math.PI*density * (Math.pow(v.get(index), 2)/2));
			// angle.add(Math.atan(Vy.get(index)/Vx.get(index)));
			index++;
		}
		
		ArrayList<Double>[] set = new ArrayList[2];
		set[0] = x;
		set[1] = y;
		return set;
	}
	
	// fits a polynomial equation to the set of points, only accurate from launch to touchdown
	private static Double[] getCurve(ArrayList<Double>[] coords, int degree) {
		ArrayList<WeightedObservedPoint> points = new ArrayList<>();
		for(int i = 0; i < coords[0].size(); i++) {
			points.add(new WeightedObservedPoint(1, coords[0].get(i), coords[1].get(i)));
		}
		Double[] fit = toObjectDouble(PolynomialCurveFitter.create(4).fit(points));
		return fit;
	}
	
	// returns arraylist of all trajectories at some angle over a range of initial velocities, only run once during initialization (takes a second or two)!
	public static ArrayList<Trajectory> getAllTrajectories(Double yAngle, int degree) {
		ArrayList<Trajectory> allTrajectories = new ArrayList<>();
		for(Double testInitV = 0.0; testInitV <= maxV; testInitV += vIncr) {
			ArrayList<Double>[] testPoints = getPoints(testInitV, getWarpedYAngle(testInitV, yAngle));
			if(getPeak(testPoints)[1] >= portH-outerR+ballR) {
				Double[] testCurve = getCurve(testPoints, degree);
				allTrajectories.add(new Trajectory(testPoints, testCurve, testInitV));
			}
		}
		return allTrajectories;
	}
	
	// tests all possible trajectories to find the optimal initial velocity at some distance and angle, takes ~0.0036245 sec on my laptop
	public static Double[][] getAngledInitV(ArrayList<Trajectory> allTrajectories, Double dist, Double xAngle) {
		Double initV = 0.0;
		Double optimal = 0.0;
		Double targetXAngle = 0.0;
		Double outerPassed = 0.0;
		Double innerPassed = 0.0;
		Double[] curve = new Double[allTrajectories.get(0).curve.length];
		for(int testTrajectoryNum = 0; testTrajectoryNum < allTrajectories.size(); testTrajectoryNum++) {
			Double[] testPeak = getPeak(allTrajectories.get(testTrajectoryNum).points);
			Double[] testCurve = allTrajectories.get(testTrajectoryNum).curve;
			Double[] inner = getInner(dist, xAngle);
			Double outerOffset = Math.pow(getY(inner[3], testCurve)-portH, 2);
			outerOffset += Math.pow(inner[1], 2);
			outerOffset = Math.sqrt(outerOffset);
			if(outerOffset < outerR-ballR-1) {
				outerPassed++;
				Double innerOffset = Math.abs(getY(inner[0], testCurve)-portH);
				if(innerOffset < innerR-ballR-1) {
					innerPassed++;
					if(testPeak[0] > inner[3] && testPeak[0] < inner[0]) {
						if(Math.abs(testPeak[1]-portH) < outerR-ballR) {
							Double innerOpt = (1-(innerOffset/(innerR-ballR))) * bestInner;
							Double outerOpt = (1-(outerOffset/(outerR-ballR))) * bestOuter;
							Double testOptimal = innerOpt+outerOpt;
							if(testOptimal > optimal) {
								initV = allTrajectories.get(testTrajectoryNum).initV;
								targetXAngle = inner[2];
								optimal = testOptimal;
								curve = testCurve;
							}
						}
					}
					else {
						Double innerOpt = (1-(innerOffset/(innerR-ballR))) * bestInner;
						Double outerOpt = (1-(outerOffset/(outerR-ballR))) * bestOuter;
						Double testOptimal = innerOpt+outerOpt;
						if(testOptimal > optimal) {
							initV = allTrajectories.get(testTrajectoryNum).initV;
							targetXAngle = inner[2];
							optimal = testOptimal;
							curve = testCurve;
						}
					}
				}
				else {
					Double outerOpt = (1-(outerOffset/(outerR-ballR))) * bestOuter;
					if(outerOpt > optimal) {
						initV = allTrajectories.get(testTrajectoryNum).initV;
						targetXAngle = inner[2];
						optimal = outerOpt;
						curve = testCurve;
					}
				}
			}
		}
		Double[][] returned = new Double[2][4];
		returned[0] = new Double[] {initV, targetXAngle, optimal, outerPassed, innerPassed};
		returned[1] = curve;
		return returned;
	}
	
	// uses getAngledInitV to find the optimal initial velocity at a range of distances
	public static ArrayList<Double[][]> getAngledInitVs(ArrayList<Trajectory> allTrajectories, Double distIncr, Double maxDist, Double xAngle) {
		ArrayList<Double[][]> returned = new ArrayList<>();
		for(Double testDist = distIncr; testDist <= maxDist; testDist += distIncr) {
			returned.add(new Double[2][]);
			Double[][] angledInitV = getAngledInitV(allTrajectories, testDist, xAngle);
			returned.get((int)(testDist/distIncr)-1)[1] = angledInitV[1];
			Double[] initV = new Double[angledInitV[0].length+1];
			initV[0] = testDist;
			for(int i = 0; i < angledInitV[0].length; i++) {
				initV[i+1] = angledInitV[0][i];
			}
			returned.get((int)(testDist/distIncr)-1)[0] = initV;
		}
		return returned;
	}
	
	// tests all possible trajectories to find the optimal initial velocity at some distance for an outer port shot, takes ~0.0034245 sec on my laptop
	public static Double[][] getOuterInitV(ArrayList<Trajectory> allTrajectories, Double dist) {
		Double initV = 0.0;
		Double optimal = 0.0;
		Double outerPassed = 0.0;
		Double[] curve = new Double[allTrajectories.get(0).curve.length];
		for(int testTrajectoryNum = 0; testTrajectoryNum < allTrajectories.size(); testTrajectoryNum++) {
			Double[] testCurve = allTrajectories.get(testTrajectoryNum).curve;
			Double outerOffset = Math.abs(getY(dist, testCurve)-portH);
			if(outerOffset < outerR-ballR-1) {
				outerPassed++;
				Double outerOpt = (1-(outerOffset/(outerR-ballR)));
				if(outerOpt > optimal) {
					initV = allTrajectories.get(testTrajectoryNum).initV;
					optimal = outerOpt;
					curve = testCurve;
				}
			}
		}
		Double[][] returned = new Double[2][4];
		returned[0] = new Double[] {initV, optimal, outerPassed};
		returned[1] = curve;
		return returned;
	}
	
	// uses getOuterInitV to find the optimal initial velocity at a range of distances for an outer port shot
	public static ArrayList<Double[][]> getOuterInitVs(ArrayList<Trajectory> allTrajectories, Double distIncr, Double maxDist) {
		ArrayList<Double[][]> returned = new ArrayList<>();
		for(Double testDist = distIncr; testDist <= maxDist; testDist += distIncr) {
			returned.add(new Double[2][]);
			Double[][] angledInitV = getOuterInitV(allTrajectories, testDist);
			returned.get((int)(testDist/distIncr)-1)[1] = angledInitV[1];
			Double[] initV = new Double[angledInitV[0].length+1];
			initV[0] = testDist;
			for(int i = 0; i < angledInitV[0].length; i++) {
				initV[i+1] = angledInitV[0][i];
			}
			returned.get((int)(testDist/distIncr)-1)[0] = initV;
		}
		return returned;
	}
	
	// subtracts a velocity for a moving shot and gives the new inital velocity and angle
	public static Double[] subtractVector(Double targetInitV, Double targetXAngle, Double removedInitV, Double removedXAngle) {
		Double xInitV = targetInitV * Math.sin(targetXAngle);
		Double yInitV = targetInitV * Math.cos(targetXAngle);
		xInitV -= removedInitV * Math.sin(targetXAngle);
		yInitV -= removedInitV * Math.cos(targetXAngle);
		return new Double[] {Math.sqrt(Math.pow(xInitV, 2) + Math.pow(yInitV, 2)), Math.atan(xInitV/yInitV)};
	}
	
	// plugs an x value into the polynomial equation and returns a y-value
	public static Double getY(Double x, Double[] curve) {
		Double answer = 0.0;
		for(int i = 0; i < curve.length; i++) {
			answer += curve[i] * Math.pow(x, i);
		}
		if(answer < 0) {
			return 0.0;
		}
		return answer;
	}
	
	// finds the peak of the set of points
	public static Double[] getPeak(ArrayList<Double>[] coords) {
		Double peakD = coords[0].get(0);
		Double peakH = coords[1].get(0);
		for(int i = 1; i < coords[0].size(); i++) {
			if(peakH < coords[1].get(i)) {
				peakD = coords[0].get(i);
				peakH = coords[1].get(i);
			}
		}
		return new Double[] {peakD, peakH};
	}
	
	// finds information about the inner port from outer port information
	public static Double[] getInner(Double dist, Double xAngle) {
		Double a = dist*Math.cos(xAngle)+portD;
		Double b = dist*Math.sin(xAngle);
		Double innerDist = Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
		Double targetXAngle = Math.atan(b/a);
		return new Double[] {innerDist, b/a*portD, targetXAngle, innerDist - (portD/Math.cos(targetXAngle))};
	}
	
	// runs algorithm backwards, turning a point on the trajectory to an initial velocity, do not use until algorithm has been confirmed!
	public static Double[] getMatch(ArrayList<Trajectory> allTrajectories, Double xCoord, Double yCoord) {
		Double initV = 0.0;
		Double optimal = Double.MAX_VALUE;
		for(int testTrajectoryNum = 0; testTrajectoryNum < allTrajectories.size(); testTrajectoryNum++) {
			Double testOptimal = Math.abs(getY(xCoord, allTrajectories.get(testTrajectoryNum).curve) - yCoord);
			if(testOptimal < optimal) {
				initV = allTrajectories.get(testTrajectoryNum).initV;
				optimal = testOptimal;
			}
		}
		return new Double[] {initV, optimal};
	}
	
	// finds the angle of flight at a certain distance
	public static Double getFlightAngle(Double x, Double[] curve) {
		Double incr = 0.05;
		Double first = getY(x-incr/2, curve);
		Double second = getY(x+incr/2, curve);
		if(first > 0 || second > 0) {
			return Math.atan((first-second)/0.05);
		}
		return 0.0;
	}
	
	// translates a primitive double array to an object double array
	public static Double[] toObjectDouble(double[] array) {
		Double[] objs = new Double[array.length];
		for(int i = 0; i < array.length; i++) {
			objs[i] = array[i];
		}
		return objs;
	}
	
	// and vice versa
	public static double[] toPrimitiveDouble(Double[] array) {
		double[] objs = new double[array.length];
		for(int i = 0; i < array.length; i++) {
			objs[i] = array[i];
		}
		return objs;
	}
	
	
	// simplified methods for actual use
	
	public static double sGetAngledInitV(ArrayList<Trajectory> allTrajectories, double dist, double xAngle) {
		return (double)(getAngledInitV(allTrajectories, dist, xAngle)[0][0]);
	}
	
	public static double[][] sGetAngledInitVs(ArrayList<Trajectory> allTrajectories, double distIncr, double maxDist, double xAngle) {
		ArrayList<Double[][]> complexOutput = getAngledInitVs(allTrajectories, distIncr, maxDist, xAngle);
		double[][] simpleOutput = new double[2][complexOutput.size()];
		for(int distNum = 0; distNum < complexOutput.size(); distNum++) {
			simpleOutput[0][distNum] = complexOutput.get(distNum)[0][0];
			simpleOutput[1][distNum] = complexOutput.get(distNum)[0][1];
		}
		return simpleOutput;
	}
	
	public static double sGetOuterInitV(ArrayList<Trajectory> allTrajectories, double dist) {
		return (double)(getOuterInitV(allTrajectories, dist)[0][0]);
	}
	
	public static double[][] sGetOuterInitVs(ArrayList<Trajectory> allTrajectories, double distIncr, double maxDist) {
		ArrayList<Double[][]> complexOutput = getOuterInitVs(allTrajectories, distIncr, maxDist);
		double[][] simpleOutput = new double[2][complexOutput.size()];
		for(int distNum = 0; distNum < complexOutput.size(); distNum++) {
			simpleOutput[0][distNum] = complexOutput.get(distNum)[0][0];
			simpleOutput[1][distNum] = complexOutput.get(distNum)[0][1];
		}
		return simpleOutput;
	}
	
	public static double[] sSubtractVector(double targetInitV, double targetXAngle, double removedInitV, double removedXAngle) {
		return toPrimitiveDouble(subtractVector(targetInitV, targetXAngle, removedInitV, removedXAngle));
	}
	
	public static ArrayList<Double>[] sGetPoints(double initV) {
		return getPoints(initV, defaultYAngle);
	}
	
	public static double[] sGetCurve(ArrayList<Double>[] coords) {
		return toPrimitiveDouble(getCurve(coords, defaultDegree));
	}
	
	public static double sGetY(double x, double[] curve) {
		return (double)(getY(x, toObjectDouble(curve)));
	}
	
	public static double[] sGetPeak(ArrayList<Double>[] coords) {
		return toPrimitiveDouble(getPeak(coords));
	}
	
	public static double sGetSpin(double initV) {
		return (double)(getSpin(initV));
	}
	
	public static double[] sGetInner(double dist, double xAngle) {
		return toPrimitiveDouble(getInner(dist, xAngle));
	}
	
	public static ArrayList<Trajectory> sGetAllTrajectories() {
		return getAllTrajectories(defaultYAngle, defaultDegree);
	}
	
	public static double[] sGetMatch(ArrayList<Trajectory> allTrajectories, double xCoord, double yCoord) {
		return toPrimitiveDouble(getMatch(allTrajectories, xCoord, yCoord));
	}
}
