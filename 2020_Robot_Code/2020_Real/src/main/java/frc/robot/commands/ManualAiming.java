/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.ShooterSubsystem;

import java.util.function.DoubleSupplier;

public class ManualAiming extends CommandBase {
  /**
   * Creates a new ManualAiming.
   */

  private ShooterSubsystem m_shoot;
  private DoubleSupplier m_rotationalSpeed;
  

  public ManualAiming(ShooterSubsystem tShoot, DoubleSupplier z) {
    m_shoot = tShoot;
    m_rotationalSpeed = z; //set m_rotationalSpeed to z
    // Use addRequirements() here to declare subsystem dependencies.4
    addRequirements(m_shoot);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_shoot.setAzimuthMotor(0.0);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    //TODO:
    //double m_rotSpeed = m_rotationalSpeed.getAsDouble();  //USE THIS FOR ACTUAL AZIMUTH CONTROL
    double m_rotSpeed = 0; //THIS IS SET TO 0 FOR TESTING 
    //Azimuth joystick deadzones
    if(m_rotSpeed<0.4 && m_rotSpeed>0){
      m_rotSpeed = 0;
    }
    if(m_rotSpeed>-0.4 && m_rotSpeed<0){
      m_rotSpeed = 0;
    }
    m_shoot.setAzimuthMotor(m_rotSpeed/10); //can divide the speed by ten
    // m_shoot.setAzimuthMotor(m_rotationalSpeed.getAsDouble());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  //@Override
  public boolean isFinished() {
    return false;
  }
}
