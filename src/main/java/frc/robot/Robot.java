/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.ColorMatchResult;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.ColorMatching.ColorResult;
import frc.robot.Controller.Point;
import edu.wpi.first.wpilibj.Compressor;

public class Robot extends TimedRobot {
  private final TalonSRX lm = new TalonSRX(1);
  private final TalonSRX lf = new TalonSRX(2);
  private final TalonSRX rm = new TalonSRX(4);
  private final TalonSRX rf = new TalonSRX(3);

  private final TalonSRX elevatorfront = new TalonSRX(7);
  private final TalonSRX elevatorback = new TalonSRX(6);
  private final TalonSRX wheel = new TalonSRX(5);

  private final DoubleSolenoid hatch = new DoubleSolenoid(4, 5);
  private final DoubleSolenoid wheelDeploy = new DoubleSolenoid(6, 7);
  private final Compressor compressor = new Compressor();
  
  private final ColorMatching matcher = ColorMatching.INSTANCE;
  private final Controller controller = Controller.INSTANCE;
  private boolean hatchState = false;
  private boolean wheelIsSpinning = false;
  private boolean elevatorOn = false;
  private boolean driveSwitch = true;

  private final SendableChooser<String> colorInput = new SendableChooser<String>();
  @Override
  public void robotInit() {
    lf.follow(lm);
    rf.follow(rm);

    rf.configAuxPIDPolarity(true);
    rm.configAuxPIDPolarity(true);
    compressor.start();
    colorInput.setDefaultOption("Red", "Red");
    colorInput.addOption("Blue", "Blue");
    colorInput.addOption("Green", "Green");
    colorInput.addOption("Yellow", "Yellow");
  }

  @Override
  public void robotPeriodic() {
    final ColorMatchResult result = matcher.getRawMatch();
    final ColorResult detected = matcher.getMatch();
    final ColorResult field = ColorResult.values()[(detected.ordinal() + 2) % 4];

    SmartDashboard.putNumber("Confidence", result.confidence);
    SmartDashboard.putString("Detected Color", detected.name());
    SmartDashboard.putString("Field Color", field.name());
    SmartDashboard.putBoolean("Drive Switch", driveSwitch);
    SmartDashboard.putData(colorInput);
  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
  }

  @Override
  public void teleopPeriodic() {
    final Point forward = controller.getMotion();
    if(controller.getButton(10)) {
      driveSwitch = !driveSwitch;
    }
    if(driveSwitch){
      lm.set(ControlMode.PercentOutput, forward.y*.5);
      rm.set(ControlMode.PercentOutput, -forward.x*.5);
    }
    else {
      lm.set(ControlMode.PercentOutput, -forward.x*.5);
      rm.set(ControlMode.PercentOutput, forward.y*.5);
    }
    
    SmartDashboard.putString("stickLeft", "" + forward.x);
    SmartDashboard.putString("stickRight", "" + forward.y);
    final ColorResult result = ColorResult.values()[(matcher.getMatch().ordinal() + 2) % 4];
    //SmartDashboard.putString("color?", "Red");
    //SmartDashboard.getString("color?", "none");

    if (controller.getButton(3)) {
      wheelIsSpinning = !wheelIsSpinning;
    }
    if (wheelIsSpinning) {
      wheel.set(ControlMode.PercentOutput, .75);
    }
    else {
      wheel.set(ControlMode.PercentOutput, 0);
    }

    //double elevator = controller.getDPadUp();
    if(controller.getButton(2)) {
      elevatorOn = !elevatorOn;
      elevatorfront.set(ControlMode.PercentOutput, 1);
      elevatorback.set(ControlMode.PercentOutput, 1);
    }
    else if(controller.getButton(4)){
      elevatorOn = !elevatorOn;
      elevatorfront.set(ControlMode.PercentOutput, -1);
      elevatorback.set(ControlMode.PercentOutput, -1);
    }
    else if(!elevatorOn){
      elevatorfront.set(ControlMode.PercentOutput, 0);
      elevatorback.set(ControlMode.PercentOutput, 0);
    }
    
    if(controller.getButton(5)) {
      hatch.set(Value.kReverse);
    }
    if(controller.getButton(6)) {
      hatch.set(Value.kForward);
    }

    if(controller.getButton(1)) {
      hatchState = !hatchState;
    }
    if(hatchState){
      wheelDeploy.set(Value.kForward);
    }
    else {
      wheelDeploy.set(Value.kReverse);
    }
  }

  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }

}
