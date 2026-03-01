package org.firstinspires.ftc.teamcode.tasks.command_based.core;

import java.util.LinkedList;
import java.util.function.Supplier;

public class Scheduler {
    private static final LinkedList<Task> list= new LinkedList<>();
    public static void schedule(Supplier<? extends Task> factory){
        list.addLast(factory.get());
    }
    public static void clear(){
        list.clear();
    }

    public static void schedule(Task t){
        list.addLast(t);
    }
    public static void now(Task t){
        clear();
        list.addLast(t);
    }
    public static void now(Supplier<? extends Task> supplier){
        clear();
        list.addLast(supplier.get());
    }
    public static void schedule(InstantTask t){
        Task task= ()->{
            t.run();
            return true;
        };
        list.addLast(task);

    }
    public static void update(){
        if(list.isEmpty()) return;
        if(list.getFirst().Run()) list.removeFirst();
    }
}
