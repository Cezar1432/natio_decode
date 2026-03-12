package org.firstinspires.ftc.teamcode.opmodes.tuning;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.pedropathing.PedroConstants;
import org.firstinspires.ftc.teamcode.tasks.command_based.core.Command;
import org.firstinspires.ftc.teamcode.util.BetterOpMode;

@Autonomous
public class AutoTest extends BetterOpMode {
    Follower f;
    Command command;
    @Override
    public void initialize() {
        f= PedroConstants.createFollower(hardwareMap);
        command= new Command()
                .setChassis(f)
                .lineToConstantAsync(new Pose(72, 72));
    }

    @Override
    public void initialize_loop() {

    }

    @Override
    public void on_start() {

    }

    @Override
    public void active_loop() {
        command.update();
        f.update();
    }

    @Override
    public void end() {

    }
}
