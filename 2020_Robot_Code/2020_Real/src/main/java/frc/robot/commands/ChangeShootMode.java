/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ShooterSubsystem;


public class ChangeShootMode extends CommandBase {
  /**
   * Creates a new ChangeShootMode.
   */
  ShooterSubsystem m_shoot;
  public ChangeShootMode(ShooterSubsystem shoot) 
  {
   m_shoot = shoot; 
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_shoot);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if(ShooterSubsystem.shootMode){
      System.out.println("manual");
    }else{
      System.out.println("auto");
    }
    ShooterSubsystem.shootMode = !ShooterSubsystem.shootMode;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
