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
  ShooterSubsystem m_limelight; //eventually move the setLimelightLEDMode to Limelight subsystem
 // private LedMode mode = 3;
  //this.table.getEntry("ledMode").setNumber(3);
   //3 is on 1 is off
  public ToggleLimelightLED(ShooterSubsystem limelight) { //add limelight subsystem to get network table to use line 21
    // Use addRequirements() here to declare subsystem dependencies.
    m_limelight = limelight;
    addRequirements(m_limelight);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    // if(mode == 3){
    //   mode = 1;
    // }
    // else{
    //   mode = 3;
    // }
    // m_limelight.setLimelightLEDMode(mode);
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
    return false;
  }
}
