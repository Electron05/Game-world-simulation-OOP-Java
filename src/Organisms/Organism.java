package Organisms;
import Environment.World;
public abstract class Organism {
    protected int column;
    protected int row;
    protected int age;

    protected int turnsSinceBreeding;
    protected int power;
    protected int initiative;
    protected char symbol;

    protected World world;

    public Organism(int row, int col, int power, int initiative, char symbol) {

        this.column = col;
        this.row = row;
        this.age = 0;
        this.turnsSinceBreeding = 0;
        this.power = power;
        this.initiative = initiative;
        this.symbol = symbol;
        this.world = World.getInstance();
        World.getInstance().addOrganism(this);
    }

    public abstract void action();

    public int getColumn() {
        return column;
    }
    public int getRow() {
        return row;
    }

    public void setPosition(int row, int col) {
        this.column = col;
        this.row = row;
    }

    public int getAge() {
        return age;
    }

    public void increaseAge() {
        this.age++;
        this.turnsSinceBreeding++;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getInitiative() {
        return initiative;
    }

    public void setInitiative(int initiative) {
        this.initiative = initiative;
    }

    public char getSymbol() {
        return symbol;
    }

}
