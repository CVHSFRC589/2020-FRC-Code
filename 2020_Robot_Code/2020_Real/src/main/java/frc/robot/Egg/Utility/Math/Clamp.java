package frc.robot.Egg.Utility.Math;

public class Clamp {
	public static double clamp(double min, double max, double value) {
		if (value < min) {
			value = min;
		}
		if (value > max) {
			value = max;
		}
		
		return value;
	}
}
