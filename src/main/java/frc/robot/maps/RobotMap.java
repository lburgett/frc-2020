package frc.robot.maps;

import java.util.function.BooleanSupplier;

import com.chopshop166.chopshoplib.maps.DifferentialDriveMap;
import com.chopshop166.chopshoplib.outputs.IDSolenoid;
import com.chopshop166.chopshoplib.outputs.ISolenoid;
import com.chopshop166.chopshoplib.outputs.MockDSolenoid;
import com.chopshop166.chopshoplib.outputs.MockPIDSpeedController;
import com.chopshop166.chopshoplib.outputs.MockSolenoid;
import com.chopshop166.chopshoplib.outputs.MockSpeedController;
import com.chopshop166.chopshoplib.outputs.PIDSpeedController;
import com.chopshop166.chopshoplib.outputs.SmartSpeedController;
import com.chopshop166.chopshoplib.sensors.IEncoder;
import com.chopshop166.chopshoplib.sensors.MockDigitalInput;
import com.chopshop166.chopshoplib.sensors.MockEncoder;

import edu.wpi.first.wpilibj.AddressableLED;

public class RobotMap {

    public DifferentialDriveMap getDriveMap() {
        return new DifferentialDriveMap() {

            @Override
            public SmartSpeedController getRight() {
                return new MockSpeedController();
            }

            @Override
            public SmartSpeedController getLeft() {
                return new MockSpeedController();
            }
        };
    }

    public static class IntakeMap {

        public SmartSpeedController intake() {
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
        public PIDSpeedController shooterWheel() {
            return new MockPIDSpeedController();
        }

        public double shooterHeight() {
            return 0;
        }

    }

    public ShooterMap getShooterMap() {
        return new ShooterMap();

    }

    public static class ControlPanelMap {
        public SmartSpeedController spinner() {
            return new MockSpeedController();
        }
    }

    public ControlPanelMap getControlPanelMap() {
        return new ControlPanelMap();
    }

    public static class LiftMap {

        public PIDSpeedController elevator() {
            return new MockPIDSpeedController();
        }

        public ISolenoid liftBrake() {
            return new MockSolenoid();
        }

        public BooleanSupplier upperLiftLimit() {
            return new MockDigitalInput()::getAsBoolean;
        }

        public BooleanSupplier lowerLiftLimit() {
            return new MockDigitalInput()::getAsBoolean;
        }

        public IEncoder getLiftEncoder() {
            return new MockEncoder();
        }

    }

    public LiftMap getLiftMap() {
        return new LiftMap();
    }

    public static class IndexMap {
        public SmartSpeedController singulator() {
            return new MockSpeedController();
        }

        public SmartSpeedController pierreMotor() {
            return new MockSpeedController();
        }

        public BooleanSupplier frontIntakeIR() {
            return new MockDigitalInput()::getAsBoolean;
        }

        public BooleanSupplier bottomPierreIR() {
            return new MockDigitalInput()::getAsBoolean;
        }

        public BooleanSupplier topPierreIR() {
            return new MockDigitalInput()::getAsBoolean;
        }

        public BooleanSupplier backIntakeIR() {
            return new MockDigitalInput()::getAsBoolean;
        }
    }

    public IndexMap getIndexerMap() {
        return new IndexMap();
    }

    public static class LEDMap {

        public ISolenoid visionRingLightSolenoid() {
            return new MockSolenoid();
        }

        public AddressableLED robotLED() {
            return new AddressableLED(1);
        }

        public int RobotBufferLength() {
            return 0;
        }

    }

    public LEDMap getLEDMap() {
        return new LEDMap();
    }

}