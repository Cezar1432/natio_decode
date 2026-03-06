package org.firstinspires.ftc.teamcode.tuning;

import com.arcrobotics.ftclib.hardware.motors.MotorGroup;
import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.util.wrappers.Motor;
import org.firstinspires.ftc.teamcode.util.wrappers.MotorEx;

import java.util.List;

@Configurable
@TeleOp
public class MotorExTest extends LinearOpMode {

    MotorEx m1,m2;
    List<LynxModule> hubs;
    public static double VELOCITY = 0;
    public static double vP=5.3,vI=0,vD=0.1,fS=200,fV=1.8,fA=0;
    @Override
    public void runOpMode() throws InterruptedException {
        hubs= hardwareMap.getAll(LynxModule.class);
        hubs.forEach((hub)-> hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL));
        m2 = new MotorEx(hardwareMap,"shooter2", Motor.GoBILDA.BARE).setCachingTolerance(0.01);
        m1 = new MotorEx(hardwareMap,"shooter", Motor.GoBILDA.BARE).setCachingTolerance(0.01).setInverted(true);
        m1.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        m2.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        m1.setRunMode(Motor.RunMode.VelocityControl);
        m2.setRunMode(Motor.RunMode.VelocityControl);
        m1.ACHIEVABLE_MAX_TICKS_PER_SECOND = 1900;
        m2.ACHIEVABLE_MAX_TICKS_PER_SECOND = 1900;
        m1.encoder.setDirection(Motor.Direction.FORWARD);
        m2.encoder.setDirection(Motor.Direction.FORWARD);
//        m2.setRunMode(Motor.RunMode.RawPower);
//        m1.setRunMode(Motor.RunMode.RawPower);
        waitForStart();
        while (opModeIsActive())
        {
            hubs.forEach(LynxModule::clearBulkCache);
            telemetry.addData("achievable max ticks/s", m1.ACHIEVABLE_MAX_TICKS_PER_SECOND);
            telemetry.addData("m1 vel",m1.getVelocity());
            telemetry.addData("m2 vel",m2.getVelocity());
            telemetry.addData("m1 correctedVel",m1.getCorrectedVelocity());
            telemetry.addData("m1 accel",m1.getAcceleration());
            m1.setVelocity(VELOCITY);
            m2.setVelocity(VELOCITY);
            if(gamepad1.triangleWasPressed())
            {

                    m1.setVeloCoefficients(vP,vI,vD);
                    m1.setFeedforwardCoefficients(fS,fV,fA);
                    m2.setVeloCoefficients(vP,vI,vD);
                    m2.setFeedforwardCoefficients(fS,fV,fA);
            }
            telemetry.update();
        }
    }
}
