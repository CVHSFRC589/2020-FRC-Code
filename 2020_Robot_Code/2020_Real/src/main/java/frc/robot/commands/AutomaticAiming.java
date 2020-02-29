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
import frc.robot.Constants.ShooterConstants;
import frc.robot.LimeLight;
import frc.robot.ControlMode;

public class AutomaticAiming extends CommandBase {
  /**
   * Creates a new AutomaticAiming.
   */
  ShooterSubsystem shoot;
  boolean direction = true;
  boolean endCommand = false; //set to true when stopping automatic aiming and setting the motor to 0

  public AutomaticAiming(ShooterSubsystem tempShoot) {
    shoot = tempShoot;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(shoot);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    // if(!ShooterSubsystem.on){
    //   ShooterSubsystem.on = true;
    // }
    // else{
    //   ShooterSubsystem.on = false;
    // }
    ShooterSubsystem.on = true;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(ShooterSubsystem.on){
      if(shoot.getTargetFound())
      {
        //System.out.println(shoot.getTargetFound());
        if(Math.abs(shoot.getDegRotToTarget())<0.05)    //Make LEDs GREEN
        { //If we're aligned with the target stop moving
          shoot.setAzimuthMotor(0);
          //ShooterSubsystem.on = false; //reset the on boolean, so the next time the command is called it runs (initialize makes it true first)
          //System.out.println("*******************ALIGNED*******************");
        }
        else
        {
          //System.out.println("************not aligned but in frame");
          double x = shoot.getDegRotToTarget(); 
          if(x<0)
          {  //if right of target turn left
          shoot.setAzimuthMotorAutomatic(ShooterConstants.azimuthSpeed);
          }
          else if(x>0)
          {  //if left of target turn right
          shoot.setAzimuthMotorAutomatic(-ShooterConstants.azimuthSpeed);
          }
        } 
      }
      else
      {
        //System.out.print("*LLLLLLLLLLLLll***********^^^^******");
        if(!shoot.limitLeft){
          shoot.setAzimuthMotorAutomatic(ShooterConstants.azimuthSpeed);
          direction = true;
        }
        else if(!shoot.limitRight){
          shoot.setAzimuthMotorAutomatic(-ShooterConstants.azimuthSpeed);
          direction = false;
        }
        else{
          if(direction){
            shoot.setAzimuthMotorAutomatic(ShooterConstants.azimuthSpeed);
          }
          else{
            shoot.setAzimuthMotorAutomatic(-ShooterConstants.azimuthSpeed);
          }
        }
      }
    }
    //Ah yes so many brackets so easy to read
    else{
      shoot.setAzimuthMotor(0);
      endCommand = true;
    }
  }



  //OTHER EXECUTE
  // @Override
  // public void execute() {
  //   // if(shoot.getTargetFound()){ //If we're aligned with the target stop moving
  //   //   shoot.setAzimuthMotor(0);
  //   // }

  //   if(shoot.getTargetFound())
  //   {
  //     System.out.println(shoot.getTargetFound());

  //     if(Math.abs(shoot.getDegRotToTarget())<0.05)    //Make LEDs GREEN
  //     { 
  //       shoot.setAzimuthMotor(0);
  //       System.out.println("*******************ALIGNED*******************");
  //     }
  //     else
  //     {
  //       System.out.println("************not aligned but in frame");
  //       double x = shoot.getDegRotToTarget(); 
  //       if(x<0)
  //       {  //if right of target turn left
  //        shoot.setAzimuthMotor(ShooterConstants.azimuthSpeed);
  //       }
  //       else if(x>0)
  //       {  //if left of target turn right
  //       shoot.setAzimuthMotor(-ShooterConstants.azimuthSpeed);
  //       }
  //     } 
  //   }
  //   else
  //   {
  //     //System.out.print("*LLLLLLLLLLLLll***********^^^^******");
  //     if(!shoot.limitLeft){
  //       shoot.setAzimuthMotor(ShooterConstants.azimuthSpeed);
  //       direction = true;
  //     }
  //     else if(!shoot.limitRight){
  //       shoot.setAzimuthMotor(-ShooterConstants.azimuthSpeed);
  //       direction = false;
  //     }
  //     else{
  //       if(direction){
  //         shoot.setAzimuthMotor(ShooterConstants.azimuthSpeed);
  //       }
  //       else{
  //         shoot.setAzimuthMotor(-ShooterConstants.azimuthSpeed);
  //       }
  //     }
  //   }
  //   //shoot.setAzimuthMotor(0.1);

  //  // System.out.println("*************(((()))))********");
  //   //t++;
  // }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
   if((shoot.getTargetFound() && Math.abs(shoot.getDegRotToTarget())<0.5)){ //endCommand is for if the button gets pressed again to stop automatic aiming
    return true;
   }
  return false;
  }
}
