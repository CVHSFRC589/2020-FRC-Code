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
  public NetworkTable data;
  public NetworkTable table;
  public NetworkTableEntry tx;
  public NetworkTableEntry ty;
  public NetworkTableEntry ta;
  private boolean aiming;
  public double yDegr;
  public double limelightHeight = 25;
  public double tyTangent;
  public double targetElevation;
  public double dist;
  public double yNegative;
  public double yRadians;
  public double magicNumber = 96.65;

  public double goodX, goodY, goodA;

  public LimelightSubsystem() {
    table = NetworkTableInstance.getDefault().getTable("limelight");
    tx = table.getEntry("tx");
    ty = table.getEntry("ty");
    ta = table.getEntry("ta");

    /*
     * data = NetworkTableInstance.getDefault().getTable("visiondata"); tx =
     * data.getEntry("tx"); ty = data.getEntry("ty"); ta = data.getEntry("ta");
     */
    goodX = 0;
    goodY = 0;
    goodA = 0;
    aiming = false;
    dist = 0;

    // toggle = new JoystickButton(frc.robot.Robot.j1, 10);

  }

  // Public methods
  public void updateLimeLightValues() {
    try{
      // tx = data.getEntry("tx");
      ty = data.getEntry("ty");
      // ta = data.getEntry("ta");      
    } catch(NullPointerException e){
      SmartDashboard.putString("Errors", "yep");
    }


    // double x = tx.getDouble(0.0);
     yNegative = ty.getDouble(0.0);
     //TEMPORARY AIM CORRECTION, NOT PERFECT
    // yNegative = yNegative;
     // double area = ta.getDouble(0.0);

    SmartDashboard.putNumber("Distance inches", dist);
    SmartDashboard.putNumber("Network Table Y", yNegative);
    SmartDashboard.putNumber("Target Elevation", targetElevation);
    SmartDashboard.putNumber("tyTangent", tyTangent);
    SmartDashboard.putNumber("yRadians", yRadians);
    SmartDashboard.putString("Errors", "nah");

    getDistance();
  }

  public void toggleAimAssist() {
    if (aiming = true) {
      aiming = false;
      SmartDashboard.putNumber("No longer aiming", 0);
    } else {
      aiming = true;
      SmartDashboard.putNumber("Aiming", 1);
    }
  }

  public void correctAim() {
    // bennetsMath();
    // IF HOOD
    // aimTurret(yPosition-yDegr);
    // IF AZIMUTH
    // aimTurret(xPosition-tx);
    // rotateRobot(0-ty);
  }

  public void getDistance() {
    yRadians = Math.toRadians(yNegative);
    tyTangent = Math.tan(yRadians);
    targetElevation = magicNumber  - limelightHeight;
    dist = targetElevation / tyTangent;
    // dist should be around 159 in
  }

  public void bennetsMath() {
    double yAngle = yRadians;
    yAngle = yAngle + 0;
    if(aiming){
    
    }
  }
}