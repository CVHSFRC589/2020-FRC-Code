package frc.robot.aiming;

import java.util.ArrayList;
import java.lang.Math;
import java.text.DecimalFormat;

import org.apache.commons.math3.fitting.*;

public class Trajectory {
	
	// WARNING: IMPERIAL UNITS (INCHES) & RADIAN INPUTS AND OUTPUTS ARE USED, CONVERT AT YOUR OWN RISK
	
	final public static double g = 386.0885827;
	final public static double robotH = 24.0;
	final public static double ballR = 3.5;
	final public static double timeIncr = 0.01;
	final public static double density = 0.00004317211;
	final public static double ballM = 0.308647;
	final public static double dragC = 0.55;
	final public static int defaultDegree = 4;
	
	
	// constructs a trajectory containing points, the polynomial curve, the initial velocity, and the angle from the horizontal
	public double[][] points;
	public double[] curve;
	public double initV;
	public double yAngle;
	
	public double targetXAngle;
	public double dist;
	public double optimal;
	
	public double maxDist;
	
	public Trajectory(double initV, double yAngle) {
		this.points = getPoints(initV, yAngle);
		this.curve = getCurve(points);
		this.initV = initV;
		this.yAngle = yAngle;
		
		this.targetXAngle = 0.0;
		this.dist = 0.0;
		this.optimal = 0.0;
		
		this.maxDist = this.points[0][this.points[0].length-1];
	}
	
	public Trajectory(double[][] points, double[] curve, double initV, double yAngle) {
		this.points = points;
		this.curve = curve;
		this.initV = initV;
		this.yAngle = yAngle;
		
		this.targetXAngle = 0.0;
		this.dist = 0.0;
		this.optimal = 0.0;
		
		this.maxDist = this.points[0][this.points[0].length-1];
	}
	
	// translates initial velocity to spin, calibrate further!
	public static double getSpin(double initV) {
		return getY(initV, new double[] {0, 0.0562369, -0.000043438, 2.2332*Math.pow(10, -8)});
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
		if(x > maxDist) {
			return 0.0;
		}
		double answer = 0.0;
		for(int i = 0; i < this.curve.length; i++) {
			answer += this.curve[i] * Math.pow(x, i);
		}
		if(answer < 0) {
			return 0.0;
		}
		return answer;
	}
	
	public static double getY(double x, double[] curve) {
		double answer = 0.0;
		for(int i = 0; i < curve.length; i++) {
			answer += curve[i] * Math.pow(x, i);
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
	
	public String getCurveEquation() {
		String equation = "";
		for(int i = curve.length-1; i >= 0; i--) {
			if(i == 0) {
				equation += removeEs(curve[i]);
			}
			else if (i == 1) {
				equation += removeEs(curve[i]) + "x + ";
			}
			else {
				equation += removeEs(curve[i]) + "x^" + (i) + " + ";
			}
		}
		return equation;
	}
	
	public static String getCurveEquation(double[] curve) {
		String equation = "";
		for(int i = curve.length-1; i >= 0; i--) {
			if(i == 0) {
				equation += removeEs(curve[i]);
			}
			else if (i == 1) {
				equation += removeEs(curve[i]) + "x + ";
			}
			else {
				equation += removeEs(curve[i]) + "x^" + (i) + " + ";
			}
		}
		return equation;
	}
	
	public static String removeEs(double num) {
		String oldNum = "" + num;
		String format = "0";
		int notation = 0;
		if(oldNum.indexOf("E") >= 0) {
			if(oldNum.indexOf(".") >= 0) {
				notation += oldNum.substring(oldNum.indexOf(".")+1, oldNum.indexOf("E")).length();
			}
			notation += Math.abs(Integer.parseInt(oldNum.substring(oldNum.indexOf("E")+1)));
		}
		else if(oldNum.indexOf(".") >= 0) {
			notation += oldNum.substring(oldNum.indexOf(".")+1).length();
		}
		
		if(notation > 0) {
			format += ".";
		}
		for(int i = 0; i < notation; i++) {
			format += "0";
		}
		DecimalFormat decimalFormat = new DecimalFormat(format);
		String newNum = decimalFormat.format(num);
		return newNum;
	}
	
	public Trajectory clone() {
		return new Trajectory(points, curve, initV, yAngle).setDetails(targetXAngle, dist, optimal);
	}
}