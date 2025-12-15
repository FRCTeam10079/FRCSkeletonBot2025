package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.hardware.CANrange;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.signals.AbsoluteSensorRangeValue;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.PivotIntakeConstants;

public class PivotIntakeSubsystem extends SubsystemBase {
    
    // Hardware
    private final TalonFX pivotMotor;
    private final TalonFX intakeWheelMotor;
    private final CANcoder pivotEncoder;
    private final CANrange objectSensor; 
    
    // Control Requests
    private final PositionVoltage pivotPositionControl;
    private final DutyCycleOut intakePowerControl;
    
    // State
    private double currentPivotSetpoint;
    
    // Constants
    private final double INTAKE_SPEED = -0.4; 
    private final double SHOOT_SPEED = 0.95; 
    private final double SHOOT_ANGLE = 0.35; 
    
    public PivotIntakeSubsystem() {
        pivotMotor = new TalonFX(PivotIntakeConstants.PIVOT_MOTOR_ID);
        intakeWheelMotor = new TalonFX(PivotIntakeConstants.INTAKE_WHEEL_MOTOR_ID);
        pivotEncoder = new CANcoder(PivotIntakeConstants.PIVOT_ENCODER_ID);
        objectSensor = new CANrange(PivotIntakeConstants.CORAL_SENSOR_ID);
        
        configureHardware();
        
        pivotPositionControl = new PositionVoltage(0).withSlot(0);
        intakePowerControl = new DutyCycleOut(0);
        
        currentPivotSetpoint = PivotIntakeConstants.STOWED_POSITION;
    }
    
    private void configureHardware() {
        // Pivot Config
        TalonFXConfiguration pivotConfig = new TalonFXConfiguration();
        pivotConfig.Slot0.kP = PivotIntakeConstants.PIVOT_KP;
        pivotConfig.Slot0.kD = PivotIntakeConstants.PIVOT_KD;
        pivotConfig.Slot0.kG = PivotIntakeConstants.PIVOT_KG;
        pivotConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;
        pivotMotor.getConfigurator().apply(pivotConfig);
        
        // Sync absolute encoder
        pivotMotor.setPosition(pivotEncoder.getAbsolutePosition().getValueAsDouble());

        // Intake Config
        TalonFXConfiguration intakeConfig = new TalonFXConfiguration();
        intakeConfig.MotorOutput.NeutralMode = NeutralModeValue.Coast; 
        intakeWheelMotor.getConfigurator().apply(intakeConfig);

        // Encoder Config
        CANcoderConfiguration encoderConfig = new CANcoderConfiguration();
        encoderConfig.MagnetSensor.AbsoluteSensorRange = AbsoluteSensorRangeValue.Unsigned_0To1;
        pivotEncoder.getConfigurator().apply(encoderConfig);
    }
    
    // Position Methods
    public void setPivotPosition(double position) {
        currentPivotSetpoint = position;
        pivotMotor.setControl(pivotPositionControl.withPosition(position));
    }
    
    public void moveToFloor() { setPivotPosition(PivotIntakeConstants.INTAKE_POSITION); }
    public void moveToStow()  { setPivotPosition(PivotIntakeConstants.STOWED_POSITION); }
    public void moveToShoot() { setPivotPosition(SHOOT_ANGLE); }
    
    // Roller Methods
    public void suckBall() {
        intakeWheelMotor.setControl(intakePowerControl.withOutput(INTAKE_SPEED));
    }
    
    public void shootBall() {
        intakeWheelMotor.setControl(intakePowerControl.withOutput(SHOOT_SPEED));
    }
    
    public void stopIntake() {
        intakeWheelMotor.setControl(intakePowerControl.withOutput(0));
    }
    
    // Status Methods
    public boolean hasObject() {
        return objectSensor.getDistance().getValueAsDouble() < 0.20; 
    }
    
    public boolean isAtSetpoint() {
        return Math.abs(pivotMotor.getPosition().getValueAsDouble() - currentPivotSetpoint) < 0.05;
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Pivot Pos", pivotMotor.getPosition().getValueAsDouble());
        SmartDashboard.putBoolean("Has Object", hasObject());
    }
}