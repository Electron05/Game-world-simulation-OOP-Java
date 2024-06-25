package Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;


import Organisms.Organism;
import Organisms.Plants.Species.*;
import Organisms.Animals.Species.*;
import Organisms.Animals.Animal;

import UI.GameUI;
public class World { //Singleton Class
    private int width, height;

    private boolean isGameOver, isHumanTurn;

    private static World instance;

    private GameUI game;


    private int initialTurnOrganismsAmount, turnIterator, iterationsAdjustment; // Used to help with adding and removing organisms during the turn. Keeps organisms vector in order.

    private List<Organism> organisms; // List of all organisms
    private Organism[][] organismGrid; // 2D array representing the spatial distribution of organisms

    private World(int width, int height, GameUI game) {
        this.width = width;
        this.height = height;
        this.game = game;
        this.organisms = new ArrayList<>();
        this.organismGrid = new Organism[height][width];
        this.isHumanTurn = false;
        this.isGameOver = false;
    }
    public static World getInstance() {
        if (instance == null) {
            instance = new World(1, 1,null);
            System.out.println("Warning! World instance requested before initialization. Returning 1x1.");
        }
        return instance;
    }
    public static World getInstance(int width, int height, GameUI game) {
        if (instance == null) {
            instance = new World(width, height, game);
        }
        return instance;
    }

    public GameUI getGameUI() {
        return game;
    }

    public Organism getOrganism(int row, int col) {
        return organismGrid[row][col];
    }

    public void addOrganism(Organism o) {
        organismGrid[o.getRow()][o.getColumn()] = o;
        game.refreshCell(o.getRow(), o.getColumn());
        iterationsAdjustment++;
        //inserting organism in the list sorted by initiative (from highest to lowest)
        // (when two organisms have the same initiative, oldest is first
        int organismsCount = organisms.size();
        for (int i = 0; i < organismsCount; i++) {
            if (organisms.get(organismsCount - 1 - i).getInitiative() >= o.getInitiative()) {
                organisms.add(organismsCount - i, o);
                if(o.getInitiative() > 4)
                    turnIterator++;
                return;
            }
        }
        organisms.add(0, o);
    }

    public void removeOrganism(Organism o) {
        organismGrid[o.getRow()][o.getColumn()] = null;
        int index = organisms.indexOf(o);

        System.out.println("Removing " + o.getSymbol() + " at " + o.getRow() + " " + o.getColumn() );

        iterationsAdjustment--;
        if(index <= turnIterator) {
            turnIterator--;
        }
        organisms.remove(o);

        game.refreshCell(o.getRow(), o.getColumn());
    }


    public void moveOrganism(Organism organism, int newRow, int newCol) {
        organismGrid[organism.getRow()][organism.getColumn()] = null;
        game.refreshCell(organism.getRow(), organism.getColumn());

        organismGrid[newRow][newCol] = organism;
        organism.setPosition(newRow, newCol);
        game.refreshCell(newRow, newCol);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void startOfNewTurn() {
        actionsBeforeHuman(); // --> actions after human --> startOfNewTurn

        //game.addLog(Integer.toString(organisms.size()));
    }
    private void actionsBeforeHuman(){
        iterationsAdjustment = 0;
        initialTurnOrganismsAmount = organisms.size();
        for (turnIterator = 0; turnIterator < initialTurnOrganismsAmount + iterationsAdjustment; turnIterator++) {
            Organism o = organisms.get(turnIterator);
            //System.out.println(turnIterator + " + "+ iterationsAdjustment + " + " + o.getSymbol());
            if (o instanceof Human) {
                if(o.getAge()==0)
                {
                    o.increaseAge();
                    actionsAfterHuman();
                }
                ((Human) o).EnableMove();
                break;
            }

            if(o.getAge() == 0) {
                o.increaseAge();
                continue;
            }
            o.action();
        }
    }

    public void actionsAfterHuman(){
        for (turnIterator = turnIterator+1; turnIterator < initialTurnOrganismsAmount + iterationsAdjustment; turnIterator++) {
            Organism o = organisms.get(turnIterator);
            if(o.getAge() == 0) {
                o.increaseAge();
                continue;
            }
            o.action();
        }
        startOfNewTurn();
    }

    public void startingPopulation() {

        /*
        new Dandelion(world.getHeight()-1,world.getWidth()-1);
        new Grass(1,0);
        new Guarana(world.getHeight()-1,0);
        new Hogweed(0,world.getWidth()-1);
        new Wolf(6,6);
        new Antilope(10,10);
        new Sheep(5,0);
        */
        int mode = 0; //for easy startingPopulation control, set 1 and choose specific organisms above
        if(mode==1)
            return;
        int numOrganisms = 2;
        Class[] organismTypes = {Wolf.class, Sheep.class, Turtle.class, Fox.class, Antilope.class, Grass.class, Guarana.class, Nightshade.class, Hogweed.class, Dandelion.class};
        Random rand = new Random();

        for (int i = 0; i < numOrganisms; i++) {
            for (Class organismType : organismTypes) {
                int finalX, finalY;
                do {
                    finalX = rand.nextInt(getWidth());
                    finalY = rand.nextInt(getHeight());
                } while (getOrganism(finalY, finalX) != null);

                Organism newOrganism = null;
                try {
                    newOrganism = (Organism) organismType.getConstructor(int.class, int.class).newInstance(finalY, finalX);
                } catch (Exception e) {
                    System.out.println("Warning, could not instantiate organim during starting population");
                }
                if (newOrganism == null) {
                    System.out.println("Warning, could not instantiate organim during starting populatio 2");
                }
            }
        }

    }

    public void readSave(Human human) {
        try (Scanner scanner = new Scanner(new File("save.txt"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                List<String> tokens = Arrays.asList(line.split(" "));

                if (tokens.size() > 2) {
                    Organism organism = null;
                    switch (tokens.get(2).charAt(0)) {
                        case 'A': organism = new Antilope(Integer.parseInt(tokens.get(0)), Integer.parseInt(tokens.get(1))); break;
                        case 'F': organism = new Fox(Integer.parseInt(tokens.get(0)), Integer.parseInt(tokens.get(1))); break;
                        case 'T': organism = new Turtle(Integer.parseInt(tokens.get(0)), Integer.parseInt(tokens.get(1))); break;
                        case 'W': organism = new Wolf(Integer.parseInt(tokens.get(0)), Integer.parseInt(tokens.get(1))); break;
                        case 'S': organism = new Sheep(Integer.parseInt(tokens.get(0)), Integer.parseInt(tokens.get(1))); break;
                        case '#': organism = new Grass(Integer.parseInt(tokens.get(0)), Integer.parseInt(tokens.get(1))); break;
                        case '&': organism = new Guarana(Integer.parseInt(tokens.get(0)), Integer.parseInt(tokens.get(1))); break;
                        case '%': organism = new Nightshade(Integer.parseInt(tokens.get(0)), Integer.parseInt(tokens.get(1))); break;
                        case '!': organism = new Hogweed(Integer.parseInt(tokens.get(0)), Integer.parseInt(tokens.get(1))); break;
                        case '@': organism = new Dandelion(Integer.parseInt(tokens.get(0)), Integer.parseInt(tokens.get(1))); break;

                        case 'H': human.setBurnDuration(Integer.parseInt(tokens.get(4)));
                                  human.setBurnCooldown(Integer.parseInt(tokens.get(5)));
                                  moveOrganism(human,Integer.parseInt(tokens.get(0)), Integer.parseInt(tokens.get(1)));
                                  break;
                        default: System.out.println("Unrecognized organism in save file.");
                    }
                    if (organism instanceof Animal) {
                        organism.setPower(Integer.parseInt(tokens.get(3)));
                    }
                    //organism.increaseAge();
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Save file not found.");
        }
    }

    public void writeSave() {
        try (PrintWriter save = new PrintWriter("save.txt")) {
            save.println(width + " " + height);
            for (Organism organism : organisms) {
                save.print(organism.getRow() + " " + organism.getColumn() + " " + organism.getSymbol());
                if (organism instanceof Human) {
                    Human human = (Human) organism;
                    save.println(" " + human.getPower() + " " + human.getBurnDuration() + " " + human.getBurnCooldown());
                } else if (organism instanceof Animal) {
                    save.println(" " + ((Animal) organism).getPower());
                } else {
                    save.println();
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error occurred when creating save file.");
        }
    }


}