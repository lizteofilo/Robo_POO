package src.classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class RoboInteligente extends Robo {

    private static final String[] DIRECOES = { "up", "down", "right", "left" };
    public RoboInteligente(String cor) {
        super(cor);
    }
    public RoboInteligente(String cor, String nome) {
        super(cor, nome);
    }

    @Override
    public String getTipo() {
        return "robô inteligente";
    }

    @Override
    public void mover(String direcao) throws MovimentoInvalidoException {
        try {
            super.mover(direcao);
        } catch (MovimentoInvalidoException e) {
            System.out.println(e.getMessage());

            List<String> candidatas = new ArrayList<>(Arrays.asList(DIRECOES));
            String falhou = (direcao == null) ? "" : direcao.trim().toLowerCase();
            if (!candidatas.remove(falhou)) {
                throw e; 
            }
            Collections.shuffle(candidatas);

            for (String outra : candidatas) {
                try {
                    super.mover(outra);
                    return; 
                } catch (MovimentoInvalidoException e2) {
                    System.out.println(e2.getMessage());
                }
            }
            throw e; 
        }
    }
}