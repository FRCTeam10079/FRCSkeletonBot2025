package frc.robot;

import java.util.HashMap;

/**
 * CONSTANTS - Robot Configuration
 * 
 * SKELETON TEMPLATE - Customize for each season's robot
 * 
 * This file contains all robot constants organized by subsystem.
 * Update these values when building the actual robot.
 */
public final class Constants {

    // Inches to Meters
    public static final double inToM = 0.0254;

    public enum StartingPos {
        LEFT,
        RIGHT,
        CENTER
    }

    /**
     * Scoring positions (customize for the game)
     */
    public enum ScoringPos {
        LEFT,
        RIGHT,
        CENTER
    }
    
    // Keep ReefPos for backward compatibility (can be removed for new season)
    public enum ReefPos {
        LEFT,
        RIGHT
    }

    // MECHANISM 1 CONSTANTS (e.g., Elevator)
    public static class Mechanism1Constants {
        // Motor CAN IDs
        public static final int MOTOR_ID = 10;
        public static final int ENCODER_ID = 11;
        
        // Positions (in rotations or other units)
        public static final double HOME_POSITION = 0.0;
        public static final double LOW_POSITION = 1.0;
        public static final double MID_POSITION = 2.0;
        public static final double HIGH_POSITION = 3.0;
        
        // PID Constants
        public static final double kP = 1.0;
        public static final double kI = 0.0;
        public static final double kD = 0.0;
        public static final double kG = 0.0;  // Gravity feedforward
        
        // Tolerances
        public static final double POSITION_TOLERANCE = 0.5;
    }

    // MECHANISM 2 CONSTANTS (e.g., Arm/Pivot)
    public static class Mechanism2Constants {
        // Motor CAN IDs
        public static final int MOTOR_ID = 20;
        public static final int ENCODER_ID = 21;
        public static final int SENSOR_ID = 22;
        
        // Positions (in rotations)
        public static final double STOWED_POSITION = 0.0;
        public static final double DEPLOYED_POSITION = 0.5;
        public static final double SCORING_POSITION = 0.25;
        
        // Motor speeds
        public static final double INTAKE_SPEED = 0.5;
        public static final double OUTTAKE_SPEED = -0.5;
        
        // Sensor thresholds
        public static final double DETECTION_DISTANCE_M = 0.03;
        
        // PID Constants
        public static final double kP = 0.2;
        public static final double kD = 0.02;
        public static final double kG = 0.0;
        public static final double POSITION_TOLERANCE = 0.09;
    }

    // MECHANISM 3 CONSTANTS (e.g., Shooter/Intake Wheels)
    public static class Mechanism3Constants {
        // Motor CAN IDs
        public static final int MOTOR_ID = 30;
        
        // Speeds
        public static final double INTAKE_SPEED = 0.8;
        public static final double HOLD_SPEED = 0.1;
        public static final double OUTTAKE_SPEED = -0.8;
        
        // Current limits for game piece detection
        public static final double CURRENT_SPIKE_THRESHOLD = 20.0;
    }

    // PIVOT INTAKE CONSTANTS (Legacy - from 2025 season)
    // Can be removed or kept for reference
    public static class PivotIntakeConstants {
        // Motor CAN IDs
        public static final int PIVOT_MOTOR_ID = 20;
        public static final int INTAKE_WHEEL_MOTOR_ID = 18;
        public static final int PIVOT_ENCODER_ID = 21;
        public static final int CORAL_SENSOR_ID = 22;
        
        // Pivot positions (in rotations)
        public static final double STOWED_POSITION = 0.46;
        public static final double STOWED_POSITION_WITH_CORAL = 0.438;
        public static final double INTAKE_POSITION = 0.0;
        public static final double REEF_SCORING_POSITION = 0.25;
        
        // Motor speeds
        public static final double INTAKE_SPEED = -0.3;
        public static final double INTAKE_REVERSE_SPEED = 0.3;
        
        // Sensor thresholds
        public static final double CORAL_DETECTED_DISTANCE_M = 0.03;
        public static final double CORAL_TOO_LOW_DISTANCE = 0.01;
        
        // PID Constants
        public static final double PIVOT_KP = 0.2;
        public static final double PIVOT_KD = 0.02;
        public static final double PIVOT_KG = -3;
        public static final double PIVOT_TOLERANCE = 0.09;
    }

    // UTILITY FUNCTIONS
    
    /**
     * Check if a value is in an array
     */
    public static boolean contains(double[] array, double value) {
        for (double element : array) {
            if (element == value) {
                return true;
            }
        }
        return false;
    }

    // APRIL TAG POSITIONS (Update for each season's field)
    public class AprilTagMaps {
        // Field Map Source: Update URL for current season
        // A HashMap of April Tag positions: Key = ID, Value = [X, Y, Z, Yaw, Pitch]
        public static final HashMap<Integer, double[]> aprilTagMap = new HashMap<>();
        static {
            // Points are in inches, Angles are in degrees
            // UPDATE THESE FOR EACH SEASON'S FIELD LAYOUT
            
            // RED SIDE TAGS
            //aprilTagMap.put(1, new double[]{0.0, 0.0, 0.0, 0.0, 0.0});
            //aprilTagMap.put(2, new double[]{0.0, 0.0, 0.0, 0.0, 0.0});
            aprilTagMap.put(3, new double[]{455.15, 317.15, 51.25, 270.0, 0.0});
            aprilTagMap.put(4, new double[]{365.20, 241.64, 73.54, 0.0, 30.0});
            aprilTagMap.put(5, new double[]{365.20, 75.39, 73.54, 0.0, 30.0});
            aprilTagMap.put(6, new double[]{530.49, 130.17, 12.13, 300.0, 0.0});
            aprilTagMap.put(7, new double[]{546.87, 158.50, 12.13, 0.0, 0.0});
            aprilTagMap.put(8, new double[]{530.49, 186.83, 12.13, 60.0, 0.0});
            aprilTagMap.put(9, new double[]{497.77, 186.83, 12.13, 120.0, 0.0});
            aprilTagMap.put(10, new double[]{481.39, 158.50, 12.13, 180.0, 0.0});
            aprilTagMap.put(11, new double[]{497.77, 130.17, 12.13, 240.0, 0.0});
            
            // BLUE SIDE TAGS
            aprilTagMap.put(14, new double[]{325.68, 241.64, 73.54, 180.0, 30.0});
            aprilTagMap.put(15, new double[]{325.68, 75.39, 73.54, 180.0, 30.0});
            aprilTagMap.put(16, new double[]{235.73, -0.15, 51.25, 90.0, 0.0});
            aprilTagMap.put(17, new double[]{160.39, 130.17, 12.13, 240.0, 0.0});
            aprilTagMap.put(18, new double[]{144.00, 158.50, 12.13, 180.0, 0.0});
            aprilTagMap.put(19, new double[]{160.39, 186.83, 12.13, 120.0, 0.0});
            aprilTagMap.put(20, new double[]{193.10, 186.83, 12.13, 60.0, 0.0});
            aprilTagMap.put(21, new double[]{209.49, 158.50, 12.13, 0.0, 0.0});
            aprilTagMap.put(22, new double[]{193.10, 130.17, 12.13, 300.0, 0.0});
        }
    }
}
