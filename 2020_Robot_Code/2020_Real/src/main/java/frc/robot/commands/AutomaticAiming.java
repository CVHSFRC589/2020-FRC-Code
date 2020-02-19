/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.Motors;

import frc.robot.LimeLight;
import frc.robot.ControlMode;

public class AutomaticAiming extends CommandBase {
  /**
   * Creates a new AutomaticAiming.
   */
  ShooterSubsystem shoot;

  public AutomaticAiming(ShooterSubsystem tempShoot) {
    shoot = tempShoot;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(shoot);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(shoot.getTargetFound()){ //If we're aligned with the target stop moving
      shoot.setAzimuthMotor(0);
    }
    else{
      double x = shoot.getDegRotToTarget(); 
      if(x<0){  //if left of target turn right
        shoot.setAzimuthMotor(0.2);
      }
      else if(x>0){  //if right of target turn left
        shoot.setAzimuthMotor(-0.2);
      }
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return limey.getIsTargetFound();
  }
}
