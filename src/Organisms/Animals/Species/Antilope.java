package Organisms.Animals.Species;

import java.util.*;

import Organisms.Animals.Animal;
public class Antilope extends Animal {
    private Random rand;

    public Antilope(int row, int col) {
        super(row,col,4,4,'A');
        this.rand = new Random();
    }

    @Override
    public void action() {
        increaseAge();

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1},{-2,0},{2,0},{0,-2},{0,2}}; // n s w e N S W E

        // Convert the array to a list and shuffle it
        List<int[]> shuffledDirections = Arrays.asList(directions);
        Collections.shuffle(shuffledDirections);

        for (int[] direction : shuffledDirections) {
            int finalRow = getRow() + direction[0];
            int finalColumn = getColumn() + direction[1];


            if (finalColumn < 0 || finalColumn >= world.getWidth() || finalRow < 0 || finalRow >= world.getHeight()) // check if outside borders
            {
                continue;
            }
            performAction(finalRow, finalColumn);
            return;

        }
    };

    @Override
    public void getCollided(Animal attacker, int row, int col) {
        // 50% chance to escape
        if (rand.nextBoolean()) {
            int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // N, S, W, E

            // Convert the array to a list and shuffle it
            List<int[]> shuffledDirections = Arrays.asList(directions);
            Collections.shuffle(shuffledDirections);

            for (int[] direction : shuffledDirections) {
                int finalRow = getRow() + direction[0];
                int finalColumn = getColumn() + direction[1];

                if (finalRow < 0 || finalRow >= world.getWidth() || finalColumn < 0 || finalColumn >= world.getHeight()) // check if outside borders
                {
                    continue;
                }
                // If the tile is free, move the Antilope there and return
                if (world.getOrganism(finalRow, finalColumn) == null) {
                    world.moveOrganism(this, finalRow, finalColumn);
                    world.moveOrganism(attacker, row, col);
                    world.getGameUI().addLog(attacker.getSymbol() + " tried attacking A. It escaped!");
                    return;
                }
            }
        }

        // If the Antilope couldn't escape, call the parent class's getCollided method
        super.getCollided(attacker, row, col);
    }
}