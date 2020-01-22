/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ControlPanelSubsystem extends SubsystemBase {
  /**
   * Creates a new ControlPanel.
   */

  public static DoubleSolenoid m_controlPanel;

  public ControlPanelSubsystem() {
    m_controlPanel = new DoubleSolenoid(frc.robot.Constants.OIConstants.controlPanelForwardChannel, frc.robot.Constants.OIConstants.controlPanelReverseChannel);
    m_controlPanel.set(DoubleSolenoid.Value.kForward);

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void deploy(){
    m_controlPanel.set(DoubleSolenoid.Value.kReverse);
  }
  public void retract(){
    m_controlPanel.set(DoubleSolenoid.Value.kForward);
  }
}
