// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.subsystems.SuperstructureSubsystem;
import frc.robot.commands.ExampleCommand;

/**
 * ROBOT CONTAINER - State Machine Integration
 * 
 * This class integrates the Master State Machine and Superstructure with driver controls and autonomous commands.
 * 
 * SKELETON TEMPLATE - Customize for the robot's mechanisms
 */
public class RobotContainer {
    // MASTER STATE MACHINE - Controls the entire robot
    private final RobotStateMachine robotStateMachine = RobotStateMachine.getInstance();
    
    // Joysticks
    public final CommandXboxController joystick = new CommandXboxController(0);   // Driver
    public final CommandXboxController joystick2 = new CommandXboxController(1);  // Operator
    public final CommandXboxController joystick3 = new CommandXboxController(2);  // Test/Debug

    // SUBSYSTEMS - Add your actual subsystems here
    // Example subsystems (replace with your actual mechanisms)
    public final ExampleSubsystem mechanism1 = new ExampleSubsystem();  // e.g., Elevator
    public final ExampleSubsystem mechanism2 = new ExampleSubsystem();  // e.g., Pivot/Arm
    public final ExampleSubsystem mechanism3 = new ExampleSubsystem();  // e.g., Intake/Shooter
    
    // SUPERSTRUCTURE STATE MACHINE - Coordinates all mechanisms
    public final SuperstructureSubsystem superstructure = new SuperstructureSubsystem(
        mechanism1, mechanism2, mechanism3
    );

    // Auto chooser
    private final SendableChooser<Command> autoChooser = new SendableChooser<>();

    public RobotContainer() {
        // NAMED COMMANDS FOR PATHPLANNER AUTONOMOUS
        // Register state machine commands for use in PathPlanner autos
        
        // INTAKE/COLLECTION COMMANDS
        NamedCommands.registerCommand("Collect Game Piece", superstructure.collectGamePiece());
        NamedCommands.registerCommand("Collect and Transfer", superstructure.collectAndTransfer());
        NamedCommands.registerCommand("Transfer", superstructure.transferGamePiece());
        
        // SCORING COMMANDS
        NamedCommands.registerCommand("Score Low", superstructure.scoreLow());
        NamedCommands.registerCommand("Score Mid", superstructure.scoreMid());
        NamedCommands.registerCommand("Score High", superstructure.scoreHigh());
        
        // UTILITY COMMANDS
        NamedCommands.registerCommand("Return to Idle", superstructure.returnToIdle());
        NamedCommands.registerCommand("Enter Manual", superstructure.enterManualMode());
        NamedCommands.registerCommand("Exit Manual", superstructure.exitManualMode());
        
        // ENDGAME COMMANDS
        NamedCommands.registerCommand("Execute Endgame", superstructure.executeEndgame());
        
        // AUTO CHOOSER SETUP
        // If using PathPlanner:
        // autoChooser = AutoBuilder.buildAutoChooser("Default Auto Name");
        
        // For skeleton, use simple chooser
        autoChooser.setDefaultOption("Do Nothing", new InstantCommand());
        autoChooser.addOption("Example Auto", createExampleAuto());
        SmartDashboard.putData("Auto Mode", autoChooser);

        // Configure button bindings
        configureBindings();
    }

    /**
     * Configure controller button bindings
     * 
     * CUSTOMIZE THESE BINDINGS FOR YOUR ROBOT
     */
    private void configureBindings() {
        // DRIVER CONTROLS (joystick - Controller 0)
        
        // A Button: Example - Lock wheels (updates state machine)
        joystick.a().onTrue(Commands.runOnce(() -> 
            robotStateMachine.setDrivetrainMode(RobotStateMachine.DrivetrainMode.LOCKED)))
            .onFalse(Commands.runOnce(() -> 
            robotStateMachine.setDrivetrainMode(RobotStateMachine.DrivetrainMode.FIELD_CENTRIC)));
        
        // B Button: Example - Slow mode
        joystick.b().onTrue(Commands.runOnce(() ->
            robotStateMachine.setDrivetrainMode(RobotStateMachine.DrivetrainMode.SLOW_MODE)))
            .onFalse(Commands.runOnce(() ->
            robotStateMachine.setDrivetrainMode(RobotStateMachine.DrivetrainMode.FIELD_CENTRIC)));
        
        // Right Trigger: Score (using current superstructure state)
        joystick.rightTrigger().onTrue(superstructure.scoreHigh());
        
        // Left Trigger: Score low
        joystick.leftTrigger().onTrue(superstructure.scoreLow());

        // OPERATOR CONTROLS (joystick2 - Controller 1)
        
        // Face buttons - Scoring at different levels
        joystick2.a().onTrue(superstructure.scoreLow());
        joystick2.b().onTrue(superstructure.scoreMid());
        joystick2.y().onTrue(superstructure.scoreHigh());
        
        // X Button: Return to idle
        joystick2.x().onTrue(superstructure.returnToIdle());
        
        // POV Up: Full intake sequence (collect + transfer)
        joystick2.povUp().onTrue(superstructure.collectAndTransfer());
        
        // POV Down: Collect only
        joystick2.povDown().onTrue(superstructure.collectGamePiece());
        
        // POV Right: Enter manual override mode
        joystick2.povRight().onTrue(superstructure.enterManualMode());
        
        // POV Left: Exit manual mode
        joystick2.povLeft().onTrue(superstructure.exitManualMode());
        
        // Bumpers: Endgame
        joystick2.leftBumper().and(joystick2.rightBumper())
            .onTrue(superstructure.executeEndgame());
        
        // DEBUG/TEST CONTROLS (joystick3 - Controller 2)
        // Add test-specific controls here
    }
    
    /**
     * Example autonomous routine using state machine
     */
    private Command createExampleAuto() {
        return Commands.sequence(
            // Score preloaded game piece
            superstructure.scoreHigh(),
            
            // Wait a moment
            Commands.waitSeconds(0.5),
            
            // Could add driving commands here with PathPlanner
            
            // Return to idle
            superstructure.returnToIdle()
        );
    }

    /**
     * Get the selected autonomous command
     */
    public Command getAutonomousCommand() {
        Command selected = autoChooser.getSelected();
        if (selected != null) return selected;
        return new InstantCommand();
    }
}