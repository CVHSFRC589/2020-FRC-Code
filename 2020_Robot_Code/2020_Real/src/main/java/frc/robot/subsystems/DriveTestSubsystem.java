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
//import sun.awt.FwDispatcher;

public class DriveTestSubsystem extends SubsystemBase {
  /**
   * Creates a new DriveTestSubsystem.
   */
  private static final CANSparkMax m_leftMotor = new CANSparkMax(DriveConstants.kLeftMotorPort, MotorType.kBrushless);
  private static final CANSparkMax m_rightMotor = new CANSparkMax(DriveConstants.kRightMotorPort, MotorType.kBrushless);



  private final DifferentialDrive m_drive = new DifferentialDrive(m_leftMotor, m_rightMotor);
  public DriveTestSubsystem() {
    
  }

  /**
   * @param fwd
   * @param rot
   */
  public void arcadeDrive(double fwd, double rot){
    m_drive.arcadeDrive(fwd, rot);
    //m_leftMotor.set(0.2);
    //m_rightMotor.set(0.2);
  }

  public void setMotors(){
    m_leftMotor.set(0.5);
    m_rightMotor.set(0.5);
  }
  
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  /**
   * @param maxOutput
   */
  public void setMaxOutput(double maxOutput){
    m_drive.setMaxOutput(maxOutput);
  }
}
