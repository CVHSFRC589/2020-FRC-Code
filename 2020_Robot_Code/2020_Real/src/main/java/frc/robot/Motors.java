/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.EncoderType;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

/**
 * Add your docs here.
 */
public class Motors {
    public static class Manager{
        public static double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, maxRPM;
        private static CANPIDController m_pidController;
        public static void initialize(CANSparkMax motor) {
            motor.set(0);
            motor.restoreFactoryDefaults();
            m_pidController = motor.getPIDController();

        }
        
        public static void initializePID(){
            // PID coefficients
            kP = 5e-5; 
            kI = 1e-6;
            kD = 0; 
            kIz = 0; 
            kFF = 0; 
            kMaxOutput = 1; 
            kMinOutput = -1;
            maxRPM = 5700;

            // set PID coefficients
            m_pidController.setP(kP);
            m_pidController.setI(kI);
            m_pidController.setD(kD);
            m_pidController.setIZone(kIz);
            m_pidController.setFF(kFF);
            m_pidController.setOutputRange(kMinOutput, kMaxOutput);
        }

        public static void setPIDSpeed(CANSparkMax motor, double speed){
            //do pid control
            m_pidController.setReference(speed, ControlType.kVelocity);
        }
    }
}
