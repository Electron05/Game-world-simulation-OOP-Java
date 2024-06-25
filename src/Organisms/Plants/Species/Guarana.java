package Organisms.Plants.Species;
import Organisms.Organism;
import Organisms.Plants.Plant;

public class Guarana extends Plant{
    public Guarana(int row, int col) {
        super(row, col, 0, '&');
    }

    private final int powerIncrease = 3;

    @Override
    public void getEaten(Organism o) {
        o.setPower(o.getPower()+powerIncrease);
        world.getGameUI().addLog("& was eaten by " + o.getSymbol() + " and its power is now " + o.getPower());
        this.world.removeOrganism(this);
    }
}
