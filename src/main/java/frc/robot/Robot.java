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

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.ColorMatching.ColorResult;
import frc.robot.Controller.Point;

public class Robot extends TimedRobot {
  private final TalonSRX lf = new TalonSRX(0);
  private final TalonSRX lb = new TalonSRX(1);
  private final TalonSRX rf = new TalonSRX(2);
  private final TalonSRX rb = new TalonSRX(3);

  private final TalonSRX elevatorBack = new TalonSRX(4);
  private final TalonSRX elevatorFront = new TalonSRX(5);
  private final TalonSRX wheel = new TalonSRX(6);

  private final ColorMatching matcher = ColorMatching.INSTANCE;
  private final Controller controller = Controller.INSTANCE;

  @Override
  public void robotInit() {
    lb.follow(lf);
    rb.follow(rf);

    rb.configAuxPIDPolarity(true);
    rf.configAuxPIDPolarity(true);
  }

  @Override
  public void robotPeriodic() {
    ColorMatchResult result = matcher.getRawMatch();
    ColorResult detected = matcher.getMatch();
    ColorResult field = ColorResult.values()[(detected.ordinal() + 2) % 4];

    SmartDashboard.putNumber("Confidence", result.confidence);
    SmartDashboard.putString("Detected Color", detected.name());
    SmartDashboard.putString("Field Color", field.name());
  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
  }

  @Override
  public void teleopPeriodic() {
    Point forward = controller.getMotion();
    lf.set(ControlMode.PercentOutput, forward.x);
    rf.set(ControlMode.PercentOutput, forward.y);

    ColorResult result = ColorResult.values()[(matcher.getMatch().ordinal() + 2) % 4];
    if (controller.getButton(1 /* Button A or whatever */) && result != ColorResult.Green) {
      wheel.set(ControlMode.PercentOutput, 0.25);
    }

    double elevator = controller.getDPadUp();
    elevatorBack.set(ControlMode.PercentOutput, elevator);
    elevatorFront.set(ControlMode.PercentOutput, elevator);
  }

  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }

}
