package Organisms.Plants.Species;
import Organisms.Plants.Plant;
import Organisms.Organism;
import Organisms.Animals.Animal;

public class Hogweed extends Plant{
    public Hogweed(int row, int col) {
        super(row, col, 10, '!');
    }

    @Override
    public void getEaten(Organism attacker) {
        world.getGameUI().addLog(attacker.getSymbol() + " ate " + getSymbol() + " and died :(");
        world.removeOrganism(attacker);
        world.removeOrganism(this);
    }
    private void killNeighbours() {
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // N, S, W, E
        for (int[] direction : directions) {
            int newRow = row + direction[0];
            int newCol = column + direction[1];
            if (newRow >= 0 && newRow < world.getHeight() && newCol >= 0 && newCol < world.getWidth()) {
                Organism neighbour = world.getOrganism(newRow, newCol);
                if (neighbour instanceof Animal) {
                    world.getGameUI().addLog(neighbour.getSymbol() + " got too close to Hogweed (!) and died :( ");
                    world.removeOrganism(neighbour);
                }
            }
        }
    }
    @Override
    public void action(){
        killNeighbours();
        super.action();
    }

}
