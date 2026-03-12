package org.firstinspires.ftc.teamcode.decode.subsystems;

import com.qualcomm.robotcore.util.ElapsedTime;
import java.util.Map;

/**
 * Subsistema que controla os motores de disparo (Flywheels) e o servo da cancela.
 */
public class ShooterSubsystem {

    private RobotHardware robot;
    private double rpmAlvo = 0;
    private boolean atirando = false;
    private ElapsedTime tempoDeTiro = new ElapsedTime();

    public ShooterSubsystem(RobotHardware robot) {
        this.robot = robot;
    }

    /**
     * Liga os motores do shooter usando o TreeMap de distância.
     */
    public void calcularEAtualizarVelocidade(boolean ativado, double distanciaPolegadas) {
        if (ativado) {
            // Busca no TreeMap a velocidade ideal para a distância
            // floorEntry pega o valor igual ou menor mais próximo
            Map.Entry<Integer, Integer> entry = Constants.SHOOTER_RPM_MAP.floorEntry((int)distanciaPolegadas);
            
            if (entry != null) {
                rpmAlvo = entry.getValue();
            } else {
                rpmAlvo = Constants.SHOOTER_BASE_RPM;
            }

            robot.shooter1.setVelocity(rpmAlvo);
            robot.shooter2.setPower(rpmAlvo / 2800.0);
        } else {
            rpmAlvo = Constants.SHOOTER_RPM_REPOUSO;
            if (rpmAlvo == 0) {
                robot.shooter1.setPower(0);
                robot.shooter2.setPower(0);
            } else {
                robot.shooter1.setVelocity(rpmAlvo);
                robot.shooter2.setPower(rpmAlvo / 2800.0);
            }
        }
    }

    public void gerenciarDisparo(boolean botaoApertado, int quantidadeBolas, boolean miraCorreta) {
        double erroDeVelocidade = rpmAlvo - robot.shooter1.getVelocity();
        boolean rpmOk = Math.abs(erroDeVelocidade) <= Constants.SHOOTER_MARGEM_ERRO_RPM;

        if (botaoApertado && rpmOk && miraCorreta && !atirando && quantidadeBolas > 0) {
            atirando = true;
            tempoDeTiro.reset();
        }

        if (atirando) {
            robot.cancela.setPosition(Constants.CANCELA_ABERTA);
            if (quantidadeBolas == 0 || tempoDeTiro.seconds() > 1.0) {
                atirando = false;
            }
        } else {
            robot.cancela.setPosition(Constants.CANCELA_FECHADA);
        }
    }

    public double getRpmAlvo() {
        return rpmAlvo;
    }
}
