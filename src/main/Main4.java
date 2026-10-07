package src.main;

import src.classes.Bomba;
import src.classes.Obstaculo;
import src.classes.Rocha;
import src.classes.Robo;
import src.classes.RoboInteligente;
import src.classes.Tabuleiro;
import src.classes.MovimentoInvalidoException;

import java.util.Random;
import java.util.Scanner;

public class Main4 {

    private static final int MAX_RODADAS = 1000;
    private static final int MAX_BOMBAS = 5;
    private static final int MAX_ROCHAS = 5;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random random = new Random();
        int lado = Tabuleiro.LADO;
        int ultimo = lado - 1;

        System.out.println("Área de " + lado + "x" + lado + ". Os robôs começam em (0,0).");
        int ax = lerInt(sc, "Posição x do alimento: ", 0, ultimo);
        int ay = lerInt(sc, "Posição y do alimento: ", 0, ultimo);
        while (ax == 0 && ay == 0) {
            System.out.println("O alimento não pode ficar na posição inicial (0,0).");
            ax = lerInt(sc, "Posição x do alimento: ", 0, ultimo);
            ay = lerInt(sc, "Posição y do alimento: ", 0, ultimo);
        }

        Tabuleiro tabuleiro = new Tabuleiro(ax, ay);
        int livres = lado * lado - 2;
        int idObstaculo = 1;

        int maxBombas = Math.min(MAX_BOMBAS, livres);
        int bombas = lerInt(sc, "Quantas bombas? (0 a " + maxBombas + "): ", 0, maxBombas);
        for (int i = 0; i < bombas; i++) {
            inserir(sc, tabuleiro, new Bomba(idObstaculo++), "bomba");
        }
        int maxRochas = Math.min(MAX_ROCHAS, livres - bombas);
        int rochas = lerInt(sc, "Quantas rochas? (0 a " + maxRochas + "): ", 0, maxRochas);
        for (int i = 0; i < rochas; i++) {
            inserir(sc, tabuleiro, new Rocha(idObstaculo++), "rocha");
        }

        //Robo[] robos = { new Robo("azul", "r1"), new RoboInteligente("verde", "r2") };
        Robo roboNormal = new Robo("azul", "r1");

        Robo roboInteligente =
                new RoboInteligente("verde", "r2", roboNormal);

        Robo[] robos = {
                roboNormal,
                roboInteligente
        };
        System.out.println("\nr1 = robô normal | r2 = robô inteligente");
        tabuleiro.imprimir(robos);
        tabuleiro.imprimirSituacao(robos);

        Robo vencedor = null;
        boolean encerrou = false;
        int rodada = 0;

        while (vencedor == null && !encerrou && rodada < MAX_RODADAS
                && (!robos[0].isExplodido() || !robos[1].isExplodido())) {

            rodada++;
            System.out.println("--- Rodada " + rodada + " ---");

            for (Robo atual : robos) {
                if (atual.isExplodido()) continue;

                try {
                    atual.mover(random.nextInt(4) + 1);
                } catch (MovimentoInvalidoException e) {
                    System.out.println(e.getMessage());
                }

                tabuleiro.verificarObstaculo(atual);

                tabuleiro.imprimir(robos);
                Tabuleiro.pausar();

                if (!atual.isExplodido() && atual.encontrouAlimento(ax, ay)) {
                    vencedor = atual;
                    break;
                }
            }

            tabuleiro.imprimirSituacao(robos);

            boolean fimDeJogo = vencedor != null
                    || (robos[0].isExplodido() && robos[1].isExplodido());
            //If do continuar e parar o jogo
            /*if (!fimDeJogo) {
                encerrou = !continuar(sc);
            }*/
        }

        System.out.println("=== Resultado ===");
        if (vencedor != null) {
            System.out.println(vencedor.getRotulo() + " encontrou o alimento!");
        } else if (robos[0].isExplodido() && robos[1].isExplodido()) {
            System.out.println("Os dois robôs explodiram.");
        } else if (encerrou) {
            System.out.println("Encerrado pelo usuário.");
        } else {
            System.out.println("Limite de " + MAX_RODADAS + " rodadas atingido: o alimento pode estar cercado.");
        }
        for (Robo r : robos) {
            String situacao = r.isExplodido() ? "explodiu"
                    : (r.encontrouAlimento(ax, ay) ? "encontrou o alimento" : "não chegou ao alimento");
            System.out.println(r.getRotulo() + " (" + situacao + "): "
                    + r.getValidos() + " movimentos válidos e " + r.getInvalidos() + " inválidos"
                    + " (total de tentativas: " + (r.getValidos() + r.getInvalidos()) + ")");
        }
        sc.close();
    }

    private static void inserir(Scanner sc, Tabuleiro tabuleiro, Obstaculo o, String nome) {
        int ultimo = Tabuleiro.LADO - 1;
        while (true) {
            System.out.println("Posição da " + nome + " " + o.getId() + ":");
            int x = lerInt(sc, "  x: ", 0, ultimo);
            int y = lerInt(sc, "  y: ", 0, ultimo);
            if (tabuleiro.inserirObstaculo(o, x, y)) return;
            System.out.println("  Posição ocupada, igual à do alimento ou à inicial (0,0). Tente outra.");
        }
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

            }
            System.out.println("Valor inválido. Digite um inteiro entre " + min + " e " + max + ".");
        }
    }
}