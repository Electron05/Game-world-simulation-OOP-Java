package Organisms.Plants.Species;
import Organisms.Plants.Plant;
import java.util.Random;
public class Dandelion extends Plant {
    private Random rand;
    public Dandelion(int row, int col) {
        super(row, col, 0, '@');
        this.rand = new Random();
    }
    @Override
    public void action() {
        increaseAge();
        if(noSpace)
            return;
        for (int i = 0; i < 3; i++) {
            int randomNumber = rand.nextInt(oneInHowManyChanceToGrow+1); //0-6
            if(randomNumber == 0) {
                tryToGrow();
            }
        }
    }
}
