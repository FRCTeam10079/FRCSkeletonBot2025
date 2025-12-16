package frc.robot.subsystems;

import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.CANrange;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IntakeMotorSubsystem extends SubsystemBase {

    // Motors and sensors
    private final TalonFX motor = new TalonFX(0);
    private final CANcoder encoder = new CANcoder(0);
    private final Timer intakeTimer = new Timer();

    // Controls
    private final PositionVoltage positionControl = new PositionVoltage(0).withSlot(0);

    // Variables
    private double currentSpeed = 0;
    private boolean hasItem = false;

    public IntakeMotorSubsystem() {
        // configure motors and sensors
        configureMotor();
        // set initial positions
    }

    // Configure motor
    private void configureMotor() {
         // configure motor settings
        motor.set(0);
        setIntakeSpeed(0);
    }

    // Set setpoint
    public Command setIntakeSpeed(double voltage){
        System.out.println("sngjwengksbghnserjnhrwejgnhekjrgnhbwkrjbhkerbghkjshrfbghef " + System.currentTimeMillis());
        return Commands.run(() -> setMotorSpeed(voltage), this);
    }
    public void setMotorSpeed(double voltage){
        motor.set(voltage);
        currentSpeed = voltage;
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
        SmartDashboard.putNumber("Intake Speed", currentSpeed);
    }
}