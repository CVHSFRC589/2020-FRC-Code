/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.util.Hashtable;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
//import frc.robot.commands.ExampleCommand;

import frc.robot.subsystems.DriveSubsystem;
import frc.robot.commands.ToggleIntake;
import frc.robot.commands.ToggleLimelightLED;
import frc.robot.commands.TrenchSpeed;
import frc.robot.commands.DriveToDistance;
import frc.robot.commands.AdvancedAiming;
import frc.robot.commands.AutomaticAiming;
import frc.robot.commands.ChangeCameraMode;
import frc.robot.commands.ChangeLoadDirection;
import frc.robot.commands.ChangeShootMode;
import frc.robot.commands.ChangeStreamMode;
import frc.robot.commands.DefaultShoot;
import frc.robot.commands.DeployIntake;
import frc.robot.commands.DriveController;
import frc.robot.commands.DrivePID;
import frc.robot.commands.ExtendClimber;
import frc.robot.commands.InitiationLineSpeed;
import frc.robot.commands.LimelightLEDON;
import frc.robot.commands.RetractClimber;
import frc.robot.commands.RetractIntake;
import frc.robot.commands.ReverseIntake;
import frc.robot.commands.ShootPID;
import frc.robot.commands.SmartShoot;
import frc.robot.commands.SwitchDriveDirection;
import frc.robot.commands.DriveController;
import frc.robot.commands.UpdateLimelight;
import frc.robot.commands.ManualAiming;
import frc.robot.commands.ManuallyShoot;
import frc.robot.commands.ManuallyLoad;
import frc.robot.commands.DrivePID;

import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.ControlPanelSubsystem;
//import frc.robot.subsystems.LEDSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.PneumaticsSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.LimelightSubsystem;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
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
  private final LimelightSubsystem m_LimelightSubsystem = new LimelightSubsystem();

  public final DriveSubsystem m_drive = new DriveSubsystem();
  private final ShooterSubsystem m_shoot = new ShooterSubsystem();
  private final ClimberSubsystem m_climb = new ClimberSubsystem();
  //private final LEDSubsystem m_led = new LEDSubsystem();
  private final IntakeSubsystem m_intake = new IntakeSubsystem();
  public final AdvancedAiming m_aim = new AdvancedAiming(m_shoot);

  private static boolean m_toggleAutoAim = false;
  

  // Driver's joystick(s)
  public static final Joystick j1 = new Joystick(0);
  public static final Joystick j2 = new Joystick(1);

  Button constantDrive; //Mainly a test

  //Button number assignments
  //j1
  final int climberExtend = 11; //j1
  final int climberRetract = 10; //j1
  final int switchDriveDirection = 6; //j1
  final int changeStreamMode = 9; //j1
  final int switchCameraMode = 8; //j1


  //j2
  final int intakeDeploy = 4; //j2
  final int intakeRetract = 5; //j2
  final int intakeToggle = 3; //j2
  final int intakeReverse = 8; //j2
  //final int targetAlign = 7; //j2
  final int loadBall = 1; //j2
  final int reverseLoad = 9; //j2
  final int shootBall = 2; //j2
  final int toggleLimelightLED = 6; //j2
  final int initiationLineSpeed = 11; //j2
  final int trenchSpeed = 12; //j2
  final int changeShootMode = 10;//j2

  //final int constDrive = 7;
  //{constantDrive = new JoystickButton(j1, constDrive);}

  public final Hashtable<String, CommandBase> commands = new Hashtable<String, CommandBase>();

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Configure the button bindings
    //m_LimelightSubsystem.setDefaultCommand(new UpdateLimelight(m_LimelightSubsystem));
    configureButtonBindings();

    //Default command for shooter (MANUAL)
    // m_shoot.setDefaultCommand(
    //   new ManualAiming(m_shoot, 
    //     () -> j2.getZ()));

    //Real default command for shooter(auto)
    m_shoot.setDefaultCommand(
      new DefaultShoot(m_shoot,
        () -> j2.getZ()));


    //Default command for drive
    //used for arcadedrive
    m_drive.setDefaultCommand(
      new DriveController(
        m_drive, 
        () -> j1.getY(),  //can set to j2.getZ() to use twist reading on joystick
        () -> j1.getX(),
        () -> j1.getZ()));  //j1.getZ() to use a dynamic multiplier

    // m_drive.setDefaultCommand(
    //   new DrivePID(
    //     m_drive));
    // m_shoot.setDefaultCommand(
    //   new ShootPID(m_shoot));    
      


    AutomaticAiming A = new AutomaticAiming(m_shoot);
    commands.put("AutomaticAiming", A);
    ManuallyShoot M = new ManuallyShoot(m_shoot);
    commands.put("ManuallyShoot", M);
    DeployIntake D = new DeployIntake(m_intake);
    commands.put("DeployIntake", D);
    ToggleIntake T = new ToggleIntake(m_intake);
    commands.put("ToggleIntake", T);
    RetractIntake R = new RetractIntake(m_intake);
    commands.put("RetractIntake", R);
    ManuallyLoad M2 = new ManuallyLoad(m_shoot);
    commands.put("ManuallyLoad", M2);
    LimelightLEDON L = new LimelightLEDON(m_shoot, m_LimelightSubsystem);
    commands.put("LimelightLEDON", L);
    
    //constantDrive.toggleWhenPressed(new DriveToDistance(m_drive, 24), true);
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by instantiating a {@link GenericHID} or one of its subclasses
   * ({@link edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then
   * passing it to a {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    //configure button bindings for each command, constants can be found
    //new JoystickButton(j1, switchDriveDirection).whenPressed(new SwitchDriveDirection(m_drive), true);

    new JoystickButton(j1, climberExtend).whenPressed(new ExtendClimber(m_climb));
    new JoystickButton(j1, climberRetract).whenPressed(new RetractClimber(m_climb));

    new JoystickButton(j2, intakeDeploy).whenPressed(new DeployIntake(m_intake));
    new JoystickButton(j2, intakeToggle).whenPressed(new ToggleIntake(m_intake));
    new JoystickButton(j2, intakeRetract).whenPressed(new RetractIntake(m_intake));
    new JoystickButton(j2, intakeReverse).whenPressed(new ReverseIntake(m_intake));
    
    //Automated Shooting
    //new JoystickButton(j2, targetAlign).whenPressed(new AutomaticAiming(m_shoot), true);
    // new JoystickButton(j2, targetAlign).whileHeld(new AdvancedAiming(m_shoot));  
    new JoystickButton(j2, changeShootMode).whenPressed(new ChangeShootMode(m_shoot));

    //Manual Shooting
    //new JoystickButton(j2, shootBall).whenPressed(new ManuallyShoot(m_shoot));
    new JoystickButton(j2, shootBall).whenPressed(new SmartShoot(m_shoot));
    //reverse direction of loadBall while button is held
    new JoystickButton(j2, reverseLoad).whenPressed(new ChangeLoadDirection());
    new JoystickButton(j2, reverseLoad).whenReleased(new ChangeLoadDirection());

    new JoystickButton(j2, loadBall).whenPressed(new ManuallyLoad(m_shoot));
    new JoystickButton(j2, loadBall).whenReleased(new ManuallyLoad(m_shoot));
    new JoystickButton(j2, initiationLineSpeed).whenPressed(new InitiationLineSpeed(m_shoot));
    new JoystickButton(j2, trenchSpeed).whenPressed(new TrenchSpeed(m_shoot));


    new JoystickButton(j1, changeStreamMode).whenPressed(new ChangeStreamMode(m_shoot));
    new JoystickButton(j1, switchCameraMode).whenPressed(new ChangeCameraMode(m_shoot));
    new JoystickButton(j2, toggleLimelightLED).whenPressed(new ToggleLimelightLED(m_shoot, m_LimelightSubsystem));
    
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
