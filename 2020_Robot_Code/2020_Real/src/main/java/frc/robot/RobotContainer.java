/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
//import frc.robot.commands.ExampleCommand;

import frc.robot.subsystems.DriveSubsystem;
import frc.robot.commands.ConstantDrive;
import frc.robot.commands.DeployIntake;
import frc.robot.commands.DriveController;
import frc.robot.commands.ExtendClimber;
import frc.robot.commands.RetractIntake;
import frc.robot.commands.ReverseIntake;
import frc.robot.commands.SwitchDriveDirection;
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
import edu.wpi.first.wpilibj2.command.button.Button;
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

  private final DriveSubsystem m_drive = new DriveSubsystem();
  private final ClimberSubsystem m_climb = new ClimberSubsystem();
  private final IntakeSubsystem m_intake = new IntakeSubsystem();
  
  // USE m_driveTest with default drive (which runs basic arcade)
   //private final DriveSubsystem m_driveTest = new DriveSubsystem();

  // Driver's joystick(s)
  public static final Joystick j1 = new Joystick(0);

  Button extendClimber;
  Button retractClimber;

  Button deployIntake;
  Button retractIntake;
  Button toggleIntakeActivated;
  Button reverseIntakeWheels;

  Button alignToTarget;
  Button shootBalls;

  Button constantDrive; //Mainly a test

  //Button number assignments    no numbers are final
  final int climberExtend = 1; //j2
  final int climberRetract = 2; //j2
  final int intakeDeploy = 4;  
  final int intakeRetract = 5; //j2
  final int intakeToggle = 3; //j2
  final int intakeReverse = 10; //j2
  final int targetAlign = 2; 
  final int shoot = 1;
  final int constDrive = 7;
 // final int shoot = 1;
  final int switchDriveDirection = 6;

  {
    // extendClimber = new JoystickButton(j1, climberExtend);
    // retractClimber = new JoystickButton(j1, climberRetract);

    // deployIntake = new JoystickButton(j1, intakeDeploy);
    // retractIntake = new JoystickButton(j1, intakeRetract);
    // toggleIntakeActivated = new JoystickButton(j1, intakeToggle);
    // reverseIntakeWheels = new JoystickButton(j1, intakeReverse);

    constantDrive = new JoystickButton(j1, constDrive);
  }


  //The joysticks
  //Joystick m_leftJoystick = new Joystick(1);
  //Joystick m_rightJoystick = new Joystick(2);


  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Configure the button bindings
   // m_LimelightSubsystem.setDefaultCommand(new UpdateLimelight(m_LimelightSubsystem));
    configureButtonBindings();

    m_drive.setDefaultCommand(
      new DriveController(
        m_drive, 
        () -> j1.getY(), 
        () -> j1.getX(),
        () -> j1.getZ()));
    
    constantDrive.toggleWhenPressed(new ConstantDrive(m_drive, 0.5), true);
    //make a button with a when pressed that feeds DriveController negative inputs (to reverse the motors)

  }

  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by instantiating a {@link GenericHID} or one of its subclasses
   * ({@link edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then
   * passing it to a {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    new JoystickButton(j1, switchDriveDirection).whenPressed(new SwitchDriveDirection(m_drive));
    new JoystickButton(j1, climberExtend).whenPressed(new ExtendClimber(m_climb));
    new JoystickButton(j1, climberRetract).whenPressed(new ExtendClimber(m_climb));
    new JoystickButton(j1, intakeDeploy).whenPressed(new DeployIntake(m_intake));
    new JoystickButton(j1, intakeRetract).whenPressed(new RetractIntake(m_intake));
    // new JoystickButton(j1, intakeToggle).whenPressed(new ToggleIntake(m_intake));
    new JoystickButton(j1, intakeReverse).whenPressed(new ReverseIntake(m_intake));
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
