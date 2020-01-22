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
  //public static DoubleSolenoid hook;
  //public static DoubleSolenoid intake;
  //public static DoubleSolenoid controlPanel;
  public static Compressor c;

  public Pneumatics() {
  // hook  = new DoubleSolenoid(frc.robot.Constants.OIConstants.climbForwardChannel, frc.robot.Constants.OIConstants.climbReverseChannel);
   //intake = new DoubleSolenoid(frc.robot.Constants.OIConstants.intakeForwardChannel, frc.robot.Constants.OIConstants.intakeReverseChannel);
   //controlPanel = new DoubleSolenoid(frc.robot.Constants.OIConstants.controlPanelForwardChannel, frc.robot.Constants.OIConstants.controlPanelReverseChannel);
   c = new Compressor(frc.robot.Constants.OIConstants.CompressorPort);
   c.stop();

   //hook.set(DoubleSolenoid.Value.kForward);
   //intake.set(DoubleSolenoid.Value.kForward);
//   controlPanel.set(DoubleSolenoid.Value.kForward);


   //Still need to do all the SmartDashboard stuff
   

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
  // public void deployIntake(){
  //   intake.set(DoubleSolenoid.Value.kReverse);

  // }
  // public void retractIntake(){
  //   intake.set(DoubleSolenoid.Value.kForward);
  // }

  // public void deployHook(){
  //   hook.set(DoubleSolenoid.Value.kReverse);
    
  // }
  // public void retractHook(){
  //   hook.set(DoubleSolenoid.Value.kForward);
  // }

}
