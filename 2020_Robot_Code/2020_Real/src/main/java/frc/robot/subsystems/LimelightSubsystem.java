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
    //Get the new yangle value from the network table
    try{
      ty = table.getEntry("ty");
      SmartDashboard.putString("Errors", "nah");   
    } catch(final NullPointerException e){
      SmartDashboard.putString("Errors", "yep");
    }
    //turn the network table value into a double
    yAngle = ty.getDouble(0.0);
    getDistance();
    //Display the values on the Smart Dashboard
    SmartDashboard.putNumber("Distance inches", dist);
    SmartDashboard.putNumber("Network Table Y", yAngle);
  }

  public void getDistance() {
    //Angle (in degrees) the limelight is mounted at
    final double limeLightDefAngle = 16.3; //NEW VALUE NEEDED
    //Add the mount angle to the yangle gotten
    yAngle = yAngle + limeLightDefAngle;
    //Convert the new yangle into radians for the tangent
    yRadians = Math.toRadians(yAngle);
    //Get the tangent of the new yangle
    tyTangent = Math.tan(yRadians);
    //Divide the tangent by the magic number gotten from a sample at 166.6in from target straight on
    dist = 70.5 / tyTangent;           //NEW NUMBER MAY NEED TO BE GOTTEN WITH NEW ROBOT
    //plug the number into the desmos graph
    dist = dist * 1.096 - 16.0466;     //Desmos correction graph REQUIRES TESTING WITH NEW ROBOT
  }

  public void toggleAimAssist() {
    //If its true, make it false and say its not aiming and vice versa
    if (limelightTargetingStatic = true) {
      limelightTargetingStatic = false;
      SmartDashboard.putNumber("No longer aiming", 0);
    } else {
      limelightTargetingStatic = true;
      SmartDashboard.putNumber("Aiming", 1);
    }
  }
  
  public static boolean getLimelightTargeting()
  {
    return limelightTargetingStatic;  
  } 
}