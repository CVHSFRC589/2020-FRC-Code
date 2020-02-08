import java.util.ArrayList;
import java.math.*;

import org.apache.commons.math3.fitting.*;

public class Trajectory {
	
	// WARNING: IMPERIAL UNITS (INCHES) & RADIANS ARE USED, CONVERT AT YOUR OWN RISK
	
	public static double g = 386.0885827; //inches/second^2
	public static double portH = 98.25; //inches
	public static double portD = 29.25; //inches
	public static double robotH = 24; //inches
	public static double ballR = 3.5; //inches
	public static double outerR = 15; //inches
	public static double innerR = 6.5; //inches
	public static double maxYAngle = Math.toRadians(45); //rad
	public static double minYAngle = Math.toRadians(0); //rad
	public static double vIncr = 0.0625; //inches/second
	public static double maxV = 352; //inches/second
	public static double angleIncr = 0.01; //degrees
	public static double timeIncr = 0.01; //seconds
	public static double density = 0.000043714; //pounds/inches^3
	public static double ballM = 0.3086471671; //pounds
	public static double dragC = 0.55; //unitless
	//dist is in inches
	//initV is in inches/second
	//xAngle is in rad
	//yAngle is in rad
	//spin is in revolutions/second
	//curve is array of polynomial factors
	
	public static ArrayList<Double>[] getPoints(double initV, double yAngle, double spin) {
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
		double k = density*Math.PI*Math.pow(ballR, 2) / (2*ballM);
		
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
	
	public static double[] getCurve(ArrayList<Double>[] coords, int degree) {
		ArrayList<WeightedObservedPoint> points = new ArrayList<>();
		for(int i = 0; i < coords[0].size(); i++) {
			points.add(new WeightedObservedPoint(1, coords[0].get(i), coords[1].get(i)));
		}
		double[] fit = PolynomialCurveFitter.create(4).fit(points);
		return fit;
	}
	
	public static double getY(double x, double[] curve) {
		double answer = 0;
		for(int i = 0; i < curve.length; i++) {
			answer += curve[i] * Math.pow(x, i);
		}
		return answer;
	}
	
	public static double[] getPeak(ArrayList<Double>[] coords) {
		double peakD = 0;
		double peakH = 0;
		for(int i = 0; i < coords[0].size(); i++) {
			if(peakH < coords[1].get(i)) {
				peakD = coords[0].get(i);
				peakH = coords[1].get(i);
			}
		}
		return new double[] {peakD, peakH};
	}
	
	public static double getSpin(double initV) {
		return 5;
	}
	
	public static double[] getInner(double dist, double xAngle) {
		if(xAngle == 0) {
			return new double[] {dist+portD, 0};
		}
		double a = dist*Math.cos(xAngle)+portD;
		double b = dist*Math.sin(xAngle);
		double innerDist = Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
		return new double[] {innerDist, b/a*portD, Math.atan(b/a)};
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
	
	public static ArrayList<Double[]>[] getOptimalInitVs(double xAngle, double yAngle, double distIncr, double maxDist, int degree) {
		ArrayList<ArrayList<Double>[]> allPoints = new ArrayList<>();
		ArrayList<Double[]> allCurves = new ArrayList<>();
		ArrayList<Double> allInitVs = new ArrayList<>();
		ArrayList<Double>[] testPoints;
		for(double testInitV = 0; testInitV <= maxV; testInitV += vIncr) {
			testPoints = getPoints(testInitV, yAngle, getSpin(testInitV));
			if(getPeak(testPoints)[1] >= portH-outerR+ballR) {
				allPoints.add(testPoints);
				allCurves.add(toObjectDouble(getCurve(allPoints.get(allPoints.size()-1), degree)));
				allInitVs.add(testInitV);
			}
		}
		
		ArrayList<Double[]> optimalCurves = new ArrayList<>();
		ArrayList<Double[]> optimalInitVs = new ArrayList<>();
		double initV;
		double optimal;
		Double[] curve;
		double outerPassed;
		double innerPassed;
		double[] testPeak;
		double[] testCurve;
		double outerOffset;
		double innerOffset;
		double innerOpt;
		double outerOpt;
		double[] inner;
		for(double testDist = distIncr; testDist <= maxDist; testDist += distIncr) {
			initV = 0;
			optimal = 0;
			curve = new Double[degree+1];
			outerPassed = 0;
			innerPassed = 0;
			for(int testCurveNum = 0; testCurveNum < allCurves.size(); testCurveNum++) {
				testCurve = toPrimitiveDouble(allCurves.get(testCurveNum));
				outerOffset = Math.pow(getY(testDist, testCurve)-portH, 2);
				inner = getInner(testDist, xAngle);
				outerOffset += Math.pow(inner[1], 2);
				outerOffset = Math.sqrt(outerOffset);
				if(outerOffset < outerR-ballR-0.5) {
					outerPassed++;
					innerOffset = Math.abs(getY(inner[0], testCurve)-portH);
					if(innerOffset < innerR-ballR-1) {
						innerPassed++;
						testPeak = getPeak(allPoints.get(testCurveNum));
						if(testPeak[0] > testDist && testPeak[0] < inner[0]) {
							if(Math.abs(testPeak[1]-portH) < outerR-ballR-1) {
								innerOpt = (1-(innerOffset/(innerR-ballR))) * 400;
								outerOpt = (1-(outerOffset/(outerR-ballR))) * 100;
								if(innerOpt+outerOpt > optimal) {
									initV = allInitVs.get(testCurveNum);
									optimal = innerOpt+outerOpt;
									curve = allCurves.get(testCurveNum);
								}
							}
						}
						else {
							innerOpt = (1-(innerOffset/(innerR-ballR))) * 400;
							outerOpt = (1-(outerOffset/(outerR-ballR))) * 100;
							if(innerOpt+outerOpt > optimal) {
								initV = allInitVs.get(testCurveNum);
								optimal = innerOpt+outerOpt;
								curve = allCurves.get(testCurveNum);
							}
						}
					}
					else {
						outerOpt = (1-(outerOffset/(outerR-ballR))) * 100;
						if(outerOpt > optimal) {
							initV = allInitVs.get(testCurveNum);
							optimal = outerOpt;
							curve = allCurves.get(testCurveNum);
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
	
	public static double[] getAngledInitV(double dist, double yAngle, double xAngle) {
		double initV = 0;
		double optimal = 0;
		int heightPassed = 0;
		int outerPassed = 0;
		int innerPassed = 0;
		for(int initVIncr = 0; initVIncr < maxV/vIncr; initVIncr++) {
			double testInitV = ((int)(vIncr*initVIncr*(1/vIncr)))/(1/vIncr);
			ArrayList<Double>[] testPoints = getPoints(testInitV, yAngle, getSpin(initV));
			double[] testPeak = getPeak(testPoints);
			if(testPeak[1] >= portH) {
				heightPassed++;
				double[] testCurve = getCurve(testPoints, 4);
				double outerOffset = Math.abs(getY(dist, testCurve)-portH);
				if(outerOffset < (outerR-ballR)) {
					outerPassed++;
					double innerOffset = Math.abs(getY(getInner(dist, xAngle)[0], testCurve)-portH);
					if(innerOffset < (innerR-ballR)) {
						innerPassed++;
						if(testPeak[0] > dist && testPeak[0] < getInner(dist, xAngle)[0]) {
							if(testPeak[1] < (outerR-ballR)-1) {
								double innerOpt = (1-(innerOffset/(innerR-ballR))) * 200;
								double outerOpt = (1-(outerOffset/(outerR-ballR))) * 100;
								double testOptimal = innerOpt+outerOpt;
								if(testOptimal > optimal) {
									initV = testInitV;
									optimal = testOptimal;
								}
							}
						}
						else {
							double innerOpt = (1-(innerOffset/(innerR-ballR))) * 200;
							double outerOpt = (1-(outerOffset/(outerR-ballR))) * 100;
							double testOptimal = innerOpt+outerOpt;
							if(testOptimal > optimal) {
								initV = testInitV;
								optimal = testOptimal;
							}
						}
						
					}
				}
			}
		}
		return new double[] {initV, optimal, heightPassed, outerPassed, innerPassed};
	}
}