package org.firstinspires.ftc.teamcode.opmodes;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

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
import org.firstinspires.ftc.teamcode.tasks.command_based.core.Command;
import org.firstinspires.ftc.teamcode.tasks.command_based.core.Scheduler;
import org.firstinspires.ftc.teamcode.tasks.command_based.core.Task;
import org.firstinspires.ftc.teamcode.tasks.seasonal_tasks.Shoot;
import org.firstinspires.ftc.teamcode.util.BetterOpMode;
import org.firstinspires.ftc.teamcode.util.wrappers.BetterGamepad;

import java.util.List;

@TeleOp
@Configurable
public class  test extends BetterOpMode {

    public static class ShootFortaTunabil implements Task {
        private Command s;

        public double pos;

        public ShootFortaTunabil(){
            s= new Command()
            .addTask(()-> {
                        Intake.start();
                         pos = Shooter.servo.getPosition();
                    })
                    .addTask(()->Math.abs(Math.abs(Shooter.motor1.getVelocity())- Math.abs(Shooter.motor1.getVelocity()))< velocityThreshold)
                    .addTask(Spindexer::shootRandom)
                    .waitSeconds(waitTime)
                    .addTask(()->Shooter.servo.setPosition(pos+increment))
                    .waitSeconds(waitTime2)
                    .addTask(()->Shooter.servo.setPosition(pos+coef* increment))
                    .waitSeconds(1)
                    .addTask(Spindexer::turnBack)
                    .addTask(()->{
                        Shooter.servo.setPosition(pos);
                    });
        }

        @Override
        public boolean Run() {
            s.update();
            return s.done();
        }
    }
    PedroConstants constants;
    BetterGamepad gamepadEx1;
    public Alliance a;
    public static double  velocityThreshold = 300,coef = 2, increment= -0.015, waitTime= 0.28, waitTime2= 0.13;
    ;
    double last= 0, now;
    List<LynxModule> hubs;
    Chassis drive;
    public static PIDFCoefficients coeffs;
    public static double p= 10, i=0, d=0, f= 15;
    public static double vel;
    Robot r;

    @Override
    public void initialize() {
        r = new Robot(hardwareMap, telemetry, this.a)
                .setOpModeType(Robot.OpModeType.TELEOP)
                .initialize();
        Robot.robotPose = new Pose();
        this.a = Alliance.RED;
        gamepadEx1 = new BetterGamepad(gamepad1);
        coeffs = new PIDFCoefficients(p,i,d,f);
        //  follower= PedroConstants.createFollower(hardwareMap);
        drive= new Chassis(hardwareMap)
                .setSuppliers(()-> -gamepad1.left_stick_y
                        ,()-> -gamepad1.left_stick_x
                        ,()-> -gamepad1.right_stick_x)
                .setLimiters(6,6,20)
                .fieldCentric(true)
                .startTeleOpDrive(true);

        gamepadEx1.getButton(BetterGamepad.Buttons.CROSS).whenPressed(Intake::toggle, BetterGamepad.Type.PARALLEL);
        gamepadEx1.getButton(BetterGamepad.Buttons.TOUCHPAD)
                .whenPressed(() -> {
                            double x = this.a == Alliance.BLUE ? Turret.FIELD_LENGTH - PedroConstants.dtWidth / 100 : PedroConstants.dtWidth / 100;
                            double y = PedroConstants.dtLength / 100;
                            double heading = Math.toRadians(90);
                            Robot.odo.setPosX(x, DistanceUnit.METER);//x
                            Robot.odo.setPosY(y, DistanceUnit.METER);//y
                            Robot.odo.setHeading(heading, AngleUnit.RADIANS);
                        }
                        ,
                        BetterGamepad.Type.PARALLEL);
        gamepadEx1.getButton(BetterGamepad.Buttons.RIGHT_BUMPER).whenPressed(ShootFortaTunabil::new, BetterGamepad.Type.PARALLEL);
    }

    @Override
    public void initialize_loop() {

    }

    @Override
    public void on_start() {
    Spindexer.turnBack();
    }
    public static double testpos;
    @Override
    public void active_loop() {
        Shooter.servo.setPosition(Shooter.servo.getPosition() + 0.0025 *
               ((gamepadEx1.getDouble(BetterGamepad.Trigger.RIGHT_TRIGGER) - gamepadEx1.getDouble(BetterGamepad.Trigger.LEFT_TRIGGER))));
        now = System.nanoTime();
        Shooter.motor1.setVelocity(vel);
        Shooter.motor2.setVelocity(vel);
        telemetry.addData("hz", 1e9 / (now - last));
        last = now;
        telemetry.addData("hoodPos", Shooter.servo.getPosition());
        telemetry.addData("vel",Shooter.motor1.getVelocity());
        telemetry.addData("pp x", Robot.odo.getPosX(DistanceUnit.INCH));
        telemetry.addData("pp y", Robot.odo.getPosY(DistanceUnit.INCH));
        telemetry.addData("heading",Robot.odo.getHeading(AngleUnit.DEGREES));
        telemetry.addData("turret s1 pos", Turret.turret1.getPosition());
        telemetry.addData("turret s2 pos", Turret.turret2.getPosition());
        telemetry.addData("yCorner", Turret.yCorner);
        telemetry.addData("xCorner", Turret.xCorner);
        telemetry.addData("turret Relative", Turret.turretRelative);
        telemetry.addData("robot relative", Turret.robotRelative);
        telemetry.addData("field relative", Turret.fieldRelative);
        telemetry.addData("dist", Math.hypot(Turret.xCorner, Turret.yCorner) );
        telemetry.update();
        gamepadEx1.update();
        drive.update();
        Turret.update();
        Spindexer.update();
        r.update();
    }

    @Override
    public void end() {

    }
}
