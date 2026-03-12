package org.firstinspires.ftc.teamcode.opmodes.tuning;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robot.Alliance;
import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.robot.subsystem.Spindexer;
import org.firstinspires.ftc.teamcode.tasks.command_based.core.Command;
import org.firstinspires.ftc.teamcode.util.BetterOpMode;
import org.firstinspires.ftc.teamcode.util.wrappers.BetterGamepad;

@TeleOp
public class TuningSpiner extends BetterOpMode {
    Robot r;
    @Override
    public void initialize() {
        r= new Robot(hardwareMap, telemetry, Alliance.RED)
                .setOpModeType(Robot.OpModeType.TELEOP)
                        .initialize();
        gamepadEx1.getButton(BetterGamepad.Buttons.CROSS).whenPressed(()-> Spindexer.setPosition(.5), BetterGamepad.Type.MAIN);

    }

    @Override
    public void initialize_loop() {
    }

    @Override
    public void on_start() {

    }

    @Override
    public void active_loop() {
        telemetry.addData("pos 1", Spindexer.s1.getPosition());
        telemetry.addData("pos 2", Spindexer.s2.getPosition());
        telemetry.update();
        Spindexer.setPosition(Spindexer.s1.getPosition() + 0.0025 * gamepadEx1.getDouble(BetterGamepad.Trigger.LEFT_X));
    }

    @Override
    public void end() {

    }
}
