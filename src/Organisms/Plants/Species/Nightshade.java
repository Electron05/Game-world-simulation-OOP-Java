package Organisms.Plants.Species;
import Organisms.Organism;
import Organisms.Plants.Plant;

public class Nightshade extends Plant {
    public Nightshade(int row, int col) {
        super(row,col,99,'%');
    }
    @Override
    public void getEaten(Organism attacker) {
        world.getGameUI().addLog(attacker.getSymbol() + " ate " + getSymbol() + " and died :(");
        world.removeOrganism(attacker);
        world.removeOrganism(this);
    }
}
