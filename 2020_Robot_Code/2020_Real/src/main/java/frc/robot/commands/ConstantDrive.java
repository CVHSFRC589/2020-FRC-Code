/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.DriveSubsystem;

public class ConstantDrive extends CommandBase {
  /**
   * Creates a new ConstantDrive.
   */

  private double speed = 0;
  private DriveSubsystem drive;

  public ConstantDrive(DriveSubsystem d, double s) {
    // Use addRequirements() here to declare subsystem dependencies.
    speed = s;
    drive = d;
    addRequirements(drive);

  }

  // @Override
  // protected void setInterruptible(boolean interruptible){

  // }


  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    //drive.setMotors(speed, -speed, 1);
    drive.tankDrive(speed, speed, 1);
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

  //@Override
  protected void interrupted(){
    end(true);
  }
}