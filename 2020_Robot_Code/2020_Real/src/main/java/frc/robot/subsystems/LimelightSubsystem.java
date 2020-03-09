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



  public class LimelightSubsystem extends SubsystemBase {
    /**
     * Creates a new Limelight.
     */
    public static NetworkTable table;
    public static NetworkTableEntry ty;
    public static NetworkTableEntry tx;
    public double limelightHeight = 25;
    private double tyTangent;
    public double dist;
    public double yAngleDegrees;
    public double yAngleRadians; 
    public static double yOffset;
    public static double xOffset;
    public static double point3Angle;
    public static boolean limelightTargetingStatic = true;
  
    public LimelightSubsystem() {
      table = NetworkTableInstance.getDefault().getTable("limelight");
      ty = table.getEntry("ty");
      limelightTargetingStatic = false;

      //comment this out to turn on limelight light
      
      // toggle = new JoystickButton(frc.robot.Robot.j1, 10);
    }

   // Public methods
   public void updateLimeLightValues() {
    //Get the new yangle value from the network table
    gettyValue();
    //turn the network table value into a double
    yAngleDegrees = yOffset;
    getDistance();
    //Display the values on the Smart Dashboard
    SmartDashboard.putNumber("Distance inches", dist);
    SmartDashboard.putNumber("Network Table Y", yAngleDegrees);
    displayXOffset();
  }
  public double getDistance() {
    //Angle (in degrees) the limelight is mounted at
    final double limeLightDefAngle = 16.3; //NEW VALUE NEEDED
    //Add the mount angle to the yangle gotten
    yAngleDegrees = yAngleDegrees + limeLightDefAngle;
    //Convert the new yangle into radians for the tangent
    yAngleRadians = Math.toRadians(yAngleDegrees);
    //Get the tangent of the new yangle
    tyTangent = Math.tan(yAngleRadians);
    //Divide the tangent by the magic number gotten from a sample at 166.6in from target straight on
    dist = 81.375 / tyTangent;           //NEW NUMBER MAY NEED TO BE GOTTEN WITH NEW ROBOT
    //plug the number into the desmos graph
    //dist = dist * 1.096 - 16.0466;     //Desmos correction graph REQUIRES TESTING WITH NEW ROBOT
  }

  public static void toggleAimAssist() {
    //If its true, make it false and say its not aiming and vice versa
    if (limelightTargetingStatic = true) {
      limelightTargetingStatic = false;
      SmartDashboard.putNumber("No longer aiming", 0);
    } else {
      limelightTargetingStatic = true;
      SmartDashboard.putNumber("Aiming", 1);
    }
  }

  public NetworkTable getNetworkTable(){
    return table;
  } 


  public static double gettxValue(){
    tx = table.getEntry("tx");
    xOffset = tx.getDouble(0.0);
    return xOffset;
    //xOffset += ; WHATEVER ANGLE THE LIMELIGHT IS MOUNTED AT
  }

  public double gettyValue(){
    ty = table.getEntry("ty");
    yOffset = ty.getDouble(0.0);
    return yOffset;
  }

  private void displayXOffset(){
    gettxValue();
    SmartDashboard.putNumber("Degrees off from target:", xOffset);
  }

/*
  //May or may not be used
  public void calculate3PointGoalAngle(){  //this code is a mess im sorry
    NetworkTableEntry tx = table.getEntry("tx");
    double xAngle = tx.getDouble(0.0);
    double limelightDistFromWall = 0; //From Distance Sensors, not yet coded
    double limelightXDistFromTarget;
    xAngle = Math.toRadians(xAngle);
    limelightXDistFromTarget = Math.sin(xAngle) * dist;
    double distTo3Point = Math.sqrt((limelightDistFromWall*limelightDistFromWall) + (limelightXDistFromTarget *limelightXDistFromTarget));
    double angleCLime2Pt3Pt = Math.acos(distTo3Point*distTo3Point - dist*dist -870.25/ (2*dist*870.25));
    point3Angle = Math.asin((29.5 * Math.sin(angleCLime2Pt3Pt)) / distTo3Point);
  }
*/
  // public double get3PointAngle(){
  //   calculate3PointGoalAngle();
  //   return point3Angle;
  // }
  
  public static boolean getLimelightTargeting()
  {
    return limelightTargetingStatic;  
  } 
}