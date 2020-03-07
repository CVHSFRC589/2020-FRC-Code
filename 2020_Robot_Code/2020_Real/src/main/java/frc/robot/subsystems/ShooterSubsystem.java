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
import com.revrobotics.CANSparkMax.IdleMode;

import java.util.function.DoubleSupplier;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.robot.Constants.ShooterConstants;
import frc.robot.ControlMode.LedMode;
import frc.robot.ControlMode.StreamType;
import frc.robot.Manager;
import frc.robot.Constants.DriveConstants;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
//import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.ControlMode;
import frc.robot.LimeLight;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import static edu.wpi.first.wpilibj.DoubleSolenoid.Value.kForward;
import static edu.wpi.first.wpilibj.DoubleSolenoid.Value.kReverse;

public class ShooterSubsystem extends SubsystemBase {
  /**
   * Creates a new ShooterSubsystem.
   */
  public static CANSparkMax m_loadingWheel = new CANSparkMax(ShooterConstants.kLoadingWheelMotorPort,
      MotorType.kBrushless); // loading wheel
  public static CANSparkMax m_shootingWheel = new CANSparkMax(ShooterConstants.kMainWheelMotorPort,
      MotorType.kBrushless); // shooting wheel
  public static CANSparkMax m_azimuthControl = new CANSparkMax(ShooterConstants.kAzimuthMotorPort,
      MotorType.kBrushless); // motor to control turret's horizontal motion

  public static CANEncoder m_loaderEncoder = new CANEncoder(m_loadingWheel, EncoderType.kHallSensor,
      DriveConstants.kEncoderCPR);
  public static CANEncoder m_shooterEncoder = new CANEncoder(m_shootingWheel, EncoderType.kHallSensor,
      DriveConstants.kEncoderCPR);
  public static CANEncoder m_azimuthEncoder = new CANEncoder(m_azimuthControl, EncoderType.kHallSensor,
      DriveConstants.kEncoderCPR);

  public static DoubleSolenoid m_gateSolenoid = new DoubleSolenoid(ShooterConstants.kGateForwardChannel,
      ShooterConstants.kGateReverseChannel);

  // Manager is used for PID control
  Manager m_loaderPID = new Manager(m_loadingWheel);
  Manager m_shooterPID = new Manager(m_shootingWheel);

  // Control Mode stuff
  ControlMode m_cameraController = new ControlMode();
  LimeLight m_limeLight = new LimeLight("limelight");
  LimelightSubsystem m_limey = new LimelightSubsystem();

  public static boolean shootMode = false;// false = auto, true = manual

  private int m_cameraMode = 0; // 0 -> targeting mode, 1 -> camera stream

  private StreamType m_streamMode = StreamType.kStandard; // 0 -> standard (side-by-side), 1 -> Picture in Picture main,
                                                          // 2 -> Picture in picture secondary

  // Turret limit switches
  public DigitalInput m_leftLimit = new DigitalInput(ShooterConstants.leftLimitInputChannel);
  public DigitalInput m_rightLimit = new DigitalInput(ShooterConstants.rightLimitInputChannel);

  public static boolean shootingWheelRunning = false;
  public double initialAzimuthEncoderValue = 0;

  public boolean limitLeft = false;
  public boolean limitRight = false;

  public static boolean on = false; // used in AutomaticAiming

  private AHRS Navx = new AHRS(SPI.Port.kMXP);

  // Might have a solenoid to gatekeep power cells (between storage and shooting
  // system)

  public NetworkTable table;
  public NetworkTableEntry tx;

  public ShooterSubsystem() {
    m_loadingWheel.set(0);
    m_shootingWheel.set(0);

    m_loadingWheel.setIdleMode(IdleMode.kBrake);
    m_azimuthControl.setIdleMode(IdleMode.kCoast);

    m_shootingWheel.restoreFactoryDefaults();
    m_loadingWheel.setSmartCurrentLimit(ShooterConstants.azimuthMaxCurrentLimit);

    m_shooterPID.initializePID(ShooterConstants.shootingSpeed);
    m_loaderPID.initializePID(ShooterConstants.loadingSpeed);

    m_gateSolenoid.set(kForward);

    // 1 IS OFF, 3 FORCES ON BUT USE 0 TO KEEP CURRENT PIPELINE SETTINGS
    m_limey.getNetworkTable().getEntry("ledMode").setNumber(0);

    m_azimuthEncoder.setPosition(0);
    initialAzimuthEncoderValue = m_azimuthEncoder.getPosition();

    m_azimuthControl.setIdleMode(IdleMode.kCoast);
  }

  public void shootPID() {
    m_shooterPID.setPIDSpeed(1);
    m_loaderPID.setPIDSpeed(1);
  }

  public boolean getTargetFound() {
    return m_limeLight.getIsTargetFound();
  }

  public double getDegRotToTarget() {
    return m_limeLight.getdegRotationToTarget();
  }

  // Motors (regular and PID control)
  public void setLoadingMotor(double speed) {
    m_loadingWheel.set(speed);
  }

  public void setLoadingMotorPID(double speed) {
    m_loaderPID.setPIDSpeed(speed);
  }

  public void setShootingMotor(double speed) {
    m_shootingWheel.set(speed);
  }

  public void setShootingMotorPID(double speed) {
    m_shooterPID.setPIDSpeed(speed);
  }

  public void setAzimuthMotor(double azimuthSpeed) {
    // System.out.print(m_azimuthEncoder.getPosition());

    // DigitalInput returns false if limit switch was hit

    // check if encoder limit was reached (in case of magnetic limit switch failure)

    boolean leftEncoderLimitHit = false;
    boolean rightEncoderLimitHit = false;
    if ((Math.abs(m_azimuthEncoder.getPosition())
        - initialAzimuthEncoderValue) >= ShooterConstants.azimuthEncoderLimit) {
      m_azimuthControl.set(0);
      if (m_azimuthEncoder.getPosition() < initialAzimuthEncoderValue) { // Once we've crossed encoder limits move back
                                                                         // to center
        leftEncoderLimitHit = true;
      }
      if ((m_azimuthEncoder.getPosition() > initialAzimuthEncoderValue)) {
        rightEncoderLimitHit = true;
      }
    } else {
      leftEncoderLimitHit = false;
      rightEncoderLimitHit = false;
    }

    // If neither limit switch is hit and the encoder limit hasn't been reached,
    // we're fine (set the motors to the desired speed)
    if ((m_leftLimit.get() && m_rightLimit.get()) && (!leftEncoderLimitHit && !rightEncoderLimitHit)) {
      m_azimuthControl.set(azimuthSpeed);
      limitLeft = m_leftLimit.get();
      limitRight = m_rightLimit.get();
    }
    // If the left limit switch was hit or the left encoder limit was reached and
    // we're moving right, we're fine
    // If the right limit switch was hit or the right encoder limit was reached and
    // we're moving left, we're fine
    // If both the above and not both switches are hit at the same time
    else if ((((!m_leftLimit.get() || leftEncoderLimitHit) && (azimuthSpeed > 0))
        || ((!m_rightLimit.get() || rightEncoderLimitHit) && (azimuthSpeed < 0)))
        && ((m_leftLimit.get() || m_rightLimit.get()))) {
      m_azimuthControl.set(azimuthSpeed);
      limitLeft = m_leftLimit.get();
      limitRight = m_rightLimit.get();
    } else {
      m_azimuthControl.set(0);
      limitLeft = m_leftLimit.get();
      limitRight = m_rightLimit.get();
    }
    //System.out.println(m_azimuthEncoder.getPosition());

  }

  public boolean direction = false;

  public void setAzimuthMotorAutomatic(double azimuthSpeed){
    if(m_limeLight.getIsTargetFound()){
      double xOffset = m_limey.gettxValue();
      m_azimuthControl.set(0.1);
      if ((-.5 < xOffset) && (xOffset < .5)) {
        azimuthSpeed = 0;
      } else {
        azimuthSpeed = (xOffset / -20.500000) / 2;
        if ((xOffset > -2.5) && (xOffset < 2.5)) {
          azimuthSpeed = azimuthSpeed * 1.5;
        }
      }
    } else {
      if (!limitLeft) {
        setAzimuthMotor(ShooterConstants.azimuthSpeedAuto);
        direction = true;
      } else if (!limitRight) {
        setAzimuthMotor(-ShooterConstants.azimuthSpeedAuto);
        direction = false;
      } else {
        if (direction) {
          setAzimuthMotor(ShooterConstants.azimuthSpeedAuto);
        } else {
          setAzimuthMotor(-ShooterConstants.azimuthSpeedAuto);
        }
      }
    }
    
    
    boolean leftEncoderLimitHit = false;
    boolean rightEncoderLimitHit = false;
    if ((Math.abs(m_azimuthEncoder.getPosition())
        - initialAzimuthEncoderValue) >= ShooterConstants.azimuthEncoderLimit) {
      if (m_azimuthEncoder.getPosition() < initialAzimuthEncoderValue) {
        leftEncoderLimitHit = true;
      }
      if (m_azimuthEncoder.getPosition() > initialAzimuthEncoderValue) {
        rightEncoderLimitHit = true;
      }
    }

    // If neither limit switch is hit and the encoder limit hasn't been reached,
    // we're fine (set the motors to the desired speed)
    if ((m_leftLimit.get() && m_rightLimit.get()) && (!leftEncoderLimitHit && !rightEncoderLimitHit)) {
      m_azimuthControl.set(azimuthSpeed);
      limitLeft = m_leftLimit.get();
      limitRight = m_rightLimit.get();
    }
    // If the left limit switch was hit or the left encoder limit was reached and
    // we're moving right, we're fine
    // If the right limit switch was hit or the right encoder limit was reached and
    // we're moving left, we're fine
    // If both the above and not both switches are hit at the same time
    else if ((((!m_leftLimit.get() || leftEncoderLimitHit) && (azimuthSpeed > 0))
        || ((!m_rightLimit.get() || rightEncoderLimitHit) && (azimuthSpeed < 0)))
        && ((m_leftLimit.get() || m_rightLimit.get()))) {
      m_azimuthControl.set(azimuthSpeed);
      limitLeft = m_leftLimit.get();
      limitRight = m_rightLimit.get();
    } else {
      m_azimuthControl.set(0);
      limitLeft = m_leftLimit.get();
      limitRight = m_rightLimit.get();
    }
  }

  public void sweepTurret(){
    if(m_leftLimit.get()){
      m_azimuthControl.set(-(ShooterConstants.azimuthSpeedAuto*0.5));
    }
    else if(m_rightLimit.get()){
      m_azimuthControl.set(ShooterConstants.azimuthSpeedAuto*0.5);
    }
    else{
      m_azimuthControl.set(0);
    }
  }

  // TODO: make getter method for position of azimuth motor and speed of loading
  // and shooting motors

  public void switchCameraMode() {
    if (m_cameraMode == 0) {
      m_cameraMode = 1;
      LimelightSubsystem.toggleAimAssist();
    } else {
      m_cameraMode = 0;
      LimelightSubsystem.toggleAimAssist();
    }
  }

  public void changeStreamMode() {
    if (m_streamMode == StreamType.kStandard) {
      m_streamMode = StreamType.kPiPMain;
    } else if (m_streamMode == StreamType.kPiPMain) {
      m_streamMode = StreamType.kPiPSecondary;
    } else {
      m_streamMode = StreamType.kStandard;
    }
    m_limeLight.setStream(m_streamMode);
  }

  public void setLimelightLEDMode(LedMode mode) {
    m_limeLight.setLEDMode(mode);
  }

  public void openGate() {
    m_gateSolenoid.set(kReverse);
  }

  public void closeGate() {
    m_gateSolenoid.set(kForward);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void dumblyTurnTurret(DoubleSupplier rotationalSpeed) {
    double m_rotSpeed = rotationalSpeed.getAsDouble(); // USE THIS FOR ACTUAL AZIMUTH CONTROL
    // double m_rotSpeed = 0; //THIS IS SET TO 0 FOR TESTING
    // Azimuth joystick deadzones
    if (m_rotSpeed < 0.2 && m_rotSpeed > 0) {
      m_rotSpeed = 0;
    }
    if (m_rotSpeed > -0.2 && m_rotSpeed < 0) {
      m_rotSpeed = 0;
    }
    // System.out.print("********************888");

    setAzimuthMotor(-m_rotSpeed / 10);
  }

  public void smartlyTurnTurret() {
    if (ShooterSubsystem.on) {
      // Target on screen
      if (getTargetFound()) {
        if (Math.abs(getDegRotToTarget()) < 0.1) // Make LEDs GREEN
        { // If we're aligned with the target stop moving
          setAzimuthMotor(0);

        } else {
          double x = getDegRotToTarget();
          if (x < 0) { // if right of target turn left
            rotateLeft();
          } else if (x > 0) { // if left of target turn right
            rotateRight();
          }
        }
      } else // Target off screen
      {
        double angle = getAngle(); // angle of the robot
        double turretAngle = getTurretAngle(); // angle of the turret

        if (Math.abs(turretAngle - angle) > 3) {
          if ((angle < 90 && turretAngle < 90) || (angle > 270 && turretAngle > 270)) {
            if (angle < turretAngle) {
              rotateLeft();
            } else {
              rotateRight();
            }
          } else {
            if (angle > turretAngle) {
              rotateLeft();
            } else {
              rotateRight();
            }
          }
        } else {
          setAzimuthMotor(0);
        }
      }
    }
    // Ah yes so many brackets so easy to read
    // really love to see it
    else {
      setAzimuthMotor(0);
    }
  }

  void rotateRight() {
    System.out.println("Right");
    setAzimuthMotorAutomatic(-ShooterConstants.azimuthSpeedAuto);
  }

  void rotateLeft() {
    System.out.println("Left");
    setAzimuthMotorAutomatic(ShooterConstants.azimuthSpeedAuto);
  }

  double getTurretAngle() {
    double angle = ShooterSubsystem.m_azimuthEncoder.getPosition();
    angle *= -3;
    boolean negative = (angle < 0) ? true : false;
    angle = Math.abs(angle);
    while (angle > 360) {
      angle -= 360;
    }
    angle = (negative) ? 360 - angle : angle;
    return angle;
  }

  double getAngle() {
    double angle = -Navx.getAngle();
    boolean negative = (angle < 0) ? true : false;
    angle = Math.abs(angle);
    while (angle > 360) {
      angle -= 360;
    }
    angle = (negative) ? 360 - angle : angle;
    return angle;
  }

}
