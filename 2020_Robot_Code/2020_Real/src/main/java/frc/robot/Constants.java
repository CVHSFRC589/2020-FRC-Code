/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants.  This class should not be used for any other purpose.  All constants should be
 * declared globally (i.e. public static).  Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    //Constants for each subsystem
    public static final class DriveConstants{
        public static final int kLeftMotorPort = 11; //11
        public static final int kRightMotorPort = 12; //12

        //No ports for the encoders because they're built in
        //public static final int kLeftCANEncoderPort = 0;
        //public static final int kRightCANEncoderPort = 1;
        public static final boolean kLeftCANEncoderReversed = false;
        public static final boolean kRightCANEncoderReversed = false; //Right probably has to be set to true

        //For every 10.7 turns of the motor shaft the gear turns 1
        //The encoder has 42 ticks per revolution
        //42*10.7 = 449.4 counts per revolution
        public static final double kWheelDiameterInches = 6; //wheel diameter
        public static final double gearRatio = 10.7;
        public static final int kEncoderCPR = 450;
        public static final double kEncoderIPR = kWheelDiameterInches*Math.PI; // 18.85 inches per revolution (circumference)
        public static final double kEncoderCPI = 0.583333333333; //idk why this even works
        
        public static final double maxAutoSpeed = 0.4;
    }

    public static final class ClimberConstants{
        public static final int[] kHookSolenoidPorts = new int[]{3, 4};

    }
    public static final class IntakeConstants{
        public static final int[] kIntakeSolenoidPorts = new int[]{6, 5};
        public static final int kIntakeMotorPort = 40;
        
        public static final double kIntakeMotorSpeed = 0.45;
    }
    public static final class ShooterConstants{
        public static final int kLoadingWheelMotorPort = 22;
        public static final int kMainWheelMotorPort = 21;
        public static final int kAzimuthMotorPort = 30; //30;

        public static final int kGateForwardChannel = 1;
        public static final int kGateReverseChannel = 2;


        public static final int leftLimitInputChannel = 1;
        public static final int rightLimitInputChannel = 2;

        public static double shootingSpeed = -1;  //-0.52, 0.62
        public static double loadingSpeed = 0.5; //0.4 //not final because gets made negative to de-jam balls
        public static final double azimuthSpeed = 0.6;
        public static final double azimuthSpeedAuto = 0.3;
        public static final double azimuthSpeedMax = 0.4;
    
        public static final int azimuthMaxCurrentLimit = 40; //in Amps 
        
        public static final double azimuthEncoderLimit = 30; //change to whatever it needs to be
    }
    public static final class LEDConstants{
        public static final int kLEDPort = 1;//needs to be changed at some point
    }
}
