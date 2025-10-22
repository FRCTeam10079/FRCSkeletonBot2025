// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.commands.ExampleCommand;

public class RobotContainer {
    public final CommandXboxController joystick = new CommandXboxController(0);
    public final CommandXboxController joystick2 = new CommandXboxController(1);
    public final CommandXboxController joystick3 = new CommandXboxController(2);

    public final ExampleSubsystem dumpRoller = new ExampleSubsystem();
    public final ExampleSubsystem elevator = new ExampleSubsystem();
    public final ExampleSubsystem pivotSub = new ExampleSubsystem();

    private final SendableChooser<Command> autoChooser = new SendableChooser<>();

    public RobotContainer() {
        autoChooser.setDefaultOption("Do Nothing", new InstantCommand());
        SmartDashboard.putData("Auto Mode", autoChooser);
        configureBindings();
    }

    private void configureBindings() {
        joystick.a().onTrue(new ExampleCommand(dumpRoller).exampleCommand1());
        joystick.b().onTrue(new ExampleCommand(dumpRoller).exampleCommand2());
    }

    public Command getAutonomousCommand() {
        Command selected = autoChooser.getSelected();
        if (selected != null) return selected;
        return new InstantCommand();
    }
}