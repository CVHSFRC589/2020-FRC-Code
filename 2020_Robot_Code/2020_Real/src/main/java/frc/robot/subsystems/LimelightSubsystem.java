/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
//import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.LimeLightTargeting;

public class LimelightSubsystem extends SubsystemBase {
  /**
   * Creates a new Limelight.
   */
  public NetworkTable table;
  public NetworkTableEntry ty;
  public double limelightHeight = 25;
  public double tyTangent;
  public double dist;
  public double yAngle;
  public double yRadians; 
  public static boolean limelightTargetingStatic = true;

  public LimelightSubsystem() {
    table = NetworkTableInstance.getDefault().getTable("limelight");
    ty = table.getEntry("ty");
    limelightTargetingStatic = false;
    // toggle = new JoystickButton(frc.robot.Robot.j1, 10);
  }

  // Public methods
  public void updateLimeLightValues() {
    try{
      ty = table.getEntry("ty");
      SmartDashboard.putString("Errors", "nah");   
    } catch(final NullPointerException e){
      SmartDashboard.putString("Errors", "yep");
    }
    yAngle = ty.getDouble(0.0);
    getDistance();
    SmartDashboard.putNumber("Distance inches", dist);
    SmartDashboard.putNumber("Network Table Y", yAngle);

    //SmartDashboard.putNumber("Target Elevation", targetElevation);
     //SmartDashboard.putNumber("tyTangent", tyTangent);
     //SmartDashboard.putNumber("yRadians", yRadians);
  }

  public void toggleAimAssist() {
    if (limelightTargetingStatic = true) {
      limelightTargetingStatic = false;
      SmartDashboard.putNumber("No longer aiming", 0);
    } else {
      limelightTargetingStatic = true;
      SmartDashboard.putNumber("Aiming", 1);
    }
  }

  public void getDistance() {
    final double limeLightDefAngle = 16.3;
    yAngle = yAngle + limeLightDefAngle;
    yRadians = Math.toRadians(yAngle);
    tyTangent = Math.tan(yRadians);
    dist = 70.5 / tyTangent;           //NEW NUMBER MAY NEED TO BE GOTTEN WITH NEW ROBOT
    dist = dist * 1.096 - 16.0466;     //Desmos correction graph REQUIRES TESTING WITH NEW ROBOT
  }
  
  public static boolean getLimelightTargeting()
  {
    return limelightTargetingStatic;  
  } 
}