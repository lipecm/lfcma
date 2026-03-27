package org.firstinspires.ftc.teamcode.decode.subsystems;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;
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
    public LLResultTypes.FiducialResult getMelhorFiducial(int[] tagsPermitidas) {
        LLResult res = getResultado();
        if (res == null || !res.isValid()) return null;

        List<LLResultTypes.FiducialResult> fiducials = res.getFiducialResults();
        LLResultTypes.FiducialResult melhor = null;

        for (LLResultTypes.FiducialResult f : fiducials) {
            boolean permitida = false;
            for (int id : tagsPermitidas) {
                if (f.getFiducialId() == id) {
                    permitida = true;
                    break;
                }
            }

            if (permitida) {
                // Prioriza o alvo com menor erro horizontal (mais central)
                if (melhor == null || Math.abs(f.getTargetXDegrees()) < Math.abs(melhor.getTargetXDegrees())) {
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
        LLResultTypes.FiducialResult f = getMelhorFiducial(tagsPermitidas);
        return (f != null) ? f.getTargetXDegrees() : 0;
    }

    /**
     * Estima a distância até o alvo. 
     * Nota: O valor 1500 é uma constante de calibração que depende da altura/ângulo da câmera.
     */
    public double getDistanciaEstimada(int[] tagsPermitidas) {
        LLResultTypes.FiducialResult f = getMelhorFiducial(tagsPermitidas);
        if (f != null) {
            // Usando a área (targetArea) como base para distância.
            return 1500.0 / f.getTargetArea();
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
