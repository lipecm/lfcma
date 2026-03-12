package org.firstinspires.ftc.teamcode.decode.subsystems;

import com.qualcomm.robotcore.hardware.Gamepad;

public class DriveSubsystem {
    private RobotHardware robot;

    public DriveSubsystem(RobotHardware robot) {
        this.robot = robot;
    }

    /**
     * Move o robô usando a matemática padrão Mecanum.
     * Ajustado para o mapeamento: d0=FL, d1=FR, d2=RL, d3=RR
     */
    public void dirigir(Gamepad gamepad) {
        // Driver 1 controla a tração
        double y = -gamepad.left_stick_y; // Frente/Trás
        double x = gamepad.left_stick_x * 1.1; // Lado/Lado (Strafe)
        double rx = gamepad.right_stick_x; // Rotação

        // Cálculo de potências
        double fl = y + x + rx;
        double rl = y - x + rx;
        double fr = y - x - rx;
        double rr = y + x - rx;

        // Normalização
        double max = Math.max(Math.abs(fl), Math.max(Math.abs(rl), 
                     Math.max(Math.abs(fr), Math.max(Math.abs(rr), 1.0))));

        robot.frontalEsquerdo.setPower(fl / max);
        robot.traseiroEsquerdo.setPower(rl / max);
        robot.frontalDireito.setPower(fr / max);
        robot.traseiroDireito.setPower(rr / max);
    }
}
