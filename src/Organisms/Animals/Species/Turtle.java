package Organisms.Animals.Species;

import Organisms.Animals.Animal;
import java.util.Random;
public class Turtle extends Animal {

    private Random rand;

    public Turtle(int row, int col) {
        super(row, col, 2, 1,'T');
        this.rand = new Random();
    }
    @Override
    public void action() {
        if (rand.nextInt(4) == 0) {
            super.action();
        }
    }
    @Override
    public void getCollided(Animal attacker,int row,int col) {
        if(attacker.getPower()<5) {
            world.getGameUI().addLog(attacker.getSymbol() + " tried attacking T and has to retreat.");
            return;
        }

        super.getCollided(attacker,row,col);
    }
}
