package org.firstinspires.ftc.teamcode.opmodes.tuning;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.robot.Alliance;
import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.robot.subsystem.Intake;
import org.firstinspires.ftc.teamcode.robot.subsystem.Shooter;
import org.firstinspires.ftc.teamcode.robot.subsystem.Spindexer;
import org.firstinspires.ftc.teamcode.util.BetterOpMode;
import org.firstinspires.ftc.teamcode.util.wrappers.BetterGamepad;

@TeleOp
public class ShooterTestOneByOne extends LinearOpMode {
    DcMotorEx[] m;
    public static int power = 1;
    long now, last= 0, dt;

    @Override
    public void runOpMode() throws InterruptedException {
        m[0] = hardwareMap.get(DcMotorEx.class, "shooter");
        m[1] = hardwareMap.get(DcMotorEx.class, "shooter2");
        m[0].setDirection(DcMotorSimple.Direction.REVERSE);
        waitForStart();
        while (opModeIsActive()) {
            now = System.nanoTime();
            dt = now - last;
            telemetry.addData("hz", 1e9 / dt);
            //telemetry.addData("velo", m1.getVelocity());
            last = now;
            m[0].setPower(power);
            m[1].setPower(1 - power);
            telemetry.addData("current motor", power + 1);
            telemetry.addData( "current velocity", m[1 - power].getVelocity());
            if(gamepad1.circleWasPressed())
                power = 1 - power;
            if( gamepad1.crossWasPressed() ) {
                Intake.toggle();
            }
            telemetry.update();
        }

    }
}

