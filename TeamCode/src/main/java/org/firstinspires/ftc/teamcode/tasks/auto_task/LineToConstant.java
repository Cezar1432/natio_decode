package org.firstinspires.ftc.teamcode.tasks.auto_task;

import com.bylazar.field.Line;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathBuilder;

import org.firstinspires.ftc.teamcode.tasks.command_based.core.Task;

public class LineToConstant implements Task {

    Follower f;
    Pose p;
    boolean started;
    public LineToConstant(Follower f, Pose p){
        this.f= f;
        this.p= p;
        started= false;
    }
    @Override
    public boolean Run() {
        if(!started){
            Path path= new Path(new BezierLine(f.getPose(), p));
            f.followPath(path);
            started= true;
        }
        return !f.isBusy();
    }
}
