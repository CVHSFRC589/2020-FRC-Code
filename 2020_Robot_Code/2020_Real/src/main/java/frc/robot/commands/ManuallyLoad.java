/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.ShooterSubsystem;

import frc.robot.Constants.ShooterConstants;

public class ManuallyLoad extends CommandBase {
  /**
   * Creates a new ManuallyLoad.
   */
  ShooterSubsystem shoot;
  private static boolean runLoad = true;
  public ManuallyLoad(ShooterSubsystem tShoot) {
    shoot = tShoot;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(shoot);
    shoot.setLoadingMotor(0);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    //start loading motor if it isn't running and if the shooting motor is on
    if(runLoad && ShooterSubsystem.shootingWheelRunning){
      shoot.setLoadingMotor(ShooterConstants.loadingSpeed);
      shoot.setAzimuthMotor(0); 
      //shoot.setLoadingMotorPID(ShooterConstants.loadingSpeed);
      runLoad = false;
    }
    //stop loading motor if it was already running
    else{
      shoot.setLoadingMotor(0);
      //shoot.setLoadingMotorPID(0);
      runLoad = true;
    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
