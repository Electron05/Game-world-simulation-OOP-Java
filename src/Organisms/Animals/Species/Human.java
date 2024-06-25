package Organisms.Animals.Species;

import Environment.World;
import Organisms.Animals.Animal;
import Organisms.Organism;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Human extends Animal implements KeyListener {
    private boolean movePermission;

    private int burnCooldown,burnDuration;

    public Human(int row, int col) {
        super(row, col, 5, 4, 'H');
        this.burnCooldown = 5;
        burnDuration = 0;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (!movePermission) {
            return;
        }


        int newCol = getColumn();
        int newRow = getRow();

        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                newRow--; // Up
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                newRow++; // Down
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                newCol--; // Left
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                newCol++; // Right
                break;
            case KeyEvent.VK_E:
                world.writeSave();
                world.getGameUI().addLog("Game saved!");
                return;
            case KeyEvent.VK_B:
                if(burnCooldown<=0){
                    TurnOnFire();
                    burnNeighbours();
                    world.actionsAfterHuman();
                    return;
                }
                world.getGameUI().addLog("You can't burn yet! Cooldown: "+burnCooldown);
                return;
            default:
                return;
        }

        if (burnDuration!=0) {
            burnDuration--;
        }
        else if (burnCooldown!=0)
            burnCooldown--;

        // Check if the new position is within the world's boundaries
        if (newCol >= 0 && newCol < World.getInstance().getWidth() && newRow >= 0 && newRow < World.getInstance().getHeight()) {

            world.getGameUI().clearLog();
            world.getGameUI().addLog("Burn Cooldown: "+burnCooldown);
            performAction(newRow, newCol);
            movePermission = false;
            if(burnDuration!=0)
                burnNeighbours();
            world.actionsAfterHuman();
        }
    }

    @Override
    public void action() {
        // if action of human gets called, do nothing I don't want it to move randomly, ever
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Needs to be implemented due to KeyListener interface
    }

    private void TurnOnFire(){
        burnDuration=5;
        burnCooldown=5;
    }

    private void burnNeighbours(){
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {1,1},{-1,-1},{-1,1},{1,-1}}; // N, S, W, E,
        for (int[] direction : directions) {
            int newRow = row + direction[0];
            int newCol = column + direction[1];
            if (newRow >= 0 && newRow < world.getHeight() && newCol >= 0 && newCol < world.getWidth()) {
                Organism neighbour = world.getOrganism(newRow, newCol);
                if(neighbour!=null) {
                    world.removeOrganism(neighbour);
                    world.getGameUI().addLog(neighbour.getSymbol() + " got burned by Human!");
                }
                world.getGameUI().getCell(newRow, newCol).getBurned();
            }
        }
    }

    public void EnableMove() {

        System.out.println("Human's turn");
        //game.refreshAllCells();
        //refreshing ALL Cells every turn dramatically slows down the game
        //so I decided to refresh only the cells where something moves/gets created/gets eaten
        this.movePermission = true;
    }

    public int getBurnDuration(){
        return burnDuration;
    }
    public void setBurnDuration(int burnDuration){
        this.burnDuration = burnDuration;
    }
    public int getBurnCooldown(){
        return burnCooldown;
    }
    public void setBurnCooldown(int burnCooldown){
        this.burnCooldown = burnCooldown;
    }

}