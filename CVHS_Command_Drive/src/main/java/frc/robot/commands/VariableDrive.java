/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveSubsystem;

public class VariableDrive extends CommandBase {
  /**
   * Creates a new VariableDrive.
   */

  private final DriveSubsystem m_drive;
  private double x;
  private double y;
  private double z;
  
  public VariableDrive(DriveSubsystem drive) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_drive = drive;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    x = 0;
    y = 0;
    z = 0;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    //m_drive.arcadeDrive();
    x = frc.robot.RobotContainer.j1.getX();
    y = frc.robot.RobotContainer.j1.getY();
    z = (frc.robot.RobotContainer.j1.getZ()*(-1)+1)/2; //multiplying by -1 because the z axis is reversed for some reason

    m_drive.setDriveMotors(x, y, z);; //if setDriveMotors is a cosntinuous method put the variable definitions in these parameters
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
