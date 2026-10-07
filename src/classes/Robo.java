package src.classes;

import java.util.ArrayList;
import java.util.List;

public class Robo {

    private int x;
    private int y;
    private String cor;
    private String nome;

    private int xAnterior;
    private int yAnterior;

    private int validos;
    private int invalidos;

    private boolean explodido;

    private List<String> posicoesVisitadas = new ArrayList<>();


    public Robo(String cor) {
        this(cor, cor);
    }


    public Robo(String cor, String nome) {

        this.cor = cor;
        this.nome = nome;

        this.x = 0;
        this.y = 0;

        this.xAnterior = 0;
        this.yAnterior = 0;

        posicoesVisitadas.add("0,0");
    }


    public int getX() {
        return x;
    }


    public void setX(int x) {
        this.x = x;
    }


    public int getY() {
        return y;
    }


    public void setY(int y) {
        this.y = y;
    }


    public String getCor() {
        return cor;
    }


    public String getNome() {
        return nome;
    }


    public int getValidos() {
        return validos;
    }


    public int getInvalidos() {
        return invalidos;
    }


    public boolean isExplodido() {
        return explodido;
    }


    public List<String> getPosicoesVisitadas() {
        return posicoesVisitadas;
    }


    public String getTipo() {
        return "robô normal";
    }


    public String getRotulo() {
        return Tabuleiro.colorir(nome, cor);
    }


    public void mover(String direcao) throws MovimentoInvalidoException {

        String d = (direcao == null) ? "" : direcao.trim().toLowerCase();


        int novoX = x;
        int novoY = y;


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

                throw new MovimentoInvalidoException(getRotulo() + ": Movimento inválido: comando desconhecido '" + direcao + "'");
        }


        if (!Tabuleiro.posicaoValida(novoX, novoY)) {

            invalidos++;

            throw new MovimentoInvalidoException(getRotulo() + ": Movimento inválido: '" + nomeDirecao(d) + "' levaria para (" + novoX + "," + novoY + "), fora da área");
        }


        xAnterior = x;
        yAnterior = y;

        x = novoX;
        y = novoY;

        validos++;

        posicoesVisitadas.add(x + "," + y);


        System.out.println(getRotulo() + " está em (" + x + "," + y + ")");
    }


    private static String nomeDirecao(String comando) {

        switch (comando) {

            case "up":
                return "cima";

            case "down":
                return "abaixo";

            case "right":
                return "direita";

            case "left":
                return "esquerda";

            default:
                return comando;
        }
    }


    public void mover(int direcao) throws MovimentoInvalidoException {

        switch (direcao) {

            case 1:
                mover("up");
                break;

            case 2:
                mover("down");
                break;

            case 3:
                mover("right");
                break;

            case 4:
                mover("left");
                break;

            default:

                throw new MovimentoInvalidoException(getRotulo() + ": Movimento inválido: direção inexistente " + direcao + " (use 1 a 4)");
        }
    }


    public boolean encontrouAlimento(int alimentoX, int alimentoY) {

        return x == alimentoX && y == alimentoY;
    }


    public void explodir() {

        explodido = true;

        System.out.println(getRotulo() + " explodiu em (" + x + "," + y + ")!");
    }


    public void voltarPosicaoAnterior() {

        x = xAnterior;
        y = yAnterior;

        System.out.println(getRotulo() + " voltou para (" + x + "," + y + ")");
    }


    // Usado pelo robô inteligente para fazer backtracking
    public void definirPosicao(int x, int y) {

        this.x = x;
        this.y = y;
    }
}