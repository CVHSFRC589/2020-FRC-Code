/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.DriveSubsystem;

public class DriveController extends CommandBase {
  /**
   * Creates a new DriveController.
   */
  private final DriveSubsystem drive;
  
  
  public DriveController(DriveSubsystem drivesys) {
    // Use addRequirements() here to declare subsystem dependencies.
    drive = drivesys;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double z = frc.robot.RobotContainer.j1.getZ(); 
    double x = frc.robot.RobotContainer.j1.getX(); 
    double y = frc.robot.RobotContainer.j1.getY(); 
    //double multiplier = frc.robot.RobotContainer.j1.getZ(); 
    if(z<-.1){
      z = (1-Math.abs(z))*0.5+0.25;
    }
    else if(z>0.1){
      z = z*0.25+0.75;
    }
    else{
      z = 0;
    }
    drive.setMotors(y-x, -x+y, z);
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
