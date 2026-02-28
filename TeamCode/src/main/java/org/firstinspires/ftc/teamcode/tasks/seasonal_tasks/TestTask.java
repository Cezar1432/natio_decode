package org.firstinspires.ftc.teamcode.tasks.seasonal_tasks;

import org.firstinspires.ftc.teamcode.robot.subsystem.Intake;
import org.firstinspires.ftc.teamcode.tasks.command_based.core.Scheduler;
import org.firstinspires.ftc.teamcode.tasks.command_based.core.Task;

public class TestTask implements Task {
    Scheduler s;
    public TestTask(){
        s= new Scheduler()
                .addTask(Intake::start)
                .waitSeconds(1)
                .addTask(Intake::stop);
    }
    @Override
    public boolean Run() {
        s.update();
        return s.done();
    }
}
