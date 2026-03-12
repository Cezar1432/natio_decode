package org.firstinspires.ftc.teamcode.opmodes.tuning;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.robot.Alliance;
import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.robot.subsystem.Shooter;
import org.firstinspires.ftc.teamcode.robot.subsystem.Spindexer;
import org.firstinspires.ftc.teamcode.util.BetterOpMode;
import org.firstinspires.ftc.teamcode.util.wrappers.BetterGamepad;

@TeleOp
public class ShooterTest extends LinearOpMode {
    DcMotorEx m1,m2;
    public static double power;
    long now, last= 0, dt;

    @Override
    public void runOpMode() throws InterruptedException {
        m1 = hardwareMap.get(DcMotorEx.class, "shooter");
        m2 = hardwareMap.get(DcMotorEx.class, "shooter2");
        m1.setDirection(DcMotorSimple.Direction.REVERSE);
        waitForStart();
        while (opModeIsActive()) {
            now = System.nanoTime();
            dt = now - last;
            telemetry.addData("hz", 1e9 / dt);
            telemetry.addData("velo", m1.getVelocity());
            last = now;
            m1.setPower(1);
            m2.setPower(1);
            telemetry.update();
        }

    }
}
