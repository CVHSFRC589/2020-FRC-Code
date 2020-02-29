/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.ControlMode.LedMode;
import frc.robot.subsystems.LimelightSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

public class ToggleLimelightLED extends CommandBase {
  /**
   * Creates a new ToggleLimelightLED.
   */
  ShooterSubsystem m_shooter; //eventually move the setLimelightLEDMode to Limelight subsystem
  LimelightSubsystem m_limelight;
  private static boolean limelightON = false;
  public ToggleLimelightLED(ShooterSubsystem shooter, LimelightSubsystem limelight) { //add limelight subsystem to get network table to use line 21
    // Use addRequirements() here to declare subsystem dependencies.
    m_shooter = shooter;
    m_limelight = limelight;
    addRequirements(m_shooter, m_limelight);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    //3 is on, 1 is off
    if(limelightON){
      m_limelight.getNetworkTable().getEntry("ledMode").setNumber(1);
      limelightON = false;
    }
    else{
      m_limelight.getNetworkTable().getEntry("ledMode").setNumber(0);
      //System.out.println("4444444444444");
      limelightON = true;
    }
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
