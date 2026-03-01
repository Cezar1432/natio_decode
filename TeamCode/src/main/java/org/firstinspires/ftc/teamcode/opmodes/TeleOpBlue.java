package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.robot.Alliance;
import org.firstinspires.ftc.teamcode.util.wrappers.BetterGamepad;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp
public class TeleOpBlue extends TeleOp {
    @Override
    public void setAlliance() {
        super.a= Alliance.BLUE;
    }

    @Override
    public void setDriveSuppliers() {
        super.forward= ()-> gamepadEx1.getDouble(BetterGamepad.Trigger.LEFT_Y);
        super.strafe= ()-> gamepadEx1.getDouble(BetterGamepad.Trigger.LEFT_X);
        super.rotation= ()-> -gamepadEx1.getDouble(BetterGamepad.Trigger.RIGHT_X);
    }
}
