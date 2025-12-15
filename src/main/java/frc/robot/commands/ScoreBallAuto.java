package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import com.ctre.phoenix6.swerve.SwerveRequest;

import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.PivotIntakeSubsystem;

public class ScoreBallAuto extends SequentialCommandGroup {

    // Swerve Requests
    private final SwerveRequest.RobotCentric driveForward = new SwerveRequest.RobotCentric()
            .withVelocityX(1.0).withVelocityY(0).withRotationalRate(0);
            
    private final SwerveRequest.RobotCentric driveBackward = new SwerveRequest.RobotCentric()
            .withVelocityX(-1.0).withVelocityY(0).withRotationalRate(0);
            
    private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();

    public ScoreBallAuto(CommandSwerveDrivetrain drivetrain, PivotIntakeSubsystem pivotSub) {
        addCommands(
            // 1. Prepare Intake
            new InstantCommand(() -> pivotSub.moveToFloor(), pivotSub),
            new InstantCommand(() -> pivotSub.suckBall(), pivotSub),
            new WaitUntilCommand(() -> pivotSub.isAtSetpoint()),

            // 2. Drive to Game Piece
            drivetrain.applyRequest(() -> driveForward).withTimeout(1.5),
            
            // 3. Detect and Secure
            new WaitUntilCommand(() -> pivotSub.hasObject()).withTimeout(0.5),
            drivetrain.applyRequest(() -> brake).withTimeout(0.1),
            
            // Stop rollers and lift slightly
            new InstantCommand(() -> pivotSub.stopIntake(), pivotSub),
            new InstantCommand(() -> pivotSub.moveToStow(), pivotSub),

            // 4. Drive to Goal
            drivetrain.applyRequest(() -> driveBackward).withTimeout(1.5),
            drivetrain.applyRequest(() -> brake).withTimeout(0.1),

            // 5. Aim
            new InstantCommand(() -> pivotSub.moveToShoot(), pivotSub),
            new WaitUntilCommand(() -> pivotSub.isAtSetpoint()), 
            
            // 6. Fire
            new InstantCommand(() -> pivotSub.shootBall(), pivotSub),
            Commands.waitSeconds(0.5),
            
            // 7. Reset
            new InstantCommand(() -> pivotSub.stopIntake(), pivotSub),
            new InstantCommand(() -> pivotSub.moveToStow(), pivotSub)
        );
    }
}