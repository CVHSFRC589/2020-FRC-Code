/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.robot.Constants.DriveConstants;


public class Pneumatics extends SubsystemBase {
  /**
   * Creates a new Pneumatics.
   */

  //public static CANSparkMax climb;
  public static DoubleSolenoid climb;
  public static DoubleSolenoid intakeDeploy;
  public static DoubleSolenoid controlPanel;
  public static Compressor c;

  public Pneumatics() {
   climb  = new DoubleSolenoid(frc.robot.Constants.OIConstants.climbForwardChannel, frc.robot.Constants.OIConstants.climbReverseChannel);
   intakeDeploy = new DoubleSolenoid(frc.robot.Constants.OIConstants.intakeForwardChannel, frc.robot.Constants.OIConstants.intakeReverseChannel);
   controlPanel = new DoubleSolenoid(frc.robot.Constants.OIConstants.controlPanelForwardChannel, frc.robot.Constants.OIConstants.controlPanelReverseChannel);
   c = new Compressor(frc.robot.Constants.OIConstants.CompressorPort);
   c.stop();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    //compress();
  }

  public void compress(){
    if(c.getPressureSwitchValue()){  
      c.start();
    }
    else{
      c.stop();
    }
    
  }
}
