/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;

import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.Constants.ShooterConstants;

public class AdvancedAiming extends CommandBase {
  /**
   * Creates a new AdvancedAiming.
   */
  ShooterSubsystem shoot;
  boolean direction = true;
  AHRS Navx;

  public AdvancedAiming(ShooterSubsystem tempShoot) {
    shoot = tempShoot;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(shoot);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    ShooterSubsystem.on = true;
    Navx = new AHRS(SPI.Port.kMXP);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(ShooterSubsystem.on){
      //Target on screen
      if(shoot.getTargetFound())
      {
        if(Math.abs(shoot.getDegRotToTarget())<0.05)    //Make LEDs GREEN
        { //If we're aligned with the target stop moving
          shoot.setAzimuthMotor(0);
        }
        else
        {
          double x = shoot.getDegRotToTarget(); 
          if(x<0)
          {  //if right of target turn left
            rotateLeft();
          }
          else if(x>0)
          {  //if left of target turn right
            rotateRight();
          }
        } 
      }
      else //Target off screen
      {
        double angle = getAngle();
        if (angle < 90) {
          if (getTurretAngle() < angle) {
            rotateRight();
          } else {
            rotateLeft();
          }
        } else if (angle < 180) { // 90 < angle < 180
          rotateRight();
        } else if (angle < 270) { // 180 < angle < 270
          rotateLeft();
        } else {// 270 < angle < 360
          if (getTurretAngle() > angle) {
            rotateRight();
          } else {
            rotateLeft();
          }
        }
      }
    }
    //Ah yes so many brackets so easy to read
    //really love to see it
    else{
      shoot.setAzimuthMotor(0);
    }
  }

  void rotateRight() {
    if (!shoot.limitRight) {
      shoot.setAzimuthMotorAutomatic(ShooterConstants.azimuthSpeed);
    }
  }

  void rotateLeft() {
    if (!shoot.limitLeft) {
      shoot.setAzimuthMotorAutomatic(-ShooterConstants.azimuthSpeed);
    }
  }

  double getTurretAngle() {
    double angle = ShooterSubsystem.m_azimuthEncoder.getPosition();
    boolean negative = (angle < 0) ? true : false; 
    angle = Math.abs(angle);
    while (angle  > 360) {
      angle -= 360;
    }
    angle = (negative) ? 360 - angle : angle;
    return angle;
  }

  double getAngle() {
    double angle = Navx.getAngle();
    boolean negative = (angle < 0) ? true : false; 
    angle = Math.abs(angle);
    while (angle  > 360) {
      angle -= 360;
    }
    angle = (negative) ? 360 - angle : angle;
    return angle;
  }


  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
