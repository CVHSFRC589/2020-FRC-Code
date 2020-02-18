/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.ShooterConstants;
import frc.robot.Constants.DriveConstants;
import frc.robot.subsystems.DriveSubsystem;

public class DriveToDistance extends CommandBase {
  /**
   * Creates a new ConstantDrive.
   */

  private double distance = 0;
  private DriveSubsystem drive;
  private boolean finishedTraveling = false;
  private double initialEncoderValue;

  public DriveToDistance(DriveSubsystem d, double s) {
    // Use addRequirements() here to declare subsystem dependencies.
    distance = s;
    drive = d;
    addRequirements(drive);
    
  }
  
  // @Override
  // protected void setInterruptible(boolean interruptible){
    
    // }
    
    
    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
      drive.setLeft(0);
      System.out.println("init: " + drive.getLeft());
    }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    //drive.setMotors(speed, -speed, 1);
    
    //(Encoder ticks / Ticks per inch) = inches

    //drive.getLeft() gives revolutions of motor shaft
    //divide by 10.7(gear ratio - 10.7 revolutions of the motor shaft makes one revolution of the actual wheel) to get revolutions of actual wheel
    //multiply by wheel circumference to get inches traveled
    int distTraveled = (int)((drive.getLeft()-initialEncoderValue)/DriveConstants.gearRatio * DriveConstants.kEncoderIPR);
    if(Math.abs(distTraveled) >= distance){
      finishedTraveling = true;
    }
    drive.tankDrive(0.7, 0.7, 1);
  }


  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return finishedTraveling;
  }

  //@Override
  protected void interrupted(){
    end(true);
  }
}
