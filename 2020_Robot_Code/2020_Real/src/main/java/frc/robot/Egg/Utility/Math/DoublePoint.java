package frc.robot.Egg.Utility.Math;

import java.awt.Point;
import java.io.Serializable;

//One problem I have is the Point class.  While it is nice it doesn't work with non integer
//numbers, so I wrote this class to express points as a pair of doubles

public class DoublePoint implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 6401719774352885562L;
	public double x;
	public double y;
	
	public DoublePoint() {}
	
	public DoublePoint(double X, double Y) {
		
		x = X;
		y = Y;
	}
	
	public DoublePoint(Point P) {
		x = P.getX();
		y = P.getY();
	}
	
	public Double getX() {
		return x;		
	}
	
	public Double getY() {
		return y;		
	}
	
	@Override
	public String toString() {
		return "X=" + x + " Y=" + y;		
	}
	
	public Point getPoint() {		
		return (new Point((int)x, (int)y));
	}
	
	public boolean isNear(DoublePoint P, int range) {
		if (this.x - P.x < range && this.y - P.y < range) {
			return true;
		}
		return false;
	}
	
	public static DoublePoint divide(DoublePoint P, double no) {
		return new DoublePoint(P.x / no, P.y / no);
	}
	
	public static DoublePoint multiply(DoublePoint P, double no) {
		return new DoublePoint(P.x * no, P.y * no);
	}
	
	public static double getDistance(DoublePoint P1, DoublePoint P2) {
		return Math.sqrt(Math.pow(P1.x - P2.x, 2) + Math.pow(P1.y - P2.y, 2));
	}
	
	public static DoublePoint add(DoublePoint P1, DoublePoint P2) {
		return new DoublePoint(P1.x + P2.x, P1.y + P2.y);
	}
}
