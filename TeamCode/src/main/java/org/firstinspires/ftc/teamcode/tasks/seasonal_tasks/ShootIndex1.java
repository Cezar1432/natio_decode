package org.firstinspires.ftc.teamcode.tasks.seasonal_tasks;

import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.robot.subsystem.Intake;
import org.firstinspires.ftc.teamcode.robot.subsystem.Shooter;
import org.firstinspires.ftc.teamcode.robot.subsystem.Spindexer;
import org.firstinspires.ftc.teamcode.tasks.command_based.core.Command;
import org.firstinspires.ftc.teamcode.tasks.command_based.core.Task;

public class ShootIndex1 implements Task {

    Command command;
    public ShootIndex1(){
        command= new Command()
                .addTask(()-> Shooter.shooting= true)
                .addTask(()->Spindexer.turnTo(Spindexer.Slots.SLOT_4))
                .waitSeconds(0.6)
                .addTask(new ShootSpindexerDeJenaFar(1));

    }
    @Override
    public boolean Run() {
        command.update();
        return command.done();
    }
}
