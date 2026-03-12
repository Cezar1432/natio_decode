package org.firstinspires.ftc.teamcode.robot;

import com.arcrobotics.ftclib.controller.PIDFController;
import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.Pose;
import com.pedropathing.math.Vector;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.teamcode.pedropathing.MySwerve;
import org.firstinspires.ftc.teamcode.pedropathing.PedroConstants;
import org.firstinspires.ftc.teamcode.util.math.MathStuff;
import org.firstinspires.ftc.teamcode.util.math.SlewRateLimiter;

import java.util.function.DoubleSupplier;

@Configurable
public class Chassis {


    public MySwerve swerve;
    SlewRateLimiter forward, strafe, rot;
    public Chassis(HardwareMap hardwareMap){
        forward= new SlewRateLimiter(Double.MAX_VALUE);
        strafe= new SlewRateLimiter(Double.MAX_VALUE);
        rot= new SlewRateLimiter(Double.MAX_VALUE);
        swerve= PedroConstants.getSwerve(hardwareMap);
    }
    DoubleSupplier leftStickX, leftStickY, rightStickX;
    public Chassis setSuppliers(DoubleSupplier leftStickX, DoubleSupplier leftStickY, DoubleSupplier rightStickX){
        this.leftStickX= leftStickX;
        this.leftStickY= leftStickY;
        this.rightStickX= rightStickX;
        return this;
    }

    public Chassis setLimiters(double fwd, double strafe, double rot){
        forward= new SlewRateLimiter(fwd);
        this.strafe= new SlewRateLimiter(strafe);
        this.rot= new SlewRateLimiter(rot);
        return this;
    }

    boolean robotCentric;
    public Chassis fieldCentric(boolean robotCentric){
        this.robotCentric= robotCentric;
        return this;
    }

    public double lastAngle= 0, err;
    public void update(){
        Vector inputs= new Vector(new Pose(strafe.calculate(leftStickX.getAsDouble()), forward.calculate(leftStickY.getAsDouble())));
        if(robotCentric){
            inputs.rotateVector(-Robot.robotPose.getHeading());
        }
        double rotation= rot.calculate(rightStickX.getAsDouble());
        if(Math.abs(rotation)< 1e-2 && usingHeadingPIDF) {
            if(Math.abs(lastAngle- Robot.robotPose.getHeading())> Math.toRadians(30)) {
                err = MathStuff.normalizeRadians(MathStuff.normalizeRadians(Robot.robotPose.getHeading()) - MathStuff.normalizeRadians(lastAngle));
                rotation = -headingController.calculate(0, err);
            }
        }
        else{
            lastAngle=Robot.robotPose.getHeading();
        }
        swerve.arcadeDrive(inputs.getXComponent(), inputs.getYComponent(), rotation);
    }
    public static PIDFController headingController;
    boolean usingHeadingPIDF= false;
    public Chassis setHeadingPIDF(PIDFCoefficients coefs){
        usingHeadingPIDF= true;
        headingController= new PIDFController(coefs.p, coefs.i, coefs.d, coefs.f);
        return this;
    }
    public Chassis startTeleOpDrive(){
        swerve.startTeleopDrive();
        return this;
    }
    public Chassis startTeleOpDrive(boolean breakMode){
        swerve.startTeleopDrive(breakMode);
        return this;
    }
}
