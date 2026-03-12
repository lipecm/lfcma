package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.decode.subsystems.Constants;
import org.firstinspires.ftc.teamcode.decode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.decode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.decode.subsystems.LimelightSubsystem;
import org.firstinspires.ftc.teamcode.decode.subsystems.RobotHardware;
import org.firstinspires.ftc.teamcode.decode.subsystems.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.decode.subsystems.TurretSubsystem;

/**
 * Classe Base para o TeleOp.
 * Removido Pedro Pathing conforme solicitado.
 */
public abstract class MainTeleOp extends OpMode {

    protected RobotHardware robot;
    protected DriveSubsystem drive;
    protected IntakeSubsystem intake;
    protected ShooterSubsystem shooter;
    protected TurretSubsystem turret;
    protected LimelightSubsystem limelight;

    protected boolean isBlueAlliance = true;

    @Override
    public void init() {
        robot = new RobotHardware();
        robot.init(hardwareMap);

        drive = new DriveSubsystem(robot);
        intake = new IntakeSubsystem(robot);
        shooter = new ShooterSubsystem(robot);
        turret = new TurretSubsystem(robot);

        limelight = new LimelightSubsystem();
        limelight.init(hardwareMap);

        telemetry.addData("Status", "Inicializado - Aliança: " + (isBlueAlliance ? "AZUL" : "VERMELHA"));
        telemetry.update();
    }

    @Override
    public void loop() {
        // --- SENSORES ---
        int bolasNoRobo = 0;
        double limitePresenca = 0.35;
        if (robot.sensorBola1.getVoltage() < limitePresenca) bolasNoRobo++;
        if (robot.sensorBola2.getVoltage() < limitePresenca) bolasNoRobo++;
        if (robot.sensorBola3.getVoltage() < limitePresenca) bolasNoRobo++;

        // --- TRAÇÃO ---
        drive.dirigir(gamepad1);

        // --- INTAKE ---
        boolean querColetar = gamepad1.left_bumper || gamepad2.left_bumper;
        boolean querEjetar = gamepad1.right_bumper || gamepad2.right_bumper;
        intake.atualizar(querColetar, querEjetar);

        // --- VISÃO E TORRETA ---
        LLResult alvo = limelight.getResultado();
        double erroTX = limelight.getErroHorizontal();
        double distancia = limelight.getDistanciaEstimada();
        boolean alvoDetectado = limelight.temAlvo();

        // Lógica de Mira: Prioridade Limelight (AprilTag) > Manual
        if (gamepad1.left_trigger > 0 || gamepad2.left_trigger > 0) {
            if (alvoDetectado) {
                // Se vê AprilTag, mira nela (mais preciso) usando PID
                turret.mirarLimelight(erroTX);
            } else {
                // Sem localização externa (Pedro Pathing), não podemos mirar por coordenada field-centric de forma confiável.
                // Mantém a torreta parada ou permite ajuste manual fino.
                turret.controleManual(0);
            }
        } else {
            turret.controleManual(gamepad2.left_stick_x * 0.5);
        }

        // --- SHOOTER ---
        boolean querAtirar = gamepad1.right_trigger > 0 || gamepad2.right_trigger > 0;
        shooter.calcularEAtualizarVelocidade(querAtirar, distancia);

        boolean miraBoa = Math.abs(erroTX) <= 5;
        boolean botaoDisparo = gamepad1.cross || gamepad1.square || gamepad2.square;
        shooter.gerenciarDisparo(botaoDisparo, bolasNoRobo, miraBoa);

        // --- TELEMETRIA ---
        telemetry.addData("Aliança", isBlueAlliance ? "AZUL" : "VERMELHA");
        telemetry.addData("Bolas", bolasNoRobo);
        
        if (alvoDetectado) {
            telemetry.addData("Distância AprilTag (pol)", String.format("%.1f", distancia));
            telemetry.addData("Erro TX", String.format("%.2f", erroTX));
        } else {
            telemetry.addData("Distância", "ALVO NÃO DETECTADO");
        }
        
        telemetry.addData("Shooter RPM Alvo", String.format("%.0f", shooter.getRpmAlvo()));
        telemetry.addData("Shooter RPM Atual", String.format("%.0f", robot.shooter1.getVelocity()));
        telemetry.update();
    }

    @Override
    public void stop() {
        limelight.parar();
    }
}
