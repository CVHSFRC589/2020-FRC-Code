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

  private final DoubleSupplier xx;
  private final DoubleSupplier yy;
  private final DoubleSupplier zz;
  

  /**
   * @param subsystem
   * @param forward
   * @param rotation
   */
  public DefaultDrive(DriveTestSubsystem subsystem, DoubleSupplier forward, DoubleSupplier rotation, DoubleSupplier x, DoubleSupplier y, DoubleSupplier z) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_drive = subsystem;
    m_forward = forward;
    m_rotation = rotation;
    addRequirements(m_drive);

    xx = x;
    yy = y; 
    zz = z;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

  }
 
  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double x = xx.getAsDouble();
    double y = yy.getAsDouble();
    double z = zz.getAsDouble();
    if(x>=-.1 && x<=0.1){
      x = 0;
    }
    if(y>=-.1 && y<=0.1){
      y = 0;
    }

    double m_for = m_forward.getAsDouble();
    double m_rot = m_rotation.getAsDouble();
    if(m_for>=-0.2 && m_for<=0.2){
      m_for = 0;
    }
    if(m_rot>=-0.2 && m_rot<=0.2){
      m_rot = 0;
    }

    m_drive.arcadeDrive(m_for*z, m_rot*z);
    // m_drive.setMotors(y+x, -(y-x), z);
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
