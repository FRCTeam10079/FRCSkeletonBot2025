package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.subsystems.ShooterSubsystem;

public class ShooterCommand extends Command {

    // Subsystems
    private ShooterSubsystem subsystem;

    // Timer
    // private Timer timer;

    // PIDs
    // private PIDController pidX;
    // private PIDController pidY;
    // private PIDController pidRotate;

    // Target pose
    // private Pose2d targetPose;

    // Constructor
    public ShooterCommand(ShooterSubsystem subsystem) {
        this.subsystem = subsystem;
        addRequirements(subsystem);
    }

    public Command ShooterCommand1() {
        return Commands.runOnce(() -> System.out.println("Example command 1"));
    }

    public Command ShooterCommand2() {
        return Commands.run(() -> System.out.println("Example command 2 running"), subsystem);
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