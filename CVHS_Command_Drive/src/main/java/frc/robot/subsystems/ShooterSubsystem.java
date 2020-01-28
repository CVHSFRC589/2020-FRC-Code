/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.OIConstants;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;


public class ShooterSubsystem extends SubsystemBase {
  /**
   * Creates a new Shooter.
   */
  public static CANSparkMax m_azimuth;
  public static CANSparkMax m_elevation;
  public static CANSparkMax m_flywheel;

  public ShooterSubsystem() {
    m_azimuth = new CANSparkMax(OIConstants.kAzimuthMotor, MotorType.kBrushless);
    m_elevation = new CANSparkMax(OIConstants.kElevationMotor, MotorType.kBrushless);
    m_flywheel = new CANSparkMax(OIConstants.kShooterMotor, MotorType.kBrushless);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

  }

  public void setElevationSpeed(double speed){
    m_elevation.set(speed);
  }
  public void setAzimuthSpeed(double speed){
    m_azimuth.set(speed);
  }
  public void setFlywheelSpeed(double speed){
    m_flywheel.set(speed);
  }

  public double getAzimuthSpeed(){
    return m_azimuth.get();
  }
  public double getElevationSpeed(){
    return m_elevation.get();
  }
  public double getFlywheelSpeed(){
    return m_flywheel.get();
  }

}
