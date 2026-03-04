package org.firstinspires.ftc.teamcode.util.wrappers;

import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorControllerEx;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties;
import com.qualcomm.robotcore.hardware.configuration.annotations.MotorType;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.Rotation;
import org.firstinspires.ftc.teamcode.util.math.PDSFCoefficients;
import org.firstinspires.ftc.teamcode.util.math.PDSFController;

@DeviceProperties(
        xmlTag = "betterMotorEx",
        name = "BetterMotorEx",
        description = "generic motor",
        builtIn = false
)
@MotorType(
        ticksPerRev = 1550,
        gearing = 55,
        maxRPM = 6000,
        achieveableMaxRPMFraction = 1.0,
        orientation = Rotation.CCW
)
public class BetterMotorEx extends DcMotorImplEx implements DcMotorEx, HardwareDevice {

    double ticksPerRevolution= 0;
    ElapsedTime time;
    long startTime;

    public BetterMotorEx(DcMotorController controller, int portNumber, int ticksPerRevolution) {
        super(controller, portNumber);
        this.ticksPerRevolution= ticksPerRevolution;
        startTime= System.nanoTime();
        time= new ElapsedTime();


    }public BetterMotorEx(DcMotorController controller, int portNumber) {
        super(controller, portNumber);

        time= new ElapsedTime();//aaa

    }
    double cachingTolerance= 0;
    public BetterMotorEx setCachingTolerance(double tolerance){
        this.cachingTolerance= tolerance;
        return this;
    }
    public BetterMotorEx(DcMotorControllerEx controllerEx, int portNumber){
        super(controllerEx, portNumber);
        time= new ElapsedTime();
    }


    public enum RunMode{
        RUN, PID, VELOCITY_PID
    }
    RunMode runMode= RunMode.RUN;
    double velocity= 0;
    private double targetPos= 0;
    @Override
    public void setPower(double power){
        super.setPower(power);
        lastPower= 0;
        runMode= RunMode.RUN;
    }
    private double lastPower= 0;
    PIDFController controller;
    double maxVelocity;
    public void set(double output){
        if(runMode.equals(RunMode.VELOCITY_PID)){
            double power= output + controller.calculate(getVelocity(), output * maxVelocity);
            this.setPower(power);
        }
    }



    public void resetEncoder(){
        setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

}
