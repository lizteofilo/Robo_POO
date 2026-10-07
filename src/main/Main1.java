package src.main;

import src.classes.Tabuleiro;
import src.classes.Robo;
import src.classes.MovimentoInvalidoException;

import java.util.Scanner;


public class Main1 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int ultimo = Tabuleiro.LADO - 1;

        System.out.println("Área de " + Tabuleiro.LADO + "x" + Tabuleiro.LADO
                + " (coordenadas de 0 a " + ultimo + "). O robô começa em (0,0).");
        int ax = lerInt(sc, "Posição x do alimento: ", 0, ultimo);
        int ay = lerInt(sc, "Posição y do alimento: ", 0, ultimo);
        while (ax == 0 && ay == 0) {
            System.out.println("O alimento não pode ficar na posição inicial (0,0).");
            ax = lerInt(sc, "Posição x do alimento: ", 0, ultimo);
            ay = lerInt(sc, "Posição y do alimento: ", 0, ultimo);
        }

        Tabuleiro tabuleiro = new Tabuleiro(ax, ay);
        tabuleiro.setMostrarAlimento(true); // o alimento fica escondido
        Robo r1 = new Robo("azul", "r1");

        System.out.println("\nAgora encontre o alimento!");
        tabuleiro.imprimir(r1);

        boolean desistiu = false;
        while (!r1.encontrouAlimento(ax, ay) && !desistiu) {
            System.out.println("1 = cima | 2 = baixo | 3 = direita | 4 = esquerda | 0 = desistir");
            int opcao = lerInt(sc, "Escolha o movimento: ", 0, 4);

            if (opcao == 0) {
                desistiu = true;
            } else {
                try {
                    r1.mover(opcao);
                } catch (MovimentoInvalidoException e) {
                    System.out.println(e.getMessage());
                }
                tabuleiro.imprimir(r1);
            }
        }

        System.out.println("=== Resultado ===");
        if (desistiu) {
            System.out.println("Você desistiu. O alimento estava em (" + ax + "," + ay + ").");
        } else {
            System.out.println(r1.getRotulo() + " encontrou o alimento em (" + ax + "," + ay + ")!");
        }
        System.out.println(r1.getRotulo() + ": " + r1.getValidos()
                + " movimentos válidos e " + r1.getInvalidos() + " inválidos");
        sc.close();
    }

    private static int lerInt(Scanner sc, String msg, int min, int max) {
        while (true) {
            System.out.print(msg);
            try {
                int v = Integer.parseInt(sc.next().trim());
                if (v >= min && v <= max) return v;
            } catch (NumberFormatException e) {

            }
            System.out.println("Valor inválido. Digite um inteiro entre " + min + " e " + max + ".");
        }
    }
}