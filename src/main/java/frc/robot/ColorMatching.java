package frc.robot;

import java.util.HashMap;
import java.util.Map;

import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.util.Color;

class ColorMatching
{
    private static final ColorSensorV3 sensor = new ColorSensorV3(Port.kOnboard);

    private static final Color redTarget = ColorMatch.makeColor(0.561, 0.232, 0.114);
    private static final Color yellowTarget = ColorMatch.makeColor(0.361, 0.524, 0.113);
    private static final Color blueTarget = ColorMatch.makeColor(0.143, 0.427, 0.429);
    private static final Color greenTarget = ColorMatch.makeColor(0.197, 0.561, 0.240);

    private static final ColorMatch matcher = new ColorMatch();

    private static final Map<Color, ColorResult> results = new HashMap<>();

    public static final ColorMatching INSTANCE = new ColorMatching();

    private ColorMatching()
    {
        matcher.addColorMatch(redTarget);
        matcher.addColorMatch(yellowTarget);
        matcher.addColorMatch(blueTarget);
        matcher.addColorMatch(greenTarget);

        results.put(redTarget, ColorResult.Red);
        results.put(yellowTarget, ColorResult.Yellow);
        results.put(blueTarget, ColorResult.Blue);
        results.put(greenTarget, ColorResult.Green);
    }

    public ColorMatchResult getRawMatch()
    {
        return matcher.matchClosestColor(sensor.getColor());
    }

    public ColorResult getMatch()
    {
        return results.get(getRawMatch().color);
    }

    public enum ColorResult
    {
        Red, Yellow, Green, Blue, Unknown
    }
}