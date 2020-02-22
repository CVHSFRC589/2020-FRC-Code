/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.LimeLight;
import frc.robot.subsystems.LimelightSubsystem;

public class LEDSubsystem extends SubsystemBase {
  /**
   * Creates a new IntakeSubsystem.
   */

  AddressableLED m_led;
  AddressableLEDBuffer m_ledBuffer;

  public LEDSubsystem() {
    // PWM port 9
    m_led = new AddressableLED(9);

    m_ledBuffer = new AddressableLEDBuffer(60);
    m_led.setLength(m_ledBuffer.getLength());

    m_led.setData(m_ledBuffer);
    m_led.start();
  }

  public void LED() {
    for (var i = 0; i < m_ledBuffer.getLength(); i++) {
      // Sets the specified LED to the HSV values for red
      m_ledBuffer.setHSV(i, 180, 98, 59);
    }
    m_led.setData(m_ledBuffer);
  }

  private void turretAlignmentLEDs(){
    int i = 0;
    double xOffset = LimelightSubsystem.gettxValue();
    if(-10 < xOffset && xOffset < 10){
      m_ledBuffer.setHSV(i, 118, 70, 47); //Set farthest 2 LEDs
      m_led.setData(m_ledBuffer);
      if(-7.5 < xOffset && xOffset < 7.5){
        m_ledBuffer.setHSV(i+1, 118, 80, 47); //Set third LEDs
        m_led.setData(m_ledBuffer);
        if(-4 < xOffset && xOffset < 4){
          m_ledBuffer.setHSV(i+2, 118, 90, 47); //Set second LEDs
          m_led.setData(m_ledBuffer);
            if(-1 < xOffset && xOffset < 1){
            m_ledBuffer.setHSV(i+3, 118, 100, 47); //Set frist LEDs
            m_led.setData(m_ledBuffer);
          }
        }
      }
    }
  }

  // private void frontLEDs(){
  //   int i;
  // //boolean forwardDrive = DriveSubsystem.m_driveForward;
  //   while(forwardDrive){
  //     m_ledBuffer.setHSV(i, 190 + (i * (80/m_ledBuffer.getLength())), 100, 50); //Set farthest 2 LEDs
  //     m_led.setData(m_ledBuffer);
  //     i = i + 1;
  //     if(i > m_ledBuffer.getLength())
  //     {
  //       i = 0;
  //     }
  //   }
  //  m_ledBuffer.setHSV(i, 270 - (i * (80/m_ledBuffer.getLength())), 100, 50); //Set farthest 2 LEDs
  //  m_led.setData(m_ledBuffer);
  //   i = i + 1;

  //  if(i > m_ledBuffer.getLength())
  //  {
  //   i = 0;
  //  }
  //   }
 }
/*
  private fullFireSpeed(){

  }
*/
