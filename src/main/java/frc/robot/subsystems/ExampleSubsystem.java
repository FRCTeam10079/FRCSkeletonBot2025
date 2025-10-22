package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ExampleSubsystem extends SubsystemBase {

    // Motors and sensors
    // private final TalonFX motor = new TalonFX(0);
    // private final CANcoder encoder = new CANcoder(0);
    // private final CANrange sensor = new CANrange(0);

    // Controls
    // private final PositionVoltage positionControl = new PositionVoltage(0).withSlot(0);

    // Variables
    // private double currentSetpoint = 0;
    // private boolean hasItem = false;

    public ExampleSubsystem() {
        // configure motors and sensors
        // configureMotor();
        // set initial positions
    }

    // Configure motor
    // private void configureMotor() {
    //     // configure motor settings
    // }

    // Set setpoint
    public void setSetpoint(double setpoint) {
        // set motor setpoint
        // currentSetpoint = setpoint;
        // motor.setControl(positionControl.withPosition(setpoint));
    }

    // Example methods
    public double exampleMethod() {
        // example method
        System.out.println("Example Method");
        return 0.0;
    }

    @Override
    public void periodic() {
        // update dashboard
        // SmartDashboard.putNumber("Position", getPosition());
        // SmartDashboard.putBoolean("At Setpoint", isAtSetpoint());
        // SmartDashboard.putBoolean("Item Detected", hasItem());
    }
}