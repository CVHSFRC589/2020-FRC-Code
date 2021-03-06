/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.LimelightSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

import frc.robot.Constants.ShooterConstants;

public class SmartShoot extends CommandBase {
  /**
   * Creates a new ManuallyShoot.
   */
  ShooterSubsystem shoot;
  LimelightSubsystem lime = new LimelightSubsystem();
  private static boolean runShooter = true;
  public SmartShoot(ShooterSubsystem tempShoot) {
    // Use addRequirements() here to declare subsystem dependencies.
    shoot = tempShoot;
    addRequirements(shoot);
    shoot.setShootingMotor(0);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    // if(runShooter){ //if we want to run the shooter
    //   shoot.setShootingMotor(ShooterConstants.shootingSpeed);
    //   //shoot.setShootingMotorPID(ShooterConstants.shootingSpeed);
    //   runShooter = false;
    //   ShooterSubsystem.shootingWheelRunning = true;
    // }
    // else{   //the button was pressed while the shooter was on, so turn it off
    //   shoot.setShootingMotor(0);
    //   shoot.setShootingMotorPID(0);
    //   runShooter = true;
    //   ShooterSubsystem.shootingWheelRunning = false;
    // }
   // shoot.setLoadingMotor(ShooterConstants.loadingSpeed); //set this constant to one for the loading motor
   // System.out.println("CALLED ***************" + ShooterConstants.shootingSpeed);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(runShooter){ //if runShooter    //if we want to run the shooter
      //vikram's crazy code
      double distToTarget = lime.getDistance();
      System.out.println("Distance to target" + distToTarget);
      //maximum optimal speed is ~ 0.6
      //assume that, when 120 inches away, we want to go at this speed
      shoot.setShootingMotor(distToTarget/120 * ShooterConstants.shootingSpeed);
      runShooter = false;
    }
    else{   //the button was pressed while the shooter was on, so turn it off
      shoot.setShootingMotor(0);
      //shoot.setShootingMotorPID(0);
      runShooter = true;
      ShooterSubsystem.shootingWheelRunning = false;
    }
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
