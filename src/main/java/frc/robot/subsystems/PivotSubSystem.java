package frc.robot.subsystems;

import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.CANrange;
import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.units.Units.RotationsPerSecond;
import edu.wpi.first.units.Units.RotationsPerSecondPerSecond;

public class PivotSubsystem extends SubsystemBase {

    // Hardware
    private final TalonFX pivotMotor = new TalonFX(20);
    private final CANcoder pivotEncoder = new CANcoder(18);
    private final CANrange objectSensor = new CANrange(21);

    // Motion Magic request
    private final MotionMagicVoltage mm = new MotionMagicVoltage(0).withSlot(0);

    // Pivot positions
    private static final double INTAKE_POS = 0.0;    // Down
    private static final double STOW_POS = 0.42;     // Up

    // Track current target
    private double currentSetPoint = STOW_POS;

    public PivotSubsystem() {
        configureMotor();

        // Assume pivot starts stowed
        pivotEncoder.setPosition(STOW_POS);
        pivotMotor.setPosition(STOW_POS);
    }

    private void configureMotor() {
        TalonFXConfiguration config = new TalonFXConfiguration();

        // Motor output settings
        config.MotorOutput.NeutralMode = NeutralModeValue.Brake;
        config.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

        // Encoder feedback
        config.Feedback.FeedbackRemoteSensorID = 18; // pivotEncoder ID
        config.Feedback.FeedbackSensorSource = FeedbackSensorSourceValue.RemoteCANcoder;

        // Motion Magic settings
        config.MotionMagic
            .withMotionMagicCruiseVelocity(RotationsPerSecond.of(0.5))
            .withMotionMagicAcceleration(RotationsPerSecondPerSecond.of(1));

        // PID settings
        config.Slot0.kP = 0.02;
        config.Slot0.kD = 0.4;
        config.Slot0.GravityType = GravityTypeValue.Arm_Cosine;

        // Soft limits
        config.SoftwareLimitSwitch.ForwardSoftLimitThreshold = STOW_POS;
        config.SoftwareLimitSwitch.ForwardSoftLimitEnable = true;
        config.SoftwareLimitSwitch.ReverseSoftLimitThreshold = INTAKE_POS;
        config.SoftwareLimitSwitch.ReverseSoftLimitEnable = true;

        pivotMotor.getConfigurator().apply(config);
    }

    // Generic setpoint method
    public void setPivotSetpoint(double setPoint) {
        currentSetPoint = setPoint;
        pivotMotor.setControl(mm.withPosition(setPoint));
    }

    // Convenience methods
    public void intakePosition() {
        setPivotSetpoint(INTAKE_POS);
    }

    public void stowPosition() {
        setPivotSetpoint(STOW_POS);
    }

    // Status checks
    public boolean atTarget() {
        return Math.abs(pivotEncoder.getPosition().getValueAsDouble() - currentSetPoint) < 0.01;
    }

    public boolean hasObject() {
        return objectSensor.getDistance().getValueAsDouble() < 0.15;
    }

    // Command methods
    public Command intakePivotCommand() {
        return Commands.runOnce(() -> intakePosition());
    }

    public Command stowPivotCommand() {
        return Commands.runOnce(() -> stowPosition());
    }

    public Command setPivotCommand(double setPoint) {
        return Commands.runOnce(() -> setPivotSetpoint(setPoint));
    }
}
