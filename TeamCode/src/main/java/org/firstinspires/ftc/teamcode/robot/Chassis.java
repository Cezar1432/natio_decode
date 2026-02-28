package org.firstinspires.ftc.teamcode.robot;

import com.pedropathing.ftc.drivetrains.Swerve;
import com.pedropathing.geometry.Pose;
import com.pedropathing.math.Vector;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.pedropathing.MySwerve;
import org.firstinspires.ftc.teamcode.pedropathing.PedroConstants;
import org.firstinspires.ftc.teamcode.util.math.SlewRateLimiter;

import java.util.function.DoubleSupplier;

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
    public Chassis robotCentric(boolean robotCentric){
        this.robotCentric= robotCentric;
        return this;
    }

    public void update(){
        Vector inputs= new Vector(new Pose(strafe.calculate(leftStickX.getAsDouble()), forward.calculate(leftStickY.getAsDouble())));
        if(robotCentric){
            inputs.rotateVector(Robot.robotPose.getHeading());
        }
        swerve.arcadeDrive(inputs.getXComponent(), inputs.getYComponent(), rot.calculate(rightStickX.getAsDouble()));
    }
}
