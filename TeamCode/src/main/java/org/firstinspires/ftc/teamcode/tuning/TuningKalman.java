package org.firstinspires.ftc.teamcode.tuning;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.pedropathing.PedroConstants;
import org.firstinspires.ftc.teamcode.robot.Alliance;
import org.firstinspires.ftc.teamcode.robot.Chassis;
import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.robot.subsystem.Intake;
import org.firstinspires.ftc.teamcode.robot.subsystem.Shooter;
import org.firstinspires.ftc.teamcode.robot.subsystem.Spindexer;
import org.firstinspires.ftc.teamcode.robot.subsystem.Turret;
import org.firstinspires.ftc.teamcode.tasks.seasonal_tasks.Shoot;
import org.firstinspires.ftc.teamcode.tasks.seasonal_tasks.ShootForta;
import org.firstinspires.ftc.teamcode.util.BetterOpMode;
import org.firstinspires.ftc.teamcode.util.MultipleTelemetry;
import org.firstinspires.ftc.teamcode.util.wrappers.BetterGamepad;

import java.util.function.DoubleSupplier;

@TeleOp
@Configurable
public class TuningKalman extends BetterOpMode {
    private MultipleTelemetry telemetry;
    private Robot robot;
    Chassis drive;
    public DoubleSupplier forward, strafe, rotation;
    Alliance a;

    private double lastShotTime = 0;
    private boolean measuringRecovery = false;

    private boolean samplingNoise = false;
    private int noiseSamples = 0;
    private double noiseSum = 0;
    private double noiseSumSq = 0;
    private double noiseVariance = 0;

    private double velBeforeShot = 0;
    private double measuredDrop = 0;
    private boolean captureDrop = false;

    @Override
    public void initialize() {
        telemetry = new MultipleTelemetry(super.telemetry, PanelsTelemetry.INSTANCE.getFtcTelemetry());
        this.a = Alliance.RED;
        robot = new Robot(hardwareMap, this.telemetry, this.a)
                .setOpModeType(Robot.OpModeType.TELEOP)
                .initialize();
        gamepadEx1 = new BetterGamepad(gamepad1);
        drive = new Chassis(hardwareMap)
                .setSuppliers(()-> -gamepad1.left_stick_y
                        ,()-> -gamepad1.left_stick_x
                        ,()-> -gamepad1.right_stick_x)
                .setLimiters(6, 6, 20)
                .fieldCentric(true)
                .startTeleOpDrive(true);
        gamepadEx1.getButton(BetterGamepad.Buttons.DPAD_LEFT)
                .whenPressed(this::measureNoise, BetterGamepad.Type.PARALLEL);

        gamepadEx1.getButton(BetterGamepad.Buttons.DPAD_RIGHT)
                .whenPressed(this::measureDrop, BetterGamepad.Type.PARALLEL);


        gamepadEx1.getButton(BetterGamepad.Buttons.TRIANGLE)
                .whenPressed(Shooter::setKalmanCoefs, BetterGamepad.Type.PARALLEL);
        gamepadEx1.getButton(BetterGamepad.Buttons.CROSS).whenPressed(Intake::toggle, BetterGamepad.Type.PARALLEL);
        gamepadEx1.getButton(BetterGamepad.Buttons.TOUCHPAD)
                .whenPressed(() -> {
                            double x = this.a == Alliance.BLUE ? Turret.FIELD_LENGTH - PedroConstants.dtWidth / 100 : PedroConstants.dtWidth / 100;
                            double y = PedroConstants.dtLength / 100;
                            double heading = Math.toRadians(90);
                            Robot.odo.setPosX(x, DistanceUnit.METER);
                            Robot.odo.setPosY(y, DistanceUnit.METER);
                            Robot.odo.setHeading(heading, AngleUnit.RADIANS);
                        }
                        ,
                        BetterGamepad.Type.PARALLEL);
        gamepadEx1.getButton(BetterGamepad.Buttons.RIGHT_BUMPER)
                .whenPressed(Shoot::new, BetterGamepad.Type.PARALLEL);
        gamepadEx1.getButton(BetterGamepad.Buttons.LEFT_BUMPER)
                .whenPressed(ShootForta::new, BetterGamepad.Type.PARALLEL);
    }

    @Override
    public void initialize_loop() {
    }

    @Override
    public void on_start() {
        Spindexer.turnBack();
    }

    @Override
    public void active_loop() {
        Shooter.servo.setPosition(Shooter.servo.getPosition() + 0.0025 *
                ((gamepadEx1.getDouble(BetterGamepad.Trigger.RIGHT_TRIGGER) - gamepadEx1.getDouble(BetterGamepad.Trigger.LEFT_TRIGGER))));
        robot.update();
        telemetry.update();
        drive.update();
        Spindexer.update();
        Shooter.update();
        Turret.update();
        gamepadEx1.update();
        if (samplingNoise && noiseSamples < 200) {
            double vel = Shooter.motor1.getVelocity();
            noiseSamples++;
            noiseSum += vel;
            noiseSumSq += vel * vel;

            if (noiseSamples >= 200) {
                double mean = noiseSum / noiseSamples;
                noiseVariance = (noiseSumSq / noiseSamples) - (mean * mean);
                samplingNoise = false;
            }
        }

        if (captureDrop && velBeforeShot > 0) {
            double currentVel = Shooter.motor1.getVelocity();
            double timeSinceShot = System.currentTimeMillis() - lastShotTime;

            if (timeSinceShot > 50 && timeSinceShot < 100) {
                measuredDrop = velBeforeShot - currentVel;
                captureDrop = false;
            }
        }

        if (measuringRecovery) {
            double currentVel = Shooter.est;
            double timeSinceShot = System.currentTimeMillis() - lastShotTime;}

            if (noiseVariance > 0) {
                telemetry.addData("NOISE MEASURED", "");
                telemetry.addData("  Variance", "%.2f", noiseVariance);
                telemetry.addData("  StdDev", "%.2f", Math.sqrt(noiseVariance));
                telemetry.addData("  SUGGESTED R", "%.0f", noiseVariance * 3);
            }

            if (measuredDrop > 0) {
                telemetry.addData("DROP MEASURED", "");
                telemetry.addData("  Before shot", "%.0f", velBeforeShot);
                telemetry.addData("  Measured drop", "%.0f", measuredDrop);
                telemetry.addData("  Current estimated", "%.0f", Shooter.estimatedVelocityDrop);
            }

        }


    @Override
    public void end() {

    }

    private void measureNoise() {
        samplingNoise = true;
        noiseSamples = 0;
        noiseSum = 0;
        noiseSumSq = 0;
        noiseVariance = 0;
    }

    private void measureDrop() {
        velBeforeShot = Shooter.motor1.getVelocity();
        lastShotTime = System.currentTimeMillis();
        captureDrop = true;
        measuredDrop = 0;
    }
}

