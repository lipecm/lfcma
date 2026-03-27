package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.decode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.decode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.decode.subsystems.LimelightSubsystem;
import org.firstinspires.ftc.teamcode.decode.subsystems.RobotHardware;
import org.firstinspires.ftc.teamcode.decode.subsystems.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.decode.subsystems.TurretSubsystem;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import java.util.Locale;

/**
 * Classe Base para o TeleOp.
 * Integração de Transmissão Mecanum Field-Centric, Turret e Shooter.
 */
public abstract class MainTeleOp extends OpMode {

    protected RobotHardware robot;
    protected DriveSubsystem drive;
    protected IntakeSubsystem intake;
    protected ShooterSubsystem shooter;
    protected TurretSubsystem turret;
    protected LimelightSubsystem limelight;
    protected Follower follower;

    protected boolean isBlueAlliance = true;

    @Override
    public void init() {
        robot = new RobotHardware();
        robot.init(hardwareMap);

        // Pedro Pathing fornece o Heading para o Field-Centric
        follower = Constants.createFollower(hardwareMap);

        drive = new DriveSubsystem(robot, follower);
        intake = new IntakeSubsystem(robot);
        shooter = new ShooterSubsystem(robot);
        turret = new TurretSubsystem(robot);

        limelight = new LimelightSubsystem();
        limelight.init(hardwareMap);

        telemetry.addData("Status", "Pronto - Aliança: " + (isBlueAlliance ? "AZUL" : "VERMELHA"));
        telemetry.update();
    }

    @Override
    public void loop() {
        // Atualiza odometria/seguidor
        follower.update();

        // IDs das Tags baseados na aliança
        int[] tagsAlvo = isBlueAlliance ? LimelightSubsystem.BLUE_TAGS : LimelightSubsystem.RED_TAGS;

        // --- SENSORES DE CARGA (Bolas) ---
        int bolasNoRobo = 0;
        double limitePresenca = 0.35;
        if (robot.sensorBola1.getVoltage() < limitePresenca) bolasNoRobo++;
        if (robot.sensorBola2.getVoltage() < limitePresenca) bolasNoRobo++;
        if (robot.sensorBola3.getVoltage() < limitePresenca) bolasNoRobo++;

        // --- TRAÇÃO ---
        // Stick Direito = Move | Stick Esquerdo = Gira
        drive.dirigir(gamepad1);

        // --- INTAKE ---
        boolean querColetar = gamepad1.left_bumper || gamepad2.left_bumper;
        boolean querEjetar = gamepad1.right_bumper || gamepad2.right_bumper;
        intake.atualizar(querColetar, querEjetar);

        // --- VISÃO E TORRETA ---
        double erroTX = limelight.getErroHorizontal(tagsAlvo);
        double distancia = limelight.getDistanciaEstimada(tagsAlvo);
        boolean alvoDetectado = limelight.temAlvo(tagsAlvo);

        // Mira Automática (Segue apenas as tags da aliança selecionada)
        if (gamepad1.left_trigger > 0 || gamepad2.left_trigger > 0) {
            if (alvoDetectado) {
                turret.mirarLimelight(erroTX);
            }
        } else {
            // Controle manual no Analógico Esquerdo do GP2
            turret.controleManual(gamepad2.left_stick_x * 0.6);
        }

        // --- SHOOTER ---
        boolean querAtirar = gamepad1.right_trigger > 0 || gamepad2.right_trigger > 0;
        shooter.calcularEAtualizarVelocidade(querAtirar, distancia);

        // Disparo: requer mira centralizada (erro < 5 graus)
        boolean miraBoa = Math.abs(erroTX) <= 5 || !alvoDetectado; // Se não houver alvo, permite manual
        boolean botaoDisparo = gamepad1.cross || gamepad1.square || gamepad2.square;
        shooter.gerenciarDisparo(botaoDisparo, bolasNoRobo, miraBoa);

        // --- TELEMETRIA ---
        telemetry.addData("Aliança", isBlueAlliance ? "AZUL" : "VERMELHA");
        telemetry.addData("Alvo Correto Visível", alvoDetectado ? "SIM" : "NÃO");
        
        if (alvoDetectado) {
            // Distância calculada via Limelight (ajuste a constante 1500 no subsistema se necessário)
            telemetry.addData("Distância ao Alvo (pol)", String.format(Locale.US, "%.1f", distancia));
            telemetry.addData("Erro Horizontal (TX)", String.format(Locale.US, "%.2f", erroTX));
        } else {
            telemetry.addData("Distância", "PROCURANDO TAGS " + (isBlueAlliance ? "11-13" : "14-16"));
        }

        telemetry.addData("Bolas no Intake", bolasNoRobo);
        telemetry.addData("Heading", String.format(Locale.US, "%.1f°", Math.toDegrees(follower.getPose().getHeading())));
        telemetry.update();
    }

    @Override
    public void stop() {
        limelight.parar();
    }
}
