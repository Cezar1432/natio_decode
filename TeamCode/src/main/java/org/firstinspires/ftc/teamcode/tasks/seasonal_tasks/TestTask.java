package org.firstinspires.ftc.teamcode.tasks.seasonal_tasks;

import org.firstinspires.ftc.teamcode.robot.subsystem.Intake;
import org.firstinspires.ftc.teamcode.tasks.command_based.core.Command;
import org.firstinspires.ftc.teamcode.tasks.command_based.core.Task;

public class TestTask implements Task {
    Command s;
    public TestTask(){
        s= new Command()
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
