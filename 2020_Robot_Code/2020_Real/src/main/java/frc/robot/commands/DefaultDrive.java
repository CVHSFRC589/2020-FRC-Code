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
import frc.robot.subsystems.DriveTestSubsystem;

public class DefaultDrive extends CommandBase {
  /**
   * Creates a new DefaultDrive.
   */
  private final DriveTestSubsystem m_drive;
  private final DoubleSupplier m_forward;
  private final DoubleSupplier m_rotation;

  /**
   * @param subsystem
   * @param forward
   * @param rotation
   */
  public DefaultDrive(DriveTestSubsystem subsystem, DoubleSupplier forward, DoubleSupplier rotation) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_drive = subsystem;
    m_forward = forward;
    m_rotation = rotation;
    addRequirements(m_drive);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
   m_drive.arcadeDrive(m_forward.getAsDouble(), m_rotation.getAsDouble());
    //m_drive.setMotors();
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
