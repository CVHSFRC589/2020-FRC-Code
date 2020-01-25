/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.buttons.Trigger;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
//import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This is a demo program showing the use of the DifferentialDrive class. Runs
 * the motors with arcade steering.
 */
public class Robot extends TimedRobot {
  private final WPI_TalonSRX m_leftMotor = new WPI_TalonSRX(1);
  private final WPI_TalonSRX m_rightMotor = new WPI_TalonSRX(2);
  private final DifferentialDrive m_robotDrive = new DifferentialDrive(m_leftMotor, m_rightMotor);
  private final Joystick m_stick = new Joystick(0);
  private Button trigger = new JoystickButton(m_stick, 1);
  boolean held = false;
  // private double z = m_stick.getZ();

  @Override
  public void teleopPeriodic() {
    // Drive with arcade drive.
    // That means that the Y axis drives forward
    // and backward, and the X turns left and right.
    // held = trigger.get();
    // if(held){
    //   while(true){
    //     m_leftMotor.set(0.3);
    //     m_rightMotor.set(0.3);
    //     if(!trigger.get()){
    //       break;
    //     }
    //   }
    // }
      m_robotDrive.arcadeDrive(m_stick.getY()*((m_stick.getZ()*(-1)+1)/2), m_stick.getX()*((m_stick.getZ()*(-1)+1)/2));
    
  }
}
