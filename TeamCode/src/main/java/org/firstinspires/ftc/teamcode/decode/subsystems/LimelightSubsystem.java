package org.firstinspires.ftc.teamcode.decode.subsystems;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/**
 * Subsistema responsável pela visão computacional usando Limelight 3A.
 */
public class LimelightSubsystem {
    private Limelight3A lime;

    public void init(HardwareMap hwMap) {
        lime = hwMap.get(Limelight3A.class, "limelight");
        lime.pipelineSwitch(0); // Geralmente 0 para AprilTags
        lime.start();
    }

    public LLResult getResultado() {
        return lime.getLatestResult();
    }

    public boolean temAlvo() {
        LLResult res = getResultado();
        return res != null && res.isValid();
    }

    public double getErroHorizontal() {
        LLResult resultado = getResultado();
        if (resultado != null && resultado.isValid()) {
            return resultado.getTx();
        }
        return 0;
    }

    public double getDistanciaEstimada() {
        LLResult res = getResultado();
        if (res != null && res.isValid()) {
            // Se estiver usando AprilTags e Botpose
            Pose3D botpose = res.getBotpose_MT2();
            if (botpose != null) {
                // Cálculo de distância 2D simples até a origem ou alvo específico pode ser feito aqui
                // Por enquanto, retornamos a distância direta se disponível ou usamos TA como fallback
                return Math.hypot(botpose.getPosition().x, botpose.getPosition().y) * 39.37; // Metros para Polegadas
            }
            // Fallback usando área (ta) se calibrado
            return 1500 / res.getTa();
        }
        return 100; // Padrão
    }

    public void atualizarOrientacao(double headingGraus) {
        lime.updateRobotOrientation(headingGraus);
    }

    public Pose3D getBotPose() {
        LLResult res = getResultado();
        if (res != null && res.isValid()) {
            return res.getBotpose_MT2();
        }
        return null;
    }

    public void parar() {
        lime.stop();
    }
}
