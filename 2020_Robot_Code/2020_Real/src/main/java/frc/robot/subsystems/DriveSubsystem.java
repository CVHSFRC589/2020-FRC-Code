/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.CANSparkMax;
import com.revrobotics.EncoderType;

import java.util.function.DoubleSupplier;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.robot.Constants.DriveConstants;
import frc.robot.Manager;
//import sun.awt.FwDispatcher;

public class DriveSubsystem extends SubsystemBase {
  /**
   * Creates a new DriveTestSubsystem.
   */
  private static final CANSparkMax m_leftMotor = new CANSparkMax(DriveConstants.kLeftMotorPort, MotorType.kBrushless);
  private static final CANSparkMax m_rightMotor = new CANSparkMax(DriveConstants.kRightMotorPort, MotorType.kBrushless);
  
  Manager leftManager = new Manager(m_leftMotor);
  Manager rightManager = new Manager(m_rightMotor);

  // private boolean m_driveForward = true;
  boolean m_driveForward = true;
  // private final CANEncoder leftEncoder = new CANEncoder(m_leftMotor, EncoderType.kHallSensor, DriveConstants.kEncoderCPR);
  // private final CANEncoder rightEncoder = new CANEncoder(m_rightMotor, EncoderType.kHallSensor, DriveConstants.kEncoderCPR);

  private final CANEncoder leftEncoder = m_leftMotor.getEncoder(EncoderType.kHallSensor, 42);
  private final CANEncoder rightEncoder = m_rightMotor.getEncoder(EncoderType.kHallSensor, 42);

  private final DifferentialDrive m_drive = new DifferentialDrive(m_leftMotor, m_rightMotor);

  public DriveSubsystem() {
    leftEncoder.setPosition(0);
    rightEncoder.setPosition(0);
   
    leftManager.initializePID();
    rightManager.initializePID();
  }

  /**
   * @param fwd
   * @param rot
   */
  public void arcadeDrive(double fwd, double rot){
    if(m_driveForward){
       m_drive.arcadeDrive(-fwd, rot);
      System.out.println("Forward drive");
    }
    if(!m_driveForward){
       m_drive.arcadeDrive(fwd, -rot);
    }
  }
  public void drivePID(){
    leftManager.setPIDSpeed(1);
    System.out.println("yes 1");
    rightManager.setPIDSpeed(1);
    System.out.println("yes 2");
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
    System.out.println("*********************" + "moving forward: " + m_driveForward + "*********************");
    setMotors(0, 0, 0);
  }

  public double getLeft(){
    return leftEncoder.getPosition();
  }
  public double getRight(){
    return rightEncoder.getPosition();
  }
  public void setLeft(int position){
    leftEncoder.setPosition(position);
  }
  public void setRight(int position){
    rightEncoder.setPosition(position);
  }

}
