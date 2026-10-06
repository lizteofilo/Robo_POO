package src.main;

import src.classes.Tabuleiro;
import src.classes.Robo;
import src.classes.RoboInteligente;
import src.classes.MovimentoInvalidoException;
import java.util.Random;
import java.util.Scanner;

public class Main3 {

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
       // Robo[] robos = { new Robo("azul", "r1"), new RoboInteligente("verde", "r2") };
        Robo roboNormal = new Robo("azul", "r1");

        Robo roboInteligente =
                new RoboInteligente("verde", "r2", roboNormal);

        Robo[] robos = {
                roboNormal,
                roboInteligente
        };
        boolean[] achou = new boolean[2];

        System.out.println("r1 = robô normal | r2 = robô inteligente");
        tabuleiro.imprimir(robos);
        tabuleiro.imprimirSituacao(robos);

        boolean encerrou = false;
        int rodada = 0;

        while (!terminou(achou) && !encerrou) {
            rodada++;
            System.out.println("--- Rodada " + rodada + " ---");

            for (int i = 0; i < robos.length; i++) {
                if (achou[i]) continue; 

                try {
                    robos[i].mover(random.nextInt(4) + 1);
                } catch (MovimentoInvalidoException e) {
                    System.out.println(e.getMessage());
                }

                tabuleiro.imprimir(robos);   
                Tabuleiro.pausar();          

                if (robos[i].encontrouAlimento(ax, ay)) {
                    achou[i] = true;
                    System.out.println(robos[i].getRotulo() + " encontrou o alimento!");
                }
            }

            tabuleiro.imprimirSituacao(robos);
            //If do continuar e parar o jogo
            /*if (!terminou(achou)) {
                encerrou = !continuar(sc);
            }*/
        }

        System.out.println("=== Resultado ===");
        if (encerrou) {
            System.out.println("Encerrado pelo usuário antes de o alimento ser encontrado.");
        } else {
            System.out.println("Fim da partida: ambos os robôs encontraram o alimento!");
        }
        for (int i = 0; i < robos.length; i++) {
            Robo r = robos[i];
            System.out.println(r.getRotulo() + (achou[i] ? " (achou)" : " (não achou)") + ": "
                + r.getValidos() + " movimentos válidos e " + r.getInvalidos() + " inválidos"
                + " (total de tentativas: " + (r.getValidos() + r.getInvalidos()) + ")");
        }
        sc.close();
    }

    private static boolean terminou(boolean[] achou) {
        
        return achou[0] && achou[1];
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