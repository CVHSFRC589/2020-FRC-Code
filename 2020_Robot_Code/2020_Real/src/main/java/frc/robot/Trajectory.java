package frc.robot;

import java.util.ArrayList;
import java.lang.Math;

import org.apache.commons.math3.fitting.*;

public class Trajectory {
	
	// WARNING: IMPERIAL UNITS (INCHES) & RADIANS ARE USED, CONVERT AT YOUR OWN RISK
	
	final public static Double g = 386.0885827; //inches/second^2
	final public static Double portH = 98.25; //inches
	final public static Double portD = 29.25; //inches
	final public static Double robotH = 24.0; //inches
	final public static Double ballR = 3.5; //inches
	final public static Double outerR = 15.0; //inches
	final public static Double innerR = 6.5; //inches
	final public static Double maxYAngle = Math.toRadians(45); //rad
	final public static Double minYAngle = Math.toRadians(0); //rad
	final public static Double vIncr = 0.0625; //inches/second
	final public static Double maxV = 1000.0; //inches/second
	final public static Double timeIncr = 0.01; //seconds
	final public static Double density = 0.000043714; //pounds/inches^3
	final public static Double ballM = 0.3086471671; //pounds
	final public static Double dragC = 0.55; //unitless
	final public static Double bestInner = 400.0; //unitless
	final public static Double bestOuter = 100.0; //unitless
	//dist is in inches
	//initV is in inches/second
	//xAngle is in rad
	//yAngle is in rad
	//spin is in revolutions/second
	//curve is array of polynomial factors
	
	public ArrayList<Double>[] points;
	public Double[] curve;
	public Double initV;
	public Trajectory(ArrayList<Double>[] points, Double[] curve, Double initV) {
		this.points = points;
		this.curve = curve;
		this.initV = initV;
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<Double[]>[] getOptimalInitVs(ArrayList<Trajectory> allTrajectories, Double distIncr, Double maxDist, Double xAngle, Double yAngle, int degree) {
		ArrayList<Double[]> optimalCurves = new ArrayList<>();
		ArrayList<Double[]> optimalInitVs = new ArrayList<>();
		Double initV;
		Double optimal;
		Double[] curve;
		Double outerPassed;
		Double innerPassed;
		Double[] testPeak;
		Double[] testCurve;
		Double outerOffset;
		Double innerOffset;
		Double innerOpt;
		Double outerOpt;
		Double[] inner;
		for(Double testDist = distIncr; testDist <= maxDist; testDist += distIncr) {
			initV = 0.0;
			optimal = 0.0;
			curve = new Double[degree+1];
			outerPassed = 0.0;
			innerPassed = 0.0;
			for(int testTrajectoryNum = 0; testTrajectoryNum < allTrajectories.size(); testTrajectoryNum++) {
				testCurve = allTrajectories.get(testTrajectoryNum).curve;
				outerOffset = Math.pow(getY(testDist, testCurve)-portH, 2);
				inner = getInner(testDist, xAngle);
				outerOffset += Math.pow(inner[1], 2);
				outerOffset = Math.sqrt(outerOffset);
				if(outerOffset < outerR-ballR-0.5) {
					outerPassed++;
					innerOffset = Math.abs(getY(inner[0], testCurve)-portH);
					if(innerOffset < innerR-ballR-1) {
						innerPassed++;
						testPeak = getPeak(allTrajectories.get(testTrajectoryNum).points);
						if(testPeak[0] > testDist && testPeak[0] < inner[0]) {
							if(Math.abs(testPeak[1]-portH) < outerR-ballR-1) {
								innerOpt = (1-(innerOffset/(innerR-ballR))) * bestInner;
								outerOpt = (1-(outerOffset/(outerR-ballR))) * bestOuter;
								if(innerOpt+outerOpt > optimal) {
									initV = allTrajectories.get(testTrajectoryNum).initV;
									optimal = innerOpt+outerOpt;
									curve = allTrajectories.get(testTrajectoryNum).curve;
								}
							}
						}
						else {
							innerOpt = (1-(innerOffset/(innerR-ballR))) * bestInner;
							outerOpt = (1-(outerOffset/(outerR-ballR))) * bestOuter;
							if(innerOpt+outerOpt > optimal) {
								initV = allTrajectories.get(testTrajectoryNum).initV;
								optimal = innerOpt+outerOpt;
								curve = allTrajectories.get(testTrajectoryNum).curve;
							}
						}
					}
					else {
						outerOpt = (1-(outerOffset/(outerR-ballR))) * bestInner;
						if(outerOpt > optimal) {
							initV = allTrajectories.get(testTrajectoryNum).initV;
							optimal = outerOpt;
							curve = allTrajectories.get(testTrajectoryNum).curve;
						}
					}
				}
			}
			optimalCurves.add(curve);
			optimalInitVs.add(new Double[] {testDist, initV, optimal, outerPassed, innerPassed});
		}
		ArrayList<Double[]>[] returned = new ArrayList[2];
		returned[0] = optimalInitVs;
		returned[1] = optimalCurves;
		return returned;
	}
	
	public static Double[][] getAngledInitV(ArrayList<Trajectory> allTrajectories, Double dist, Double xAngle, Double yAngle, int degree) {
		Double initV = 0.0;
		Double optimal = 0.0;
		Double outerPassed = 0.0;
		Double innerPassed = 0.0;
		Double[] curve = new Double[degree+1];
		for(int testTrajectoryNum = 0; testTrajectoryNum < maxV/vIncr; testTrajectoryNum++) {
			Double[] testPeak = getPeak(allTrajectories.get(testTrajectoryNum).points);
			Double outerOffset = Math.abs(getY(dist, allTrajectories.get(testTrajectoryNum).curve)-portH);
			if(outerOffset < (outerR-ballR)) {
				outerPassed++;
				Double innerOffset = Math.abs(getY(getInner(dist, xAngle)[0], allTrajectories.get(testTrajectoryNum).curve)-portH);
				if(innerOffset < (innerR-ballR)) {
					innerPassed++;
					if(testPeak[0] > dist && testPeak[0] < getInner(dist, xAngle)[0]) {
						if(testPeak[1] < (outerR-ballR)-1) {
							Double innerOpt = (1-(innerOffset/(innerR-ballR))) * bestInner;
							Double outerOpt = (1-(outerOffset/(outerR-ballR))) * bestOuter;
							Double testOptimal = innerOpt+outerOpt;
							if(testOptimal > optimal) {
								initV = allTrajectories.get(testTrajectoryNum).initV;
								optimal = testOptimal;
								curve = allTrajectories.get(testTrajectoryNum).curve;
							}
						}
					}
					else {
						Double innerOpt = (1-(innerOffset/(innerR-ballR))) * bestInner;
						Double outerOpt = (1-(outerOffset/(outerR-ballR))) * bestOuter;
						Double testOptimal = innerOpt+outerOpt;
						if(testOptimal > optimal) {
							initV = allTrajectories.get(testTrajectoryNum).initV;
							optimal = testOptimal;
							curve = allTrajectories.get(testTrajectoryNum).curve;
						}
					}
				}
				else {
					Double outerOpt = (1-(outerOffset/(outerR-ballR))) * bestInner;
					if(outerOpt > optimal) {
						initV = allTrajectories.get(testTrajectoryNum).initV;
						optimal = outerOpt;
						curve = allTrajectories.get(testTrajectoryNum).curve;
					}
				}
			}
		}
		Double[][] returned = new Double[2][4];
		returned[0] = new Double[] {initV, optimal, outerPassed, innerPassed};
		returned[1] = curve;
		return returned;
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<Double>[] getPoints(Double initV, Double yAngle, Double spin) {
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
		Double k = density*Math.PI*Math.pow(ballR, 2) / (2*ballM);
		
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
		while(y.get(index-1) >= 0) {
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
	
	public static Double[] getCurve(ArrayList<Double>[] coords, int degree) {
		ArrayList<WeightedObservedPoint> points = new ArrayList<>();
		for(int i = 0; i < coords[0].size(); i++) {
			points.add(new WeightedObservedPoint(1, coords[0].get(i), coords[1].get(i)));
		}
		Double[] fit = toObjectDouble(PolynomialCurveFitter.create(4).fit(points));
		return fit;
	}
	
	public static Double getY(Double x, Double[] curve) {
		Double answer = 0.0;
		for(int i = 0; i < curve.length; i++) {
			answer += curve[i] * Math.pow(x, i);
		}
		return answer;
	}
	
	public static Double[] getPeak(ArrayList<Double>[] coords) {
		Double peakD = 0.0;
		Double peakH = 0.0;
		for(int i = 0; i < coords[0].size(); i++) {
			if(peakH < coords[1].get(i)) {
				peakD = coords[0].get(i);
				peakH = coords[1].get(i);
			}
		}
		return new Double[] {peakD, peakH};
	}
	
	public static Double getSpin(Double initV) {
		return 5.0;
	}
	
	public static Double[] getInner(Double dist, Double xAngle) {
		if(xAngle == 0) {
			return new Double[] {dist+portD, 0.0};
		}
		Double a = dist*Math.cos(xAngle)+portD;
		Double b = dist*Math.sin(xAngle);
		Double innerDist = Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
		return new Double[] {innerDist, b/a*portD, Math.atan(b/a)};
	}
	
	public static Double[] toObjectDouble(double[] array) {
		Double[] objs = new Double[array.length];
		for(int i = 0; i < array.length; i++) {
			objs[i] = array[i];
		}
		return objs;
	}
	
	public static double[] toPrimitiveDouble(Double[] array) {
		double[] objs = new double[array.length];
		for(int i = 0; i < array.length; i++) {
			objs[i] = array[i];
		}
		return objs;
	}
	
	public static ArrayList<Double[]>[] getOptimalInitVs(Double xAngle, Double yAngle, Double distIncr, Double maxDist, int degree) {
		return getOptimalInitVs(getAllTrajectories(yAngle, degree), xAngle, yAngle, distIncr, maxDist, degree);
	}
	
	public static ArrayList<Trajectory> getAllTrajectories(Double yAngle, int degree) {
		ArrayList<Trajectory> allTrajectories = new ArrayList<>();
		for(Double testInitV = 0.0; testInitV <= maxV; testInitV += vIncr) {
			ArrayList<Double>[] testPoints = getPoints(testInitV, yAngle, getSpin(testInitV));
			if(getPeak(testPoints)[1] >= portH-outerR+ballR) {
				Double[] testCurve = getCurve(testPoints, degree);
				allTrajectories.add(new Trajectory(testPoints, testCurve, testInitV));
			}
		}
		return allTrajectories;
	}
	
	public static Trajectory getTrajectory(Double initV, Double yAngle, int degree) {
		ArrayList<Double>[] testPoints = getPoints(initV, yAngle, getSpin(initV));
		Double[] testCurve = getCurve(testPoints, degree);
		Trajectory trajectory = new Trajectory(testPoints, testCurve, initV);
		return trajectory;
	}
	
	public static Double[][] getAngledInitV(Double dist, Double xAngle, Double yAngle, int degree) {
		return getAngledInitV(getAllTrajectories(yAngle, degree), dist, xAngle, yAngle, degree);
	}
}
