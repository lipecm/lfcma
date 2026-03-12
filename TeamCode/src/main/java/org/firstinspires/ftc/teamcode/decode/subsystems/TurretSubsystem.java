package org.firstinspires.ftc.teamcode.decode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Subsistema responsável por girar a base de tiro (Turret).
 */
public class TurretSubsystem {
    private RobotHardware robot;

    // Constantes PID para a torreta baseadas no código de teste (FrepeCode/LimeViewing)
    private double kp = -1.0; 
    private double ki = -0.0005;
    private double kd = -1.5;

    private double ultimoErro = 0;
    private double ultimaIntegral = 0;

    public TurretSubsystem(RobotHardware robot) {
        this.robot = robot;
        this.robot.turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.robot.turret.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    /**
     * Mira usando o erro direto da Limelight (TX).
     */
    public void mirarLimelight(double erroTX) {
        // PID simples para centralizar o TX em 0
        double power = pid(erroTX);
        robot.turret.setPower(Math.max(-1.0, Math.min(1.0, power)));
    }

    /**
     * Mira baseada em coordenadas do campo (Field Centric).
     * @param targetX Coordenada X do alvo
     * @param targetY Coordenada Y do alvo
     * @param robotX X atual do robô (via Pedro Pathing ou Limelight)
     * @param robotY Y atual do robô
     * @param robotHeading Heading atual do robô em radianos
     */
    public void mirarCoordenada(double targetX, double targetY, double robotX, double robotY, double robotHeading) {
        // Ângulo atual da torreta em relação ao robô (convertendo ticks para radianos)
        // Usando a fórmula do LimeViewing: (28 * 5.2 * 6) parece ser a redução total
        double angTurret = 2 * Math.PI * robot.turret.getCurrentPosition() / (28 * 5.2 * 6);
        
        // Ângulo global da torreta
        double alpha = robotHeading + angTurret - (Math.PI / 2);
        
        // Ângulo necessário para apontar para o alvo
        double theta = Math.atan2(targetY - robotY, targetX - robotX);
        
        // Erro entre onde a torreta está apontando e onde deveria
        double erro = Math.toDegrees(theta - alpha);
        
        // Normalização do erro
        while (erro > 180) erro -= 360;
        while (erro < -180) erro += 360;

        double power = pid(erro);
        robot.turret.setPower(Math.max(-1.0, Math.min(1.0, power)));
    }

    public void controleManual(double forca) {
        robot.turret.setPower(forca);
    }

    private double pid(double erro) {
        double proporcional = erro * kp;
        double derivativo = (erro - ultimoErro) * kd;
        double integrativo = (erro * ki) + ultimaIntegral;

        ultimoErro = erro;
        ultimaIntegral = integrativo;

        return proporcional + integrativo + derivativo;
    }
    
    public int getPosicao() {
        return robot.turret.getCurrentPosition();
    }
}
