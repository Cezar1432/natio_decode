package org.firstinspires.ftc.teamcode.pedropathing;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.control.FilteredPIDFCoefficients;
import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.CoaxialPod;
import com.pedropathing.ftc.drivetrains.SwerveConstants;
import com.pedropathing.ftc.localization.constants.PinpointConstants;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;


@Configurable
public class PedroConstants {
    public static PIDFCoefficients secondaryHeadingCoeffs = new PIDFCoefficients(0.8, 0, 0.015, 0);

    public static PIDFCoefficients headingCoeffs = new PIDFCoefficients(1.75, 0, 0.003, 0);

    public static double xOffset= 16, yOffset= 5.35;

    public static double leftFrontOffset= -50, rightFrontOffset = -25, leftBackOffset= 80, rightBackOffset= -60;
    public static FollowerConstants followerConstants = new FollowerConstants()
            .forwardZeroPowerAcceleration(-197.1)
            .lateralZeroPowerAcceleration(-197.1)
            .useSecondaryDrivePIDF(true).useSecondaryHeadingPIDF(true)
            .useSecondaryTranslationalPIDF(true)

            .translationalPIDFCoefficients(new PIDFCoefficients(0.125, 0, 0.008, 0))
            .secondaryTranslationalPIDFCoefficients(new PIDFCoefficients(0.0825, 0, 0.008, 0))

            .headingPIDFCoefficients(headingCoeffs)
            .secondaryHeadingPIDFCoefficients(secondaryHeadingCoeffs)

            .drivePIDFCoefficients(new FilteredPIDFCoefficients(0.005, 0, 0.00003, 0.6, 0.13))
            .secondaryDrivePIDFCoefficients(
                    new FilteredPIDFCoefficients(0.004, 0, 0.000002, 0.6, 0.13))


            // .drivePIDFCoefficients(new FilteredPIDFCoefficients(0,0,0,0,0))
            // .secondaryDrivePIDFCoefficients(new FilteredPIDFCoefficients(0,0,0,0,0))

            // .predictiveBrakingCoefficients(new PredictiveBrakingCoefficients(
            // 0.05, //0.05 to 0.3
            // 0,//0.38735914623969386,
            // 0.002)
            // )
            .centripetalScaling(0.0005).
            mass(13.732);

    public static PinpointConstants localizerConstants = new PinpointConstants()
            .forwardPodY(xOffset) //-74.5mm
            .strafePodX(yOffset) //-150.2
            .distanceUnit(DistanceUnit.INCH).hardwareMapName("pinpoint")
            .encoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD)
            .forwardEncoderDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD)
            .strafeEncoderDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED);

    public static SwerveConstants swerveConstants = new SwerveConstants()
            .velocity(83)
//            .zeroPowerBehavior(SwerveConstants.ZeroPowerBehavior.IGNORE_ANGLE_CHANGES)
            .useBrakeModeInTeleOp(true);

    // F - front: .130, back: .190
    // P=0.00645 D=0.00019
    // P=0.00549 D=0.00028

    private static double kP = 0.0055 * 180 / Math.PI;
    private static double kD = 0.00015 * 180 / Math.PI;
    private static double kFFront = 0.0130;
    private static double kFBack = 0.0190;


    private static double dtLength = 26.8/2;
    private static double dtWidth = 34.4/2;

    public static CoaxialPod leftFront(HardwareMap hardwareMap) {
        CoaxialPod pod = new CoaxialPod(hardwareMap, "sm1", "ss2", "se2",
                new PIDFCoefficients(kP, 0, kD, kFFront), DcMotorSimple.Direction.REVERSE,
                DcMotorSimple.Direction.FORWARD, Math.toRadians(leftFrontOffset), new Pose(dtLength, dtWidth),
                0, 3.3, true);
        pod.setMotorCachingThreshold(0.01);
        pod.setServoCachingThreshold(0.01);
        return pod;
    }

    public static CoaxialPod rightFront(HardwareMap hardwareMap) {
        CoaxialPod pod = new CoaxialPod(hardwareMap, "sm2", "ss1", "se1",
                new PIDFCoefficients(kP, 0, kD, kFFront), DcMotorSimple.Direction.FORWARD,
                DcMotorSimple.Direction.FORWARD, Math.toRadians(rightFrontOffset), new Pose(dtLength, -dtWidth),
                0.0, 3.3, true);
        pod.setMotorCachingThreshold(0.01);
        pod.setServoCachingThreshold(0.01);
        return pod;
    }

    public static CoaxialPod leftBack(HardwareMap hardwareMap) {
        CoaxialPod pod = new CoaxialPod(hardwareMap, "sm0", "ss3", "se3",
                new PIDFCoefficients(kP, 0, kD, kFBack), DcMotorSimple.Direction.REVERSE,
                DcMotorSimple.Direction.FORWARD, Math.toRadians(leftBackOffset), new Pose(-dtLength, dtWidth),
                0.0, 3.3, true);
        pod.setMotorCachingThreshold(0.01);
        pod.setServoCachingThreshold(0.01);
        return pod;
    }

    public static CoaxialPod rightBack(HardwareMap hardwareMap) {
        CoaxialPod pod = new CoaxialPod(hardwareMap, "sm3", "ss0", "se0",
                new PIDFCoefficients(kP, 0, kD, kFBack), DcMotorSimple.Direction.FORWARD,
                DcMotorSimple.Direction.FORWARD, Math.toRadians(rightBackOffset), new Pose(-dtLength, -dtWidth),
                0, 3.3, true);
        pod.setMotorCachingThreshold(0.01);
        pod.setServoCachingThreshold(0.01);
        return pod;
    }
    public static MySwerve getSwerve(HardwareMap hardwareMap){
        return new MySwerve(hardwareMap, PedroConstants.swerveConstants, PedroConstants.leftFront(hardwareMap)
                ,PedroConstants.rightFront(hardwareMap), PedroConstants.leftBack(hardwareMap), PedroConstants.rightBack(hardwareMap));
    }

    // TODO: TUNE THESE, CAN MAKE A HUGE DIFF
    // public static PathConstraints pathConstraints = new PathConstraints(0.95, 100, 1, 1);
    public static PathConstraints pathConstraints =
            new PathConstraints(0.9, 2, 2, 0.03, 50, 1, 10, 1);

    // default
    // public static PathConstraints defaultConstraints = new PathConstraints(0.995, 0.1, 0.1,
    // 0.007, 100, 1, 10, 1);
    public static
    Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap).pathConstraints(pathConstraints)
                .swerveDrivetrain(swerveConstants, leftFront(hardwareMap), rightFront(hardwareMap),
                        leftBack(hardwareMap), rightBack(hardwareMap))
                .pinpointLocalizer(localizerConstants).build();
    }
}



