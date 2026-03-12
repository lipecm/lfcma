//package org.firstinspires.ftc.teamcode.decode.nacional;
//import com.pedropathing.follower.Follower;
//import com.pedropathing.geometry.Pose;
//import com.qualcomm.hardware.limelightvision.LLResult;
//import com.qualcomm.hardware.limelightvision.Limelight3A;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.robotcore.hardware.*;
//import com.qualcomm.robotcore.util.ElapsedTime;
//import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
//import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
//public abstract class TeleopNacional extends OpMode {
//    code
//            Code
//    protected Pose startingPose;
//
//    protected double xTarget;
//    protected double yTarget = 130;
//
//    protected final ElapsedTime timer = new ElapsedTime();
//    protected final ElapsedTime limetimer = new ElapsedTime();
//    protected final ElapsedTime tempo = new ElapsedTime();
//    protected final ElapsedTime dt = new ElapsedTime();
//
//    protected double dX = 0;
//    protected double dY = 0;
//    protected double startChute;
//    protected double razaoChute = 10.2;
//
//    protected int driveFlip = 1;
//
//    protected Limelight3A lime;
//    protected Follower follower;
//
//    protected DcMotor frentedireita;
//    protected DcMotor frenteesquerda;
//    protected DcMotor trasdireita;
//    protected DcMotor trasesquerda;
//
//    protected DcMotorEx shooter1;
//    protected DcMotorEx shooter2;
//
//    protected DcMotor intake;
//    protected DcMotor turret;
//
//    protected Servo cancela;
//    protected Servo led;
//
//    protected AnalogInput sf;
//    protected AnalogInput sm;
//    protected AnalogInput st;
//
//    protected int count = 0;
//    protected double presenca = 0.35;
//
//    protected double dist;
//
//    protected double erro;
//    protected double ang;
//
//    protected double kp = -1;
//    protected double ki = -0.0005;
//    protected double kd = -1.5;
//
//    protected double ultimoerro = 0;
//    protected double ultimaintegral = 0;
//
//    protected double poseX, poseY;
//    protected double lastX, lastY;
//
//    protected double vX, vY;
//
//    protected double futurePoseX, futurePoseY;
//
//    protected boolean futurePose = true;
//
//    protected double movingRatio = 0.9;
//
//    protected int margem = 125;
//
//    protected double curTargetVelocity = 0;
//
//    protected boolean atirando = false;
//
//    @Override
//    public void init() {
//
//        frentedireita = hardwareMap.get(DcMotor.class, "d1");
//        frenteesquerda = hardwareMap.get(DcMotor.class, "d0");
//        trasdireita = hardwareMap.get(DcMotor.class, "d2");
//        trasesquerda = hardwareMap.get(DcMotor.class, "d3");
//
//        shooter1 = hardwareMap.get(DcMotorEx.class, "s2");
//        shooter2 = hardwareMap.get(DcMotorEx.class, "s3");
//
//        intake = hardwareMap.get(DcMotor.class, "i1");
//        turret = hardwareMap.get(DcMotor.class, "t0");
//
//        cancela = hardwareMap.get(Servo.class, "cancela");
//        led = hardwareMap.get(Servo.class, "led");
//
//        sf = hardwareMap.get(AnalogInput.class,"sf");
//        sm = hardwareMap.get(AnalogInput.class,"sm");
//        st = hardwareMap.get(AnalogInput.class,"st");
//
//        lime = hardwareMap.get(Limelight3A.class,"limelight");
//
//        lime.pipelineSwitch(0);
//
//        PIDFCoefficients pidf = new PIDFCoefficients(1000,0,0,20);
//
//        shooter1.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER,pidf);
//        shooter2.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER,pidf);
//
//        follower = Constants.createFollower(hardwareMap);
//
//        follower.setStartingPose(startingPose);
//
//        dt.reset();
//    }
//
//    @Override
//    public void start() {
//        lime.start();
//        cancela.setPosition(0.25);
//    }
//
//    @Override
//    public void loop() {
//
//        contBola();
//        pedro();
//        mecanum();
//        shooter();
//        mira();
//        chutar();
//        intake();
//        led();
//    }
//    protected void mira() {
//    }
//
//    private void shooter() {
//
//        double curVelocity = shooter1.getVelocity();
//
//        erro = curTargetVelocity - curVelocity;
//
//        boolean shooter = gamepad2.right_trigger > 0;
//
//        if (shooter) {
//
//            curTargetVelocity = startChute + (razaoChute * dist);
//
//        } else {
//
//            curTargetVelocity = 1300;
//        }
//
//        shooter1.setVelocity(curTargetVelocity);
//
//        shooter2.setPower(curTargetVelocity / 2800);
//    }
//
//    private void chutar() {
//
//        boolean rpmOk = Math.abs(erro) <= margem;
//
//        if (gamepad2.square && rpmOk && !atirando && count > 0) {
//            atirando = true;
//        }
//
//        if (atirando) {
//
//            cancela.setPosition(0.19);
//
//            if (count == 0) {
//
//                cancela.setPosition(0.25);
//
//                atirando = false;
//            }
//
//        } else {
//
//            cancela.setPosition(0.25);
//        }
//    }
//
//    private void mecanum() {
//
//        double ly = Math.pow(gamepad1.right_stick_y,2) * Math.signum(gamepad1.right_stick_y);
//        double lx = Math.pow(gamepad1.right_stick_x,2) * Math.signum(gamepad1.right_stick_x);
//
//        double xlinha = lx * Math.cos(follower.getPose().getHeading())
//                - ly * Math.sin(follower.getPose().getHeading());
//
//        double ylinha = lx * Math.sin(follower.getPose().getHeading())
//                + ly * Math.cos(follower.getPose().getHeading());
//
//        frentedireita.setPower(driveFlip * (-ylinha - xlinha - gamepad1.left_stick_x));
//        frenteesquerda.setPower(driveFlip * (-ylinha + xlinha + gamepad1.left_stick_x));
//        trasesquerda.setPower(driveFlip * (-ylinha - xlinha + gamepad1.left_stick_x));
//        trasdireita.setPower(driveFlip * (-ylinha + xlinha - gamepad1.left_stick_x));
//    }
//
//    private void contBola() {
//
//        count = 0;
//
//        if (sf.getVoltage() < presenca) count++;
//        if (sm.getVoltage() < presenca) count++;
//        if (st.getVoltage() < presenca) count++;
//    }
//
//    private void pedro() {
//        follower.update();
//    }
//
//    private void intake() {
//
//        if (gamepad2.left_bumper || gamepad1.left_bumper || gamepad2.square) {
//
//            intake.setPower(1);
//
//        } else if (gamepad2.right_bumper || gamepad1.right_bumper) {
//
//            intake.setPower(-1);
//
//        } else {
//
//            intake.setPower(0);
//        }
//    }
//
//    protected double pid(double erro) {
//
//        double proporcional = erro * kp;
//        double derivativo = (erro - ultimoerro) * kd;
//        double integrativo = (erro * ki) + ultimaintegral;
//
//        ultimoerro = erro;
//        ultimaintegral = integrativo;
//
//        return proporcional + integrativo + derivativo;
//    }
//
//    private void led() {
//
//        if (count == 3) {
//
//            led.setPosition(0.388);
//
//        } else {
//
//            led.setPosition(0.277);
//        }
//    }
//}