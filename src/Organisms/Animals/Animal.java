package Organisms.Animals;
import Organisms.Organism;
import Organisms.Plants.Plant;

import java.util.List;
import java.util.Arrays;
import java.util.Collections;


public class Animal extends Organism {

    private final int turnsToBreed = 5;

    public Animal(int row, int col, int power, int initiative, char symbol) {
        super(row, col, power, initiative, symbol);
    }
    public void action() {
        increaseAge();

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // N, S, W, E

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
            if(performAction(finalRow, finalColumn) == 1)
                return;
        }
    };
    protected int performAction(int finalRow, int finalColumn) { // return 0 if can't breed, otherwise 1
        if(world.getOrganism(finalRow, finalColumn) == null) // if the tile is empty
        {
            world.moveOrganism(this, finalRow, finalColumn);
            return 1;
        }

        Organism target = world.getOrganism(finalRow, finalColumn);
        if(target instanceof Plant)
        {
            ((Plant) target).getEaten(this);
            if(world.getOrganism(row,column) != null)// If animal did not die
                world.moveOrganism(this, finalRow, finalColumn);
            return 1;
        }
        if (this.getClass().equals(target.getClass())) {
            if(turnsSinceBreeding >= turnsToBreed) {
                breed();
                return 1;
            }
            return 0;
        }
        ((Animal) target).getCollided(this, finalRow, finalColumn);
        return 1;
    }



    public void getCollided(Animal attacker,int row,int col) {
        if (attacker.getPower() >= getPower()) {
            world.removeOrganism(this);
            world.moveOrganism(attacker, row, col);
            world.getGameUI().addLog(attacker.getSymbol() + " attacked " + getSymbol() + " and won.");
        }
        else {
            world.getGameUI().addLog(attacker.getSymbol() + " attacked " + getSymbol() + " and lost.");
            world.removeOrganism(attacker);
            world.getGameUI().refreshCell(attacker.getRow(), attacker.getColumn());
        }
    }

    private void breed() {
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // N, S, W, E

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
            if(world.getOrganism(finalRow, finalColumn) != null) // if the tile is not empty
            {
                continue;
            }
            try {
                Animal newAnimal = this.getClass().getConstructor(int.class, int.class).newInstance(finalRow, finalColumn);
                world.getGameUI().addLog("New " + newAnimal.getSymbol() + " has been born at (" + finalRow + ", " + finalColumn + ")");
                turnsSinceBreeding = 0;
                return;
            } catch (Exception e) {
                System.out.println("Warning, could not instantiate animal during breeding");
            }
        }
        world.getGameUI().addLog("No place to breed for " + getSymbol());
    }

}