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

public class DriveController extends CommandBase {
  /**
   * Creates a new DefaultDrive.
   */
  private final DriveSubsystem m_drive;
  private final DoubleSupplier m_forward;
  private final DoubleSupplier m_rotation;

  private final DoubleSupplier zz;
  

  /**
   * @param subsystem
   * @param forward
   * @param rotation
   */
  public DriveController(DriveSubsystem subsystem, DoubleSupplier forward, DoubleSupplier rotation, DoubleSupplier z) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_drive = subsystem;
    m_forward = forward;
    m_rotation = rotation;
    addRequirements(m_drive);
    zz = z;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

  }
 
  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {    
    //Set the z axis from 0.25 to 1
    double z = zz.getAsDouble();
    z = -z;
    if(z<0){
      z = (1-Math.abs(z))*0.5+0.25;
    }
    else {
      z = z*0.25+0.75;
    }

    //Set driver joystick deadzones
    double m_for = m_forward.getAsDouble();
    double m_rot = m_rotation.getAsDouble();
    if(m_for>=-0.2 && m_for<=0.2){
      m_for = 0;
    }
    if(m_rot>=-0.2 && m_rot<=0.2){
      m_rot = 0;
    }
    
    //Arcade drive with z multiplier
    m_drive.arcadeDrive(m_for*z, z*m_rot);
    //System.out.println("left encoder: " + m_drive.getLeft());
    //Arcade Drive without a multiplier
    //m_drive.arcadeDrive(m_forward.getAsDouble(), m_rotation.getAsDouble());
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
