package org.firstinspires.ftc.teamcode.decode.subsystems;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

public class RobotHardware {
    // Motores da Base (Tração)
    public DcMotor frontalEsquerdo, frontalDireito, traseiroEsquerdo, traseiroDireito;

    // Mecanismos
    public DcMotorEx shooter1, shooter2;
    public DcMotor intake, turret;

    // Servos e Sensores
    public Servo cancela/*, led*/;
    public AnalogInput sensorBola1, sensorBola2, sensorBola3;

    public void init(HardwareMap hwMap) {
        // --- MAPEAMENTO DA TRAÇÃO ---
        // Ajustado para o padrão comum: d0=FL, d1=FR, d2=RL, d3=RR
        frontalEsquerdo = hwMap.get(DcMotor.class, "d0");
        frontalDireito = hwMap.get(DcMotor.class, "d1");
        traseiroEsquerdo = hwMap.get(DcMotor.class, "d2");
        traseiroDireito = hwMap.get(DcMotor.class, "d3");

        // Inverter os motores do lado direito para que potência positiva mova o robô para frente
        frontalDireito.setDirection(DcMotor.Direction.REVERSE);
        traseiroDireito.setDirection(DcMotor.Direction.REVERSE);

        // Fazer os motores frearem imediatamente ao soltar o analógico do controle
        frontalEsquerdo.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontalDireito.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        traseiroEsquerdo.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        traseiroDireito.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // --- MAPEAMENTO DO SHOOTER ---
        shooter1 = hwMap.get(DcMotorEx.class, "s2");
        shooter2 = hwMap.get(DcMotorEx.class, "s3");

        // Mantendo os seus coeficientes PIDF para controle de velocidade precisa
        PIDFCoefficients pidf = new PIDFCoefficients(1000, 0, 0, 20);
        shooter1.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidf);
        shooter2.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidf);

        shooter1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooter2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // --- OUTROS MECANISMOS E SENSORES ---
        intake = hwMap.get(DcMotor.class, "i1");
        turret = hwMap.get(DcMotor.class, "t0");
        cancela = hwMap.get(Servo.class, "cancela");
        // led = hwMap.get(Servo.class, "led");

        sensorBola1 = hwMap.get(AnalogInput.class, "sf");
        sensorBola2 = hwMap.get(AnalogInput.class, "sm");
        sensorBola3 = hwMap.get(AnalogInput.class, "st");
    }
}
