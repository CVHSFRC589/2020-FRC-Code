/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.Manager;

import frc.robot.LimeLight;
import frc.robot.ControlMode;

public class AutomaticAiming extends CommandBase {
  /**
   * Creates a new AutomaticAiming.
   */
  ShooterSubsystem shoot;
  boolean direction = true;

  int t = 0;

  public AutomaticAiming(ShooterSubsystem tempShoot) {
    shoot = tempShoot;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(shoot);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    t = 0;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // if(shoot.getTargetFound()){ //If we're aligned with the target stop moving
    //   shoot.setAzimuthMotor(0);
    // }
    if(shoot.getTargetFound())
    {
      if(shoot.getDegRotToTarget()<0.05)    //Make LEDs GREEN
      { //If we're aligned with the target stop moving
        shoot.setAzimuthMotor(0);
      }
      else
      {
        double x = shoot.getDegRotToTarget(); 
        if(x<0)
        {  //if right of target turn left
         shoot.setAzimuthMotor(-0.1);
        }
        else if(x>0)
        {  //if left of target turn right
        shoot.setAzimuthMotor(0.1);
        }
      } 
    }
    else
    {
      if(!shoot.limitLeft){
        shoot.setAzimuthMotor(0.1);
        direction = true;
      }
      else if(!shoot.limitRight){
        shoot.setAzimuthMotor(-0.1);
        direction = false;
      }
      else{
        if(direction){
          shoot.setAzimuthMotor(0.1);
        }
        else{
          shoot.setAzimuthMotor(-0.1);
        }
      }
    }
    //shoot.setAzimuthMotor(0.1);

   // System.out.println("*************(((()))))********");
    t++;
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    // if (t > 100) { return true; }
    return shoot.getTargetFound();

  }
}
