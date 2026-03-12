package org.firstinspires.ftc.teamcode.tasks.seasonal_tasks;

import org.firstinspires.ftc.teamcode.robot.subsystem.Intake;
import org.firstinspires.ftc.teamcode.robot.subsystem.Shooter;
import org.firstinspires.ftc.teamcode.robot.subsystem.Spindexer;
import org.firstinspires.ftc.teamcode.tasks.command_based.core.Command;
import org.firstinspires.ftc.teamcode.tasks.command_based.core.Task;

public class ShootBasic implements Task {

    Command command;
    public ShootBasic(){
        command= new Command()
                .addTask(()-> Shooter.shooting= true)
                .addTask(Intake::start)
                .addTask(Spindexer::shootRandom)
//                .waitSeconds(Shooter.dtSeconds)
//                .addTask(()->Shooter.servo.setPosition(Shooter.servo.getPosition()- Shooter.downPos))
//                .waitSeconds(Shooter.dtSeconds)
//                .addTask(()->Shooter.servo.setPosition(Shooter.servo.getPosition()- Shooter.downPos))
//                .waitSeconds(2 * Shooter.dtSeconds)
//                .addTask(Shooter::onShot)
//                .waitSeconds(Shooter.dtSeconds)
//                .addTask(Shooter::onShot)
                .waitSeconds(2)
                .addTask(Spindexer::turnBack)
                .addTask(()-> Shooter.shooting= false);

    }
    @Override
    public boolean Run() {
        command.update();
        return command.done();
    }
}
