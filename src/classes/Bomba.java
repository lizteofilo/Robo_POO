package src.classes;

public class Bomba extends Obstaculo {

    public Bomba(int id) {
        super(id);
    }

    @Override
    public void bater(Robo robo) {
        System.out.println(robo.getRotulo() + " pisou na bomba " + id + "!");
        robo.explodir();
    }
}