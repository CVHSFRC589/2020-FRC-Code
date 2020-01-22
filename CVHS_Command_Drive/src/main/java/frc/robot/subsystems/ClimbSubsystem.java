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

  public static DoubleSolenoid m_hook;

  public ClimbSubsystem() {
    m_hook  = new DoubleSolenoid(frc.robot.Constants.OIConstants.climbForwardChannel, frc.robot.Constants.OIConstants.climbReverseChannel);
    m_hook.set(DoubleSolenoid.Value.kForward);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void extend(){
    m_hook.set(DoubleSolenoid.Value.kReverse);
    
  }
  public void retract(){
    m_hook.set(DoubleSolenoid.Value.kForward);
  }
}
