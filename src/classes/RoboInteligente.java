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

    // Guarda o caminho atual do robô
    private List<String> caminho =
            new ArrayList<>();


    public RoboInteligente(
            String cor,
            Robo roboNormal) {

        super(cor);

        this.roboNormal = roboNormal;

        caminho.add("0,0");
    }


    public RoboInteligente(
            String cor,
            String nome,
            Robo roboNormal) {

        super(cor, nome);

        this.roboNormal = roboNormal;

        caminho.add("0,0");
    }


    @Override
    public String getTipo() {
        return "robô inteligente";
    }


    @Override
    public void mover(String direcao)
            throws MovimentoInvalidoException {

        /*
         * Primeiro tenta a direção recebida.
         */

        if (podeExplorar(direcao)) {

            super.mover(direcao);

            adicionarAoCaminho();

            return;
        }


        /*
         * Se a direção não funcionar,
         * tenta as outras três.
         */

        List<String> candidatas =
                new ArrayList<>(
                        Arrays.asList(DIRECOES)
                );


        String d = direcao == null
                ? ""
                : direcao.trim().toLowerCase();


        candidatas.remove(d);

        Collections.shuffle(candidatas);


        for (String outra : candidatas) {

            if (podeExplorar(outra)) {

                System.out.println(
                        getRotulo()
                                + " escolheu "
                                + outra
                );

                super.mover(outra);

                adicionarAoCaminho();

                return;
            }
        }


        /*
         * Nenhuma direção nova.
         *
         * Agora estamos em um beco sem saída.
         *
         * Faz backtracking.
         */

        System.out.println(
                getRotulo()
                        + " chegou a um beco sem saída."
        );


        voltarNoCaminho();


        /*
         * Depois de voltar, procura uma
         * nova direção.
         */

        List<String> novas =
                new ArrayList<>(
                        Arrays.asList(DIRECOES)
                );


        Collections.shuffle(novas);


        for (String outra : novas) {

            if (podeExplorar(outra)) {

                System.out.println(
                        getRotulo()
                                + " encontrou uma nova direção: "
                                + outra
                );

                super.mover(outra);

                adicionarAoCaminho();

                return;
            }
        }


        /*
         * Se ainda não encontrou,
         * continua voltando pelo caminho.
         */

        moverDepoisDoBacktracking();
    }


    private void moverDepoisDoBacktracking()
            throws MovimentoInvalidoException {

        /*
         * Enquanto houver posições anteriores
         * no caminho, continua voltando.
         */

        while (caminho.size() > 1) {

            voltarNoCaminho();


            List<String> candidatas =
                    new ArrayList<>(
                            Arrays.asList(DIRECOES)
                    );


            Collections.shuffle(candidatas);


            for (String direcao : candidatas) {

                if (podeExplorar(direcao)) {

                    System.out.println(
                            getRotulo()
                                    + " voltou e encontrou "
                                    + direcao
                    );

                    super.mover(direcao);

                    adicionarAoCaminho();

                    return;
                }
            }
        }


        throw new MovimentoInvalidoException(
                getRotulo()
                        + ": não existem mais caminhos disponíveis."
        );
    }


    /*
     * Verifica se uma direção leva para uma
     * posição que ainda não foi explorada.
     */

    private boolean podeExplorar(String direcao) {

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


        /*
         * Está fora do tabuleiro?
         */

        if (!Tabuleiro.posicaoValida(
                novoX,
                novoY)) {

            return false;
        }


        String novaPosicao =
                novoX + "," + novoY;


        /*
         * O inteligente já explorou essa posição?
         */

        if (getPosicoesVisitadas()
                .contains(novaPosicao)) {

            return false;
        }


        /*
         * O robô normal já passou por essa posição?
         */

        if (roboNormal != null
                && roboNormal
                .getPosicoesVisitadas()
                .contains(novaPosicao)) {

            return false;
        }


        return true;
    }


    /*
     * Adiciona a posição atual ao caminho.
     */

    private void adicionarAoCaminho() {

        caminho.add(
                getX() + "," + getY()
        );
    }


    /*
     * Volta uma posição no caminho.
     *
     * IMPORTANTE:
     * voltar não significa apagar a posição
     * do histórico de visitadas.
     *
     * Ele apenas volta fisicamente para
     * procurar outro caminho.
     */

    private void voltarNoCaminho() {

        if (caminho.size() <= 1)
            return;


        // Remove a posição atual
        caminho.remove(
                caminho.size() - 1
        );


        // Pega a posição anterior
        String anterior =
                caminho.get(
                        caminho.size() - 1
                );


        String[] partes =
                anterior.split(",");


        int novoX =
                Integer.parseInt(partes[0]);


        int novoY =
                Integer.parseInt(partes[1]);


        definirPosicao(
                novoX,
                novoY
        );


        System.out.println(
                getRotulo()
                        + " voltou para ("
                        + novoX
                        + ","
                        + novoY
                        + ")"
        );
    }
}