package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedropathing.MySwerve;
import org.firstinspires.ftc.teamcode.pedropathing.PedroConstants;
import org.firstinspires.ftc.teamcode.robot.subsystem.Intake;
import org.firstinspires.ftc.teamcode.tasks.command_based.core.Scheduler;
import org.firstinspires.ftc.teamcode.util.wrappers.BetterGamepad;

import java.util.List;

@TeleOp
public class  test extends LinearOpMode {
    Scheduler s;
    PedroConstants constants;
    BetterGamepad gamepadEx1;
    double last= 0, now;
    List<LynxModule> hubs;
    MySwerve swerve;
    @Override
    public void runOpMode() throws InterruptedException {
        swerve= PedroConstants.getSwerve(hardwareMap);
        s = new Scheduler();
       //  follower= PedroConstants.createFollower(hardwareMap);
        hubs= hardwareMap.getAll(LynxModule.class);
        hubs.forEach(hub -> hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL));
        waitForStart();
        //follower.startTeleOpDrive();

        swerve.startTeleopDrive(false);
        while (opModeIsActive()){
            hubs.forEach(LynxModule::clearBulkCache);
            now= System.nanoTime();
            telemetry.addData("hz", 1e9/(now- last));
            last= now;
            telemetry.update();
            if(gamepad1.crossWasPressed())
                swerve= PedroConstants.getSwerve(hardwareMap);
         //   follower.setTeleOpDrive(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x);
           swerve.arcadeDrive(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x);
        }
    }
}