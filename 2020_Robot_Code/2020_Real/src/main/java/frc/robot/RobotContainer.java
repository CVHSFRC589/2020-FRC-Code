/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Joystick;
//import frc.robot.commands.ExampleCommand;

import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.DriveTestSubsystem;
import frc.robot.commands.DefaultDrive;
import frc.robot.commands.DriveController;

import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.ControlPanelSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.PneumaticsSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.LimelightSubsystem;
import frc.robot.commands.UpdateLimelight;
//import frc.robot.subsystems.ExampleSubsystem;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj.Joystick;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  // private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();
  private final LimelightSubsystem m_LimelightSubsystem = new LimelightSubsystem();
  // private final ExampleCommand m_autoCommand = new
  // ExampleCommand(m_exampleSubsystem);

  //private final DriveSubsystem m_drive = new DriveSubsystem();
  
  // USE m_driveTest with default drive (which runs basic arcade)
   private final DriveTestSubsystem m_driveTest = new DriveTestSubsystem();

  // Driver's joystick(s)
  public final static Joystick j1 = new Joystick(0);
  private static final LimelightSubsystem m_light = new LimelightSubsystem();


  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Configure the button bindings
    m_LimelightSubsystem.setDefaultCommand(new UpdateLimelight(m_LimelightSubsystem));
    configureButtonBindings();
    // m_drive.setDefaultCommand(new DriveController(m_drive, () -> j1.getX(), () ->
    // j1.getY(), () -> j1.getZ()));

    // m_drive.setDefaultCommand(
    //   new DriveController(
    //     m_drive, 
    //     () -> j1.getX(), 
    //     () -> j1.getY(), 
    //     () -> j1.getZ()));

    // m_driveTest.setDefaultCommand(
    //   new DefaultDrive(
    //     m_driveTest, 
    //     () -> j1.getY(), 
    //     () -> j1.getX()));

    m_driveTest.setDefaultCommand(
      new DefaultDrive(
        m_driveTest, 
        () -> j1.getY(), 
        () -> j1.getX(),
        () -> j1.getX(), 
        () -> j1.getY(),
        () -> j1.getZ())) ;

  }

  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by instantiating a {@link GenericHID} or one of its subclasses
   * ({@link edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then
   * passing it to a {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    new JoystickButton(frc.robot.Robot.j1, 10)
    .whenPressed(() -> m_light.toggleAimAssist());
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  // public Command getAutonomousCommand() {
  // An ExampleCommand will run in autonomous
  // return m_autoCommand;
  // }
}
