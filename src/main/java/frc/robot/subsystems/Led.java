package frc.robot.subsystems;

import com.chopshop166.chopshoplib.outputs.ISolenoid;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.maps.RobotMap.LEDMap;

public class Led extends SubsystemBase {
    AddressableLED liftLED;
    ISolenoid ringLight;
    AddressableLEDBuffer liftBuffer;

    public Led(LEDMap map) {

        liftBuffer = new AddressableLEDBuffer(map.liftBufferLength());
        liftLED = map.liftLED();
        ringLight = map.visionRingLightSolenoid();

    }

    public CommandBase ringLightOn() {
        CommandBase cmd = new InstantCommand(() -> {
            ringLight.set(true);
        }, this);
        cmd.setName("Ring Light On");
        return cmd;
    }

    public CommandBase ringLightOff() {
        CommandBase cmd = new InstantCommand(() -> {
            ringLight.set(false);
        }, this);
        cmd.setName("Ring Light Off");
        return cmd;
    }

    public CommandBase robotLedColor(int hue, int saturation, int value) {
        CommandBase cmd = new InstantCommand(() -> {
            for (var i = 0; i < liftBuffer.getLength(); i++) {
                liftBuffer.setHSV(i, hue, saturation, value);
            }
        }, this);
        cmd.setName("Vision Stream");
        return cmd;
    }
}