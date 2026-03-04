package org.firstinspires.ftc.teamcode.tasks.seasonal_tasks;

import com.bylazar.configurables.annotations.Configurable;

import org.firstinspires.ftc.teamcode.robot.subsystem.Intake;
import org.firstinspires.ftc.teamcode.robot.subsystem.Shooter;
import org.firstinspires.ftc.teamcode.robot.subsystem.Spindexer;
import org.firstinspires.ftc.teamcode.tasks.command_based.core.Command;
import org.firstinspires.ftc.teamcode.tasks.command_based.core.Task;

@Configurable
public class ShootForta implements Task {
    private Command s;

    public static double  velocityThreshold = 300,coef = 1.7, increment= -0.015, waitTime= 0.28, waitTime2= 0.13;


    public double pos;

    public ShootForta(){
        s= new Command()
                .addTask(()-> {
                    Intake.start();
                    pos = Shooter.servo.getPosition();
                })
                .addTask(()->Math.abs(Math.abs(Shooter.motor1.getVelocity())- Math.abs(Shooter.motor1.getVelocity()))< velocityThreshold)
                .addTask(Spindexer::shootRandom)
                .waitSeconds(waitTime)
                .addTask(()->Shooter.servo.setPosition(pos+increment))
                .waitSeconds(waitTime2)
                .addTask(()->Shooter.servo.setPosition(pos+coef* increment))
                .waitSeconds(1)
                .addTask(Spindexer::turnBack)
                .addTask(()->{
                    Shooter.servo.setPosition(pos);
                });
    }

    @Override
    public boolean Run() {
        s.update();
        return s.done();
    }
}
