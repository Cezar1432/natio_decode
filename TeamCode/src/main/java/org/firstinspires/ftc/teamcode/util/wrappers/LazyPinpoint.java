package org.firstinspires.ftc.teamcode.util.wrappers;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchSimple;
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties;
import com.qualcomm.robotcore.hardware.configuration.annotations.I2cDeviceType;

@I2cDeviceType
@DeviceProperties(
        name = "Lazy Pinpoint"
        ,
        xmlTag = "LazyPinpoint"
)
public class LazyPinpoint extends GoBildaPinpointDriver implements HardwareDevice {
    public LazyPinpoint(I2cDeviceSynchSimple deviceClient, boolean deviceClientIsOwned) {
        super(deviceClient, deviceClientIsOwned);
    }

    double hz;
    double updateTime;
    double lastTime;
    public void setUpdateRate(double hz){
        this.hz= hz;
        updateTime= 1/hz;
        lastTime= System.currentTimeMillis();
    }

    @Override
    public void update(){
        if(System.currentTimeMillis()- lastTime> updateTime){
            update();
            lastTime= System.currentTimeMillis();
        }
    }
    @Override
    public void update(ReadData data){
        if(System.currentTimeMillis()- lastTime> updateTime){
            update(data);
            lastTime= System.currentTimeMillis();
        }
    }
}
