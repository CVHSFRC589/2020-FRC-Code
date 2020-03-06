package frc.robot.aiming;

import java.util.ArrayList;
import java.lang.Math;

import org.apache.commons.math3.fitting.*;

public class Trajectory {
	
	// WARNING: IMPERIAL UNITS (INCHES) & RADIAN INPUTS AND OUTPUTS ARE USED, CONVERT AT YOUR OWN RISK
	
	final public static double g = 386.0885827; //inches/second^2
	final public static double robotH = 24.0; //inches
	final public static double ballR = 3.5; //inches
	final public static double timeIncr = 0.01; //seconds
	final public static double density = 0.00004317211; //pounds/inches^3
	final public static double ballM = 0.308647; //pounds
	final public static double dragC = 0.55; //unitless
	final public static int defaultDegree = 4; //polynomial degree
	
	
	// constructs a trajectory containing points, the polynomial curve, and the initial velocity
	public double[][] points;
	public double[] curve;
	public double initV;
	public double yAngle;
	
	public double targetXAngle;
	public double dist;
	public double optimal;
	
	public Trajectory(double initV, double yAngle) {
		this.points = getPoints(initV, yAngle);
		this.curve = getCurve(points);
		this.initV = initV;
		this.yAngle = yAngle;
		
		this.targetXAngle = 0.0;
		this.dist = 0.0;
		this.optimal = 0.0;
	}
	
	public Trajectory(double[][] points, double[] curve, double initV, double yAngle) {
		this.points = points;
		this.curve = curve;
		this.initV = initV;
		this.yAngle = yAngle;
		
		this.targetXAngle = 0.0;
		this.dist = 0.0;
		this.optimal = 0.0;
	}
	
	// translates initial velocity to spin
	public static double getSpin(double initV) {
		return 0; //getY(initV, new double[] {2.2332*Math.pow(10, -8), -0.000043438, 0.0562369, 0.0});
	}
	
	// evaluates a flight path using incremental points of the ball including gravity, lift, and drag
	public static double[][] getPoints(double initV, double yAngle) {
		double spin = getSpin(initV);
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
		double k = (density*Math.PI*Math.pow(ballR, 2)) / (2*ballM);
		
		x.add(0.0);
		y.add(robotH);
		// t.add(0.0);
		v.add(initV);
		Vx.add(initV * Math.cos(yAngle));
		Vy.add(initV * Math.sin(yAngle));
		Cl.add(1/(2 + v.get(0)/(spin*2*Math.PI*ballR)));
		Cd.add(dragC + 1/Math.pow(22.5 + 4.2*Math.pow(v.get(0)/(spin*2*Math.PI*ballR), 2.5), 0.4));
		// Fl.add(Cl.get(0) * Math.pow(this.ballR, 2)*Math.PI*this.density * (Math.pow(v.get(0), 2)/2));
		// Fd.add(Cd.get(0) * Math.pow(this.ballR, 2)*Math.PI*this.density * (Math.pow(v.get(0), 2)/2));
		// angle.add(yAngle);
		
		int index = 1;
		while(y.get(index-1) >= 0 || index <= 10) {
			Vx.add(Vx.get(index-1) - (k*v.get(index-1)*(Cd.get(index-1)*Vx.get(index-1) + Cl.get(index-1)*Vy.get(index-1)))*timeIncr);
			Vy.add(Vy.get(index-1) + (k*v.get(index-1)*(Cl.get(index-1)*Vx.get(index-1) - Cd.get(index-1)*Vy.get(index-1))-g)*timeIncr);
			v.add(Math.sqrt(Math.pow(Vx.get(index), 2) + Math.pow(Vy.get(index), 2)));
			x.add(x.get(index-1) + (Vx.get(index-1) + Vx.get(index))/2*timeIncr);
			y.add(y.get(index-1) + (Vy.get(index-1) + Vy.get(index))/2*timeIncr);
			// t.add(t.get(index-1) + this.timeIncr);
			Cl.add(1/(2 + v.get(index)/(spin*2*Math.PI*ballR)));
			Cd.add(dragC + 1/Math.pow(22.5 + 4.2*Math.pow(v.get(index)/(spin*2*Math.PI*ballR), 2.5), 0.4));
			// Fl.add(Cl.get(index) * Math.pow(this.ballR, 2)*Math.PI*this.density * (Math.pow(v.get(index), 2)/2));
			// Fd.add(Cd.get(index) * Math.pow(this.ballR, 2)*Math.PI*this.density * (Math.pow(v.get(index), 2)/2));
			// angle.add(Math.atan(Vy.get(index)/Vx.get(index)));
			index++;
		}
		Double[] format = new Double[0];
		double[][] set = new double[][] {toPrimitive(x.toArray(format)), toPrimitive(y.toArray(format))};
		return set;
	}
	
	private static double[] toPrimitive(Double[] set) {
		double[] newSet = new double[set.length];
		for(int i = 0; i < set.length; i++) {
			newSet[i] = set[i];
		}
		return newSet;
	}
	
	// fits a polynomial equation to the set of points, only accurate from launch to touchdown
	public static double[] getCurve(double[][] coords) {
		ArrayList<WeightedObservedPoint> weightedPoints = new ArrayList<>();
		for(int i = 0; i < coords[0].length; i++) {
			weightedPoints.add(new WeightedObservedPoint(1, coords[0][i], coords[1][i]));
		}
		double[] fit = PolynomialCurveFitter.create(defaultDegree).fit(weightedPoints);
		return fit;
	}
	
	public Trajectory setDetails(double targetXAngle, double dist, double optimal) {
		this.targetXAngle = targetXAngle;
		this.dist = dist;
		this.optimal = optimal;
		return this;
	}
	
	// plugs an x value into the polynomial equation and returns a y-value
	public double getY(double x) {
		double answer = 0.0;
		for(int i = 0; i < this.curve.length; i++) {
			answer += this.curve[i] * Math.pow(x, i);
		}
		if(answer < 0) {
			return 0.0;
		}
		return answer;
	}
	
	// finds the peak of the set of points
	public double[] getPeak() {
		double peakD = this.points[0][0];
		double peakH = this.points[1][0];
		for(int i = 1; i < this.points[0].length; i++) {
			if(peakH < this.points[1][i]) {
				peakD = this.points[0][i];
				peakH = this.points[1][i];
			}
		}
		return new double[] {peakD, peakH};
	}
	
	public static double[] getPeak(double[][] points) {
		double peakD = points[0][0];
		double peakH = points[1][0];
		for(int i = 1; i < points[0].length; i++) {
			if(peakH < points[1][i]) {
				peakD = points[0][i];
				peakH = points[1][i];
			}
		}
		return new double[] {peakD, peakH};
	}
	
	// finds the angle of flight at a certain distance
	public double getFlightAngle(double x) {
		double incr = 0.05;
		double first = getY(x-incr/2);
		double second = getY(x+incr/2);
		if(first > 0 || second > 0) {
			return Math.atan((first-second)/0.05);
		}
		return 0.0;
	}
	
	public Trajectory clone() {
		return new Trajectory(points, curve, initV, yAngle).setDetails(targetXAngle, dist, optimal);
	}
}