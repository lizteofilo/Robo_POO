package src.classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RoboInteligente extends Robo {

    private static final String[] DIRECOES = {
            "up",
            "down",
            "right",
            "left"
    };

    private Robo roboNormal;


    public RoboInteligente(
            String cor,
            Robo roboNormal) {

        super(cor);

        this.roboNormal = roboNormal;
    }


    public RoboInteligente(
            String cor,
            String nome,
            Robo roboNormal) {

        super(cor, nome);

        this.roboNormal = roboNormal;
    }


    @Override
    public String getTipo() {
        return "robô inteligente";
    }


    @Override
    public void mover(String direcao)
            throws MovimentoInvalidoException {

        // Primeiro tenta a direção que recebeu
        if (podeIr(direcao)) {

            super.mover(direcao);

            return;
        }


        // Cria as outras direções
        List<String> candidatas =
                new ArrayList<>(
                        Arrays.asList(DIRECOES)
                );


        // Remove a direção que já foi tentada
        candidatas.remove(
                direcao == null
                        ? ""
                        : direcao.trim().toLowerCase()
        );


        // Mistura as direções
        Collections.shuffle(candidatas);


        // Tenta encontrar uma casa nova
        for (String outra : candidatas) {

            if (podeIr(outra)) {

                System.out.println(
                        getRotulo()
                                + " mudou para "
                                + outra
                );

                super.mover(outra);

                return;
            }
        }


        /*
         * Se chegou aqui, nenhuma das direções
         * disponíveis leva para uma casa nova.
         *
         * Então volta para a posição anterior.
         */

        System.out.println(
                getRotulo()
                        + " não encontrou nenhuma direção nova."
        );


        voltarPosicaoAnterior();


        /*
         * Depois de voltar, tenta novamente
         * encontrar uma direção diferente.
         */

        candidatas =
                new ArrayList<>(
                        Arrays.asList(DIRECOES)
                );

        Collections.shuffle(candidatas);


        for (String outra : candidatas) {

            if (podeIr(outra)) {

                System.out.println(
                        getRotulo()
                                + " voltou e escolheu "
                                + outra
                );

                super.mover(outra);

                return;
            }
        }


        // Se mesmo depois de voltar não encontrou
        // nenhuma direção, realmente está preso.
        throw new MovimentoInvalidoException(
                getRotulo()
                        + ": não encontrei nenhuma direção possível."
        );
    }


    private boolean podeIr(String direcao) {

        if (direcao == null)
            return false;


        String d =
                direcao.trim().toLowerCase();


        int novoX = getX();
        int novoY = getY();


        switch (d) {

            case "up":
                novoY++;
                break;

            case "down":
                novoY--;
                break;

            case "right":
                novoX++;
                break;

            case "left":
                novoX--;
                break;

            default:
                return false;
        }


        // Está fora do tabuleiro?
        if (!Tabuleiro.posicaoValida(novoX, novoY)) {
            return false;
        }


        String novaPosicao =
                novoX + "," + novoY;


        // O inteligente já passou por essa posição?
        if (getPosicoesVisitadas()
                .contains(novaPosicao)) {

            return false;
        }


        // O normal já passou por essa posição?
        if (roboNormal != null
                && roboNormal
                .getPosicoesVisitadas()
                .contains(novaPosicao)) {

            return false;
        }


        return true;
    }
}