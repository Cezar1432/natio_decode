package org.firstinspires.ftc.teamcode.tasks.command_based.core;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;

import org.firstinspires.ftc.teamcode.tasks.command_based.command_types.Wait;

import java.util.LinkedList;
import java.util.function.Supplier;

public class Scheduler {



    Follower f;
    LinkedList<TaskFactory> factory;

    boolean last= true, current= false, justDone= false;
    public LinkedList<Task> list;

    public Scheduler(){
        list = new LinkedList<>();
        factory= new LinkedList<>();

    }
    public Scheduler(Follower f){this.f =f; list = new LinkedList<>();
factory= new LinkedList<>();
    }
    public boolean done(){
        return list.isEmpty();
    }
    @Deprecated
    public int getQueueSize(){
        return list.size();
    }

    public Scheduler addChassis(Follower chassis){
        this.f= chassis;
        return this;
    }




    public Scheduler addTask(Task t){
        factory.addLast(()-> t);
        list.addLast(t);
        return this;
    }
    public Scheduler addTask(InstantTask t){

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
    public Scheduler waitSeconds(double sec){


        return this;
    }
    @Deprecated
    public Scheduler waitSecondsFirst(double sec){
        list.addFirst(new Wait(sec));
        return this;
    }

    public void removeAllTasks(){
        list = new LinkedList<>();
    }
    @Deprecated
    public Scheduler addFirst(Task t){
        list.addFirst(t);
        return this;
    }
    public Scheduler copy(){
        Scheduler copy= new Scheduler();
        for(Task t: list)
            copy.addTask(t);

        return copy;
    }

    public Task getAsTask(){
        Task task;
        class SchedulerAsTask implements Task{

            Scheduler s;
            public SchedulerAsTask(Scheduler s){
                this.s= s.copy();
            }
            @Override
            public boolean Run() {
                s.update();
                return s.done();
            }
        }
        return new SchedulerAsTask(this);
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
