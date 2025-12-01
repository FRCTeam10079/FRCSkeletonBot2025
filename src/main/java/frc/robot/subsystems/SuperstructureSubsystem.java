package frc.robot.subsystems;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotStateMachine;
import frc.robot.RobotStateMachine.GameState;

/**
 * SUPERSTRUCTURE STATE MACHINE
 * This subsystem coordinates ALL mechanisms (elevator, intake, shooter, etc.)
 * and manages game-specific scoring sequences through state transitions.
 * 
 * SKELETON TEMPLATE - Customize for each season's mechanisms
 * 
 * HOW TO USE:
 * 1. Add your actual subsystems as dependencies (elevator, intake, shooter, etc.)
 * 2. Define RobotState enum with states for your game's mechanisms
 * 3. Implement applyStateGoals() to set mechanism positions for each state
 * 4. Create command sequences for game actions (collect, score, etc.)
 */
public class SuperstructureSubsystem extends SubsystemBase {
    
    // Master state machine reference
    private final RobotStateMachine masterStateMachine = RobotStateMachine.getInstance();
    
    // SUBSYSTEM DEPENDENCIES - Add your actual subsystems here
    // Example subsystem references (replace with your actual subsystems):
    // private final ElevatorSubsystem elevator;
    // private final IntakeSubsystem intake;
    // private final ShooterSubsystem shooter;
    // private final ArmSubsystem arm;
    
    // For skeleton, we'll use placeholder subsystems
    private final ExampleSubsystem mechanism1;
    private final ExampleSubsystem mechanism2;
    private final ExampleSubsystem mechanism3;
    
    // Current state
    private RobotState currentState = RobotState.IDLE;
    private RobotState requestedState = RobotState.IDLE;
    
    /**
     * ROBOT STATE MACHINE STATES
     * 
     * CUSTOMIZE THESE STATES FOR YOUR GAME'S MECHANISMS
     * 
     * Each state defines:
     * - mechanism1Level: Position/level for first mechanism (e.g., elevator level)
     * - mechanism2Position: Position for second mechanism (e.g., arm angle)
     * - mechanism3Active: Whether third mechanism should be active (e.g., intake wheels)
     * - description: Human-readable description
     * 
     * EXAMPLE STATES (replace with your game-specific states):
     */
    public enum RobotState {
        // IDLE/HOME STATES
        IDLE(0, 0.0, false, "Robot stowed and ready"),
        
        // INTAKE STATES - Collecting game pieces
        INTAKE_DEPLOY(0, 1.0, false, "Deploying intake mechanism"),
        INTAKE_COLLECTING(0, 1.0, true, "Collecting game piece"),
        INTAKE_STOWING(0, 0.5, false, "Stowing intake with game piece"),
        
        // TRANSFER STATES - Moving game piece between mechanisms
        TRANSFERRING(0, 0.5, true, "Transferring game piece"),
        GAME_PIECE_LOADED(0, 0.0, false, "Game piece loaded, ready to score"),
        
        // SCORING STATES - Scoring at different positions/levels
        SCORING_LOW_PREP(1, 0.0, false, "Preparing to score low"),
        SCORING_LOW_EXECUTE(1, 0.0, false, "Scoring at low position"),
        
        SCORING_MID_PREP(2, 0.0, false, "Preparing to score mid"),
        SCORING_MID_EXECUTE(2, 0.0, false, "Scoring at mid position"),
        
        SCORING_HIGH_PREP(3, 0.0, false, "Preparing to score high"),
        SCORING_HIGH_EXECUTE(3, 0.0, false, "Scoring at high position"),
        
        // ENDGAME STATES - Climbing/parking/etc
        ENDGAME_PREP(0, 0.0, false, "Preparing for endgame"),
        ENDGAME_EXECUTE(0, 0.0, false, "Executing endgame action"),
        ENDGAME_COMPLETE(0, 0.0, false, "Endgame complete"),
        
        // MANUAL OVERRIDE
        MANUAL(0, 0.0, false, "Manual control active");
        
        // State properties - goals for each mechanism
        // Customize these fields for your mechanisms
        public final int mechanism1Level;      // e.g., elevator level
        public final double mechanism2Position; // e.g., arm position in rotations
        public final boolean mechanism3Active;  // e.g., intake wheels running
        public final String description;
        
        RobotState(int mechanism1Level, double mechanism2Position, boolean mechanism3Active, String description) {
            this.mechanism1Level = mechanism1Level;
            this.mechanism2Position = mechanism2Position;
            this.mechanism3Active = mechanism3Active;
            this.description = description;
        }
    }
    
    /**
     * Constructor - inject subsystem dependencies
     * 
     * Replace ExampleSubsystem parameters with your actual subsystem types
     */
    public SuperstructureSubsystem(
            ExampleSubsystem mechanism1,
            ExampleSubsystem mechanism2,
            ExampleSubsystem mechanism3) {
        this.mechanism1 = mechanism1;
        this.mechanism2 = mechanism2;
        this.mechanism3 = mechanism3;
    }
    
    /**
     * Request a state transition
     * Immediately applies the state transition for responsive control
     */
    public void requestState(RobotState newState) {
        requestedState = newState;
        currentState = newState;
        
        // Log state transition immediately
        System.out.println("State transition: " + currentState.name() + " - " + currentState.description);
        Logger.recordOutput("Superstructure/StateTransition", currentState.name() + " - " + currentState.description);
        Logger.recordOutput("Superstructure/TransitionTimestamp", edu.wpi.first.wpilibj.Timer.getFPGATimestamp());
        
        // Apply state goals immediately
        applyStateGoals();
    }
    
    /**
     * Get current state
     */
    public RobotState getCurrentState() {
        return currentState;
    }
    
    /**
     * Check if robot is in a specific state
     */
    public boolean isInState(RobotState state) {
        return currentState == state;
    }
    
    /**
     * Check if subsystems have reached state goals
     * Used by commands to wait for mechanisms to reach position before continuing
     * 
     * IMPLEMENT THIS FOR YOUR ACTUAL SUBSYSTEMS
     */
    private boolean hasReachedStateGoals() {
        // EXAMPLE IMPLEMENTATION:
        // RobotState state = currentState;
        // 
        // // Check elevator position
        // boolean elevatorAtGoal = Math.abs(
        //     elevator.getPosition() - elevator.positions[state.mechanism1Level]
        // ) < 0.5;
        // 
        // // Check arm position
        // boolean armAtGoal = Math.abs(
        //     arm.getPosition() - state.mechanism2Position
        // ) < 0.05;
        // 
        // return elevatorAtGoal && armAtGoal;
        
        // SKELETON: Always return true for now
        return true;
    }
    
    @Override
    public void periodic() {
        // State machine continuously applies state goals to subsystems
        // This ensures mechanisms stay at correct positions even if disturbed
        applyStateGoals();
        
        // Update telemetry (AdvantageKit + SmartDashboard)
        updateTelemetry();
    }
    
    /**
     * Update telemetry
     */
    private void updateTelemetry() {
        // ADVANTAGEKIT LOGGING
        Logger.recordOutput("Superstructure/State/Current", currentState.name());
        Logger.recordOutput("Superstructure/State/Description", currentState.description);
        Logger.recordOutput("Superstructure/State/RequestedState", requestedState.name());
        Logger.recordOutput("Superstructure/Goals/AtGoals", hasReachedStateGoals());
        Logger.recordOutput("Superstructure/Goals/Mechanism1Level", currentState.mechanism1Level);
        Logger.recordOutput("Superstructure/Goals/Mechanism2Position", currentState.mechanism2Position);
        Logger.recordOutput("Superstructure/Goals/Mechanism3Active", currentState.mechanism3Active);
        
        // SMARTDASHBOARD (LEGACY)
        SmartDashboard.putString("Superstructure State", currentState.name());
        SmartDashboard.putString("State Description", currentState.description);
        SmartDashboard.putBoolean("At State Goals", hasReachedStateGoals());
    }
    
    /**
     * Apply current state's goals to all subsystems
     * This continuously updates subsystems based on the current state
     * 
     * IMPLEMENT THIS FOR YOUR ACTUAL SUBSYSTEMS
     */
    private void applyStateGoals() {
        // EXAMPLE IMPLEMENTATION:
        // // Set elevator position based on current state
        // elevator.setLevel(currentState.mechanism1Level);
        // 
        // // Set arm position based on current state
        // arm.setPosition(currentState.mechanism2Position);
        // 
        // // Intake wheels are typically controlled by commands, not continuous state
        // // This allows for precise timing in sequences
        
        // SKELETON: Just logging for now
        // mechanism1.setSetpoint(currentState.mechanism1Level);
        // mechanism2.setSetpoint(currentState.mechanism2Position);
    }
    
    // COMMAND SEQUENCES - Game-specific action sequences
    
    /**
     * COLLECT GAME PIECE: Generic intake sequence
     * Updates master state machine with game states
     * 
     * Customize this for your game's intake mechanism
     */
    public Command collectGamePiece() {
        return Commands.sequence(
            // Update master game state
            Commands.runOnce(() -> {
                masterStateMachine.setGameState(GameState.COLLECTING_GROUND);
                System.out.println("=== INTAKE SEQUENCE STARTED ===");
            }),
            
            // State 1: Deploy intake
            Commands.runOnce(() -> {
                System.out.println("=== INTAKE: Deploying mechanism ===");
                requestState(RobotState.INTAKE_DEPLOY);
            }),
            Commands.waitUntil(this::hasReachedStateGoals).withTimeout(3.0),
            
            // State 2: Run intake and collect
            Commands.runOnce(() -> {
                System.out.println("=== INTAKE: Starting collection ===");
                requestState(RobotState.INTAKE_COLLECTING);
            }),
            
            // Run intake until game piece detected OR timeout
            // IMPLEMENT: Replace with actual intake command that checks sensor
            Commands.waitSeconds(2.0).withTimeout(10.0),
            
            // State 3: Stow with game piece
            Commands.runOnce(() -> {
                System.out.println("=== INTAKE: Stowing with game piece ===");
                requestState(RobotState.INTAKE_STOWING);
            }),
            Commands.waitUntil(this::hasReachedStateGoals).withTimeout(5.0),
            
            // Update game state - game piece secured
            Commands.runOnce(() -> {
                masterStateMachine.setGameState(GameState.GAME_PIECE_SECURED);
                System.out.println("=== INTAKE SEQUENCE COMPLETE ===");
            })
        );
    }
    
    /**
     * TRANSFER GAME PIECE: Move from intake to scoring mechanism
     */
    public Command transferGamePiece() {
        return Commands.sequence(
            // Update master game state
            Commands.runOnce(() -> {
                masterStateMachine.setGameState(GameState.TRANSFERRING);
                System.out.println("=== TRANSFER: Starting ===");
            }),
            
            // State: Transfer
            Commands.runOnce(() -> requestState(RobotState.TRANSFERRING)),
            Commands.waitUntil(this::hasReachedStateGoals).withTimeout(3.0),
            
            // IMPLEMENT: Add actual transfer mechanism commands here
            Commands.waitSeconds(0.5),
            
            // State: Game piece loaded
            Commands.runOnce(() -> requestState(RobotState.GAME_PIECE_LOADED)),
            Commands.runOnce(() -> masterStateMachine.setGameState(GameState.GAME_PIECE_LOADED))
        );
    }
    
    /**
     * FULL AUTO INTAKE: Collect and transfer in one sequence
     */
    public Command collectAndTransfer() {
        return Commands.sequence(
            collectGamePiece(),
            transferGamePiece()
        );
    }
    
    /**
     * SCORE LOW: Score at low position
     */
    public Command scoreLow() {
        return scoreAtLevel(RobotState.SCORING_LOW_PREP, RobotState.SCORING_LOW_EXECUTE, GameState.SCORING_LOW);
    }
    
    /**
     * SCORE MID: Score at mid position
     */
    public Command scoreMid() {
        return scoreAtLevel(RobotState.SCORING_MID_PREP, RobotState.SCORING_MID_EXECUTE, GameState.SCORING_MID);
    }
    
    /**
     * SCORE HIGH: Score at high position
     */
    public Command scoreHigh() {
        return scoreAtLevel(RobotState.SCORING_HIGH_PREP, RobotState.SCORING_HIGH_EXECUTE, GameState.SCORING_HIGH);
    }
    
    /**
     * Generic scoring sequence helper
     * Updates master game state during scoring
     */
    private Command scoreAtLevel(RobotState prepState, RobotState executeState, GameState scoringGameState) {
        return Commands.sequence(
            // Debug output
            Commands.runOnce(() -> System.out.println("Starting scoring sequence: " + prepState.name())),
            
            // Update master game state
            Commands.runOnce(() -> masterStateMachine.setGameState(scoringGameState)),
            
            // Prep: Move mechanisms to scoring position
            Commands.runOnce(() -> {
                System.out.println("Setting prep state: " + prepState.name());
                requestState(prepState);
            }),
            Commands.waitUntil(this::hasReachedStateGoals).withTimeout(5.0),
            
            // Execute: Score game piece
            Commands.runOnce(() -> {
                System.out.println("Executing score: " + executeState.name());
                requestState(executeState);
            }),
            
            // IMPLEMENT: Add actual scoring mechanism command here
            // e.g., shooter.shoot(), outtake.run(), etc.
            Commands.waitSeconds(0.5),
            
            // Wait before returning to idle
            Commands.waitSeconds(0.3),
            
            // Return to idle
            Commands.runOnce(() -> {
                System.out.println("Returning to idle");
                requestState(RobotState.IDLE);
            }),
            Commands.waitUntil(this::hasReachedStateGoals).withTimeout(5.0),
            Commands.runOnce(() -> masterStateMachine.setGameState(GameState.IDLE))
        );
    }
    
    /**
     * IDLE: Return robot to home/stowed position
     */
    public Command returnToIdle() {
        return Commands.sequence(
            Commands.runOnce(() -> requestState(RobotState.IDLE)),
            Commands.waitUntil(this::hasReachedStateGoals),
            Commands.runOnce(() -> masterStateMachine.setGameState(GameState.IDLE))
        );
    }
    
    /**
     * ENDGAME: Execute endgame sequence (climb, park, etc.)
     */
    public Command executeEndgame() {
        return Commands.sequence(
            // Update states
            Commands.runOnce(() -> {
                masterStateMachine.setGameState(GameState.PREPARING_ENDGAME);
                requestState(RobotState.ENDGAME_PREP);
            }),
            Commands.waitUntil(this::hasReachedStateGoals).withTimeout(3.0),
            
            // Execute endgame
            Commands.runOnce(() -> {
                masterStateMachine.setGameState(GameState.EXECUTING_ENDGAME);
                requestState(RobotState.ENDGAME_EXECUTE);
            }),
            
            // IMPLEMENT: Add actual endgame mechanism commands here
            Commands.waitSeconds(2.0),
            
            // Complete
            Commands.runOnce(() -> {
                masterStateMachine.setGameState(GameState.ENDGAME_COMPLETE);
                requestState(RobotState.ENDGAME_COMPLETE);
            })
        );
    }
    
    /**
     * MANUAL MODE: Allow direct subsystem control (bypasses state machine)
     */
    public Command enterManualMode() {
        return Commands.sequence(
            Commands.runOnce(() -> {
                masterStateMachine.setGameState(GameState.MANUAL_OVERRIDE);
                requestState(RobotState.MANUAL);
            })
        );
    }
    
    /**
     * EXIT MANUAL MODE: Return to state machine control
     */
    public Command exitManualMode() {
        return Commands.runOnce(() -> requestState(RobotState.IDLE));
    }
}
