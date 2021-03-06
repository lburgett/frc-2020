package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import com.chopshop166.chopshoplib.maps.DifferentialDriveMap;
import com.chopshop166.chopshoplib.outputs.SmartSpeedController;
import com.chopshop166.chopshoplib.sensors.IEncoder;
import com.chopshop166.chopshoplib.PersistenceCheck;

import edu.wpi.first.wpilibj.GyroBase;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Log;

/**
 * 1) What does it do? Makes motors turn a certain amount depending on how much
 * an axis is triggered
 * 
 * 2) What modes does it have? Not at the moment
 * 
 * 3) What interactions does it have with other subsystems? Does not have any
 * interactions with other systems other than the space it shares on the robot
 * 
 * 4) How is it triggered -> OI? No triggers runs by default
 * 
 * 5) Does it store any state? Right and Left motor groups.
 * 
 * 6) Sensors? Encoders, Gyro
 */
public class Drive extends SubsystemBase implements Loggable {

    @Log.SpeedController
    private final SmartSpeedController rightMotorGroup;
    @Log.SpeedController
    private final SmartSpeedController leftMotorGroup;
    @Log.Gyro
    private final GyroBase gyro;

    private final DifferentialDrive driveTrain;
    @Log.Encoder
    private final IEncoder driveRightEncoder;
    @Log.Encoder
    private final IEncoder driveLeftEncoder;
    @Log
    private final PIDController pid;

    private final double ALIGN_PID_FEED = 0.2;

    /**
     * Gets the left and right motor(s) from robot map and then puts them into a
     * differential drive
     * 
     * @param map represents the drive map
     */
    public Drive(final DifferentialDriveMap map) {
        super();
        rightMotorGroup = map.getRight();
        leftMotorGroup = map.getLeft();
        gyro = map.getGyro();
        driveTrain = new DifferentialDrive(leftMotorGroup, rightMotorGroup);
        driveTrain.setRightSideInverted(false);
        pid = new PIDController(0.0106, 0.0004, 0.008);
        driveRightEncoder = rightMotorGroup.getEncoder();
        driveLeftEncoder = leftMotorGroup.getEncoder();
    }

    public CommandBase cancel() {
        final CommandBase cmd = new InstantCommand(() -> {

        }, this);
        cmd.setName("Drive Cancel");
        return cmd;
    }

    public CommandBase drivePastLine() {
        return driveDistance(40, 0.5);
    }

    /**
     * Maps the drive axises
     * 
     * @param forward defines the forward direction
     * @param turn    defines the direction to turn
     * @return returns a run command so drive will stay running as long as drive is
     *         being called
     */
    public CommandBase drive(final DoubleSupplier forward, final DoubleSupplier turn) {
        final CommandBase cmd = new RunCommand(() -> {
            final double yAxis = forward.getAsDouble();
            final double xAxis = turn.getAsDouble();
            driveTrain.arcadeDrive(yAxis, xAxis);
        }, this);
        cmd.setName("Drive");
        return cmd;
    }

    public CommandBase arcadeTurning() {
        final CommandBase cmd = new FunctionalCommand(() -> {
        }, () -> {
            driveTrain.arcadeDrive(0, (SmartDashboard.getNumber("Ratio Offset", 0) * 0.5));
        }, (interrupted) -> {
            driveTrain.stopMotor();
        }, () -> {
            return Math.abs(SmartDashboard.getNumber("Ratio Offset", 0)) <= 0.1;
        }, this);
        cmd.setName("Arcade Drive turning");
        return cmd;
    }

    public CommandBase driveDistance(final double distance, final double speed) {
        final CommandBase cmd = new FunctionalCommand(() -> {
            driveLeftEncoder.reset();
            driveRightEncoder.reset();
        }, () -> {
            driveTrain.arcadeDrive(speed, 0);
        }, (interrupted) -> {
            driveTrain.stopMotor();
        }, () -> {
            final double avg = (driveLeftEncoder.getDistance() + driveRightEncoder.getDistance()) / 2;
            return (avg >= distance);
        }, this);
        cmd.setName("Drive Distance");
        return cmd;
    }

    public CommandBase turnDegrees(final double degrees, final double speed) {
        final CommandBase cmd = new FunctionalCommand(() -> {
            gyro.reset();
        }, () -> {
            double realSpeed = speed;
            if (degrees < 0 && speed > 0) {
                realSpeed *= -1;
            }
            driveTrain.arcadeDrive(0, realSpeed);
        }, (interrupted) -> {
            driveTrain.stopMotor();
        }, () -> {
            return Math.abs(gyro.getAngle()) >= Math.abs(degrees);
        }, this);
        cmd.setName("Turn Degrees");
        return cmd;
    }

    public CommandBase slowTurn(final boolean isTurningRight) {
        final CommandBase cmd = new RunCommand(() -> {
            if (isTurningRight) {
                driveTrain.arcadeDrive(0, 0.4);
            } else {
                driveTrain.arcadeDrive(0, -0.4);

            }
        }, this);
        cmd.setName("Turning");
        return cmd;
    }

    public CommandBase visionAlignDegrees() {
        final CommandBase cmd = new CommandBase() {
            {
                addRequirements(Drive.this);
            }
            int i;
            PersistenceCheck check = new PersistenceCheck(25, () -> {
                return (pid.atSetpoint() || !SmartDashboard.getBoolean("Sees Target", false));

            });

            @Override
            public void initialize() {
                gyro.reset();
                pid.setSetpoint(SmartDashboard.getNumber("Angle Offset", 0));
                pid.setTolerance(0.75);
            }

            @Override
            public void execute() {

                if (pid.getPositionError() <= 5 && (i % 50 == 0)) {
                    gyro.reset();
                    pid.setSetpoint((SmartDashboard.getNumber("Angle Offset", 0)));
                    i = 0;
                }

                double turning = pid.calculate(-gyro.getAngle());
                turning += (turning < 0) ? -ALIGN_PID_FEED : ALIGN_PID_FEED;
                SmartDashboard.putNumber("pid Out", turning);
                driveTrain.arcadeDrive(0, turning);
                i++;
            }

            @Override
            public boolean isFinished() {
                return check.getAsBoolean();
            }

            @Override
            public void end(final boolean interrupted) {
                driveTrain.stopMotor();

            }
        };

        cmd.setName("Turn Degrees");

        return cmd;

    }
}
