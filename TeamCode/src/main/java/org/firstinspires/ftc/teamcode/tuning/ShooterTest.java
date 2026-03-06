package org.firstinspires.ftc.teamcode.tuning;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robot.Alliance;
import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.robot.subsystem.Shooter;
import org.firstinspires.ftc.teamcode.robot.subsystem.Spindexer;
import org.firstinspires.ftc.teamcode.util.BetterOpMode;
import org.firstinspires.ftc.teamcode.util.wrappers.BetterGamepad;

@TeleOp
@Configurable
public class ShooterTest extends BetterOpMode {
    Robot r;
    public static double m1, m2;
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
        if( gamepad1.crossWasPressed() ) {
            Shooter.motor1.setVelocity(m1);
            Shooter.motor2.setVelocity(m2);
        }
    }

    @Override
    public void end() {

    }
}
