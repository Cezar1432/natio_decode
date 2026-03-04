package org.firstinspires.ftc.teamcode.util.groups;

import androidx.annotation.NonNull;

import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;

import org.firstinspires.ftc.teamcode.util.wrappers.BetterMotor;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class MotorGroup extends BetterMotor implements Iterable<BetterMotor> {
    private final BetterMotor[] group;
    private static double MAX_VELOCITY;


    /**
     * Create a new MotorGroup with the provided Motors.
     *
     * @param leader    The leader motor.
     * @param followers The follower motors which follow the leader motor's protocols.
     */
        public MotorGroup(@NonNull BetterMotor leader, BetterMotor... followers) {
            super(leader.getController(), leader.getPortNumber());
            group = new BetterMotor[followers.length+1];
        group[0] = leader;
        System.arraycopy(followers, 0, group, 1, followers.length);
    }

    /**
     * Set the speed for each motor in the group
     *
     * @param speed The speed to set. Value should be between -1.0 and 1.0.
     */
    public void set(double speed) {
        group[0].setPower(speed);
        for (int i = 1; i < group.length; i++) {
            group[i].setPower(group[0].getPower());
        }
    }

    /**
     * @return The speed as a percentage of output
     */
    public double get() {
        return group[0].getPower();
    }

    /**
     * @return All motor target speeds as a percentage of output
     */
    public List<Double> getSpeeds() {
        return Arrays.stream(group)
                .map(BetterMotor::getPower)
                .collect(Collectors.toList());
    }

    @Override
    public double getVelocity() {
        return group[0].getVelocity();
    }

    /**
     * @return All current velocities of the motors in the group in units of distance
     * per second which is by default ticks / second
     */
    public List<Double> getVelocities() {
        return Arrays.stream(group)
                .map(BetterMotor::getVelocity)
                .collect(Collectors.toList());
    }

    @NonNull
    @Override
    public Iterator<BetterMotor> iterator() {
        return Arrays.asList(group).iterator();
    }

    /**
     * @return The position of every motor in the group in units of distance
     * which is by default ticks
     */

    @Override
    public void setMode(RunMode runmode) {
        group[0].setMode(runmode);
    }

    @Override
    public void setZeroPowerBehavior(ZeroPowerBehavior behavior) {
        for (BetterMotor motor : group) {
            motor.setZeroPowerBehavior(behavior);
        }
    }

    @Override
    public void resetEncoder() {
        group[0].resetEncoder();
    }


    @Override
    public void setTargetPosition(int target) {
        group[0].setTargetPosition(target);
    }


    @Override
    public void setVelocityPIDFCoefficients(double kp, double ki, double kd, double kf) {
        group[0].setVelocityPIDFCoefficients(kp,ki,kd,kf);
    }


    /**
     * Set the motor group to the inverted direction or forward direction.
     * This directly affects the speed rather than the direction.
     *
     * @param isInverted The state of inversion true is inverted.
     * @return This object for chaining purposes.
     */
    @Override
    public MotorGroup setInverted(boolean isInverted) {
        for (BetterMotor motor : group) {
            motor.setInverted(isInverted);
        }
        return this;
    }



    /**
     * @return a string characterizing the device type
     */
    public String getType() {
        return "Motor Group";
    }

    @Override
    public void setAproxVelocity(double velocity){
        setPower(velocity/ MAX_VELOCITY);
    }

    public double setMaxVelocity(){
        return MAX_VELOCITY;
    }
    public BetterMotor setCachingTolerence(double tolerence){
        group[0].setCachingTolerance(tolerence);
        return this;
    }

}
