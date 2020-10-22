package frc.robot.logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.influxdb.dto.Point;

import edu.wpi.first.wpilibj.Sendable;

public class SubsystemLogger {
    RobotLogger logger;
    String subSystemName;
    Vector<InfluxSendable> sendableVector = new Vector<InfluxSendable>();

    public SubsystemLogger(final RobotLogger logger, final String name) {
        this.logger = logger;
        this.subSystemName = name;
    }

    public Point.Builder createPoint() {
        return logger.createPoint(subSystemName);
    }

    public void writePoint(final Point.Builder point) {
        logger.writePoint(point);
    }

    // Register a sendable under this
    public void register(final Sendable sendable, Map<String, String> tags) {
        Map<String, String> subsysTags = new HashMap<String, String>();
        subsysTags.putAll(tags);
        subsysTags.put("Subsystem", this.subSystemName);
        InfluxSendable record = new InfluxSendable(subsysTags);
        sendable.initSendable(record);
        sendableVector.add(record);
    }

    public void updateFields() {
        for (InfluxSendable sendable : sendableVector) {
            Point.Builder point = createPoint();
            sendable.updateValues(point);
            writePoint(point);
        }
    }

}