package org.firstinspires.ftc.teamcode.robot.subsystem;

import org.firstinspires.ftc.teamcode.tasks.command_based.core.Scheduler;
import org.firstinspires.ftc.teamcode.util.wrappers.BetterServo;
import org.firstinspires.ftc.teamcode.util.wrappers.BreakBeam;
import org.firstinspires.ftc.teamcode.util.wrappers.colorsensor.BetterColorSensor;

public class Spindexer {
    public static BetterServo s1, s2;

    public static void setPosition(double pos){
        s1.setPosition(pos);
        s2.setPosition(pos);
    }
    public static void shootRandom(){
//         s1.setMaxDegrees(1100);
//         s2.setMaxDegrees(1100);
        setPosition(1);
        //  s2.setPosition(s31.getPosition());
    }

    public enum Slots{
        SLOT_1(0.9267,0), SLOT_2(.8039,0), SLOT_3(.6683  ,0),EJECT1(0.38,0),EJECT2(0.5117,0),EJECT3(0.6233,0);   //poz bune initiale
        //  SLOT_1(0.4494,0), SLOT_2(.311,0), SLOT_3(.215,0),EJECT1(0.38,0),EJECT2(0.5117,0),EJECT3(0.6233,0);
        // SLOT_1(0.4656,0), SLOT_2(.345,0), SLOT_3(.2217,0),EJECT1(0.38,0),EJECT2(0.5117,0),EJECT3(0.6233,0);

        final double frontPose, shootPose;
        // final double shootPose;
        Slots(double frontPose, double shootPose){
            this.frontPose= frontPose;
            this.shootPose= shootPose;
        }

    }



    public static void turnTo(Slots slot){
        setPosition(slot.frontPose);
        currentSlot= slot;
    }
    public static void shootSlot(Slots slot){
        s1.setPosition(slot.shootPose);
    }
    public static void turnBack(){
        turnTo(Slots.SLOT_1);
    }

    public static double minimumTime= 350;
    public static double lastTime= 0;
    public static Slots currentSlot= Slots.SLOT_1;

    public static void turnManuallyToRight(){
        s1.turn(120);
        s2.turn(-120);
    }
    public static void turnManuallyToLeft(){
        s1.turn(-120);
        s2.turn(120);
    }

    public static boolean sorting= false;
    public static void setSorting(boolean set){
        sorting= set;
    }
    public static boolean testBoolean;

    public static BreakBeam.Status beamState;
    public static BetterColorSensor colorSensor;
    public static BreakBeam breakBeam;

    public static boolean inSlot= false;

    public static double BallInDist= 10;
    public static void update(){
        if(Intake.intaking){
            if(sorting){
                throw new IllegalArgumentException("nu stim sa sortam inca");
            }
            else{
                double dist= colorSensor.getDistanceInCM();
                testBoolean= dist< BallInDist;
                beamState = breakBeam.getBeamState();
                if(System.currentTimeMillis() - lastTime > minimumTime && beamState.equals(BreakBeam.Status.BROKEN) && currentSlot != Slots.SLOT_3){

                    lastTime= System.currentTimeMillis();
                    if(currentSlot== Slots.SLOT_1)
                        turnTo(Slots.SLOT_2);
                    else if(currentSlot== Slots.SLOT_2)
                        turnTo(Slots.SLOT_3);

                }
            }
        }
    }

}
