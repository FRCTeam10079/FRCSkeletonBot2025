# Hackathon Project Grade Report: Oliver

**Date:** December 17, 2025  
**Project:** Hackathon

---

## Overall Score: **81/100** (B-)
_All scores are out of 100 (bonus is optional and does not reduce the base 100)._ 

---

## Files Reviewed

| File | Purpose |
|------|---------|
| `IntakeObject.java` | Main command for intake sequence |
| `IntakeMotorSubsystem.java` | Controls intake motor |
| `PivotSubsystem.java` | Controls pivot mechanism |

---

## Hackathon Goals Checklist

| Requirement | Status |
|-------------|--------|
| Pivot goes to certain level | Implemented |
| Pick up a ball | Partial - Command incomplete |
| Aim towards spot when picked | Partial - Setpoint method exists |
| Shoot the ball | Partial - Motor control exists |
| Bonus: Vision for pivot alignment (optional) | Not attempted |
---

## Analysis

### 1. `PivotSubsystem.java` — **24 / 24** points to this area

**What I found works:**
- Correct subsystem structure extending `SubsystemBase`
- `setSetpoint()` method properly controls motor position
- `getPosition()` returns encoder reading
- `isAtSetpoint()` with configurable tolerance - good for sequencing
- `hasItem()` method available for state tracking
- SmartDashboard logging in `periodic()` for debugging

**Examples:**
```java
public void setSetpoint(double setpoint) {
    currentSetpoint = setpoint;
    motor.setControl(positionControl.withPosition(setpoint));
    encoder.setPosition(setpoint);
}

public boolean isAtSetpoint(double tolerance){
    return Math.abs(getPosition() - currentSetpoint) < tolerance;
}
```

**My notes:**
- Motor configuration is minimal but functional for a learning project
- I recommend adding PID tuning constants in the future for smoother motion

---

### 2. `IntakeMotorSubsystem.java` — **22 / 24** points to this area

**What I found works:**
- Correct subsystem structure
- `setIntakeSpeed()` method controls motor voltage
- `setMotorSpeed()` provides direct motor control
- Speed tracking variable for dashboard
- SmartDashboard logging present

**Example:**
```java
public void setMotorSpeed(double voltage){
    motor.set(voltage);
    currentSpeed = voltage;
}
```

**My notes:**
- Game piece detection was optional but could have been nice
- Basic motor control is sufficient for the hackathon goals

---

### 3. `IntakeObject.java` — **14 / 23** points to this area

**What I found works:**
- Correct command structure extending `Command`
- Proper `addRequirements()` for both subsystems
- Has both subsystem dependencies injected
- `isFinished()` and `finish()` pattern exists

**Main Issue I found:**
The `execute()` method contains only commented-out code:
```java
@Override
public void execute() {
    // All logic is commented out - command doesn't do anything
}
```

**What was in the comments:**
You had the right idea for the sequence:
1. Deploy pivot to intake position
2. Run intake motors
3. Wait for collection
4. Stow pivot
5. Reverse motors for scoring

**My notes:**
- The approach in the comments shows understanding of the sequence
- Command composition using `.andThen()` inside `execute()` won't work as intended. It should be in `initialize()` or implemented as a `SequentialCommandGroup`
- The subsystems provide the methods needed; the command needs to call them properly

---

## What I Recommend to Complete the Project

To get the `IntakeObject` command working, I recommend:

**Option A - Simple State Machine (This is if you want to mess around with this idea):**
```java
private int state = 0;

@Override
public void execute() {
    switch(state) {
        case 0: // Deploy
            pivotSubsystem.setSetpoint(PivotConstants.INTAKE_POSITION);
            if(pivotSubsystem.isAtSetpoint()) state = 1;
            break;
        case 1: // Intake
            intakeMotorSubsystem.setMotorSpeed(0.5);
            // After time or detection, move to next state
            break;
        // ... etc
    }
}
```

**Option B - Command Composition in `RobotContainer` (I think this would be best however as it's easiest and no drawbacks):**
```java
Command intakeSequence = Commands.sequence(
    Commands.runOnce(() -> pivotSub.setSetpoint(INTAKE_POS)),
    Commands.waitUntil(() -> pivotSub.isAtSetpoint()),
    intakeSub.setIntakeSpeed(0.5).withTimeout(2.0),
    Commands.runOnce(() -> pivotSub.setSetpoint(STOWED_POS))
);
```

---

## Grading Rubric

| Category | Points (achieved / max) | Notes |
|----------|------------------------:|-------|
| Pivot Control | 24 / 24 | Subsystem works correctly |
| Intake Control | 22 / 24 | Motor control implemented |
| Command Structure | 14 / 23 | Structure good, `execute()` empty |
| Integration | 12 / 18 | `RobotContainer` has bindings |
| Code Organization | 9 / 11 | Clean file structure |
| **Base Total** | **81 / 100** | — |

_Optional bonus (vision) would add additional points if implemented; it does not reduce the base 100 if omitted._
---

## Pros

1. **Good Understanding of WPILib Structure** - Subsystems and commands are set up correctly
2. **Proper Dependency Injection** - Constructors take subsystems as parameters
3. **State Tracking** - Variables for position and status are present
4. **Dashboard Logging** - SmartDashboard usage for debugging is great!
5. **Right Approach** - The commented sequence shows the correct high-level plan

---

## My Suggestions for Next Time

1. **Test Incrementally** - I recommend getting one part working before moving to the next
2. **Command Composition** - I recommend using `SequentialCommandGroup` or `Commands.sequence()` for multi-step actions

---

## Final Summary

You built functional subsystems for both the pivot and intake mechanisms. The core infrastructure is solid: motors can be controlled, positions can be set, and the command framework is in place. The main gap I found is that the `IntakeObject` command's logic was never uncommented or implemented, leaving the sequence non-functional.

The subsystems demonstrate understanding of FRC programming patterns. With the `execute()` method properly filled in, I believe this would be a working intake system.


**Grade: B- (81/100)** - Good foundation, incomplete execution. (Bonus not achieved, but not required)
