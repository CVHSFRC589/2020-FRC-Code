/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DriveConstants;

public class ExampleSubsystem extends SubsystemBase {
  /**
   * Creates a new ExampleSubsyste.
   */

    // DEFINE MEMBER OBJECTS
  PWMVictorSPX m_leftMotor = new PWMVictorSPX(DriveConstants.ExampleConstants.kLeftMotorPort);

  public ExampleSubsystem() {

    // Initialize membersm

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
