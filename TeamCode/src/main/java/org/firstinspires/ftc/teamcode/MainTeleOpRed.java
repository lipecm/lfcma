package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "TeleOp Red Alliance", group = "Main")
public class MainTeleOpRed extends MainTeleOp {
    @Override
    public void init() {
        isBlueAlliance = false;
        super.init();
    }
}
