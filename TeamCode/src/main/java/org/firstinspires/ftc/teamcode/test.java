package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.List;

@TeleOp
public class test extends LinearOpMode {
    Follower follower;
    double last= 0, now;
    List<LynxModule> hubs;
    @Override
    public void runOpMode() throws InterruptedException {
        follower= PedroConstants.createFollower(hardwareMap);
        hubs= hardwareMap.getAll(LynxModule.class);
        hubs.forEach(hub -> hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL));
        waitForStart();
        follower.startTeleOpDrive();

        while (opModeIsActive()){
            hubs.forEach(LynxModule::clearBulkCache);
            now= System.nanoTime();
            telemetry.addData("hz", 1e9/(now- last));
            last= now;
            telemetry.update();
            follower.setTeleOpDrive(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);
            follower.update();

        }
    }
}