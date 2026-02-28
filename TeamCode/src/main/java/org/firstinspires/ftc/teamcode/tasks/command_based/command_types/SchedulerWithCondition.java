package org.firstinspires.ftc.teamcode.tasks.command_based.command_types;

import org.firstinspires.ftc.teamcode.tasks.command_based.core.Scheduler;

import java.util.function.BooleanSupplier;

public class SchedulerWithCondition {

    public Scheduler scheduler;
    public BooleanSupplier supplier;
    public SchedulerWithCondition(Scheduler scheduler, BooleanSupplier supplier){
        this.scheduler= scheduler;
        this.supplier= supplier;
    }
}
