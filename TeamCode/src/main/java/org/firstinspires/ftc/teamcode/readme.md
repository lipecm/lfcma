# 🤖 FTC Team Decode - Código Nacional (Arquitetura Modular)

Bem-vindo ao repositório de código do robô da equipe **Decode**. Este código foi reescrito e otimizado utilizando uma **Arquitetura Orientada a Subsistemas**.

O objetivo desta estrutura é separar o mapeamento de hardware da lógica de controle. Isso torna o código mais limpo, muito mais fácil de ler, rápido para calibrar durante competições e evita bugs onde um mecanismo interfere no outro.

---

## 🏗️ Estrutura do Código (Subsistemas)

Todo o trabalho pesado foi dividido em "caixas" independentes (subsistemas). O arquivo principal do TeleOp apenas lê os controles e envia as ordens para essas caixas.

* **`RobotHardware.java`**: É o mapa do robô. Centraliza o `hardwareMap` de todos os motores, servos e sensores.
* **`Constants.java`**: A sala de calibração. Guarda todas as variáveis importantes (RPM, posições de servo).
* **`DriveSubsystem.java`**: Responsável pela tração Mecanum (Robot-Centric).
* **`ShooterSubsystem.java`**: Controla o disparo e a cancela.
* **`TurretSubsystem.java`**: Controla o giro da base de tiro (Manual e PID).
* **`IntakeSubsystem.java`**: Coleta e ejeção de bolas.
* **`LimelightSubsystem.java`**: Visão computacional para mira automática.

---

## 🎮 Controles do Robô (Solo ou Dupla)

O código foi atualizado para que o **Gamepad 1** possa operar todas as funções do robô sozinho, mas o **Gamepad 2** ainda funciona como auxílio (Gunner).

### 🕹️ Mapeamento Centralizado (Gamepad 1 & 2)

| Botão / Analógico | Ação | Controle |
| :--- | :--- | :--- |
| **Analógico Esq (Y/X)** | Movimentação (Frente/Trás/Lado) | G1 |
| **Analógico Dir (X)** | Rotação do Robô | G1 |
| **L1 (Bumper Esq)** | **Coleta:** Puxa as bolas para dentro | G1 / G2 |
| **R1 (Bumper Dir)** | **Ejeção:** Cospe as bolas para fora | G1 / G2 |
| **L2 (Gatilho Esq)** | **Mira Automática:** Trava torreta no alvo | G1 / G2 |
| **R2 (Gatilho Dir)** | **Ligar Shooter:** Acelera as rodas de tiro | G1 / G2 |
| **Quadrado ou X (Cross)** | **Disparar:** Abre a cancela (Atira) | G1 / G2 |

> **⚠️ Nota de Segurança do Tiro:** O disparo possui uma trava de software. A bola só será disparada se: (1) O Shooter estiver ligado, (2) O RPM estiver estável, e (3) A mira estiver centralizada no alvo (margem de 5 graus).

---

## 🚀 Como instalar e usar

1. Compile o código para o Control Hub.
2. No Driver Hub, selecione o OpMode **"TeleOpLfcma"**.
3. Use o **Gamepad 1** para controlar todas as funções do robô.
