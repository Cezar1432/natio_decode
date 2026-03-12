package org.firstinspires.ftc.teamcode.tasks.seasonal_tasks;

import com.bylazar.configurables.annotations.Configurable;

import org.firstinspires.ftc.teamcode.robot.subsystem.Intake;
import org.firstinspires.ftc.teamcode.robot.subsystem.Shooter;
import org.firstinspires.ftc.teamcode.robot.subsystem.Spindexer;
import org.firstinspires.ftc.teamcode.tasks.command_based.core.Command;
import org.firstinspires.ftc.teamcode.tasks.command_based.core.Task;

@Configurable
public class ShootSpindexerDeJenaFar implements Task {

    Command command;
    public static int integer;
    //public static double wait = 0.6,velThreshold = 40;

    public static double wait= .6, velThreshold= 40;
//    public ShootSpindexerDeJenaFar(){
//        command= new Command()
//                .addTask(()-> Shooter.shooting= true)
//                .addTask(Intake::start)
//                .addTask(()->Math.abs(-Shooter.motor1.getVelocity()-Shooter.motor1.targetVelocity)<=velThreshold)
//                .addTask(()-> Spindexer.turnTo(Spindexer.Slots.SLOT_2))
//                .waitSeconds(wait)
//                .addTask(()->Math.abs(-Shooter.motor1.getVelocity()- Shooter.motor1.targetVelocity)<=velThreshold)
//                .addTask(()->Spindexer.turnTo(Spindexer.Slots.SLOT_1))
//                .waitSeconds(wait)
//                .addTask(()->Math.abs(-Shooter.motor1.getVelocity()- Shooter.motor1.targetVelocity)<=velThreshold)
//                .addTask(()->Spindexer.setPosition(1))
//                .waitSeconds(1)
////                .waitSeconds(2 * Shooter.dtSeconds)
////                .addTask(Shooter::onShot)
////                .waitSeconds(Shooter.dtSeconds)
////                .addTask(Shooter::onShot)
//                .addTask(Spindexer::turnBack)
//                .addTask(()-> Spindexer.clear())
//                .addTask(()-> Shooter.shooting= false);
//
//    }
    public ShootSpindexerDeJenaFar(int howManyIndexes){
        switch (howManyIndexes) {
            case 0: {
                integer = 0;
                command = new Command()
                        .addTask(() -> Shooter.shooting = true)
                        .addTask(Intake::start)
                        .addTask(() -> Math.abs(-Shooter.motor1.getVelocity() - Shooter.motor1.targetVelocity) <= velThreshold)
                        .addTask(() -> Spindexer.turnToOffset(Spindexer.Slots.SLOT_2))
                        .waitSeconds(wait)
                        .addTask(() -> Math.abs(-Shooter.motor1.getVelocity() - Shooter.motor1.targetVelocity) <= velThreshold)
                        .addTask(() -> Spindexer.turnToOffset(Spindexer.Slots.SLOT_1))
                        .waitSeconds(wait)
                        .addTask(() -> Math.abs(-Shooter.motor1.getVelocity() - Shooter.motor1.targetVelocity) <= velThreshold)
                        .addTask(() -> Spindexer.setPosition(1))
                        .waitSeconds(1)
//                .waitSeconds(2 * Shooter.dtSeconds)
//                .addTask(Shooter::onShot)
//                .waitSeconds(Shooter.dtSeconds)
//                .addTask(Shooter::onShot)
                        .addTask(Spindexer::turnBack)
                        .addTask(() -> Spindexer.clear())
                        .addTask(() -> Shooter.shooting = false);
                break;

            }
            case 1:{
                integer = 1;
                command = new Command()
                        .addTask(() -> Shooter.shooting = true)
                        .addTask(Intake::start)
                        .addTask(() -> Math.abs(-Shooter.motor1.getVelocity() - Shooter.motor1.targetVelocity) <= velThreshold)
                        .addTask(() -> Spindexer.turnToOffset(Spindexer.Slots.SLOT_3))
                        .waitSeconds(wait)
                        .addTask(() -> Math.abs(-Shooter.motor1.getVelocity() - Shooter.motor1.targetVelocity) <= velThreshold)
                        .addTask(() -> Spindexer.turnToOffset(Spindexer.Slots.SLOT_2))
                        .waitSeconds(wait)
                        .addTask(() -> Math.abs(-Shooter.motor1.getVelocity() - Shooter.motor1.targetVelocity) <= velThreshold)
                        .addTask(() -> Spindexer.setPosition(1))
                        .waitSeconds(1)
//                .waitSeconds(2 * Shooter.dtSeconds)
//                .addTask(Shooter::onShot)
//                .waitSeconds(Shooter.dtSeconds)
//                .addTask(Shooter::onShot)
                        .addTask(Spindexer::turnBack)
                        .addTask(() -> Spindexer.clear())
                        .addTask(() -> Shooter.shooting = false);
                break;
            }
            case 2:
            {
                integer = 2;
                command = new Command()
                        .addTask(() -> Shooter.shooting = true)
                        .addTask(Intake::start)
                        .addTask(() -> Math.abs(-Shooter.motor1.getVelocity() - Shooter.motor1.targetVelocity) <= velThreshold)
                        .addTask(() -> Spindexer.turnToOffset(Spindexer.Slots.SLOT_4))
                        .waitSeconds(wait)
                        .addTask(() -> Math.abs(-Shooter.motor1.getVelocity() - Shooter.motor1.targetVelocity) <= velThreshold)
                        .addTask(() -> Spindexer.turnToOffset(Spindexer.Slots.SLOT_3))
                        .waitSeconds(wait)
                        .addTask(() -> Math.abs(-Shooter.motor1.getVelocity() - Shooter.motor1.targetVelocity) <= velThreshold)
                        .addTask(() -> Spindexer.setPosition(1))
                        .waitSeconds(1)
//                .waitSeconds(2 * Shooter.dtSeconds)
//                .addTask(Shooter::onShot)
//                .waitSeconds(Shooter.dtSeconds)
//                .addTask(Shooter::onShot)
                        .addTask(Spindexer::turnBack)
                        .addTask(() -> Spindexer.clear())
                        .addTask(() -> Shooter.shooting = false);
                break;
            }
        }
    }
    @Override
    public boolean Run() {
        command.update();
        return command.done();
    }
}
