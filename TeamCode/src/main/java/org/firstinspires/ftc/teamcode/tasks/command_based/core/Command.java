package org.firstinspires.ftc.teamcode.tasks.command_based.core;

import com.pedropathing.follower.Follower;

import org.firstinspires.ftc.teamcode.tasks.command_based.command_types.Wait;

import java.util.LinkedList;

public class Command {



    Follower f;
    LinkedList<TaskFactory> factory;

    boolean last= true, current= false, justDone= false;
    public LinkedList<Task> list;

    public Command(){
        list = new LinkedList<>();
        factory= new LinkedList<>();

    }
    public Command(Follower f){this.f =f; list = new LinkedList<>();
factory= new LinkedList<>();
    }
    public boolean done(){
        return list.isEmpty();
    }
    @Deprecated
    public int getQueueSize(){
        return list.size();
    }

    public Command addChassis(Follower chassis){
        this.f= chassis;
        return this;
    }




    public Command addTask(Task t){
        factory.addLast(()-> t);
        list.addLast(t);
        return this;
    }
    public Command addTask(InstantTask t){

        list.addLast(()-> {
            t.run();
            return true;
        });
        factory.addLast(()-> ()->{
            t.run();
            return true;
        });
        return this;
    }

    @Deprecated
    public boolean justDone(){
        return justDone;
    }
    public Command waitSeconds(double sec){


        list.addLast(new Wait(sec));

        return this;
    }
    @Deprecated
    public Command waitSecondsFirst(double sec){
        list.addFirst(new Wait(sec));
        return this;
    }

    public void removeAllTasks(){
        list = new LinkedList<>();
    }
    @Deprecated
    public Command addFirst(Task t){
        list.addFirst(t);
        return this;
    }



    public void update(){

        if(done()) {
            current= true;
            justDone= !last;
            last= true;
            return;
        }
        current= false;
        last= false;


        Task t= list.getFirst();
        boolean result= t.Run();
        if(result)
            list.removeFirst();

    }


}
