# Hackathon Project Grade Report: Nethul

**Date:** December 17, 2025  
**Project:** Hackathon

---

## Overall Score: **96/100** (A)
_All scores are out of 100 (bonus is optional and does not reduce the base 100)._

---

## Files Reviewed

| File | Purpose |
|------|---------|
| `PivotIntakeSubsystem.java` | Controls pivot mechanism and intake rollers |
| `ScoreBallAuto.java` | Autonomous command sequence for scoring |
| `RobotContainer.java` | Controller bindings and subsystem integration |

---

## Hackathon Goals Checklist

| Requirement | Status |
|-------------|--------|
| Pivot goes to certain level | Implemented |
| Pick up a ball | Implemented |
| Aim towards spot when picked | Implemented |
| Shoot the ball | Implemented |
| Bonus: Vision for pivot alignment (optional) | Not attempted |

---

## Analysis

### 1. `PivotIntakeSubsystem.java` — **28 / 28** points to this area

**What I found works:**
- Correct subsystem structure extending `SubsystemBase`
- Full hardware configuration with PID values (`kP`, `kD`, `kG`)
- Separate position methods: `moveToFloor()`, `moveToStow()`, `moveToShoot()`
- Roller control: `suckBall()`, `shootBall()`, `stopIntake()`
- Object detection via CANrange sensor with `hasObject()`
- `isAtSetpoint()` for sequencing
- Neutral mode set to Brake for safety
- Absolute encoder sync on startup
- SmartDashboard logging in `periodic()`

**Examples:**
```java
private void configureHardware() {
    TalonFXConfiguration pivotConfig = new TalonFXConfiguration();
    pivotConfig.Slot0.kP = PivotIntakeConstants.PIVOT_KP;
    pivotConfig.Slot0.kD = PivotIntakeConstants.PIVOT_KD;
    pivotConfig.Slot0.kG = PivotIntakeConstants.PIVOT_KG;
    pivotConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    pivotMotor.getConfigurator().apply(pivotConfig);
    
    // Sync absolute encoder
    pivotMotor.setPosition(pivotEncoder.getAbsolutePosition().getValueAsDouble());
}

public boolean hasObject() {
    return objectSensor.getDistance().getValueAsDouble() < 0.20; 
}
```

**My notes:**
- Excellent motor configuration with proper PID and gravity compensation
- Good use of CANrange for game piece detection
- Clean method naming makes the code very readable
- I would suggest adding soft limits to prevent over-rotation, but this is minor

---

### 2. `ScoreBallAuto.java` — **28 / 28** points to this area

**What I found works:**
- Proper use of `SequentialCommandGroup` for multi-step autonomous
- Clear step-by-step sequence with comments
- Uses `WaitUntilCommand` for proper sequencing
- Includes timeouts to prevent infinite waits
- Proper subsystem requirements passed to `InstantCommand`
- Full cycle: prepare -> drive -> detect -> stow -> drive back -> aim -> fire -> reset

**Examples:**
```java
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
    
    // ... continues with full sequence
);
```

**My notes:**
- This is exactly the right approach for autonomous routines
- The timeouts on `WaitUntilCommand` show good defensive programming
- The brake commands between movements are a nice touch for precision

---

### 3. `RobotContainer.java` — **24 / 24** points to this area

**What I found works:**
- Clean subsystem instantiation
- Proper default command for drivetrain with deadbands
- A button: Floor intake with `onTrue`/`onFalse` for press/release behavior
- Right trigger: Shoot sequence with proper release handling
- Autonomous command properly returns the `ScoreBallAuto`

**Examples:**
```java
// A Button: Floor Intake
driverJoy.a().onTrue(
    new InstantCommand(() -> pivotSub.moveToFloor())
    .andThen(new InstantCommand(() -> pivotSub.suckBall()))
).onFalse(
    new InstantCommand(() -> pivotSub.stopIntake())
    .andThen(new InstantCommand(() -> pivotSub.moveToStow()))
);
```

**My notes:**
- Good use of `onTrue`/`onFalse` for intuitive driver control
- The chained `InstantCommand` pattern works well here
- Clean and readable bindings

---

## Grading Rubric

| Category | Points (achieved / max) | Notes |
|----------|------------------------:|-------|
| Pivot Control | 28 / 28 | Full configuration with PID, positions, detection |
| Intake/Shoot Control | 24 / 24 | Complete roller control with speeds |
| Command Structure | 28 / 28 | Excellent `SequentialCommandGroup` usage |
| Integration | 10 / 10 | `RobotContainer` fully integrated |
| Code Organization | 6 / 10 | Clean but some constants could be centralized |
| **Base Total** | **96 / 100** | — |

_Optional bonus (vision) would add additional points if implemented; it does not reduce the base 100 if omitted._

---

## Pros

1. **Complete Implementation** - All hackathon goals are fully implemented and functional
2. **Proper Motor Configuration** - PID values, neutral modes, and encoder sync are all present
3. **Excellent Command Composition** - `SequentialCommandGroup` with `WaitUntilCommand` is the right pattern
4. **Game Piece Detection** - CANrange sensor integration with `hasObject()` is great
5. **Defensive Programming** - Timeouts on wait commands prevent infinite loops
6. **Clean Teleop Controls** - `onTrue`/`onFalse` pattern makes intuitive driver controls
7. **Good Comments** - The auto routine has clear step numbers and explanations

---

## My Suggestions for Next Time

1. **Centralize Constants** - Some values like `INTAKE_SPEED`, `SHOOT_SPEED`, `SHOOT_ANGLE` are defined in the subsystem. I'd move them to `Constants.java` for easier tuning.
2. **Add Soft Limits** - Consider adding software limits to prevent the pivot from over-rotating and damaging hardware.
3. **Vision Integration** - For the bonus, you could use the Limelight to adjust the `SHOOT_ANGLE` based on distance to target.

---

## Final Summary

You delivered a complete, working implementation of the hackathon requirements. The `PivotIntakeSubsystem` has proper motor configuration with PID values, encoder sync, and game piece detection. The `ScoreBallAuto` command demonstrates excellent understanding of WPILib's command-based framework with proper use of `SequentialCommandGroup`, `WaitUntilCommand`, and timeouts. The `RobotContainer` bindings are clean and provide intuitive driver controls.

This is exactly what a working pivot intake system should look like. The code is well-organized, properly commented, and would be ready for real robot testing.

**Grade: A (96/100)** - Excellent work! All core requirements implemented and functional. (Bonus not achieved, but not required)
