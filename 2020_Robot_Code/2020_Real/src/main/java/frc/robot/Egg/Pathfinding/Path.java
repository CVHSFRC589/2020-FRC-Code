package frc.robot.Egg.Pathfinding;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import frc.robot.Egg.Utility.*;
import frc.robot.Egg.Utility.Error;
import frc.robot.Egg.Utility.Math.*;

public class Path implements Serializable {
	
	//This class is the path class.  It represents one series of navPoints and the splines 
	//between them

	/**
	 * 
	 */
	private static final long serialVersionUID = -8694272386834623413L;

	
	public ArrayList<double[]> Xconstants = new ArrayList<double[]>();
	public ArrayList<double[]> Yconstants = new ArrayList<double[]>();
	public ArrayList<DoublePoint> navPoints = new ArrayList<DoublePoint>();
	public ArrayList<DoublePoint> travelPoints = new ArrayList<DoublePoint>();
	
	int length;
	
	public int f;
	
	public double resolution = 1000;
	
	public Path(ArrayList<DoublePoint> p) {
		navPoints = p;
	}
	
	public void calculate() {
		this.run();
		populate();
	}
	
	public DoublePoint getClosest(DoublePoint P) {
		double min = DoublePoint.getDistance(P, travelPoints.get(0));
		DoublePoint minPoint = travelPoints.get(0);

		
		
		return minPoint; 
	}
	
	public void populate() {
		for (double i = 0; i < resolution; i++) {
			travelPoints.add(get(i / resolution));
		}		
		return;
	}
	
	public ArrayList<DoublePoint> getArray() {
		return travelPoints;
	}
	
	public void run() {	
		
		//Paths are set up so they have the capability to be threaded.  The run method
		//is an override of Thread that runs the program on a new thread when the class.start()
		//method is called.  I have written a calculate() method so path.calculate() can be 
		//called.  Though calculating a path is not that resource intensive, many can be 
		//calculated at once using this
		
		length = navPoints.size() - 1;
		
		ArrayList<DoublePoint> Xlist = new ArrayList<DoublePoint>();
		ArrayList<DoublePoint> Ylist = new ArrayList<DoublePoint>();

		double ty = 0, tx = 0;
		
		for (int i = 0; i < navPoints.size(); i++) {
			try {
				tx += Math.abs(navPoints.get(i).x - navPoints.get(i - 1).x);
				ty += Math.abs(navPoints.get(i).y - navPoints.get(i - 1).y);
			} catch (Exception E) {
				
			}
			Xlist.add(new DoublePoint(i, navPoints.get(i).x));
			Ylist.add(new DoublePoint(i, navPoints.get(i).y));
		}
		
		Xconstants = CubicSpline.smoothCalculate(Xlist);
		Yconstants = CubicSpline.smoothCalculate(Ylist);	
		
		populate();
	}
	
	public void reverse() {
		ArrayList<DoublePoint> temp = new ArrayList<DoublePoint>();
		
		for (int i = navPoints.size() - 1; i >= 0; i--) {
			temp.add(navPoints.get(i));
		}
				
		navPoints = temp;
	}
	
	public DoublePoint get(double T) {
		
		//This chunk of code allows the point to be acquired on the path from 0-1, where
		//0 is the beginning and 1 is the end.
		
		if (T >= 1 || T <= 0) {
			//new Error("Cannot access parts of path outside of range", ErrorType.Fatal);
		}
		
		//System.out.println(length);
		
		T *= length;
		
		//System.out.println(T);
		
		int function = (int) Math.floor(T);
		
		if (T == length) {
			function--;
		}
		
		//System.out.println(function);
		
		f = function;
		
		return new DoublePoint((
				Xconstants.get(function)[0] * Math.pow(T, 3) + 
				Xconstants.get(function)[1] * Math.pow(T, 2) + 
				Xconstants.get(function)[2] * Math.pow(T, 1) +
				Xconstants.get(function)[3] * Math.pow(T, 0)
				),(
				Yconstants.get(function)[0] * Math.pow(T, 3) + 
				Yconstants.get(function)[1] * Math.pow(T, 2) + 
				Yconstants.get(function)[2] * Math.pow(T, 1) +
				Yconstants.get(function)[3] * Math.pow(T, 0)
				) );
	}	
	
	public double getD1Y(double T) {
		if (T > 1 || T < 0) {
			new Error("Cannot access parts of path outside of range", ErrorType.Fatal);
		}
		
		//System.out.println(length);
		
		T *= length;
		
		//System.out.println(T);
		
		int function = (int) Math.floor(T);
		
		if (T == length) {
			function--;
		}
		
		return ((
				Yconstants.get(function)[0] * Math.pow(T, 2) * 3 + 
				Yconstants.get(function)[1] * Math.pow(T, 1) * 2 + 
				Yconstants.get(function)[2] * Math.pow(T, 0) * 1 +
				Yconstants.get(function)[3] * Math.pow(T, 0) * 0
				));
	}
	
	//I'm lumping the massive methods D1 and D2 together.  They are the first and second
	//derivative respectively.
	
	
	public double getD1(double T) {
		if (T > 1 || T < 0) {
			new Error("Cannot access parts of path outside of range", ErrorType.Fatal);
		}
		
		//System.out.println(length);
		
		T *= length;
		
		//System.out.println(T);
		
		int function = (int) Math.floor(T);
		
		if (T == length) {
			function--;
		}
		
		return ((
				Yconstants.get(function)[0] * Math.pow(T, 2) * 3 + 
				Yconstants.get(function)[1] * Math.pow(T, 1) * 2 + 
				Yconstants.get(function)[2] * Math.pow(T, 0) * 1 +
				Yconstants.get(function)[3] * Math.pow(T, 0) * 0
				)/(
				Xconstants.get(function)[0] * Math.pow(T, 2) * 3 + 
				Xconstants.get(function)[1] * Math.pow(T, 1) * 2 + 
				Xconstants.get(function)[2] * Math.pow(T, 0) * 1 +
				Xconstants.get(function)[3] * Math.pow(T, 0) * 0
				) );
	
	}
	
	public double getD2(double T) {
		if (T > 1 || T < 0) {
			new Error("Cannot access parts of path outside of range", ErrorType.Fatal);
		}
		
		//System.out.println(length);
		
		T *= length;
		
		//System.out.println(T);
		
		int function = (int) Math.floor(T);
		
		if (T == length) {
			function--;
		}
		
		return (
				(
				(Xconstants.get(function)[0] * Math.pow(T, 2) * 3 + 
				 Xconstants.get(function)[1] * Math.pow(T, 1) * 2 + 
				 Xconstants.get(function)[2] * Math.pow(T, 0) * 1 +
				 Xconstants.get(function)[3] * Math.pow(T, 0) * 0)
				*
				(Yconstants.get(function)[0] * Math.pow(T, 1) * 6 + 
				 Yconstants.get(function)[1] * Math.pow(T, 0) * 2 + 
				 Yconstants.get(function)[2] * Math.pow(T, 0) * 0 +
				 Yconstants.get(function)[3] * Math.pow(T, 0) * 0)
				- 
				(Yconstants.get(function)[0] * Math.pow(T, 2) * 3 + 
				 Yconstants.get(function)[1] * Math.pow(T, 1) * 2 + 
				 Yconstants.get(function)[2] * Math.pow(T, 0) * 1 +
				 Yconstants.get(function)[3] * Math.pow(T, 0) * 0) 
				*
				(Xconstants.get(function)[0] * Math.pow(T, 1) * 6 + 
				 Xconstants.get(function)[1] * Math.pow(T, 0) * 2 + 
				 Xconstants.get(function)[2] * Math.pow(T, 0) * 0 +
				 Xconstants.get(function)[3] * Math.pow(T, 0) * 0)
				) 
				/ Math.pow(
				(Xconstants.get(function)[0] * Math.pow(T, 2) * 3 + 
				 Xconstants.get(function)[1] * Math.pow(T, 1) * 2 + 
				 Xconstants.get(function)[2] * Math.pow(T, 0) * 1 +
				 Xconstants.get(function)[3] * Math.pow(T, 0) * 0), 3)				
				);
	
	}
	
	
}
