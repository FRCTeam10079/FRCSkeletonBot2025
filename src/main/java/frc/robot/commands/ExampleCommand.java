package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ExampleSubsystem;

public class ExampleCommand extends Command {

    // Subsystems
    private ExampleSubsystem subsystem;

    // Timer
    // private Timer timer;

    // PIDs
    // private PIDController pidX;
    // private PIDController pidY;
    // private PIDController pidRotate;

    // Target pose
    // private Pose2d targetPose;

    // Constructor
    public ExampleCommand(ExampleSubsystem subsystem) {
        this.subsystem = subsystem;
        addRequirements(subsystem);
    }

    @Override
    public void initialize() {
        // initialization code
    }

    @Override
    public void execute() {
        // periodic action
    }

    @Override
    public boolean isFinished() {
        // finish condition
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        // cleanup code
        subsystem.exampleMethod();
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