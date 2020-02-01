/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;

import com.revrobotics.ColorSensorV3;
import com.revrobotics.ColorMatchResult;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.ColorMatch;

public class Robot extends TimedRobot {
  private final I2C.Port i2cPort = I2C.Port.kOnboard;

  private final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);

  private final ColorMatch m_colorMatcher = new ColorMatch();

  private final Color kBlueTarget = ColorMatch.makeColor(0.143, 0.427, 0.429);
  private final Color kGreenTarget = ColorMatch.makeColor(0.197, 0.561, 0.240);
  private final Color kRedTarget = ColorMatch.makeColor(0.561, 0.232, 0.114);
  private final Color kYellowTarget = ColorMatch.makeColor(0.361, 0.524, 0.113);

  @Override
  public void robotInit() {
    m_colorMatcher.addColorMatch(kBlueTarget);
    m_colorMatcher.addColorMatch(kGreenTarget);
    m_colorMatcher.addColorMatch(kRedTarget);
    m_colorMatcher.addColorMatch(kYellowTarget);
  }

  @Override
  public void robotPeriodic() {
    /*
    Wheel Mapping: The wheel is divided into 8 wedges, clockwise pattern is red, yellow, blue, green
    The game sensor is on the outer right edge of the wheel, the easiest way of determining the current
    game color state (what the field is reading as our color) is to line up the robot on the CENTER
    closest to our side. This way we will be skipping one wedge from the game sensor.
    red<-->blue
    green<-->yellow
    */
    Color detectedColor = m_colorSensor.getColor();
    
    String colorDetected; //stores color sensed by the sensor (current robot position)
    String gameColor; //stores current color detected by the game field
    ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);
    
    if (match.color == kBlueTarget) {
      colorDetected = "Blue";
      gameColor = "Red";
    } else if (match.color == kRedTarget) {
      colorDetected = "Red";
      gameColor = "Blue";
    } else if (match.color == kGreenTarget) {
      colorDetected = "Green";
      gameColor = "Yellow";
    } else if (match.color == kYellowTarget) {
      colorDetected = "Yellow";
      gameColor = "Green";
    } else {
      colorDetected = "Unknown";
    }

    SmartDashboard.putNumber("Red", detectedColor.red);
    SmartDashboard.putNumber("Green", detectedColor.green);
    SmartDashboard.putNumber("Blue", detectedColor.blue);
    SmartDashboard.putNumber("Confidence", match.confidence);
    SmartDashboard.putString("Detected Color", colorDetected);
    SmartDashboard.putString("Game/Field Color", gameColor);
  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
  }

  @Override
  public void teleopPeriodic() {
  }

  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }

}
