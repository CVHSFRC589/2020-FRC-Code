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
        public static final int kLeftMotorPort = 0;
        public static final int kRightMotorPort = 1;

        //No ports for the encoders because they're built in
        //public static final int kLeftCANEncoderPort = 0;
        //public static final int kRightCANEncoderPort = 1;
        public static final boolean kLeftCANEncoderReversed = false;
        public static final boolean kRightCANEncoderReversed = false; //Right probably has to be set to true

        public static final int kEncoderCPR = 1024; //change for whatever it turns out to be
        public static final double kWheelDiameterInches = 6; //wheel diameter 
    }

    public static final class ClimberConstants{
        public static final int[] kHookSolenoidPorts = new int[]{0, 1};

    }
}
