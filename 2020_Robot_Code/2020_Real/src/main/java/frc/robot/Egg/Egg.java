/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Egg;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Robot;
import frc.robot.Constants.DriveConstants;

import java.util.*;
import java.awt.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.SPI;
import frc.robot.commands.*;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.Egg.Pathfinding.*;
import frc.robot.Egg.Utility.*;
import frc.robot.Egg.Utility.Math.*;
import frc.robot.Constants;

public class Egg extends CommandBase {
  Robot robot;
  Schedule schedule;
  PurePursuit purePursuit;
  Task task;

  NetworkTableEntry Schedule;
  NetworkTable table;
  NetworkTableInstance inst;

  boolean hasPure = false;

  boolean done = false;

  double Vmax;

  public Hashtable<String, CommandBase> Commands = new Hashtable<String, CommandBase>();

  public Egg(Robot R, Hashtable<String, CommandBase> commands, double maxSpeed) {
    // addRequirements(Robot.drive);
    robot = R;
    Commands = commands;


    Vmax = maxSpeed;
  }

  void getScheduleFromNT() {
    inst = NetworkTableInstance.getDefault();
    table = inst.getTable("Nest");
    Schedule = table.getEntry("Schedule");
    inst.startClientTeam(589);
    // inst.startDSClient();

    byte[] empty = { 1, 0, 0, 1, 0, 1, 0, 0, 1, 1, 1, 0, 0 };
    byte[] temp;

    Schedule.setRaw(empty);

    System.out.println("Checking");

    while (Arrays.equals(Schedule.getRaw(empty), empty)) {
      try {
        Thread.sleep(10);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    temp = Schedule.getRaw(empty);

    try (ByteArrayInputStream bais = new ByteArrayInputStream(temp);
        ObjectInputStream ois = new ObjectInputStream(bais);) {
      schedule = (Schedule) ois.readObject();
    } catch (IOException E) {
      System.out.println("-------------------------------------");
      System.out.println("Cannot Deserialize(IO Exception): " + E.getMessage() + " " + E.getCause());
      System.out.println("-------------------------------------");
      return;
    } catch (ClassNotFoundException E2) {
      System.out.println("-------------------------------------");
      System.out.println("Cannot Deserialize(Class Error): " + E2.getMessage() + " " + E2.getCause());
      System.out.println("-------------------------------------");
      return;
    }

    System.out.println("Deserialized");
  }

  boolean getScheduleFromFile() {
    try {
      FileInputStream fileIn = new FileInputStream("Schedule.dat");
      ObjectInputStream in = new ObjectInputStream(fileIn);
      schedule = (Schedule) in.readObject();
      in.close();
      fileIn.close();
    } catch (IOException i) {
      i.printStackTrace();
      return false;
    } catch (ClassNotFoundException c) {
      System.out.println("Schedule class not found");
      c.printStackTrace();
      return false;
    }

    return true;
  }

  void getTask() {
    if (schedule == null) {
      // getScheduleFromFile();
    }

    task = schedule.getNext();
    if (task.method == "Null") {
      done = true;
    }
  }

  void writeScheduleToFile(Schedule S) {
    try {
      FileOutputStream fstream = new FileOutputStream("Schedule.dat");
      ObjectOutputStream out = new ObjectOutputStream(fstream);
      out.writeObject(S);
      // Close the output stream
      out.close();
    } catch (Exception e) {// Catch exception if any
      System.err.println("Error: " + e.getMessage());
    }
  }

  boolean runningCommand = false;

  void executeTask() {
    if (task == null) {
      getTask();
    }

    if (done) {
      return;
    }

    System.out.println("***********" + task.command + "**********");

    if (task.path != null && purePursuit == null) {
      configureDrive();
    } else if (task.time != 0) {
      pause(task.time);
    } else if (task.command != null) {
      handleCommand();
    }

    

  }

  void handleCommand() {
    if (!runningCommand && purePursuit == null) {
      runningCommand = true;
      CommandScheduler.getInstance().schedule(Commands.get(task.command));
      try {
        if (task.method != null) {
          Method M = Commands.get(task.command).getClass().getDeclaredMethod(task.method);
          M.invoke(task.command);
        }
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      
    } else {
      if (runningCommand) {
        if (CommandScheduler.getInstance().isScheduled(Commands.get(task.command))) {
          runningCommand = false;
          task = null;
        } 
      }
    }
  }

  void configureDrive() {
    Path P = task.path;
    purePursuit = new PurePursuit(P, b);
  }

  boolean paused = false;
  double time;

  void pause(long duration) {
    if (!paused) {
      time = System.currentTimeMillis();
    }
    paused = true;

    if (System.currentTimeMillis() - time >= duration * 1000) {
      task = null;
      paused = false;
    }    
  }
  
  

  protected double x, y, angle = 0, b = 22;
  double oldEval = 0, oldLE = 0, oldRE = 0;
  protected AHRS Navx;  

  // Called just before this Command runs the first time
  @Override
  public void initialize() {

    System.out.println("******Running******");

    ArrayList<Task> T = new ArrayList<Task>();
    ArrayList<DoublePoint> Points = new ArrayList<DoublePoint>();
    Path P;
            
    T.add(new Task("AutomaticAiming", null));
    T.add(new Task("ManuallyShoot", null));
    T.add(new Task(3));
    T.add(new Task("ManuallyShoot", null));
    T.add(new Task("DeployIntake", null));
    T.add(new Task("ToggleIntake", null));
    Points = new ArrayList<DoublePoint>();
    Points.add(new DoublePoint(0, 0));
    Points.add(new DoublePoint(-100, 0));
    P = new Path(Points);
    P.calculate();
    T.add(new Task(P, true));
    T.add(new Task("ToggleIntake", null));
    T.add(new Task("RetractIntake", null));
    Points = new ArrayList<DoublePoint>();
    Points.add(new DoublePoint(-100, 0));
    Points.add(new DoublePoint(-10, 0));
    P = new Path(Points);
    P.calculate();
    T.add(new Task(P, true));
    T.add(new Task("AutomaticAiming", null));
    T.add(new Task("ManuallyShoot", null));
    T.add(new Task(3));
    T.add(new Task("ManuallyShoot", null));
    Schedule S = new Schedule(T);
    schedule = S;

    //writeScheduleToFile(S);

    //if (getScheduleFromFile()) {

    //} else {
      //done = true;
    //}


    Robot.driveSubsystem.setLeft(0);
    Robot.driveSubsystem.setRight(0);
    
    x = (double)0;
    y = (double)0;

    Navx = new AHRS(SPI.Port.kMXP);

    Navx.reset();

  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  public void execute() {

    if (done) {
      return;
    }

    if (task == null) {
      getTask();
    }

    executeTask();
    
    if (purePursuit != null) {
      drive();
    }
    

    
    
  }

  void drive() {
    //System.out.println("Driving");
    angle = Navx.getAngle();
    //angle += 90;
    //System.out.println("***************" + angle);
    trackPos();

    double r = -10;
    double Vr = 0, Vl = 0;


    if (purePursuit.isFinished()) {
      purePursuit = null;
      task = null;
      r = 0;
      System.out.println("Next");
      return;
    } else {
      r = purePursuit.get(x, y, angle);
    }

    //System.out.println(r);

    if (r > 0) {
      r -= b / 2;
      Vr = Vmax;
      Vl = ((r * Vr) / b) / (1 + r / b);
    } else if (r < 0) {
      r *= -1;
      r -= b / 2;
      Vl = Vmax;
      Vr = ((r * Vl) / b) / (1 + r / b);

    } else {
      Vl = 0;
      Vr = 0;
    }

    Vr = Clamp.clamp(0, Vmax, Vr);
    Vl = Clamp.clamp(0, Vmax, Vl);

    //double temp = Vr;
    //Vr = Vl;
    //Vl = temp;

    System.out.println("Values: " + Vr + " " + Vl);

    if (task.backwards) {
      double temp = Vr;
      Vr = -Vl;
      Vl = -temp;
    }
    Robot.driveSubsystem.setMotors(Vl, -Vr, 1);

    System.out.println("X: " + (int)x + " Y: " + (int)y + " Angle: " + (int)angle);
  }
  
  void trackPos() {
    boolean negative = (angle < 0) ? true : false; 
    angle = Math.abs(angle);
    while (angle  > 360) {
      angle -= 360;
    }

    if (task.backwards) {
      angle = 360 - angle;
    }

    angle = (negative) ? 360 - angle : angle;

    double EncoderR = -1 * Robot.driveSubsystem.getRight();
    double EncoderL = Robot.driveSubsystem.getLeft();

    //System.out.println(EncoderL);

    double distance = ((oldLE - EncoderL) + (oldRE - EncoderR)) / 2;// - oldEval;
    //System.out.println(EncoderR + " " + EncoderL);
    distance /= DriveConstants.kEncoderCPI;
    //System.out.println(distance);

    x -= Math.cos(angle * Math.PI / 180) * distance;
    y += Math.sin(angle * Math.PI / 180) * distance;

    oldLE = EncoderL;
    oldRE = EncoderR;

    oldEval = distance;
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  public boolean isFinished() {
    return done;
  }

  // Called once after isFinished returns true
  //@Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  //@Override
  protected void interrupted() {
  }
}
