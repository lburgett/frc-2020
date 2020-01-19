package frc.robot.subsystems;

import com.chopshop166.chopshoplib.outputs.SendableSpeedController;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.maps.RobotMap;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;

import com.revrobotics.ColorSensorV3;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorMatch;

public class ControlPanel extends SubsystemBase {

    private SendableSpeedController spinnerMotor;

    private static final double spinnerMotorSpeed = 1;

    public ControlPanel(RobotMap.ControlPanelMap map) {
        super();
        spinnerMotor = map.spinner();
    }

    public CommandBase spinForwards() {
        return new StartEndCommand(() -> {
            spinnerMotor.set(-spinnerMotorSpeed);
        }, () -> {
            spinnerMotor.stopMotor();
        }, this);
    }

    public CommandBase spinBackwards() {
        return new StartEndCommand(() -> {
            spinnerMotor.set(spinnerMotorSpeed);
        }, () -> {
            spinnerMotor.stopMotor();
        }, this);
    }

    public final I2C.Port i2cPort = I2C.Port.kOnboard;

    public final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);

    public final Color kBlueTarget = ColorMatch.makeColor(0.143, 0.427, 0.429);
    public final Color kGreenTarget = ColorMatch.makeColor(0.197, 0.561, 0.240);
    public final Color kRedTarget = ColorMatch.makeColor(0.561, 0.232, 0.114);
    public final Color kYellowTarget = ColorMatch.makeColor(0.361, 0.524, 0.113);

    public final ColorMatch m_colorMatcher = new ColorMatch();
    public static String colorString1 = "";

    public void detectColor() {

        m_colorMatcher.addColorMatch(kBlueTarget);
        m_colorMatcher.addColorMatch(kGreenTarget);
        m_colorMatcher.addColorMatch(kRedTarget);
        m_colorMatcher.addColorMatch(kYellowTarget);

        Color detectedColor = m_colorSensor.getColor();

        String colorString;
        ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);
        colorString1 = "";

        if (match.color == kBlueTarget) {
            colorString = "Blue";
        } else if (match.color == kRedTarget) {
            colorString = "Red";
        } else if (match.color == kGreenTarget) {
            colorString = "Green";
        } else if (match.color == kYellowTarget) {
            colorString = "Yellow";
        } else {
            colorString = "Unknown";
        }

        /**
         * Open Smart Dashboard or Shuffleboard to see the color detected by the sensor.
         */
        SmartDashboard.putNumber("Red", detectedColor.red);
        SmartDashboard.putNumber("Green", detectedColor.green);
        SmartDashboard.putNumber("Blue", detectedColor.blue);
        SmartDashboard.putNumber("Confidence", match.confidence);
        SmartDashboard.putString("Detected Color", colorString);
        colorString1 = colorString;

    }

    public void stageTwoRotation() {

        int iRed = 0;
        int iBlue = 0;
        int iGreen = 0;
        int iYellow = 0;
        int iUnk = 0;
        spinForwards();

        for (int i = 0; i < 100; i++) {
            detectColor();
            if (colorString1 == "Red") {
                iRed++;

            } else if (colorString1 == "Blue") {
                iBlue++;
            } else if (colorString1 == "Green") {
                iGreen++;
            } else if (colorString1 == "Yellow") {
                iYellow++;
            } else if (colorString1 == "Blue") {
                iBlue++;
            } else if (colorString1 == "Unknown") {
                iUnk++;
            }

            if (iGreen + iRed + iUnk + iYellow + iBlue == 36) {
                // this is 36 because that means that the wheel would have spun 4 rotations

            }

        }

    }

};

// I want to make a function that when engaged will take a reading of the color,
// mark that in an array, then, slowly rotate the color wheel and record every
// new color as a
// new member of the array, once the array has reached 24-40 changes (8 changes
// per rotation, 3-5 rotations). once this number of members has been reached in
// the array the motors stop
