/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.CANSparkMax;
import com.revrobotics.EncoderType;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.robot.Constants.DriveConstants;

public class DriveSubsystem extends SubsystemBase {
  /**
   * Creates a new DriveSubsystem.
   */
  public final CANSparkMax m_leftMotor = new CANSparkMax(DriveConstants.kLeftMotorPort, MotorType.kBrushless);
  public final CANSparkMax m_rightMotor = new CANSparkMax(DriveConstants.kRightMotorPort, MotorType.kBrushless);

  // //Built in CAN Encoders 
  // public static CANEncoder m_leftEncoder = new CANEncoder(m_leftMotor, EncoderType.kQuadrature, DriveConstants.kEncoderCPR);
  // public static CANEncoder m_rightEncoder = new CANEncoder(m_rightMotor, EncoderType.kQuadrature, DriveConstants.kEncoderCPR);

//JUST FOR TESTING
  private final DifferentialDrive mkz_drive = new DifferentialDrive(m_leftMotor, m_rightMotor);

  public DriveSubsystem() {
    //m_leftMotor.set(0);
    //m_rightMotor.set(0);

    //hopefully also reset encoders, but the Spark MAX API doesn't seem to have an encoder reset
  }

public void setMotors(double leftSpeed, double rightSpeed, double multiplier){
  System.out.print("mOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
   m_leftMotor.set(leftSpeed*multiplier); 
   m_rightMotor.set(rightSpeed*multiplier);
  //m_leftMotor.set(0.2); 
  //m_rightMotor.set(0.2);
 // mkz_drive.arcadeDrive(leftSpeed, rightSpeed);
}


//FOR TESTING
/**
 * @param fwd
 * @param rot
 */
public void arcadeDrive(double fwd, double rot){
  mkz_drive.arcadeDrive(fwd, rot);
}

}
