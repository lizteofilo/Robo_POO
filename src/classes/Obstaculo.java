package src.classes;

public abstract class Obstaculo {

    protected int id;

    public Obstaculo(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public abstract void bater(Robo robo);
}