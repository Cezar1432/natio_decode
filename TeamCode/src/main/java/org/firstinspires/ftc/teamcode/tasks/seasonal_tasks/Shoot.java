package org.firstinspires.ftc.teamcode.tasks.seasonal_tasks;

import org.firstinspires.ftc.teamcode.robot.subsystem.Intake;
import org.firstinspires.ftc.teamcode.robot.subsystem.Shooter;
import org.firstinspires.ftc.teamcode.robot.subsystem.Spindexer;
import org.firstinspires.ftc.teamcode.tasks.command_based.core.Command;
import org.firstinspires.ftc.teamcode.tasks.command_based.core.Task;

public class Shoot implements Task {

    Command command;
    public Shoot(){
        command= new Command()
                .addTask(Intake::start)
                .addTask(Spindexer::shootRandom)
                .waitSeconds(2 * Shooter.dtSeconds)
                .addTask(Shooter::onShot)
                .waitSeconds(Shooter.dtSeconds)
                .addTask(Shooter::onShot)
                .addTask(Spindexer::turnBack);
    }
    @Override
    public boolean Run() {
        command.update();
        return command.done();
    }
}
