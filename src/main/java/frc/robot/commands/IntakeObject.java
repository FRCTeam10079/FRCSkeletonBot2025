package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Constants.PivotConstants;
import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.subsystems.IntakeMotorSubsystem;
import frc.robot.subsystems.PivotSubsystem;

public class IntakeObject extends Command {

    // Subsystems
    private IntakeMotorSubsystem intakeMotorSubsystem;
    private PivotSubsystem pivotSubsystem;
    // Timer
    private Timer timer;
    private boolean isFinished;
    // PIDs
    // private PIDController pidX;
    // private PIDController pidY;
    // private PIDController pidRotate;

    // Target pose
    // private Pose2d targetPose;

    // Constructor
    public IntakeObject(IntakeMotorSubsystem intakeMotorSubsystem, PivotSubsystem pivotSubsystem) {
        this.intakeMotorSubsystem = intakeMotorSubsystem;
        this.pivotSubsystem = pivotSubsystem;
        addRequirements(pivotSubsystem);
        addRequirements(intakeMotorSubsystem);
        isFinished = false;
    }

    public Command exampleCommand1() {
        return Commands.runOnce(() -> System.out.println("Example command 1"));
    }

    public Command exampleCommand2() {
        return Commands.run(() -> System.out.println("Example command 2 running"), intakeMotorSubsystem);
    }

    @Override
    public void initialize() {
        // initialization code
    }

    @Override
    public void execute() {
        // Deploy
        // Commands.runOnce(() -> pivotSubsystem.setSetpoint(PivotConstants.INTAKE_POSITION), pivotSubsystem)
        // .andThen(() -> intakeMotorSubsystem.setIntakeSpeed(PivotConstants.INTAKE_SPEED), intakeMotorSubsystem)

        // .andThen(() -> Commands.waitSeconds(5))

        // .andThen(() -> intakeMotorSubsystem.setIntakeSpeed(0), intakeMotorSubsystem)
        // .andThen(() -> pivotSubsystem.setSetpoint(PivotConstants.STOWED_POSITION), pivotSubsystem)
        
        // .andThen(() -> intakeMotorSubsystem.setIntakeSpeed(PivotConstants.INTAKE_REVERSE_SPEED), intakeMotorSubsystem)

        // .andThen(() -> Commands.waitSeconds(5))

        // .andThen(() -> intakeMotorSubsystem.setIntakeSpeed(1), intakeMotorSubsystem)

        // .andThen(() -> this.finish());
    }

    @Override
    public boolean isFinished() {
        // finish condition
        return isFinished;
    }
    public void finish(){
        isFinished = true;
        System.out.println("bruh");
    }
    @Override
    public void end(boolean interrupted) {
        // cleanup code
        //subsystem.exampleMethod();
    }

    // Helper methods
    // private double[] calculateError(Pose2d currentPose, boolean rotateFirst) {
    //     // calculate velocities
    //     return new double[]{0, 0, 0};
    // }

    // private double calculateYawVelocity(double yawError) {
    //     // calculate yaw velocity
    //     return 0;
    // }
}