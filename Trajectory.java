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
	
	public static double getInnerDist(double dist, double xAngle) {
		if(xAngle == 0) {
			return dist+29.25;
		}
		// FIX THIS
		double innerDist = Math.pow(dist*Math.sin(xAngle)+29.25, 2);
		innerDist += Math.pow(dist*Math.cos(xAngle), 2);
		innerDist = Math.sqrt(innerDist);
		return innerDist;
	}
	
	public static double[] getOptimalPeak(double dist, double xAngle) {
		double[] coords = new double[2];
		double optimal = 0;
		for(double testH = portH; testH < portH+outerR-ballR; testH += 0.005) {
			for(double testD = dist; testD <= getInnerDist(dist, xAngle); testD += 0.005) {
				double testInitV = getInitV(testD, testH);
				double testYAngle = getYAngle(testD, testH, testInitV);
				double outerOffset = Math.abs(getY(dist, testYAngle, testInitV)-portH);
				if(outerOffset < (outerR-ballR)/2) {
					double innerOffset = Math.abs(getY(getInnerDist(dist, xAngle), testYAngle, testInitV)-portH);
					if(innerOffset < (innerR-ballR)/2) {
						double innerOpt = (1-(innerOffset/(innerR-ballR))) * 100;
						double outerOpt = (1-(outerOffset/(outerR-ballR))) * 100;
						double testOptimal = (innerOpt+outerOpt) / 2;
						if(testOptimal > optimal) {
							coords[0] = testD;
							coords[1] = testH;
							optimal = testOptimal;
						}
					}
				}
			}
		}
		return coords;
	}
	
	public static double[] getNormalPeak(double dist, double xAngle) {
		double[] coords = new double[2];
		double optimal = 0;
		double testD = dist + portD/2;
		for(double testH = portH; testH < portH+outerR-ballR; testH += 0.005) {
			double testInitV = getInitV(testD, testH);
			double testYAngle = getYAngle(testD, testH, testInitV);
			double outerOffset = Math.abs(getY(dist, testYAngle, testInitV)-portH);
			if(outerOffset < (outerR-ballR)/2) {
				double innerOffset = Math.abs(getY(getInnerDist(dist, xAngle), testYAngle, testInitV)-portH);
				if(innerOffset < (innerR-ballR)/2) {
					double innerOpt = (1-(innerOffset/(innerR-ballR))) * 100;
					double outerOpt = (1-(outerOffset/(outerR-ballR))) * 100;
					double testOptimal = (innerOpt+outerOpt) / 2;
					if(testOptimal > optimal) {
						coords[0] = testD;
						coords[1] = testH;
						optimal = testOptimal;
					}
				}
			}
		}
		return coords;
	}
	
	public static double getInnerXAngle(double dist, double xAngle) {
		return Math.asin((dist*Math.cos(xAngle)) / (getInnerDist(dist, xAngle)));
	}
	
	public static double getYAngle(double peakD, double peakH, double initV) {
		double theta = Math.PI/2;
		double acosed = (g*Math.pow(peakD, 2) / Math.pow(initV, 2)) + (peakH-robotH);
		acosed = acosed / (Math.sqrt(Math.pow(peakH-robotH, 2) + Math.pow(peakD, 2)));
		theta -= Math.acos(acosed % 1) / 2;
		theta -= Math.atan(peakD/(peakH-robotH)) / 2;
		theta += Math.floor(acosed) * Math.PI/4;
		return theta;
	}
	
	public static double getInitV(double peakD, double peakH) {
		double vSubO = 2*g*(peakH-robotH);
		vSubO += (g*Math.pow(peakD, 2)) / (2*(peakH-robotH));
		vSubO = Math.sqrt(vSubO);
		return vSubO;
	}
	
	public static double getY(double x, double yAngle, double initV) {
		double yPos = x*Math.tan(yAngle);
		yPos -= (g*Math.pow(x, 2)) / (2*Math.pow(initV, 2)*Math.pow(Math.cos(yAngle), 2));
		yPos += robotH;
		return yPos;
	}
}