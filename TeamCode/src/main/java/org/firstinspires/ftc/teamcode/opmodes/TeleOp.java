package org.firstinspires.ftc.teamcode.opmodes;

import static org.firstinspires.ftc.teamcode.robot.subsystem.Spindexer.sorting;

import com.bylazar.telemetry.PanelsTelemetry;

import org.firstinspires.ftc.robotcore.external.Telemetry;
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
import org.firstinspires.ftc.teamcode.tasks.seasonal_tasks.ShootForta;
import org.firstinspires.ftc.teamcode.tasks.seasonal_tasks.Spit;
import org.firstinspires.ftc.teamcode.tuning.MotorExTest;
import org.firstinspires.ftc.teamcode.util.BetterOpMode;
import org.firstinspires.ftc.teamcode.util.MultipleTelemetry;
import org.firstinspires.ftc.teamcode.util.wrappers.BetterGamepad;
import org.firstinspires.ftc.teamcode.util.wrappers.colorsensor.Colors;

import java.util.function.DoubleSupplier;

public abstract class TeleOp extends BetterOpMode {


    public Alliance a;
    public abstract void setAlliance();
    Robot robot;
    Chassis drive;
    MultipleTelemetry telemetry;
    public DoubleSupplier forward, strafe, rotation;

    public abstract void setDriveSuppliers();
    @Override
    public void initialize() {
        setAlliance();
        setDriveSuppliers();
        telemetry= new MultipleTelemetry(super.telemetry, PanelsTelemetry.INSTANCE.getFtcTelemetry());
        robot= new Robot(hardwareMap, this.telemetry, this.a)
                .setOpModeType(Robot.OpModeType.TELEOP)
                .initialize();

        drive= new Chassis(hardwareMap)
                .setSuppliers(forward, strafe, rotation)
                .setLimiters(6,6,20)
                .fieldCentric(true)
                .startTeleOpDrive(true);
        gamepadEx1.getButton(BetterGamepad.Buttons.CROSS).whenPressed(Intake::toggle, BetterGamepad.Type.PARALLEL);
        gamepadEx1.getButton(BetterGamepad.Buttons.TOUCHPAD)
                .whenPressed(()->{
                            double x= this.a== Alliance.BLUE ? Turret.FIELD_LENGTH - PedroConstants.dtWidth/100 : PedroConstants.dtWidth/100;
                            double y= PedroConstants.dtLength/100;
                            double heading= Math.toRadians(90);
                            Robot.odo.setPosX(x, DistanceUnit.METER);
                            Robot.odo.setPosY(y, DistanceUnit.METER);
                            Robot.odo.setHeading(heading, AngleUnit.RADIANS);
                        }
                        ,
                        BetterGamepad.Type.PARALLEL);

        gamepadEx1.getButton(BetterGamepad.Buttons.TRIANGLE)
                .whenPressed(Spit::new, BetterGamepad.Type.PARALLEL);

        gamepadEx1.getButton(BetterGamepad.Buttons.RIGHT_BUMPER)
                .whenPressed(ShootForta::new, BetterGamepad.Type.PARALLEL);
        gamepadEx1.getButton(BetterGamepad.Buttons.CIRCLE)
                .whenPressed(Spindexer::clearAll, BetterGamepad.Type.PARALLEL);
        gamepadEx1.getButton(BetterGamepad.Buttons.DPAD_LEFT)
                .whenPressed(() -> {
                    if( !sorting )
                        sorting = true;
                    else
                        sorting = false;
                }, BetterGamepad.Type.PARALLEL);
        gamepadEx1.getButton(BetterGamepad.Buttons.DPAD_DOWN)
                .whenPressed(Shooter::setKalmanCoefs, BetterGamepad.Type.PARALLEL);
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
        //Turret.setNeutralPosition(0.5);
       Shooter.setVelocity(MotorExTest.VELOCITY);
        robot.update();
        telemetry.update();
        drive.update();
        Spindexer.update();
        Turret.update();
        gamepadEx1.update();
        telemetry.addData("dist", Turret.dist);
        telemetry.addData("hood", Shooter.servo.getPosition());
        telemetry.addData("velocity", Shooter.motor1.getVelocity());
        telemetry.addData("kalman estimate", Shooter.est);
        telemetry.addData("corrected vel", Shooter.motor1.getCorrectedVelocity());

        if( sorting )
            telemetry.addData("distance to ball", Spindexer.dist);
    }

    @Override
    public void end() {

    }
}