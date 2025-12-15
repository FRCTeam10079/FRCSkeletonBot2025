package frc.robot;

import com.ctre.phoenix6.swerve.SwerveRequest;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.PivotIntakeSubsystem;
import frc.robot.commands.ScoreBallAuto; 
import frc.robot.generated.TunerConstants; 

public class RobotContainer {

    // Subsystems
    public final CommandSwerveDrivetrain drivetrain = TunerConstants.DriveTrain;
    public final PivotIntakeSubsystem pivotSub = new PivotIntakeSubsystem();

    // Controller
    private final CommandXboxController driverJoy = new CommandXboxController(0);

    // Drive Request
    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
        .withDeadband(0.1).withRotationalDeadband(0.1);

    public RobotContainer() {
        configureBindings();
    }

    private void configureBindings() {
        // Teleop Drive
        drivetrain.setDefaultCommand(
            drivetrain.applyRequest(() -> drive
                .withVelocityX(-driverJoy.getLeftY() * 4.0) 
                .withVelocityY(-driverJoy.getLeftX() * 4.0) 
                .withRotationalRate(-driverJoy.getRightX() * Math.PI) 
            )
        );

        // A Button: Floor Intake
        driverJoy.a().onTrue(
            new InstantCommand(() -> pivotSub.moveToFloor())
            .andThen(new InstantCommand(() -> pivotSub.suckBall()))
        ).onFalse(
            new InstantCommand(() -> pivotSub.stopIntake())
            .andThen(new InstantCommand(() -> pivotSub.moveToStow()))
        );

        // Right Trigger: Shoot
        driverJoy.rightTrigger().onTrue(
             new InstantCommand(() -> pivotSub.moveToShoot())
             .andThen(new InstantCommand(() -> pivotSub.shootBall()))
        ).onFalse(
             new InstantCommand(() -> pivotSub.stopIntake())
             .andThen(new InstantCommand(() -> pivotSub.moveToStow()))
        );
    }

    public Command getAutonomousCommand() {
        return new ScoreBallAuto(drivetrain, pivotSub);
    }
}