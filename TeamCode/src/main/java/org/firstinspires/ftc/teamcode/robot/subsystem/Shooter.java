package org.firstinspires.ftc.teamcode.robot.subsystem;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.util.math.KalmanFilter;
import org.firstinspires.ftc.teamcode.util.wrappers.BetterMotorEx;
import org.firstinspires.ftc.teamcode.util.wrappers.BetterServo;

@Configurable
public class Shooter {

    public static BetterMotorEx motor1, motor2;
    public static BetterServo servo;
    public static final double a1 = -0.0641147, b1 = 0.390703, c1 = 0.18375;
    public static final double a2 = -100.04613, b2 = 804.30749, c2 = 357.66532;
    private static final KalmanFilter velKalman = new KalmanFilter(
            0.0,   // x0
            400.0, // p0
            2000.0,// q (process noise)  -> tune
            8000.0 // r (measurement noise)-> tune
    );
    public static double shotExpectedDropTicksPerSec = 100.0; // tune
    public static double compensationGain = 1.0;              // error -> boost
    public static double compensationMaxTicksPerSec = 600.0;  // cap boost
    public static double recoveryDeadbandTicksPerSec = 30.0;
    public static double dtSeconds = 0.2; // optional

    public static double targetVelTicksPerSec = 0.0;

    public static double estimatedVelocityDrop= 150;
    public static void onShot(){
        velKalman.applyImpulse(- estimatedVelocityDrop);
    }
    public static void setVelocity(double vel){
        targetVelTicksPerSec= vel;
    }
    public static double hoodPos;
    public static void update(){
        double distt= Turret.dist;
        hoodPos = Range.clip(a1 * Math.pow(distt, 2) + b1 * distt + c1, 0.58, 0.789);
        targetVelTicksPerSec = -Math.pow(distt,2)*a2+b2*distt+c2;
        // DEFAULT getVelocity(): encoder ticks/sec
        double measTicksPerSec = motor1.getVelocity();

        double est = (dtSeconds > 0.0)
                ? velKalman.update(measTicksPerSec, dtSeconds)
                : velKalman.update(measTicksPerSec);

        double err = targetVelTicksPerSec - est;

        double boost = 0.0;
        if (Math.abs(err) > recoveryDeadbandTicksPerSec) {
            boost = compensationGain * err;
            boost = Range.clip(boost, -compensationMaxTicksPerSec, compensationMaxTicksPerSec);
        }

        double cmd = Math.max(0.0, targetVelTicksPerSec + boost);
        motor1.setVelocity(-cmd);
        motor2.setVelocity(-cmd);// ticks/sec
    }
}
