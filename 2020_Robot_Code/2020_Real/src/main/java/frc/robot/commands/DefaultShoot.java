/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ShooterSubsystem;

public class DefaultShoot extends CommandBase {
  /**
   * Creates a new DefaultShoot.
   */
  public static DoubleSupplier m_rotationalSpeed;
  ShooterSubsystem m_shoot;
  public DefaultShoot(ShooterSubsystem shoot, DoubleSupplier z) {
    m_shoot = shoot;
    m_rotationalSpeed = z;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_shoot);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(!ShooterSubsystem.shootMode) {// auto
      //System.out.println("Smart turn");
      m_shoot.smartlyTurnTurret();
      
    }
    else{//manual
      //System.out.println("Dumb turn");
      m_shoot.dumblyTurnTurret(m_rotationalSpeed);
    }
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
