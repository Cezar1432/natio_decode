package org.firstinspires.ftc.teamcode.tuning;


import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


import org.firstinspires.ftc.teamcode.robot.Alliance;
import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.robot.subsystem.Shooter;
import org.firstinspires.ftc.teamcode.util.wrappers.BetterGamepad;
import org.firstinspires.ftc.teamcode.util.wrappers.BetterMotorEx;
import org.firstinspires.ftc.teamcode.util.wrappers.Motor;
import org.firstinspires.ftc.teamcode.util.wrappers.MotorEx;

import java.util.List;

@Configurable
@TeleOp
public class MotorExTest extends LinearOpMode {

    List<LynxModule> hubs;
    Robot r;
    public static double VELOCITY = 0;
    @Override
    public void runOpMode() throws InterruptedException {
        r= new Robot(hardwareMap, telemetry, Alliance.BLUE).setOpModeType(Robot.OpModeType.TELEOP).initialize();
        hubs= hardwareMap.getAll(LynxModule.class);
        hubs.forEach((hub)-> hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL));
        waitForStart();
        while (opModeIsActive())
        {
            if(gamepad1.dpadLeftWasPressed()){
                Shooter.setCoefs();
            }
            hubs.forEach(LynxModule::clearBulkCache);
            Shooter.motor1.setVelocity(VELOCITY);
            Shooter.motor2.setVelocity(VELOCITY);
            r.update();
            telemetry.update();
            Shooter.motor1.update();
            Shooter.motor2.update();
            telemetry.addData("velocity1", Shooter.motor1.getVelocity());
            telemetry.addData("velocity2", Shooter.motor2.getVelocity());
            telemetry.addData("target", Shooter.motor1.targetVelocity);
            telemetry.addData("max", Shooter.motor1.maxVelocity);
            telemetry.addData("output", Shooter.motor1.output);
            telemetry.addData("f", Shooter.motor1.f);
            telemetry.addData("target-velocity", Shooter.motor1.getVelocity()- Shooter.motor1.targetVelocity);
        }
    }
}
