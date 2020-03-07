// /* /* /*----------------------------------------------------------------------------*/
// /* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
// /* Open Source Software - may be modified and shared by FRC teams. The code   */
// /* must be accompanied by the FIRST BSD license file in the root directory of */
// /* the project.                                                               */
// /*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.LEDConstants;

public class LEDSubsystem extends SubsystemBase {
  /**
   * Creates a new LEDSubsystem.
   */

  public static CANSparkMax ledMotor = new CANSparkMax(LEDConstants.kLEDPort, MotorType.kBrushless);
  public void LED(){
    
  }

  public static void setTurretLEDsGreen(){ //AutoAiming/Aiming Subsystems
    ledMotor.set(0.73);//lime
  }
  public static void setTurretLEDsRed(){ //AutoAiming/Aiming Subsystems
    ledMotor.set(0.61);//red
  }

  public static void climberLEDPattern(){ //Climbing subsystem
    ledMotor.set(-0.97); //Rainbow Party
  }

  // public static void drivingTurretForward(){
  //   ledMotor.set(0.01); //Lightchase color 1
  // }
  // public static void drivingIntakeForward(){
  //   ledMotor.set(0.21); //Lightchase color 2
  // }

  // public static void turretFullSpeed(){
  //   ledMotor.set(-0.57); //Fire Large
  // }
}
