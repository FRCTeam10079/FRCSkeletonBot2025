// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StructArrayPublisher;
import edu.wpi.first.networktables.StructPublisher;
import edu.wpi.first.units.measure.Velocity;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.subsystems.LimelightSubsystem;
import frc.robot.commands.ExampleCommand;
import frc.robot.generated.TunerConstants;

public class RobotContainer {
    public final double maxSpeedMetersPerSecond = 5.0;

    public final CommandXboxController joystick = new CommandXboxController(0);
    public final CommandXboxController joystick2 = new CommandXboxController(1);
    public final CommandXboxController joystick3 = new CommandXboxController(2);

    public final ExampleSubsystem dumpRoller = new ExampleSubsystem();
    public final ExampleSubsystem elevator = new ExampleSubsystem();
    public final ExampleSubsystem pivotSub = new ExampleSubsystem();


    public final CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();
    public final LimelightSubsystem limelight = new LimelightSubsystem(this);

    private final SendableChooser<Command> autoChooser = new SendableChooser<>();

    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric();

    private final Telemetry logger = new Telemetry(5);

    public RobotContainer() {
        autoChooser.setDefaultOption("Do Nothing", new InstantCommand());
        SmartDashboard.putData("Auto Mode", autoChooser);
        configureBindings();
    }

    private void configureBindings() {
        joystick2.a().onTrue(new ExampleCommand(dumpRoller).exampleCommand1());
        joystick2.b().onTrue(new ExampleCommand(dumpRoller).exampleCommand2());
        drivetrain.setDefaultCommand(
            drivetrain.applyRequest(() -> drive.withVelocityX(joystick2.getLeftY() * maxSpeedMetersPerSecond).withVelocityY(joystick2.getLeftX() * maxSpeedMetersPerSecond).withRotationalRate(-joystick2.getRightX()))
        );
        drivetrain.registerTelemetry(logger::telemeterize);
    }

    public Command getAutonomousCommand() {
        Command selected = autoChooser.getSelected();
        if (selected != null) return selected;
        return new InstantCommand();
    }
    // public LimelightSubsystem getLimelightSubsystem(){
    //     return this.limelight;
    // }
}