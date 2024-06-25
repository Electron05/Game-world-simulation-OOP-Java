package Organisms.Plants;
import Organisms.Organism;
import Organisms.Plants.Species.*;
import Organisms.Animals.Animal;

import java.util.Random;
import java.util.Collections;
import java.util.Arrays;
import java.util.List;

public class Plant extends Organism{


    protected int oneInHowManyChanceToGrow = 6;
    protected boolean noSpace;
    private Random rand;
    public Plant(int row, int col, int strength, char symbol) {
        super(row, col, strength, 0, symbol);
        this.rand = new Random();
        this.noSpace = false;
    }

    public void action() {
        increaseAge();
        if(noSpace)
            return;
        int randomNumber = rand.nextInt(oneInHowManyChanceToGrow+1); //0-6
        if(randomNumber == 0){
            tryToGrow();
        }
    }

    protected void tryToGrow() {
        System.out.println(this.getSymbol() + " tryToGrow");
        boolean maybeSpace = false;
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
            if (world.getOrganism(finalRow, finalColumn) == null) {
                growNew(finalRow, finalColumn, getSymbol());
                return;
            } else {
                //If there is a nearby animal, set that maybe in next turn there will be space to grow
                Organism organism = world.getOrganism(finalRow, finalColumn);
                if (organism instanceof Animal) {
                    maybeSpace = true;
                }
            }
        }
        if(!maybeSpace)
            noSpace = true;
    }

    private void growNew(int row, int col, char symbol) {
        switch (symbol) {
            case '#':
                new Grass(row, col);
                break;
            case '@':
                new Dandelion(row, col);
                break;
            case '&':
                new Guarana(row, col);
                break;
            case '%':
                new Nightshade(row, col);
                break;
            case '!':
                new Hogweed(row, col);
                break;
        }
    }

    public void getEaten(Organism o){
        sendSignalMaybeSpace();
        world.getGameUI().addLog(getSymbol() + " was eaten by " + o.getSymbol());
        world.removeOrganism(this);
    }

    public void setNoSpace(boolean noSpace){
        this.noSpace = noSpace;
    }

    protected void sendSignalMaybeSpace(){
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // N, S, W, E
        for (int[] direction : directions) {
            int newRow = row + direction[0];
            int newCol = column + direction[1];
            if (newRow >= 0 && newRow < world.getHeight() && newCol >= 0 && newCol < world.getWidth()) {
                Organism neighbour = world.getOrganism(newRow, newCol);
                if (neighbour instanceof Plant) {
                    ((Plant) neighbour).setNoSpace(false);
                }
            }
        }
    }
}
