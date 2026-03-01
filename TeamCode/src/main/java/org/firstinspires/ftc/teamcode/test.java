package org.firstinspires.ftc.teamcode;

import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedropathing.PedroConstants;
import org.firstinspires.ftc.teamcode.robot.Alliance;
import org.firstinspires.ftc.teamcode.robot.Chassis;
import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.tasks.command_based.core.Command;
import org.firstinspires.ftc.teamcode.util.wrappers.BetterGamepad;

import java.util.List;

@TeleOp
public class  test extends LinearOpMode {
    Command s;
    PedroConstants constants;
    BetterGamepad gamepadEx1;
    double last= 0, now;
    List<LynxModule> hubs;
    Chassis drive;
    Robot r;
    @Override
    public void runOpMode() throws InterruptedException {

        r = new Robot(hardwareMap, telemetry, Alliance.BLUE)
                .setOpModeType(Robot.OpModeType.TELEOP)
                .initialize();
        Robot.robotPose = new Pose();
       //  follower= PedroConstants.createFollower(hardwareMap);
        drive= new Chassis(hardwareMap)
                .setSuppliers(()-> -gamepad1.left_stick_y
                ,()-> -gamepad1.left_stick_x
                ,()-> -gamepad1.right_stick_x)
                .setLimiters(6,6,20)
                .robotCentric(true)
                .startTeleOpDrive(true);
        waitForStart();
        //follower.startTeleOpDrive();

        while (opModeIsActive()){
            now= System.nanoTime();
            telemetry.addData("hz", 1e9/(now- last));
            last= now;
            telemetry.update();
            drive.update();
            r.update();
         //   follower.setTeleOpDrive(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x);
        }
    }
}