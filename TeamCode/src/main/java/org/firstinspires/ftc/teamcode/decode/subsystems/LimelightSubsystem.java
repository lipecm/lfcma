package org.firstinspires.ftc.teamcode.decode.subsystems;

import com.qualcomm.hardware.limelightvision.LLFiducialResult;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import java.util.List;

/**
 * Subsistema responsável pela visão computacional usando Limelight 3A.
 */
public class LimelightSubsystem {
    private Limelight3A lime;

    // IDs das AprilTags por Aliança (Temporada Into The Deep 2024-2025)
    public static final int[] BLUE_TAGS = {11, 12, 13};
    public static final int[] RED_TAGS = {14, 15, 16};

    public void init(HardwareMap hwMap) {
        lime = hwMap.get(Limelight3A.class, "limelight");
        lime.pipelineSwitch(0); // Pipeline 0 configurado para AprilTags
        lime.start();
    }

    public LLResult getResultado() {
        return lime.getLatestResult();
    }

    /**
     * Retorna o melhor fiducial da lista de IDs permitidos (o mais centralizado).
     */
    public LLFiducialResult getMelhorFiducial(int[] tagsPermitidas) {
        LLResult res = getResultado();
        if (res == null || !res.isValid()) return null;

        List<LLFiducialResult> fiducials = res.getFiducialResults();
        LLFiducialResult melhor = null;

        for (LLFiducialResult f : fiducials) {
            boolean permitida = false;
            for (int id : tagsPermitidas) {
                if (f.getFiducialId() == id) {
                    permitida = true;
                    break;
                }
            }

            if (permitida) {
                // Prioriza o alvo com menor erro horizontal (mais central)
                if (melhor == null || Math.abs(f.getTx()) < Math.abs(melhor.getTx())) {
                    melhor = f;
                }
            }
        }
        return melhor;
    }

    public boolean temAlvo(int[] tagsPermitidas) {
        return getMelhorFiducial(tagsPermitidas) != null;
    }

    public double getErroHorizontal(int[] tagsPermitidas) {
        LLFiducialResult f = getMelhorFiducial(tagsPermitidas);
        return (f != null) ? f.getTx() : 0;
    }

    /**
     * Estima a distância até o alvo. 
     * Nota: O valor 1500 é uma constante de calibração que depende da altura/ângulo da câmera.
     */
    public double getDistanciaEstimada(int[] tagsPermitidas) {
        LLFiducialResult f = getMelhorFiducial(tagsPermitidas);
        if (f != null) {
            // Usando a área (ta) como base para distância, conforme lógica anterior.
            // Calibre este valor no robô real para maior precisão.
            return 1500.0 / f.getTa();
        }
        return 0;
    }

    public void atualizarOrientacao(double headingGraus) {
        lime.updateRobotOrientation(headingGraus);
    }

    public void parar() {
        lime.stop();
    }
}
