/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

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
  public static CANSparkMax m_leftMotor = new CANSparkMax(DriveConstants.kLeftMotorPort, MotorType.kBrushless);
  public static CANSparkMax m_rightMotor = new CANSparkMax(DriveConstants.kRightMotorPort, MotorType.kBrushless);

  //Built in CAN Encoders 
  public static CANEncoder m_leftEncoder = new CANEncoder(m_leftMotor, EncoderType.kQuadrature, DriveConstants.kEncoderCPR);
  public static CANEncoder m_rightEncoder = new CANEncoder(m_rightMotor, EncoderType.kQuadrature, DriveConstants.kEncoderCPR);

  public DriveSubsystem() {
    m_leftMotor.set(0);
    m_rightMotor.set(0);

    //hopefully also reset encoders, but the Spark MAX API doesn't seem to have an encoder reset
  }

public void setMotors(double leftSpeed, double rightSpeed, double multiplier){
  //set speed from 0.25 to 1
  double z = 0.1;
  if(multiplier<0){
    z = (1-Math.abs(multiplier))*0.5+0.25;
  }
  else{
    z = multiplier*0.25+0.75;
  }
  m_leftMotor.set(leftSpeed*z); 
  m_rightMotor.set(rightSpeed*z);
}

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
