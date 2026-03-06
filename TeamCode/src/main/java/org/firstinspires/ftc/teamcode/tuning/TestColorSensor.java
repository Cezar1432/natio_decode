package org.firstinspires.ftc.teamcode.tuning;

import static org.firstinspires.ftc.teamcode.robot.subsystem.Spindexer.shootPurple;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robot.Alliance;
import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.robot.subsystem.Intake;
import org.firstinspires.ftc.teamcode.robot.subsystem.Spindexer;
import org.firstinspires.ftc.teamcode.util.BetterOpMode;
import org.firstinspires.ftc.teamcode.util.wrappers.BetterGamepad;
import org.firstinspires.ftc.teamcode.util.wrappers.BreakBeam;
import org.firstinspires.ftc.teamcode.util.wrappers.colorsensor.BetterColorSensor;
import org.firstinspires.ftc.teamcode.util.wrappers.colorsensor.Colors;

@TeleOp
public class TestColorSensor extends BetterOpMode {
    Robot r;
    ElapsedTime timer;
    int red, green, blue;
    Colors.Color mov, verde;

    BreakBeam.Status beamState;
    Colors.Color currentColor;
    @Override
    public void initialize() {
        r = new Robot(hardwareMap, telemetry, Alliance.RED)
                .setOpModeType(Robot.OpModeType.TELEOP)
                .initialize();
        gamepadEx1.getButton(BetterGamepad.Buttons.CROSS).whenPressed(Intake::toggle, BetterGamepad.Type.PARALLEL);

        timer = new ElapsedTime();
        mov = Colors.Balls.PURPLE.color;
        verde = Colors.Balls.GREEN.color;
    }

    @Override
    public void initialize_loop() {

    }

    @Override
    public void on_start() {

    }

    @Override
    public void active_loop() {
        beamState = Spindexer.breakBeam.getBeamState();
        if( beamState.equals(BreakBeam.Status.BROKEN) ) {
            red = Spindexer.colorSensor.red();
            blue = Spindexer.colorSensor.blue();
            green = Spindexer.colorSensor.green();
            currentColor = new Colors.Color(red,green,blue);
            telemetry.addData("color dist to purple", Colors.getColorDistance(currentColor, mov));
            telemetry.addData("color dist to green", Colors.getColorDistance(currentColor, verde));
            //distanceToPurple = Colors.getColorDistance()
            telemetry.addData("rosu", red);
            telemetry.addData("current color", Spindexer.colorSensor.getColorSeenBySensor());
            telemetry.addData( "albastru", blue);
            telemetry.addData("verde", green);
        } else {
            telemetry.addData("n ai bagat bila!", 1);
        }
        telemetry.update();
        r.update();
    }

    @Override
    public void end() {

    }
}
