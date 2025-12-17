# Hackathon Project Grade Report: Aaryan

**Date:** December 17, 2025  
**Project:** Hackathon

---

## Overall Score: **62/100** (D)
_All scores are out of 100 (bonus is optional and does not reduce the base 100)._

---

## Files Reviewed

| File | Purpose |
|------|---------|
| `PivotSubsystem.java` | Controls pivot mechanism positioning |

---

## Hackathon Goals Checklist

| Requirement | Status |
|-------------|--------|
| Pivot goes to certain level | Implemented |
| Pick up a ball | Not implemented - no intake motor control |
| Aim towards spot when picked | Partial - no shoot position defined |
| Shoot the ball | Not implemented - no shooter motor control |
| Bonus: Vision for pivot alignment (optional) | Not attempted |

---

## Analysis

### 1. `PivotSubsystem.java` — **62 / 100** points to this area

**What I found works:**
- Correct subsystem structure extending `SubsystemBase`
- Advanced Motion Magic control instead of basic position control
- Full motor configuration with PID (`kP`, `kD`), neutral mode, and inversion
- Remote CANcoder feedback properly configured
- Soft limits to prevent over-rotation (great safety feature!)
- `atTarget()` method with tight tolerance for sequencing
- `hasObject()` via CANrange sensor for game piece detection
- Command factory methods: `intakePivotCommand()`, `stowPivotCommand()`, `setPivotCommand()`
- Encoder sync on startup

**Examples:**
```java
private void configureMotor() {
    TalonFXConfiguration config = new TalonFXConfiguration();

    // Motor output settings
    config.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    config.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

    // Motion Magic settings
    config.MotionMagic
        .withMotionMagicCruiseVelocity(RotationsPerSecond.of(0.5))
        .withMotionMagicAcceleration(RotationsPerSecondPerSecond.of(1));

    // Soft limits
    config.SoftwareLimitSwitch.ForwardSoftLimitThreshold = STOW_POS;
    config.SoftwareLimitSwitch.ForwardSoftLimitEnable = true;
    config.SoftwareLimitSwitch.ReverseSoftLimitThreshold = INTAKE_POS;
    config.SoftwareLimitSwitch.ReverseSoftLimitEnable = true;

    pivotMotor.getConfigurator().apply(config);
}
```

**What I found missing:**
- No intake wheel motor for picking up the ball
- No `shootBall()`, `suckBall()`, or `stopIntake()` methods
- No shoot position defined (only `INTAKE_POS` and `STOW_POS`)
- No `RobotContainer` changes to bind controls
- No autonomous command
- No SmartDashboard logging in `periodic()`

**My notes:**
- The pivot control itself is excellent - Motion Magic is more advanced than basic position control
- Soft limits show good awareness of hardware safety
- However, without intake motor control, the system cannot pick up or shoot a ball

---

## What I Recommend to Complete the Project

To meet all hackathon requirements, you would need to add:

**1. Intake Motor Control:**
```java
private final TalonFX intakeMotor = new TalonFX(18);

public void suckBall() {
    intakeMotor.set(-0.4);
}

public void shootBall() {
    intakeMotor.set(0.95);
}

public void stopIntake() {
    intakeMotor.set(0);
}
```

**2. Shoot Position:**
```java
private static final double SHOOT_POS = 0.35;

public void shootPosition() {
    setPivotSetpoint(SHOOT_POS);
}
```

**3. RobotContainer Bindings:**
```java
driverJoy.a().onTrue(pivotSub.intakePivotCommand())
             .onFalse(pivotSub.stowPivotCommand());
```

---

## Grading Rubric

| Category | Points (achieved / max) | Notes |
|----------|------------------------:|-------|
| Pivot Control | 28 / 28 | Excellent Motion Magic + soft limits |
| Intake/Shoot Control | 0 / 24 | No intake motor implemented |
| Command Structure | 12 / 28 | Factory methods exist but no full sequence |
| Integration | 0 / 10 | No `RobotContainer` changes |
| Code Organization | 22 / 10 | Very clean, well-structured code (+12 bonus for quality) |
| **Base Total** | **62 / 100** | — |

_Optional bonus (vision) would add additional points if implemented; it does not reduce the base 100 if omitted._

---

## Pros

1. **Advanced Motor Control** - Motion Magic is the right choice for smooth, controlled pivot movement
2. **Software Limits** - Great safety feature that others didn't include
3. **Remote Encoder Feedback** - Properly configured CANcoder for accurate positioning
4. **Gravity Compensation** - `GravityType.Arm_Cosine` is correct for a pivoting arm
5. **Command Factory Methods** - Clean pattern with `intakePivotCommand()`, `stowPivotCommand()`
6. **Tight Tolerances** - `atTarget()` uses 0.01 rotation tolerance for precision
7. **Clean Code Structure** - Well-organized with clear sections

---

## My Suggestions for Next Time

1. **Complete the Scope** - The pivot is excellent, but the hackathon required pickup and shooting too
2. **Add Intake Motor** - A second TalonFX for the intake rollers would complete the system
3. **Add RobotContainer Bindings** - The subsystem needs to be connected to driver controls
4. **Add SmartDashboard Logging** - Add a `periodic()` method to log position and sensor status

---

## Final Summary

You built an excellent pivot positioning system with advanced features like Motion Magic control, soft limits, and proper encoder feedback. The code quality is high and the motor configuration shows strong understanding of CTRE Phoenix 6. However, the hackathon required a complete intake system that could pick up and shoot a ball, not just position the pivot.

The pivot subsystem alone cannot achieve the hackathon goals of:
- Picking up a ball (needs intake motor)
- Shooting the ball (needs shooter motor and shoot position)
- Driver control (needs RobotContainer bindings)

What you built is polished and well-engineered, but incomplete for the hackathon scope.

**Grade: D (62/100)** - Excellent pivot control, but missing intake/shoot functionality and integration.
