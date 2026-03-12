package org.firstinspires.ftc.teamcode.decode.subsystems;

public class IntakeSubsystem {
    private RobotHardware robot;

    public IntakeSubsystem(RobotHardware robot) {
        this.robot = robot;
    }

    /**
     * Define a ação do intake.
     * @param coletar Se verdadeiro, puxa as bolas.
     * @param ejetar Se verdadeiro, cospe as bolas.
     */
    public void atualizar(boolean coletar, boolean ejetar) {
        if (coletar) {
            robot.intake.setPower(1.0);
        } else if (ejetar) {
            robot.intake.setPower(-1.0);
        } else {
            robot.intake.setPower(0.0);
        }
    }
}