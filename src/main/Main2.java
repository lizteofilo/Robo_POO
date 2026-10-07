package src.main;

import src.classes.MovimentoInvalidoException;
import src.classes.Robo;
import src.classes.Tabuleiro;

import java.util.Random;
import java.util.Scanner;

public class Main2 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random random = new Random();
        int ultimo = Tabuleiro.LADO - 1;

        System.out.println("Área de " + Tabuleiro.LADO + "x" + Tabuleiro.LADO
                + ". Os robôs começam em (0,0).");
        int ax = lerInt(sc, "Posição x do alimento: ", 0, ultimo);
        int ay = lerInt(sc, "Posição y do alimento: ", 0, ultimo);
        while (ax == 0 && ay == 0) {
            System.out.println("O alimento não pode ficar na posição inicial (0,0).");
            ax = lerInt(sc, "Posição x do alimento: ", 0, ultimo);
            ay = lerInt(sc, "Posição y do alimento: ", 0, ultimo);
        }

        Tabuleiro tabuleiro = new Tabuleiro(ax, ay);
        Robo[] robos = {new Robo("azul", "r1"), new Robo("verde", "r2")};

        tabuleiro.imprimir(robos);
        tabuleiro.imprimirSituacao(robos);

        Robo vencedor = null;
        boolean encerrou = false;
        int rodada = 0;

        while (vencedor == null && !encerrou) {
            rodada++;
            System.out.println("--- Rodada " + rodada + " ---");

            for (Robo atual : robos) {
                try {
                    atual.mover(random.nextInt(4) + 1);
                } catch (MovimentoInvalidoException e) {
                    System.out.println(e.getMessage());
                }

                tabuleiro.imprimir(robos);
                Tabuleiro.pausar();

                if (atual.encontrouAlimento(ax, ay)) {
                    vencedor = atual;
                    break;
                }
            }

            tabuleiro.imprimirSituacao(robos);

            //If do continuar e parar o jogo
            /*if (vencedor == null) {
                encerrou = !continuar(sc);
            }*/
        }

        System.out.println("=== Resultado ===");
        if (vencedor != null) {
            System.out.println("Quem achou o alimento: " + vencedor.getRotulo());
        } else {
            System.out.println("Encerrado pelo usuário: ninguém achou o alimento ainda.");
        }
        for (Robo r : robos) {
            System.out.println(r.getRotulo() + ": " + r.getValidos()
                    + " movimentos válidos e " + r.getInvalidos() + " inválidos");
        }
        sc.close();
    }
    // Metodo para escolher continuar ou parar o jogo

    /*private static boolean continuar(Scanner sc) {
        return lerInt(sc, "1 = continuar | 0 = encerrar: ", 0, 1) == 1;
    }*/

    private static int lerInt(Scanner sc, String msg, int min, int max) {
        while (true) {
            System.out.print(msg);
            try {
                int v = Integer.parseInt(sc.next().trim());
                if (v >= min && v <= max) return v;
            } catch (NumberFormatException e) {
                // cai na mensagem abaixo
            }
            System.out.println("Valor inválido. Digite um inteiro entre " + min + " e " + max + ".");
        }
    }
}