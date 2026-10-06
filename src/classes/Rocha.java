package src.classes;


public class Rocha extends Obstaculo {

    public Rocha(int id) {
        super(id);
    }

    @Override
    public void bater(Robo robo) {
        System.out.println(robo.getRotulo() + " bateu na rocha " + id + ".");
        robo.voltarPosicaoAnterior();
    }
}