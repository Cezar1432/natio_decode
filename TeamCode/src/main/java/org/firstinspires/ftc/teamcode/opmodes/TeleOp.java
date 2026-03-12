package org.firstinspires.ftc.teamcode.opmodes;

import static org.firstinspires.ftc.teamcode.opmodes.tuning.MotorExTest.VELOCITY;
import static org.firstinspires.ftc.teamcode.robot.subsystem.Spindexer.balls;
import static org.firstinspires.ftc.teamcode.robot.subsystem.Spindexer.colorSensor;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.pedropathing.PedroConstants;
import org.firstinspires.ftc.teamcode.robot.Alliance;
import org.firstinspires.ftc.teamcode.robot.Chassis;
import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.robot.subsystem.Intake;
import org.firstinspires.ftc.teamcode.robot.subsystem.Shooter;
import org.firstinspires.ftc.teamcode.robot.subsystem.Spindexer;
import org.firstinspires.ftc.teamcode.robot.subsystem.Turret;
import org.firstinspires.ftc.teamcode.tasks.command_based.command_types.ConditionalTask;
import org.firstinspires.ftc.teamcode.tasks.command_based.command_types.Wait;
import org.firstinspires.ftc.teamcode.tasks.command_based.core.Scheduler;
import org.firstinspires.ftc.teamcode.tasks.seasonal_tasks.Shoot;
import org.firstinspires.ftc.teamcode.tasks.seasonal_tasks.ShootColor;
import org.firstinspires.ftc.teamcode.tasks.seasonal_tasks.ShootIndex1;
import org.firstinspires.ftc.teamcode.tasks.seasonal_tasks.ShootIndex2;
import org.firstinspires.ftc.teamcode.tasks.seasonal_tasks.ShootPattern;
import org.firstinspires.ftc.teamcode.tasks.seasonal_tasks.ShootSpindexerDeJenaFar;
import org.firstinspires.ftc.teamcode.tasks.seasonal_tasks.Spit;
import org.firstinspires.ftc.teamcode.util.BetterOpMode;
import org.firstinspires.ftc.teamcode.util.MultipleTelemetry;
import org.firstinspires.ftc.teamcode.util.wrappers.BetterGamepad;
import org.firstinspires.ftc.teamcode.util.wrappers.colorsensor.Colors;

import java.util.function.DoubleSupplier;

@Configurable
public abstract class TeleOp extends BetterOpMode {

    public  double p=.8,d= 0.02;

    public Alliance a;
    public boolean fieldCentric= true;
    public abstract void setAlliance();
    Robot robot;
    RevColorSensorV3 clr;
    Chassis drive;
    MultipleTelemetry telemetry;
    public DoubleSupplier forward, strafe, rotation;



    public abstract void setDriveSuppliers();
    @Override
    public void initialize() {
        setAlliance();
        setDriveSuppliers();
        telemetry= new MultipleTelemetry(super.telemetry, PanelsTelemetry.INSTANCE.getFtcTelemetry());
        robot= new Robot(hardwareMap, this.telemetry, this.a)
                .setOpModeType(Robot.OpModeType.TELEOP)
                .initialize();

        drive= new Chassis(hardwareMap)
                .setSuppliers(forward, strafe, rotation)
                .setLimiters(6,6,20)
                .fieldCentric(true)
                .startTeleOpDrive(true)
                .setHeadingPIDF(new PIDFCoefficients(p,0,d,0));

        gamepadEx1.getButton(BetterGamepad.Buttons.CROSS).whenPressed(Intake::toggle, BetterGamepad.Type.PARALLEL);
        gamepadEx1.getButton(BetterGamepad.Buttons.TOUCHPAD)
                .whenPressed(()->{
                            double x= this.a== Alliance.BLUE ? Turret.FIELD_LENGTH - PedroConstants.dtWidth/100 : PedroConstants.dtWidth/100;
                            double y= PedroConstants.dtLength/100;
                            double heading= Math.toRadians(90);
                            Robot.odo.setPosX(x, DistanceUnit.METER);
                            Robot.odo.setPosY(y, DistanceUnit.METER);
                            Robot.odo.setHeading(heading, AngleUnit.RADIANS);
                            Turret.reseted = true;
                        }
                        ,
                        BetterGamepad.Type.PARALLEL);

        gamepadEx1.getButton(BetterGamepad.Buttons.TRIANGLE)
                .whenPressed(Spit::new, BetterGamepad.Type.PARALLEL);

        gamepadEx1.getButton(BetterGamepad.Buttons.RIGHT_BUMPER)
                        .whenPressed(()-> new ConditionalTask(new ShootSpindexerDeJenaFar(0), new Shoot(), ()-> Turret.dist> 3 ), BetterGamepad.Type.MAIN);
       /* gamepadEx2.getButton(BetterGamepad.Buttons.SQUARE)
                        .whenPressed(()->new ShootPattern("GPP"), BetterGamepad.Type.MAIN);
        gamepadEx2.getButton(BetterGamepad.Buttons.TRIANGLE)
                .whenPressed(()->new ShootPattern("PGP"), BetterGamepad.Type.MAIN);
        gamepadEx2.getButton(BetterGamepad.Buttons.CIRCLE)
                .whenPressed(()->new ShootPattern("PPG"), BetterGamepad.Type.MAIN);

        */
        gamepadEx1.getButton(BetterGamepad.Buttons.LEFT_BUMPER)
                        .whenPressed(Shoot::new, BetterGamepad.Type.MAIN);
        gamepadEx1.getButton(BetterGamepad.Buttons.DPAD_UP)
                        .whenPressed(()->{
                           fieldCentric= !fieldCentric;
                           drive.fieldCentric(fieldCentric);
                        }, BetterGamepad.Type.PARALLEL);

        gamepadEx1.getButton(BetterGamepad.Buttons.CIRCLE)
                        .whenPressed(()-> new ShootColor(Colors.Balls.GREEN), BetterGamepad.Type.MAIN);
//        gamepadEx1.getButton(BetterGamepad.Buttons.DPAD_UP)
//                    .whenPressed(ShootBasic::new, BetterGamepad.Type.MAIN);
//        gamepadEx1.getButton(BetterGamepad.Buttons.RIGHT_BUMPER)
//                    .whenPressed(ShootForta::new, BetterGamepad.Type.MAIN);
//        gamepadEx1.getButton(BetterGamepad.Buttons.CIRCLE)
//                .whenPressed(Spindexer::clearAll, BetterGamepad.Type.PARALLEL);
        gamepadEx1.getButton(BetterGamepad.Buttons.DPAD_LEFT)
                .whenPressed(Shooter::setCoefs, BetterGamepad.Type.PARALLEL);
        gamepadEx1.getButton(BetterGamepad.Buttons.DPAD_DOWN)
                .whenPressed(()->{Spindexer.sorting = !Spindexer.sorting;Spindexer.clear();}, BetterGamepad.Type.PARALLEL);
        gamepadEx1.getButton(BetterGamepad.Buttons.SQUARE)
                        .whenPressed(()->{Spindexer.clear();Spindexer.turnBack();}, BetterGamepad.Type.PARALLEL);
        //gamepadEx1.getButton(BetterGamepad.Buttons.DPAD_RIGHT)
          //              .whenPressed(()->drive.setHeadingPIDF(new PIDFCoefficients(p,0,d,0)), BetterGamepad.Type.PARALLEL);
        telemetry.setMsTransmissionInterval(500);
//        gamepadEx2.getButton(BetterGamepad.Buttons.LEFT_BUMPER)
//                .whenPressed(()->new ShootSpindexerDeJenaFar(1), BetterGamepad.Type.MAIN);
//        gamepadEx2.getButton(BetterGamepad.Buttons.RIGHT_BUMPER)
//                .whenPressed(()->new ShootSpindexerDeJenaFar(2), BetterGamepad.Type.MAIN);

    }

    @Override
    public void initialize_loop() {

    }

    @Override
    public void on_start() {
        Spindexer.turnBack();
    }

    long now, last, dt;
    @Override
    public void active_loop() {
        now = System.nanoTime();
        dt = now - last;
        telemetry.addData("hz", 1e9 / dt);
        last = now;
//       if(!Shooter.shooting)
//          Shooter.servo.setPosition(Shooter.servo.getPosition() + 0.0025 *
//                ((gamepadEx1.getDouble(BetterGamepad.Trigger.RIGHT_TRIGGER) - gamepadEx1.getDouble(BetterGamepad.Trigger.LEFT_TRIGGER))));
////       Shooter.setVelocity(VELOCITY);
////       Shooter.motor1.update();
        if(Spindexer.sorting)
        {
            if(gamepadEx2.getButton(BetterGamepad.Buttons.SQUARE).wasPressed())
            {
                switch (Spindexer.greenSlot){
                    case 1:
                        Scheduler.now(new ShootSpindexerDeJenaFar(0));
                        break;
                    case 2:
                      //  Scheduler.schedule(()->Spindexer.turnTo(Spindexer.Slots.SLOT_4)).schedule(new ShootSpindexerDeJenaFar(1));
                        Scheduler.now(new ShootIndex1());
                        break;
                    case 3:
                        Scheduler.now(new ShootIndex2());
                        break;
                }
            }
            if(gamepadEx2.getButton(BetterGamepad.Buttons.TRIANGLE).wasPressed())
            {
                switch (Spindexer.greenSlot){
                    case 1:
                        Scheduler.now(new ShootIndex1());
                        break;
                    case 2:
                        //  Scheduler.schedule(()->Spindexer.turnTo(Spindexer.Slots.SLOT_4)).schedule(new ShootSpindexerDeJenaFar(1));
                        Scheduler.now(new ShootSpindexerDeJenaFar(0));
                        break;
                    case 3:
                        Scheduler.now(new ShootIndex2());

                        break;
                }
            }
            if(gamepadEx2.getButton(BetterGamepad.Buttons.CIRCLE).wasPressed()){
                switch (Spindexer.greenSlot){
                    case 1:
                        Scheduler.now(new ShootIndex2());
                        break;
                    case 2:
                        //  Scheduler.schedule(()->Spindexer.turnTo(Spindexer.Slots.SLOT_4)).schedule(new ShootSpindexerDeJenaFar(1));
                        Scheduler.now(new ShootSpindexerDeJenaFar(0));
                        break;
                    case 3:
                        Scheduler.now(new ShootIndex1());
                        break;
                }
            }
        }
        if(gamepadEx1.getButton(BetterGamepad.Buttons.DPAD_RIGHT).wasPressed())
            drive.setHeadingPIDF(new PIDFCoefficients(p,0,d,0));
//        if(gamepadEx2.getButton(BetterGamepad.Buttons.RIGHT_BUMPER).wasPressed())
//        {
//            Spindexer.turnTo(Spindexer.Slots.SLOT_4);
//        }
//        if(gamepadEx2.getButton(BetterGamepad.Buttons.DPAD_RIGHT).wasPressed())
//        {
//            Spindexer.turnTo(Spindexer.Slots.SLOT_5);
//        }
        robot.update();
        telemetry.update();
        drive.update();
        Spindexer.update();
        Turret.update();
        Shooter.update();
//        telemetry.addData("Aia Sfanta si Laudata", Shooter.velocity);
        for(int i= 1; i<= 3; i++)
            telemetry.addData("ball" + i, Spindexer.balls[i]);
        telemetry.addData("green to slot", Spindexer.greenSlot);





//        telemetry.addData("velocity2", Shooter.motor2.getVelocity());
//        telemetry.addData("target", Shooter.motor1.targetVelocity);
//        telemetry.addData("sorting", Spindexer.sorting);
//        telemetry.addData("slot", Spindexer.currentSlot);
//        telemetry.addData("status", Spindexer.breakBeam.getBeamState());
//
//
//        telemetry.addData("motor 1 power", Shooter.motor1.getPower());
//        telemetry.addData("motor 2 power", Shooter.motor2.getPower());

//        if(Spindexer.sorting){
//            for(int i=1;i<=3;i++){
//                telemetry.addData("Slot"+i,balls[i]);
//            }
//            telemetry.addData("COLOR", colorSensor.getColorSeenBySensor2());
//            telemetry.addData("Distance", colorSensor.getDistanceInCM());

    }

    @Override
    public void end() {

    }
}