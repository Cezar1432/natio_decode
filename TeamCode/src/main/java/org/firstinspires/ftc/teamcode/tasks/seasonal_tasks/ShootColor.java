package org.firstinspires.ftc.teamcode.tasks.seasonal_tasks;

import org.firstinspires.ftc.teamcode.robot.subsystem.Spindexer;
import org.firstinspires.ftc.teamcode.tasks.command_based.core.Command;
import org.firstinspires.ftc.teamcode.tasks.command_based.core.Task;
import org.firstinspires.ftc.teamcode.util.wrappers.colorsensor.Colors;

public class ShootColor implements Task {
    Command command;
    int empty_slots= 0;
    boolean not_found= true;
    public static int slot, current_slot;
    public static int angleToTurn;

    public ShootColor(Colors.Balls color){
        command= new Command()
                .addTask(()->{
                    for(int i= 1; i<= 3; i++){
                        Colors.Balls ball_color= Spindexer.balls[i];
                        if(ball_color.equals(color) && not_found){
                            not_found= false;
                            slot= i;

                        }
                        else
                            if(ball_color.equals(Colors.Balls.NONE))
                                empty_slots++;

                    }
                    if(Spindexer.currentSlot.equals(Spindexer.Slots.SLOT_1))
                        current_slot= 0;
                    if(Spindexer.currentSlot.equals(Spindexer.Slots.SLOT_2))
                        current_slot= 1;
                    if(Spindexer.currentSlot.equals(Spindexer.Slots.SLOT_3)){
//                        if(Spindexer.colorSensor.getDistanceInCM()< 5)
//                            current_slot= 3;
//                        else
//                            current_slot= 2;
                        current_slot= 2;
                    }


                    angleToTurn= ((slot-1) * 120 + (2-current_slot) * 120) % 360;
                    Spindexer.turn(-angleToTurn);//angletoturn

                })
                .waitSeconds(.8)
                .addTask(()-> Spindexer.turn(120))//-120
                .addTask(()->{
                 //  Spindexer.clear(Spindexer.convertToEnum(slot));/////////
                    int slotsTurned= angleToTurn / 120;
                    int nowSlot= (current_slot+ 1+ slotsTurned) % 3;
                    if(nowSlot== 0)
                        nowSlot= 3;

                    if(nowSlot== 1)
                        Spindexer.currentSlot= Spindexer.Slots.SLOT_1;
                    if(nowSlot== 2)
                        Spindexer.currentSlot= Spindexer.Slots.SLOT_2;
                    if(nowSlot== 3)
                        Spindexer.currentSlot= Spindexer.Slots.SLOT_3;
                })
                .waitSeconds(.6);
                //.addTask(()-> Spindexer.turn(120));
    }

    @Override
    public boolean Run() {
        command.update();
        return command.done();
    }
}
