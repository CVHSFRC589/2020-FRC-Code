/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.ShooterSubsystem;

import java.util.function.DoubleSupplier;

public class ManualAiming extends CommandBase {
  /**
   * Creates a new ManualAiming.
   */

  private ShooterSubsystem shoot;
  private DoubleSupplier xx;
  

  public ManualAiming(ShooterSubsystem tShoot, DoubleSupplier x) {
    shoot = tShoot;
    xx = x;
    // Use addRequirements() here to declare subsystem dependencies.4
    addRequirements(shoot);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    shoot.setAzimuthMotor(xx.getAsDouble());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
