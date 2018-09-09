/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team6679.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	AnalogInput forwards, backwards;
	final int forwardsPin = 1;
	final int backwardsPin = 0;
	final int deadZone = 200;											// deadzone for analog inputs
	
	WPI_VictorSPX master = new WPI_VictorSPX(0);
	
	RobotDrive drive;
	
	AHRS ahrs;

	public void robotInit() {
		
		forwards = new AnalogInput(forwardsPin);
		backwards = new AnalogInput(backwardsPin);
		
		master.set(ControlMode.PercentOutput, 0);			//I think this is the type of value that will be sent out of Can bus to esc
		
		master.setNeutralMode(NeutralMode.Brake);			//I think this is the shit it does when value is 0
		
		
		drive = new RobotDrive(master,master);
	}

	public void autonomousInit() {

	}

	public void autonomousPeriodic() {

	}

	public void teleopInit() {
		System.out.println("Starting Code");
	}

	public void teleopPeriodic() {
		
		float backwardsValue = zeroing(flipAnalog(backwards.getValue(), 3580), 3);
		float forwardsValue = zeroing(forwards.getValue(), 270);
		
		forwardsValue = deadZoneLowBound(forwardsValue, deadZone);
		backwardsValue = deadZoneLowBound(backwardsValue, deadZone);
		
		forwardsValue = analogToOne(forwardsValue, 3100);
		backwardsValue = analogToOne(backwardsValue, 3550);
		
		
		
		System.out.println("Forwards = " + forwardsValue + ": Backwards = " + backwardsValue);
		float outputValue = getGreater(forwardsValue, backwardsValue);
		master.set(ControlMode.PercentOutput, outputValue);
	}

	
	public float getGreater(float a, float b) {
		if(a>b) return a;
		return b;
	}
	public float analogToOne(float value, int scale) {
		float one = value/scale;
		if(one > 1) {
			return 1;
		}
		return one;
	}

	public float flipAnalog(float value, int max) {
		float flip = max - value;
		if (flip < 0) {
			return 0;
		}
		return flip;
	}
	
	public float deadZoneLowBound(float value, int deadZone) {
		if(value <= deadZone) {
			return 0;
		}
		return value;
	}

	public float zeroing(float value, float translate) {
		float zeroed = value - translate;
		if (zeroed < 0) {
			return 0;
		}
		return zeroed;
	}

	public void testPeriodic() {
	}
}
