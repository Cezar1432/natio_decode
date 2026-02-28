package org.firstinspires.ftc.teamcode.robot.subsystem;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.UnnormalizedAngleUnit;
import org.firstinspires.ftc.teamcode.robot.Alliance;
import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.util.math.MathStuff;
import org.firstinspires.ftc.teamcode.util.wrappers.BetterServoEx;

public class Turret {

    static double NEUTRAL_POSITION= .5;
    public static double x, y, heading, xCorner, yCorner, dist, fieldRelative, robotRelative, turretRelative;
    private static final double FIELD_LENGTH= 3.65, launchTime= .2,goalCircleRasius= .18;
    public static BetterServoEx turret1, turret2;

    public static void update(){
        Pose robotPose= Robot.robotPose;
        if (robotPose.getX() != 0 && robotPose.getY() != 0) {

            x= robotPose.getX();
            y= robotPose.getY();
            heading= robotPose.getHeading();

            double xVelocity= Robot.odo.getVelX(DistanceUnit.METER);
            double yVelocity= Robot.odo.getVelY(DistanceUnit.METER);
            double hVelocity= Robot.odo.getHeadingVelocity(UnnormalizedAngleUnit.RADIANS);

            x+= launchTime * xVelocity;
            y+= launchTime * yVelocity;
            heading= MathStuff.normalizeRadians(heading + launchTime * hVelocity);



            yCorner= FIELD_LENGTH- y;
            xCorner= Robot.a== Alliance.RED ? FIELD_LENGTH- x : x;
            dist = Math.hypot(xCorner, yCorner);

            heading= Math.toDegrees(heading);
            xCorner -= goalCircleRasius;
            yCorner -= goalCircleRasius;
            fieldRelative = Math.atan(yCorner / xCorner);
            fieldRelative = Math.toDegrees(fieldRelative);
            if (Robot.a == Alliance.BLUE)
                fieldRelative = 180 - fieldRelative;
            robotRelative = normalize(fieldRelative - heading);
            turretRelative = normalize(robotRelative - 180);

            robotRelative = Robot.a == Alliance.RED ? robotRelative : -robotRelative;
            turretRelative+= 180;
            double finalPos= turretRelative/360;
            turret1.setPosition(finalPos);
            turret2.setPosition(finalPos);


        }
    }
    public static double normalize(double h){
        while (h> 180) h-=360;
        while (h< -180) h+= 360;
        return h;
    }

}

