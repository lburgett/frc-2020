package frc.robot.maps;

import com.chopshop166.chopshoplib.maps.DifferentialDriveMap;
import com.chopshop166.chopshoplib.outputs.IDSolenoid;
import com.chopshop166.chopshoplib.outputs.MockDSolenoid;
import com.chopshop166.chopshoplib.outputs.MockSpeedController;
import com.chopshop166.chopshoplib.outputs.SendableSpeedController;

public class RobotMap {

    public DifferentialDriveMap getDriveMap() {
        return null;
    }

    public static class IntakeMap {
        public SendableSpeedController intake() {
            return new MockSpeedController();
        }

        public IDSolenoid deployIntake() {
            return new MockDSolenoid();
        }
    }

    public IntakeMap getIntakeMap() {
        return new IntakeMap();
    }

    public static class ShooterMap {
        public SendableSpeedController shooterWheel() {
            return new MockSpeedController();
        }
    }

    public ShooterMap getShooterMap() {
        return new ShooterMap();
    }

    public static class ControlPanelMap {
        public SendableSpeedController spinner() {
            return new MockSpeedController();
        }
    }

    public ControlPanelMap getControlPanelMap() {
        return new ControlPanelMap();
    }

    public static class LiftMap {
        public SendableSpeedController elevator() {
            return new MockSpeedController();
        }
    }

    public LiftMap getLiftMap() {
        return new LiftMap();
    }

}