import java.util.ArrayList;
import java.math.*;

public class Trajectory {
	
	// WARNING: IMPERIAL UNITS (INCHES) & RADIANS ARE USED, CONVERT AT YOUR OWN RISK
	
	public static double g = 386.0885827;
	public static double portH = 98.25;
	public static double portD = 29.25;
	public static double robotH = 24;
	public static double ballR = 3.5;
	public static double outerR = 15;
	public static double innerR = 6.5;
	public static double maxYAngle = Math.toRadians(90);
	public static double minYAngle = Math.toRadians(0);
	public static double incr = 0.01;
	
	public static double getInnerDist(double dist, double xAngle) {
		if(xAngle == 0) {
			return dist+portD;
		}
		// FIX THIS
		double innerDist = Math.pow(dist*Math.sin(xAngle)+29.25, 2);
		innerDist += Math.pow(dist*Math.cos(xAngle), 2);
		innerDist = Math.sqrt(innerDist);
		return innerDist;
	}
	
	// MINIMUM DISTANCE IS ~18 INCHES, NO MAXIMUM
	public static double[] getOptimalVals(double dist, double xAngle) {
		double[] values = new double[2];
		double optimal = 0;
		for(int initVIncr = 0; initVIncr < 300/incr; initVIncr++) {
			double testInitV = 150 + incr*initVIncr;
			for(int yAngleIncr = 0; yAngleIncr <= Math.toDegrees(maxYAngle - minYAngle)/incr; yAngleIncr++) {
				double testYAngle = minYAngle + Math.toRadians(yAngleIncr*incr);
				double outerOffset = Math.abs(getY(dist, testYAngle, testInitV)-portH);
				if(outerOffset < (outerR-ballR)-1) {
					double innerOffset = Math.abs(getY(getInnerDist(dist, xAngle), testYAngle, testInitV)-portH);
					if(innerOffset < (innerR-ballR)-1) {
						double peakD = getPeak(testYAngle, testInitV);
						if(peakD > dist && peakD < getInnerDist(dist, xAngle)) {
							if(getY(peakD, testYAngle, testInitV) < (portH+outerR-ballR)-1) {
								double innerOpt = (1-(innerOffset/(innerR-ballR))) * 200;
								double outerOpt = (1-(outerOffset/(outerR-ballR))) * 100;
								double testOptimal = innerOpt+outerOpt;
								if(testOptimal > optimal) {
									values[0] = testInitV;
									values[1] = testYAngle;
									optimal = testOptimal;
								}
							}
						}
					}
					else {
						double innerOpt = (1-(innerOffset/(innerR-ballR))) * 200;
						double outerOpt = (1-(outerOffset/(outerR-ballR))) * 100;
						double testOptimal = innerOpt+outerOpt;
						if(testOptimal > optimal) {
							values[0] = testInitV;
							values[1] = testYAngle;
							optimal = testOptimal;
						}
					}
					
				}
			}
		}
		return values;
	}
	
	// AT 45 DEGREES, RANGE FROM ~7.5 TO ~19.5 FEET
	public static double getAngledInitV(double dist, double yAngle, double xAngle) {
		double initV = 0;
		double optimal = 0;
		for(int initVIncr = 0; initVIncr < 1000/incr; initVIncr++) {
			double testInitV = ((int)(incr*initVIncr*(1/incr)))/(1/incr);
			double outerOffset = Math.abs(getY(dist, yAngle, testInitV)-portH);
			if(outerOffset < (outerR-ballR)-1) {
				double innerOffset = Math.abs(getY(getInnerDist(dist, xAngle), yAngle, testInitV)-portH);
				if(innerOffset < (innerR-ballR)-1) {
					double peakD = getPeak(yAngle, testInitV);
					if(peakD > dist && peakD < getInnerDist(dist, xAngle)) {
						if(getY(peakD, yAngle, testInitV) < (outerR-ballR)-1) {
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
		return initV;
	}
	
	public static double getPeak(double yAngle, double initV) {
		double peakD = Math.pow(initV, 2) * Math.pow(Math.cos(yAngle), 2) * Math.tan(yAngle);
		peakD /= g;
		return peakD;
	}
	
	public static double getInnerXAngle(double dist, double xAngle) {
		return Math.asin((dist*Math.cos(xAngle)) / (getInnerDist(dist, xAngle)));
	}
	
	public static double getY(double x, double yAngle, double initV) {
		double yPos = x*Math.tan(yAngle);
		yPos -= (g*Math.pow(x, 2)) / (2*Math.pow(initV, 2)*Math.pow(Math.cos(yAngle), 2));
		yPos += robotH;
		return yPos;
	}
}
