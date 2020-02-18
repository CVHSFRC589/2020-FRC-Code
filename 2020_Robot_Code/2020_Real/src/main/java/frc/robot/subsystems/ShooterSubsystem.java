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

import java.util.function.DoubleSupplier;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.robot.Constants.ShooterConstants;
import frc.robot.ControlMode.StreamType;
import frc.robot.Motors.Manager;
import frc.robot.Motors;
import frc.robot.Constants.DriveConstants;


import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
//import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.ControlMode;
import frc.robot.LimeLight;

public class ShooterSubsystem extends SubsystemBase {
  /**
   * Creates a new ShooterSubsystem.
   */
  public static CANSparkMax m_loadingWheel = new CANSparkMax(ShooterConstants.kLoadingWheelMotorPort, MotorType.kBrushless); //loading wheel
  public static CANSparkMax m_shootingWheel = new CANSparkMax(ShooterConstants.kMainWheelMotorPort, MotorType.kBrushless); //shooting wheel
  public static CANSparkMax m_azimuthControl = new CANSparkMax(ShooterConstants.kAzimuthMotorPort, MotorType.kBrushless); //motor to control turret's horizontal motion

  public static CANEncoder m_loaderEncoder = new CANEncoder(m_loadingWheel, EncoderType.kHallSensor, DriveConstants.kEncoderCPR);
  public static CANEncoder m_shooterEncoder = new CANEncoder(m_shootingWheel, EncoderType.kHallSensor, DriveConstants.kEncoderCPR);
  public static CANEncoder m_azimuthEncoder = new CANEncoder(m_azimuthControl, EncoderType.kHallSensor, DriveConstants.kEncoderCPR);
  
  //Manager is used for PID control
  Manager m_Manager = new Manager();
  //Control Mode stuff
  ControlMode m_cameraController = new ControlMode();
  LimeLight m_limeLight = new LimeLight("limelight");
  private int m_cameraMode = 0; //0 -> targeting mode, 1 -> camera stream
  private StreamType m_streamMode = StreamType.kStandard; //0 -> standard (side-by-side), 1 -> Picture in Picture main, 2 -> Picture in picture secondary

  //Turret limit switches
  public DigitalInput m_leftLimit = new DigitalInput(ShooterConstants.leftLimitInputChannel);
  public DigitalInput m_rightLimit = new DigitalInput(ShooterConstants.rightLimitInputChannel);


  //Might have a solenoid to gatekeep power cells (between storage and shooting system)
  
  public NetworkTable table;
  public NetworkTableEntry tx;
  public NetworkTableEntry ty;
  public NetworkTableEntry ta;

  public ShooterSubsystem() {
    m_loadingWheel.set(0);
    m_shootingWheel.set(0);
    table = NetworkTableInstance.getDefault().getTable("limelight");
    tx = table.getEntry("tx");
    ty = table.getEntry("ty");
    ta = table.getEntry("ta");

    m_shootingWheel.restoreFactoryDefaults();
    m_loadingWheel.setSmartCurrentLimit(ShooterConstants.azimuthMaxCurrentLimit);

    Manager.initialize(m_shootingWheel);
    Manager.initialize(m_loadingWheel);
    Manager.initializePID(); //probably should initialize in robot init or something
  }

  public void updateLimeLightValues() {
    try{
      tx = table.getEntry("tx");
      //ty = table.getEntry("ty");
      //ta = table.getEntry("ta");      
    } catch(NullPointerException e){
      SmartDashboard.putString("Errors", "yep");
    }


    double xOffset = tx.getDouble(0.0);

  }

  public boolean getTargetFound(){
    return m_limeLight.getIsTargetFound();
  }
  public double getDegRotToTarget(){
    return m_limeLight.getdegRotationToTarget();
  }

  public void setLoadingMotor(double speed){
    Manager.setPIDSpeed(m_loadingWheel, speed);
    m_loadingWheel.set(speed);
  }
  public void setShootingMotor(double speed){
    m_shootingWheel.set(speed);
  }
  public void setAzimuthMotor(double azimuthSpeed){
    //DigitalInput returns false if limit switch was hit
    if((m_leftLimit.get() && (azimuthSpeed>0))||(m_rightLimit.get() &&(azimuthSpeed<0))){
      m_azimuthControl.set(azimuthSpeed);
    }
    else {
      m_azimuthControl.set(0);
    }
  }

  public void setAzimuthMotorJoystick(Double azimuthSpeed){
    //TEST LIMIT SWITCHES
    // if(m_leftLimit.get()){
    //   System.out.print("&& Left Limit HIT &&");
    // }
    // if(!m_leftLimit.get()){
    //   System.out.print("&& Left Limit HITN'T &&");
    // }

    // if(!m_rightLimit.get()){
    //   System.out.print("&& Right Limit FALSE &&");
    // }
    // if(m_rightLimit.get()){
    //   System.out.print("&& Right Limit TRUE &&");
    // }
    //System.out.println("LEFT: " + m_leftLimit.get());
    System.out.println("RIGHT: " + m_rightLimit.get());
     
    //If neither limit switch is hit, we're fine (set the motors to the desired speed)
    if(m_leftLimit.get() && m_rightLimit.get()){
      m_azimuthControl.set(azimuthSpeed);
    }
    //If the left limit switch was hit and we're moving right, we're fine
    //If the right limit switch was hit and we're moving left, we're fine
    //If both the above and not both switches are hit at the same time 
    else if(((!m_leftLimit.get() && (azimuthSpeed>0)) || (!m_rightLimit.get() &&(azimuthSpeed<0))) && ((m_leftLimit.get() || m_rightLimit.get()))){
      m_azimuthControl.set(azimuthSpeed);
    }
    //If a limit switch was hit and we're not moving toward the center, set the motor to 0
    else{
      //System.out.println("IM TRIGGGGGGGGGEREEDDDDDDDDDDDDDDDDD");
      m_azimuthControl.set(0);
    }
  }

  public boolean setAzimuthMotorAutomatic(double azimuthSpeed){
    boolean limitHit = false;
    if((m_leftLimit.get() && (azimuthSpeed>0)) || (m_rightLimit.get() && (azimuthSpeed<0))){
      m_azimuthControl.set(azimuthSpeed);
    }
    else {
      m_azimuthControl.set(0);
      limitHit=true;
    }
    return limitHit;
  }

  public void xOffsetCorrection(){
    
  }
  
  //TODO: make getter method for position of azimuth motor and speed of loading and shooting motors
  //also make one for the solenoid if we end up using it
  
  public void switchCameraMode(){
    if(m_cameraMode == 0){
      m_cameraMode = 1;
    }
    else{
      m_cameraMode = 0;
    }
  }

  public void changeStreamMode(){
    if(m_streamMode == StreamType.kStandard){
      m_streamMode = StreamType.kPiPMain;
    }
    else if(m_streamMode == StreamType.kPiPMain){
      m_streamMode = StreamType.kPiPSecondary;
    }
    else{
      m_streamMode = StreamType.kStandard;
    }
    m_limeLight.setStream(m_streamMode);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
