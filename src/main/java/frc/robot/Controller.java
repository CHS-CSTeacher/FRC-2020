package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.GenericHID.Hand;

class Controller {
    private static final Joystick stick = new Joystick(0);

    public static Controller INSTANCE = new Controller();

    private Controller() {
    }

    public Point getMotion()
    {
        return new Point(stick.getRawAxis(1), stick.getRawAxis(3));
    }

    public boolean getButton(int button)
    {
        return stick.getRawButtonPressed(button);
    }
    
    public boolean getRawButton(int button)
    {
        return stick.getRawButton(button);
    }
    /**
     * Returns a value between -1 and 1, with 1 being up and -1 being down.
     */
    public double getDPadUp()
    {
        int pov = stick.getPOV();
        if (pov > 180) pov = 180 - (pov % 180);
        return 1. - (pov / 90.);
    }

    public class Point {
        double x, y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }
    }
}