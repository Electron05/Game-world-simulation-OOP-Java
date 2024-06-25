package Organisms.Animals.Species;
import Organisms.Animals.Animal;

import java.util.*;

public class Fox extends Animal {
    private Random rand;
    public Fox(int row, int col) {
        super(row, col, 3, 7, 'F');
        this.rand = new Random();
    }
    public void action() {
        increaseAge();

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // N, S, W, E
        Random rand = new Random();

        // Convert the array to a list and shuffle it
        List<int[]> shuffledDirections = Arrays.asList(directions);
        Collections.shuffle(shuffledDirections);

        for (int[] direction : shuffledDirections) {
            int finalRow = getRow() + direction[0];
            int finalColumn = getColumn() + direction[1];


            if (finalColumn < 0 || finalColumn >= world.getWidth() || finalRow < 0 || finalRow >= world.getHeight()
                || (world.getOrganism(finalRow, finalColumn) != null && world.getOrganism(finalRow, finalColumn).getPower()>getPower()))
            {
                continue;
            }
            performAction(finalRow, finalColumn);
            return;

        }
    };
}
