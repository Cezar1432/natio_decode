package org.firstinspires.ftc.teamcode.robot.subsystem;

import com.arcrobotics.ftclib.controller.wpilibcontroller.SimpleMotorFeedforward;
import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.exception.TargetPositionNotSetException;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.util.math.KalmanFilter;
import org.firstinspires.ftc.teamcode.util.math.PIDController;
import org.firstinspires.ftc.teamcode.util.wrappers.BetterMotorEx;
import org.firstinspires.ftc.teamcode.util.wrappers.BetterServo;

@Configurable
public class Shooter {

    public static BetterMotorEx motor1, motor2;
    //public static MotorEx motor1,motor2;
    public static BetterServo servo,SE;
    public static double downPos= 0.013;
    public static boolean shooting= false;

    public static final double a1 =-0.0460874, b1 = 0.311703, c1 = -0.311453;
    public static final double a2 =-19.69103, b2 =263.43579, c2 = 449.07477;
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
    public static double dtSeconds = 0.23; // optional
    public static double p= -0.004,i,d,f = 1;

    public static double velocity = 0.0;
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
       motor1.setPFCoefficients(p,f);
       motor2.setPFCoefficients(p,f);
    }
    public static double estimatedVelocityDrop= 120;
    public static void onShot(){
        velKalman.applyImpulse(-estimatedVelocityDrop);
    }

    public static double power= 1;

    public static double voltageThreshold = 11.1;
        public static void setVelocity(double vel){
                double voltage = Robot.voltageSensor.getVoltage();
                double coeff = (voltage < voltageThreshold) ? voltageThreshold / voltage : 1;
                vel *= coeff;
                motor1.setVelocity(vel);
                motor2.setPower(motor1.getPower());
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
        hoodPos = Range.clip(a1 * Math.pow(distt, 2) + b1 * distt + c1, 0.1, 0.38);
        velocity = Range.clip(a2*Math.pow(distt,2)+b2*distt+c2, 0, 2100);
        setVelocity(velocity);
        servo.setPosition(hoodPos);

        motor1.update();
    }
}