package org.firstinspires.ftc.teamcode.decode.subsystems;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.hardware.Gamepad;

public class DriveSubsystem {
    private RobotHardware robot;
    private Follower follower;

    public DriveSubsystem(RobotHardware robot, Follower follower) {
        this.robot = robot;
        this.follower = follower;
    }

    /**
     * Move o robô usando campo-centrado (Field-Centric).
     * Mapeamento: Analógico DIREITO para translação, ESQUERDO para rotação.
     */
    public void dirigir(Gamepad gamepad) {
        // Sensibilidade quadrática (preservando o sinal)
        double ly = -gamepad.right_stick_y * Math.abs(gamepad.right_stick_y);
        double lx = -gamepad.right_stick_x * Math.abs(gamepad.right_stick_x);
        double rot = -gamepad.left_stick_x; // Rotação no analógico esquerdo

        // Obtém o heading do Pedro Pathing (em radianos)
        double heading = (follower != null) ? follower.getPose().getHeading() : 0;
        
        // Rotação vetorial para Field-Centric (Removido o PI/2 que causaria offset de 90°)
        double xlinha = lx * Math.cos(heading) - ly * Math.sin(heading);
        double ylinha = lx * Math.sin(heading) + ly * Math.cos(heading);

        // Mixagem de motores (Mecanum)
        // FL = -ylinha + xlinha + rot
        // FR = -ylinha - xlinha - rot
        // RL = -ylinha - xlinha + rot
        // RR = -ylinha + xlinha - rot
        
        double fl = -ylinha + xlinha + rot;
        double fr = -ylinha - xlinha - rot;
        double rl = -ylinha - xlinha + rot;
        double rr = -ylinha + xlinha - rot;

        // Normalização de potência
        double max = Math.max(Math.abs(fl), Math.max(Math.abs(rl), 
                     Math.max(Math.abs(fr), Math.max(Math.abs(rr), 1.0))));

        robot.frontalEsquerdo.setPower(fl / max);
        robot.frontalDireito.setPower(fr / max);
        robot.traseiroEsquerdo.setPower(rl / max);
        robot.traseiroDireito.setPower(rr / max);
    }

    public void setFollower(Follower follower) {
        this.follower = follower;
    }
}
