package frc.robot.Egg.Utility.Math;

public class PID {
	public double kP, kI, kD, error, oldError = 0, Derror, Ierror, clamp;
	public double setpoint;
	
	public PID(double P, double I, double D, double Clamp) {
		kP = P;
		kI = I;
		kD = D;		
		clamp = Clamp;
	}
	
	public void reset() {
		error = 0;
		Derror = 0; 
		Ierror = 0;
		oldError = 0;
	}
	
	public double loop(double input) {
		error = setpoint - input;
		Derror = error - oldError;
		if (Math.abs(Ierror) < clamp) {
			Ierror += error;
		}
		
		oldError = error;
		
		return kP * error + kI * Ierror + kD * Derror;
		
		
	}
}
