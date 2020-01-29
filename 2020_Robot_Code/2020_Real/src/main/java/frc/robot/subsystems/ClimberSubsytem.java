/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants.ClimberConstants;

import static edu.wpi.first.wpilibj.DoubleSolenoid.Value.kForward;
import static edu.wpi.first.wpilibj.DoubleSolenoid.Value.kReverse;

public class ClimberSubsytem extends SubsystemBase {
  /**
   * Creates a new ClimberSubsytem.
   */

  //No winch, this solenoid pulls robot up
  DoubleSolenoid m_hookSolenoid = new DoubleSolenoid(ClimberConstants.kHookSolenoidPorts[0], ClimberConstants.kHookSolenoidPorts[1]);

  public ClimberSubsytem() {
    m_hookSolenoid.set(kReverse);
  }

  public void deployHook(){
    m_hookSolenoid.set(kForward);
  }
  public void retractHook(){
    m_hookSolenoid.set(kReverse);
  }
  
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
