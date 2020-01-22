/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ClimbSubsystem extends SubsystemBase {
  /**
   * Creates a new ClimbSubsystem.
   */

  public static DoubleSolenoid hook;

  public ClimbSubsystem() {
    hook  = new DoubleSolenoid(frc.robot.Constants.OIConstants.climbForwardChannel, frc.robot.Constants.OIConstants.climbReverseChannel);
    hook.set(DoubleSolenoid.Value.kForward);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void deployHook(){
    hook.set(DoubleSolenoid.Value.kReverse);
    
  }
  public void retractHook(){
    hook.set(DoubleSolenoid.Value.kForward);
  }
}
