package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "TeleOp Blue Alliance", group = "Main")
public class MainTeleOpBlue extends MainTeleOp {
    @Override
    public void init() {
        isBlueAlliance = true;
        super.init();
    }
}
