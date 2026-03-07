package org.firstinspires.ftc.teamcode.robot.subsystem;

import com.arcrobotics.ftclib.controller.wpilibcontroller.SimpleMotorFeedforward;
import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.exception.TargetPositionNotSetException;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.tuning.MotorExTest;
import org.firstinspires.ftc.teamcode.util.math.KalmanFilter;
import org.firstinspires.ftc.teamcode.util.math.PIDController;
import org.firstinspires.ftc.teamcode.util.wrappers.BetterMotorEx;
import org.firstinspires.ftc.teamcode.util.wrappers.BetterServo;
import org.firstinspires.ftc.teamcode.util.wrappers.MotorEx;

@Configurable
public class Shooter {

    public static MotorEx motor1, motor2;
    //public static MotorEx motor1,motor2;
    public static BetterServo servo;

    public static final double a1 = -0.0641147, b1 = 0.390703, c1 = 0.18375;
    public static final double a2 = -100.04613, b2 = 804.30749, c2 = 357.66532;
    public static double x0 = 0, p0 = 0.88, q = 1500, r = 4900;
    public static KalmanFilter velKalman = new KalmanFilter(
            0.0,   // x0
            p0, // p0
            q,// q (process noise)  -> tune
            r // r (measurement noise)-> tune
    );
    public static double shotExpectedDropTicksPerSec = 100.0; // tune
    public static double compensationGain = 1.5;              // error -> boost
    public static double compensationMaxTicksPerSec = 600.0;  // cap boost
    public static double recoveryDeadbandTicksPerSec = 40;
    public static double dtSeconds = 0.2; // optional
    public static double p= 4,i,d,f = 20;

    public static double vel;


    public static double targetVelTicksPerSec = 0.0;
    static PIDFCoefficients shootercoeffs = new PIDFCoefficients(p, i, d, f);

    public SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(fS,fV,fA);
    public PIDController veloController = new PIDController(vP,vI,vD);
    //  public static double vP=5,vI=0,vD=0.1,fS=200,fV=1.5,fA=0;
    public static double vP=5.3,vI=0,vD=0.1,fS=200,fV=1.68,fA=0;
    public static void setKalmanCoefs(){
        velKalman = new KalmanFilter(
                x0,   // x0
                p0, // p0
                q,// q (process noise)  -> tune
                r
        );
    }

    public static void setCoefs(){
        motor1.setVeloCoefficients(vP,vI,vD);
        motor1.setFeedforwardCoefficients(fS,fV,fA);
        motor2.setVeloCoefficients(vP,vI,vD);
        motor2.setFeedforwardCoefficients(fS,fV,fA);
    }
    public static double estimatedVelocityDrop= 120;
    public static void onShot(){
        velKalman.applyImpulse(-estimatedVelocityDrop);
    }

    public static double power= 1;

    public static double voltageThreshold = 10.1;
    public static void setVelocity(double vel){
            setCoefs();
            double voltage = Robot.voltageSensor.getVoltage();
            double coeff = (voltage < voltageThreshold) ? voltageThreshold / voltage : 1;
            vel *= coeff;
            motor1.setVelocity(vel);
            motor2.setVelocity(vel);
    }
    public static boolean wereCoeffsSet = false;
    public static void setVelocity2(double vel){
        if(!wereCoeffsSet)
        {
            wereCoeffsSet = true;
            setCoefs();
        }
        try {
            motor1.setVelocity(vel);
            motor2.setVelocity(vel);
        }
        catch (TargetPositionNotSetException ignored){

        }
    }
    public static double hoodPos;
    public static double est;
    public static double boost;
    public static void update(){
        double distt= Turret.dist;
        //hoodPos = Range.clip(a1 * Math.pow(distt, 2) + b1 * distt + c1, 0.58, 0.789);
        //targetVelTicksPerSec = -Math.pow(distt,2)*a2+b2*distt+c2;
        targetVelTicksPerSec = MotorExTest.VELOCITY;
        // DEFAULT getVelocity(): encoder ticks/sec
        double measTicksPerSec = motor1.getVelocity();

        est = (dtSeconds > 0.0)
                ? velKalman.update(measTicksPerSec, dtSeconds)
                : velKalman.update(measTicksPerSec);

        double err = targetVelTicksPerSec - est;

        boost = 0.0;
        if (Math.abs(err) > recoveryDeadbandTicksPerSec) {
            boost = compensationGain * err;
            boost = Range.clip(boost, -compensationMaxTicksPerSec, compensationMaxTicksPerSec);
        }

        double cmd = Math.max(0.0, targetVelTicksPerSec + boost);
        setVelocity(cmd);// ticks/sec
      //  servo.setPosition(hoodPos);
    }
}