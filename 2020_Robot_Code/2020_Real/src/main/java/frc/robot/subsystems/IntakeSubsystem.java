/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import frc.robot.Constants.IntakeConstants;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static edu.wpi.first.wpilibj.DoubleSolenoid.Value.kForward;
import static edu.wpi.first.wpilibj.DoubleSolenoid.Value.kReverse;

import com.revrobotics.CANSparkMax;
import com.revrobotics.EncoderType;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;


public class IntakeSubsystem extends SubsystemBase {
  /**
   * Creates a new IntakeSubsystem.
   */

  //There may be two solenoids, one per side to deploy the intake system
  public static DoubleSolenoid m_intakeSolenoid = new DoubleSolenoid(IntakeConstants.kIntakeSolenoidPorts[0], IntakeConstants.kIntakeSolenoidPorts[1]);
  //public static DoubleSolenoid m_intakeSolenoid = new DoubleSolenoid(IntakeConstants.kIntakeSolenoidPorts[2], IntakeConstants.kIntakeSolenoidPorts[3]);
  
  public static CANSparkMax m_intakeMotor = new CANSparkMax(IntakeConstants.kIntakeMotorPort, MotorType.kBrushless);
  public static CANEncoder m_intakeEncoder = new CANEncoder(m_intakeMotor, EncoderType.kQuadrature, IntakeConstants.kIntakeEncoderCPR);  //EncoderCPR is probably the same as DriveConstants.kEncoderCPR
  
  public IntakeSubsystem() {
    m_intakeSolenoid.set(kReverse);
    m_intakeMotor.set(0);
  }

  public void deployIntake(){
    m_intakeSolenoid.set(kForward);
  }
  public void retractIntake(){
    m_intakeSolenoid.set(kReverse);
  }

  public void activateIntake(double speed){
    m_intakeMotor.set(speed);
  }
  public void deactivateIntake(double speed){
    m_intakeMotor.set(speed);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
