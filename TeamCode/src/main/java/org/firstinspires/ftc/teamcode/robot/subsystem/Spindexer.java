package org.firstinspires.ftc.teamcode.robot.subsystem;

import com.bylazar.configurables.annotations.Configurable;

import org.firstinspires.ftc.teamcode.tasks.command_based.core.Command;
import org.firstinspires.ftc.teamcode.util.wrappers.BetterServo;
import org.firstinspires.ftc.teamcode.util.wrappers.BreakBeam;
import org.firstinspires.ftc.teamcode.util.wrappers.colorsensor.BetterColorSensor;
import org.firstinspires.ftc.teamcode.util.wrappers.colorsensor.Colors;

import java.util.HashMap;

@Configurable
public class Spindexer {
    public static BetterServo s1, s2;
    Command s;

    public static void setPosition(double pos) {
        s1.setPosition(pos);
        s2.setPosition(pos);
    }

    public static boolean slot3Full = false;

    public static void shootRandom() {
//         s1.setMaxDegrees(1100);
//         s2.setMaxDegrees(1100);
        setPosition(1);
        clearAll();
        slot3Full = false;
        //  s2.setPosition(s31.getPosition());
    }


    public enum Slots {
        SLOT_1(.4122, 0), SLOT_2(.2922, 0), SLOT_3(.1656 , 0);//EJECT1(0.38,0),EJECT2(0.5117,0),EJECT3(0.6233,0);
        // SLOT_1(0.4656,0), SLOT_2(.345,0), SLOT_3(.2217,0),EJECT1(0.38,0),EJECT2(0.5117,0),EJECT3(0.6233,0);

        final double frontPose, shootPose;

        // final double shootPose;
        Slots(double frontPose, double shootPose) {
            this.frontPose = frontPose;
            this.shootPose = shootPose;
        }

    }


    public static void turnTo(Slots slot) {
        setPosition(slot.frontPose);
        currentSlot = slot;
    }

    public static void shootSlot(Slots slot) {
        s1.setPosition(slot.shootPose);
    }

    public static void turnBack() {
        turnTo(Slots.SLOT_1);
    }

    public static double minimumTime = 350;
    public static double infimumtime = 90;


    public static double lastTime = 0;
    public static Slots currentSlot = Slots.SLOT_1;

    public static void turnManuallyToRight() {
        s1.turn(120);
        s2.turn(-120);
    }

    public static void turnManuallyToLeft() {
        s1.turn(-120);
        s2.turn(120);
    }

    static HashMap<Slots, Colors.Balls> ballsAndSlots = new HashMap<>();


    public static Colors.Balls getBallColor(Slots slot) {
        return ballsAndSlots.get(slot);
    }

    public static void setSlotColor(Slots slot, Colors.Balls color) {
        ballsAndSlots.put(slot, color);
    }

    public static void clear(Slots slot) {
        ballsAndSlots.put(slot, null);
    }

    public static void clearAll() {
        for (Slots slot : Slots.values())
            clear(slot);
    }


    public static boolean sorting = false;

    public static void setSorting(boolean set) {
        sorting = set;
    }

    public static boolean testBoolean;

    public static BreakBeam.Status beamState;
    public static BetterColorSensor colorSensor;
    public static BreakBeam breakBeam;

    public static boolean inSlot = false;

    public static double BallInDist = 10;
    public static int[] currentValues = new int[3];
    public static String[] colors = new String[4];
    public static Colors.Balls color;

    public static void assignToNigga(Slots current, Colors.Balls currentColor) {
        int slot;
        String nume;
        if (current == Slots.SLOT_1)
            slot = 1;
        else if (current == Slots.SLOT_2)
            slot = 2;
        else
            slot = 3;

        nume = currentColor.getName();
        if (nume.equals("PURPLE"))
            nume = "purple";
        else if (nume.equals("GREEN"))
            nume = "green";
        else
            nume = "none";

        colors[slot] = nume;

    }

    public static Slots convertToEnum(int i) {
        Slots answer;
        if (i == 1)
            answer = Slots.SLOT_1;
        else if (i == 2)
            answer = Slots.SLOT_2;
        else
            answer = Slots.SLOT_3;
        return answer;
    }

    public static void shootPurple() {
        boolean shot = false;
        for (int i = 1; i <= 3; i++) {
            Slots slot = convertToEnum(i);
            if (colors[i].equals("purple")) {
                setPosition(slot.frontPose + 0.205);
                shot = true;
                colors[i] = "none";
            }
            if (shot)
                setPosition(slot.frontPose);
        }
    }

    public static void shootGreen() {
        boolean shot = false;
        for (int i = 1; i <= 3; i++) {
            Slots slot = convertToEnum(i);
            if (colors[i].equals("green")) {
                setPosition(slot.frontPose + 0.205);
                shot = true;
                colors[i] = "none";
            }
            if (shot)
                setPosition(slot.frontPose);
        }
    }

    public static boolean waiting = false;
    public static double colorTime = 0;
    public static double dist;
    public static void update() {
        if (Intake.intaking) {
            if (sorting) {
                //beamState = breakBeam.getBeamState();
                dist = colorSensor.getDistanceInCM();
            } else {
                beamState = breakBeam.getBeamState();
                if (System.currentTimeMillis() - lastTime > minimumTime && beamState.equals(BreakBeam.Status.BROKEN) && currentSlot != Slots.SLOT_3) {

                    lastTime = System.currentTimeMillis();
                    if (currentSlot == Slots.SLOT_1)
                        turnTo(Slots.SLOT_2);
                    else if (currentSlot == Slots.SLOT_2)
                        turnTo(Slots.SLOT_3);
                }
            }


        }
    }
}
