package frc.robot.subsystems;

import com.chopshop166.chopshoplib.outputs.PIDSpeedController;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.maps.RobotMap;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.lang.Math;

/**
 * What does it do? Given a target and shooter height, it calculates velocity
 * needed to hit said target, and spins the motors at a corresponding RPM. What
 * modes does it have? Semi-Auto and DUMP. what interactions does it have with
 * other subsystems? Asks the Indexer if there is a ball, and get's information
 * about the target from vision. How is it triggered/OI? A semi-auto button A-
 * or X for shoot all. Does it store any state? No. Sensors? No.
 */

public class Shooter extends SubsystemBase {
    private final PIDSpeedController shooterWheelMotor;
    public static double distanceToTarget;
    public final double shooterHeight;
    public static double verticalDistance;

    // inches/second/second
    public final static double GRAVITY = 386.2205;
    // inches
    public final static double TARGET_HEIGHT = 98.25;
    private final double MAX_RPM = 5200;
    public final static double THETA = Math.toRadians(37);
    public final static double BALL_SPEED_RATIO = 27.358;

    public Shooter(final RobotMap.ShooterMap map) {
        super();
        distanceToTarget = SmartDashboard.getNumber("Distance To Target", 0);
        shooterHeight = map.shooterHeight();
        shooterWheelMotor = map.shooterWheel();
        verticalDistance = TARGET_HEIGHT - shooterHeight;
    }

    public CommandBase spinUp() {
        return new InstantCommand(() -> {
            shooterWheelMotor.set(calculateRPM() / MAX_RPM);
        }, this);
    }

    public CommandBase spinDown() {
        return new InstantCommand(shooterWheelMotor::stopMotor, this);
    }

    /*
     * Calculates RPM with some gear ratio mathematics. (returns inches/second) Also
     * applies a 10% loss.
     */
    public static double calculateRPM() {
        return calculateVelocity() * BALL_SPEED_RATIO * 0.9;
    }

    /*
     * Finds the needed velocity to reach a target (x, y) or (distanceToTarget,
     * verticalDistance). The formula takes takes theta or launch angle, target and
     * gravity.
     */
    public static double calculateVelocity() {
        if (distanceToTarget * Math.tan(THETA) >= verticalDistance) {
            final double gravitySide = GRAVITY * distanceToTarget * distanceToTarget;
            final double tanSide = distanceToTarget * Math.tan(THETA) - verticalDistance;
            final double cosSide = Math.cos(THETA) * Math.cos(THETA);

            return Math.sqrt(gravitySide / tanSide / cosSide / 2);
        } else {
            return 0;
        }
    }
}