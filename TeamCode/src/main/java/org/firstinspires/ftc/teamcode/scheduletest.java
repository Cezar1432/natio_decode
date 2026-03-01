package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorControllerEx;

import org.firstinspires.ftc.teamcode.robot.subsystem.Intake;
import org.firstinspires.ftc.teamcode.tasks.command_based.core.Scheduler;
import org.firstinspires.ftc.teamcode.tasks.command_based.core.Task;
import org.firstinspires.ftc.teamcode.tasks.seasonal_tasks.TestTask;
import org.firstinspires.ftc.teamcode.tasks.seasonal_tasks.TestTask2;
import org.firstinspires.ftc.teamcode.util.BetterOpMode;
import org.firstinspires.ftc.teamcode.util.wrappers.BetterGamepad;
import org.firstinspires.ftc.teamcode.util.wrappers.BetterMotor;
import org.firstinspires.ftc.teamcode.util.wrappers.BetterMotorEx;
@TeleOp
public class scheduletest extends BetterOpMode {
    BetterMotorEx motor;
    DcMotorController controller;
    @Override
    public void initialize() {
            controller= hardwareMap.get(DcMotorControllerEx.class, "Expansion Hub 2");
            motor= new BetterMotorEx(controller, 0);
        Intake.motor= motor;
        gamepadEx1.getButton(BetterGamepad.Buttons.CROSS).whenPressed(TestTask::new);

    }

    @Override
    public void initialize_loop() {

    }

    @Override
    public void on_start() {

    }

    @Override
    public void active_loop() {
        if(gamepadEx1.getButton(BetterGamepad.Buttons.CIRCLE).wasPressed())
            Scheduler.add(TestTask2::new);
        if(gamepadEx1.getButton(BetterGamepad.Buttons.TRIANGLE).wasPressed())
            Scheduler.add(Intake::start);
    }

    @Override
    public void end() {

    }
}
