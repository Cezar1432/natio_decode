package org.firstinspires.ftc.teamcode.robot.subsystem;

import org.firstinspires.ftc.teamcode.util.wrappers.BetterMotorEx;

public class Intake {
    public static BetterMotorEx motor;
    static boolean intaking = false;
    public static void start(){
        motor.setPower(-1);
        intaking= true;
    }
    public static void stop(){
        motor.setPower(0);
        intaking= false;
    }
    public static void reverse(){
        motor.setPower(0.5);

    }
    public static void setPower(double power){
        motor.setPower(power);
    }
    public static void toggle(){
        if(!intaking){
            start();
        }
        else{
            stop();
        }
    }
}
