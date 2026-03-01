package org.firstinspires.ftc.teamcode;

import com.bylazar.telemetry.PanelsTelemetry;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.pedropathing.PedroConstants;
import org.firstinspires.ftc.teamcode.robot.Alliance;
import org.firstinspires.ftc.teamcode.robot.Chassis;
import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.robot.subsystem.Intake;
import org.firstinspires.ftc.teamcode.robot.subsystem.Spindexer;
import org.firstinspires.ftc.teamcode.robot.subsystem.Turret;
import org.firstinspires.ftc.teamcode.util.BetterOpMode;
import org.firstinspires.ftc.teamcode.util.MultipleTelemetry;
import org.firstinspires.ftc.teamcode.util.wrappers.BetterGamepad;
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
        gamepadEx1.getButton(BetterGamepad.Buttons.CROSS).whenPressed(Intake::toggle);
        gamepadEx1.getButton(BetterGamepad.Buttons.TOUCHPAD)
                .whenPressed(()->{
                    double x= this.a== Alliance.BLUE ? Turret.FIELD_LENGTH - PedroConstants.dtWidth/100 : PedroConstants.dtWidth/100;
                    double y= PedroConstants.dtLength/100;
                    double heading= Math.toRadians(90);
                    Robot.odo.setPosX(x, DistanceUnit.METER);
                    Robot.odo.setPosY(y, DistanceUnit.METER);
                    Robot.odo.setHeading(heading, AngleUnit.RADIANS);
                });


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
        robot.update();
        telemetry.update();
        drive.update();
        Spindexer.update();
        Turret.update();
        telemetry.addData("X", Robot.odo.getPosX(DistanceUnit.METER));
        telemetry.addData("Y", Robot.odo.getPosY(DistanceUnit.METER));
        telemetry.addData("heading",Robot.odo.getHeading(AngleUnit.RADIANS));
        telemetry.addData("turret s1 pos", Turret.turret1.getPosition());
        telemetry.addData("turret s2 pos", Turret.turret2.getPosition());
        telemetry.addData("turret Relative", Turret.turretRelative);
        telemetry.addData("state", Spindexer.breakBeam.getBeamState());
        //telemetry.update();
    }

    @Override
    public void end() {

    }
}
