package org.firstinspires.ftc.teamcode.tasks.seasonal_tasks;

import org.firstinspires.ftc.teamcode.robot.subsystem.Shooter;
import org.firstinspires.ftc.teamcode.robot.subsystem.Spindexer;
import org.firstinspires.ftc.teamcode.tasks.command_based.core.Command;
import org.firstinspires.ftc.teamcode.tasks.command_based.core.Task;
import org.firstinspires.ftc.teamcode.util.wrappers.colorsensor.Colors;

public class ShootPattern implements Task {
    Command command;
    int empty_slots= 0;
    boolean not_found= true;
    public static int slot, current_slot;
    public static int angleToTurn;
    public static int greenSlot=0,howManyIndexes;

    public ShootPattern(String s){
        for (int i = 1; i <= 3; i++)
            if(Spindexer.balls[i]== Colors.Balls.GREEN)
                greenSlot=i;
        double aux = ShootSpindexerDeJenaFar.wait;
        if(greenSlot!=0) {
            ShootSpindexerDeJenaFar.wait = 0.6;
            if (s.equals("GPP")) {
                command = new Command()
                        .addTask(()-> Shooter.shooting = true)
                        .addTask(() -> {
                            switch (greenSlot) {
                                case 1: {
                                    howManyIndexes = 0;
                                    break;
                                }
                                case 2: {
                                    howManyIndexes = 1;
                                    Spindexer.turnTo(Spindexer.Slots.SLOT_4);
                                    break;
                                }
                                case 3: {
                                    howManyIndexes = 2;
                                    Spindexer.turnTo(Spindexer.Slots.SLOT_5);
                                    break;
                                }
                            }
                        })
                        .waitSeconds(.4)
                        .addTask(new ShootSpindexerDeJenaFar(howManyIndexes))
                        .addTask(()->{
                            greenSlot = 0 ;
                            howManyIndexes =0;
                        })
                        .addTask(()-> Shooter.shooting = false);
            } else if (s.equals("PGP")) {
                command = new Command()
                        .addTask(()-> Shooter.shooting = true)
                        .addTask(() -> {
                            switch (greenSlot) {
                                case 1: {
                                    howManyIndexes = 1;
                                    Spindexer.turnTo(Spindexer.Slots.SLOT_4);
                                    break;
                                }
                                case 2: {
                                    howManyIndexes = 2;
                                    Spindexer.turnTo(Spindexer.Slots.SLOT_5);
                                    break;
                                }
                                case 3: {
                                    howManyIndexes = 0;
                                    break;
                                }
                            }
                        })
                        .waitSeconds(.4)
                        .addTask(new ShootSpindexerDeJenaFar(howManyIndexes))
                        .addTask(()->{greenSlot = 0 ;
                         howManyIndexes =0;
                        })
                        .addTask(()-> Shooter.shooting = false);
            } else if (s.equals("PPG")) {
                command = new Command()
                        .addTask(()-> Shooter.shooting = true)
                        .addTask(() -> {
                            switch (greenSlot) {
                                case 1: {
                                    howManyIndexes = 2;
                                    Spindexer.turnTo(Spindexer.Slots.SLOT_5);
                                    break;
                                }
                                case 2: {
                                    howManyIndexes = 0;
                                    break;
                                }
                                case 3: {
                                    howManyIndexes = 1;
                                    Spindexer.turnTo(Spindexer.Slots.SLOT_4);
                                    break;
                                }
                            }
                        })
                        .waitSeconds(.4)
                        .addTask(new ShootSpindexerDeJenaFar(howManyIndexes))
                        .addTask(()->{
                            greenSlot = 0 ;
                            howManyIndexes =0;

                        })
                         .addTask(()-> Shooter.shooting = false);
            }
        }
        else
            command = new Command();

        //.addTask(()-> Spindexer.turn(120));
    }

    @Override
    public boolean Run() {
        command.update();
        return command.done();
    }
}
