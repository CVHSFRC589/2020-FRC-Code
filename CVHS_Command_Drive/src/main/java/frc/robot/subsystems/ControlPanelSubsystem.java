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

  public static DoubleSolenoid controlPanel;

  public ControlPanelSubsystem() {
    controlPanel = new DoubleSolenoid(frc.robot.Constants.OIConstants.controlPanelForwardChannel, frc.robot.Constants.OIConstants.controlPanelReverseChannel);
    controlPanel.set(DoubleSolenoid.Value.kForward);

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void deployControlPanel(){
    controlPanel.set(DoubleSolenoid.Value.kReverse);
  }
  public void retractControlPanel(){
    controlPanel.set(DoubleSolenoid.Value.kForward);
  }
}
