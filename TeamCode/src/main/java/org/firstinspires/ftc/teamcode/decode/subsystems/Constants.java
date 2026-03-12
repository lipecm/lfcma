package org.firstinspires.ftc.teamcode.decode.subsystems;

import java.util.TreeMap;

/**
 * Classe que guarda todas as variáveis de calibração do robô.
 */
public class Constants {

    // --- CONSTANTES DO SHOOTER ---
    public static final double SHOOTER_BASE_RPM = 1150; 
    public static final double SHOOTER_MARGEM_ERRO_RPM = 125;
    public static final double SHOOTER_RPM_REPOUSO = 1300;

    // TreeMap para velocidade do shooter baseada na distância (Polegadas -> RPM)
    public static final TreeMap<Integer, Integer> SHOOTER_RPM_MAP = new TreeMap<>();
    static {
        SHOOTER_RPM_MAP.put(0, 1100);
        SHOOTER_RPM_MAP.put(40, 1300);
        SHOOTER_RPM_MAP.put(60, 1500);
        SHOOTER_RPM_MAP.put(80, 1750);
        SHOOTER_RPM_MAP.put(100, 2000);
        SHOOTER_RPM_MAP.put(120, 2300);
        SHOOTER_RPM_MAP.put(150, 2800);
    }

    // --- CONSTANTES DE DISTÂNCIA (REFERÊNCIA VÍDEO LIMELIGHT) ---
    // Calibre estes valores conforme o seu robô real
    public static final double CAMERA_HEIGHT = 10.0; // Altura da lente em relação ao chão (polegadas)
    public static final double TARGET_HEIGHT = 30.0; // Altura do centro do alvo (polegadas)
    public static final double CAMERA_ANGLE = 20.0;  // Ângulo de inclinação da câmera (graus)

    // Posições do Servo da Cancela
    public static final double CANCELA_ABERTA = 0.19;
    public static final double CANCELA_FECHADA = 0.25;

    // --- CONSTANTES DO LED ---
    public static final double LED_CHEIO = 0.388;
    public static final double LED_COLETANDO = 0.277;
    public static final double LED_ATIRANDO = 0.5;
}
