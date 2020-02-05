/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.DriveSubsystem;

public class DriveController extends CommandBase {
  /**
   * Creates a new DriveController.
   */
  private final DriveSubsystem drive;
  private final DoubleSupplier mz;
  private final DoubleSupplier mx;
  private final DoubleSupplier my;
  
  /**
   * 
   * @param drivesys
   * @param xx
   * @param yy
   * @param zz
   */
  public DriveController(DriveSubsystem drivesys, DoubleSupplier xx, DoubleSupplier yy, DoubleSupplier zz) {
    // Use addRequirements() here to declare subsystem dependencies.
    drive = drivesys;
    mz = zz;
    mx = xx;
    my = yy;
    addRequirements(drive);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
     double z = mz.getAsDouble();
     double x = mx.getAsDouble();
     double y = my.getAsDouble(); 
     
    if(z<0){
      z = (1-Math.abs(z))*0.5+0.25;
    }
    else {
      z = z*0.25+0.75;
    }

    if((x<0 && x>-0.1)||(x>0 && x<0.1)){
      x = 0;
    }
    if((y<0 && y>-0.1)||(y>0 && y<0.1)){
      y = 0;
    }

    // //System.out.print(x);
    drive.setMotors(y+x, y-x, z);
    //.setMotors(0.3, 0.3, 0.5);
   // System.out.print("********bob*************************************************************************************************************************");



   // drive.arcadeDrive(my.getAsDouble(), mx.getAsDouble());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
