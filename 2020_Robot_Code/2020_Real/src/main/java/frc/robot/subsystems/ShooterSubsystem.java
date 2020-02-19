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

import frc.robot.Constants.ShooterConstants;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
//import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ShooterSubsystem extends SubsystemBase {
  /**
   * Creates a new ShooterSubsystem.
   */
  public static CANSparkMax m_loadingWheel = new CANSparkMax(ShooterConstants.kLoadingWheelMotorPort, MotorType.kBrushless); //loading wheel
  public static CANSparkMax m_shootingWheel = new CANSparkMax(ShooterConstants.kMainWheelMotorPort, MotorType.kBrushless); //shooting wheel
  public static CANSparkMax m_azimuthControl = new CANSparkMax(ShooterConstants.kAzimuthMotorPort, MotorType.kBrushless); //motor to control turret's horizontal motion
  
  public static CANEncoder m_loaderEncoder = new CANEncoder(m_loadingWheel, EncoderType.kQuadrature, ShooterConstants.kEncoderCPR);
  public static CANEncoder m_shooterEncoder = new CANEncoder(m_shootingWheel, EncoderType.kQuadrature, ShooterConstants.kEncoderCPR);
  public static CANEncoder m_azimuthEncoder = new CANEncoder(m_azimuthControl, EncoderType.kQuadrature, ShooterConstants.kEncoderCPR);
  
  //Might have a solenoid to gatekeep power cells (between storage and shooting system)
  
  public NetworkTable table;
  public NetworkTableEntry tx;
  public double xOffset;
  //private double integral;
  //private double moveDistance;
  private  double azimuthSpeed;
  private double integral;

  public ShooterSubsystem() {
    m_loadingWheel.set(0);
    m_shootingWheel.set(0);
    table = NetworkTableInstance.getDefault().getTable("limelight");
    tx = table.getEntry("tx");


  }

  public void updateLimeLightValues() {
    try{
      tx = table.getEntry("tx");
    } catch(NullPointerException e){
      SmartDashboard.putString("Errors", "yep");
    }
    xOffset = tx.getDouble(0.0);
  }

  /*
  public void calculatePI()
   {
    double error = xOffset;
    integral += (error*.02);
  }
  */

  public void correctAzimuth() 
  {
    if((-.01 < xOffset)||(xOffset < .01))
    {
      azimuthSpeed = 0;
    } else {
      tx = table.getEntry("tx");
      xOffset = tx.getDouble(0.0);
      azimuthSpeed = xOffset / -20.500000;
        if ((xOffset > -1) &&(xOffset<1)) {
       azimuthSpeed = azimuthSpeed*4;
        }
     }
    m_azimuthControl.set(azimuthSpeed);
    }
    


  public void setLoadingMotor(double speed){
    m_loadingWheel.set(speed);
  }
  public void setShootingMotor(double speed){
    m_shootingWheel.set(speed);
  }
  public void setAzimuthMotor(double speed){
    m_azimuthControl.set(speed);
  }
  public double AzimuthxPos()
  {
    tx = table.getEntry("tx");
    xOffset = tx.getDouble(0.0);
    return xOffset;
  }
  
  public double getAzimuthSpeed()
  {return azimuthSpeed; }

  /*
  public void xOffsetCorrection()
  {
    
  }
  */
  
  //TODO: (seems to be done?) make getter method for position of azimuth motor and speed of loading and shooting motors
  //also make one for the solenoid if we end up using it
  
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
