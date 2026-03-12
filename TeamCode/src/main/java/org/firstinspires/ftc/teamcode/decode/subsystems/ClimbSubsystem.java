//package org.firstinspires.ftc.teamcode.decode.subsystems;
//
//import com.qualcomm.robotcore.hardware.ServoController;
//
///**
// * Subsistema para gerenciar os ganchos de escalada e seus limites.
// */
//public class ClimbSubsystem {
//    private RobotHardware robot;
//    private int estadoEscalada = 0; // 0 = Baixo, 1 = Subindo, 2 = Travado
//
//    public ClimbSubsystem(RobotHardware robot) {
//        this.robot = robot;
//    }
//
//    /**
//     * Atualiza a lógica de escalada com base nos botões apertados.
//     */
//    public void atualizar(boolean botaoTriangulo, boolean botaoX) {
//        // Lógica de estado adaptada do seu código original
//        if (botaoTriangulo) {
//            estadoEscalada = 1; // Modo manual/preparação
//        }
//
//        // Se apertar X e o sensor de limite encostar, trava na posição final
//        if (botaoX && (robot.magnetic1.isPressed() || robot.magnetic2.isPressed())) {
//            estadoEscalada = 2; // Travado na barra
//        }
//
//        // Executa as ações baseadas no estado
//        if (estadoEscalada == 2) {
//            robot.escalada2.setPosition(0.5);
//            robot.escalada1.setPosition(0.65);
//        } else if (estadoEscalada == 1) {
//            if (botaoX) {
//                robot.escalada1.setPosition(1);
//                robot.escalada2.setPosition(0);
//            } else if (botaoTriangulo) {
//                robot.escalada1.setPosition(0);
//                robot.escalada2.setPosition(1);
//            }
//        } else {
//            // Posição de repouso (estadoEscalada == 0)
//            robot.escalada1.setPosition(0.63);
//        }
//    }
//}