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

import java.util.function.DoubleSupplier;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.robot.Constants.DriveConstants;
//import sun.awt.FwDispatcher;

public class DriveSubsystem extends SubsystemBase {
  /**
   * Creates a new DriveTestSubsystem.
   */
  private static final CANSparkMax m_leftMotor = new CANSparkMax(DriveConstants.kLeftMotorPort, MotorType.kBrushless);
  private static final CANSparkMax m_rightMotor = new CANSparkMax(DriveConstants.kRightMotorPort, MotorType.kBrushless);

  
  boolean m_driveForward = true;
  private final CANEncoder leftEncoder = new CANEncoder(m_leftMotor, EncoderType.kQuadrature, DriveConstants.kEncoderCPR);
  private final CANEncoder rightEncoder = new CANEncoder(m_rightMotor, EncoderType.kQuadrature, DriveConstants.kEncoderCPR);

  private final DifferentialDrive m_drive = new DifferentialDrive(m_leftMotor, m_rightMotor);

  public DriveSubsystem() {
    
  }

  /**
   * @param fwd
   * @param rot
   */
  public void arcadeDrive(double fwd, double rot){
    if(m_driveForward)
      m_drive.arcadeDrive(-fwd, rot);
    else
      m_drive.arcadeDrive(fwd, -rot);
    //m_leftMotor.set(0.2);
    //m_rightMotor.set(0.2);
  }

  public void tankDrive(double left, double right, double multiplier){
    if(m_driveForward)
      m_drive.tankDrive(left*multiplier, right*multiplier);
    else
      m_drive.tankDrive(-left*multiplier, -right*multiplier);
  }

  public void setMotors(double left, double right, double multiplier){
    m_leftMotor.set(left*multiplier);
    m_rightMotor.set(right*multiplier);
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

  public void switchDriveDirection(){
    m_driveForward = !m_driveForward;
  }
  public double getLeft(){
    return leftEncoder.getPosition();
  }
  public double getRight(){
    return rightEncoder.getPosition();
  }

}
