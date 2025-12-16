package frc.robot.subsystems;

import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.CANrange;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class PivotSubsystem extends SubsystemBase {

    // Motors and sensors
    private final TalonFX motor = new TalonFX(0);
    private final CANcoder encoder = new CANcoder(0);
    private final CANrange sensor = new CANrange(0);

    // Controls
    private final PositionVoltage positionControl = new PositionVoltage(0).withSlot(0);

    // Variables
    private double currentSetpoint = 0;
    private double currentAngle = 0; // 0 is pointing straight forward, PI/2 is pointing left, -PI/2 is pointing right
    private boolean hasItem = true; // always true for now

    public PivotSubsystem() {
        // configure motors and sensors
        configureMotor();
        // set initial positions
    }

    // Configure motor
    private void configureMotor() {
         // configure motor settings
        motor.getPosition().setUpdateFrequency(100);
        encoder.getPosition().setUpdateFrequency(100);
    }

    // Set setpoint
    public void setSetpoint(double setpoint) {
        // set motor setpoint
        currentSetpoint = setpoint;
        motor.setControl(positionControl.withPosition(setpoint));
        encoder.setPosition(setpoint);
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
        SmartDashboard.putNumber("Pivot Angle", getPosition());
        SmartDashboard.putBoolean("At Setpoint", isAtSetpoint());
        SmartDashboard.putBoolean("Item Detected", hasItem());
    }
    public double getPosition(){
        return encoder.getAbsolutePosition(true).getValueAsDouble();
    }
    /**
     * 
     * 
     * @param tolerance Defaults to 0.05 radians.
     * 
     * @return true if distance to setpoint is less than tolerance, false otherwise.
     */
    public boolean isAtSetpoint(double tolerance){
        return Math.abs(getPosition() - currentSetpoint) < tolerance;
    }
    
    public boolean isAtSetpoint(){
        return Math.abs(getPosition() - currentSetpoint) < 0.05;
    }
    public boolean hasItem(){
        return hasItem;
    }
}
