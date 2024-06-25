package UI;

import Environment.World;
import Organisms.Animals.Species.Human;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GameUI {

    private final int logWidth = 250;

    private World world;
    private Human human;
    private JFrame frame;
    private GridButton[][] cells;
    private WorldLog worldLog; // Add WorldLog instance variable

    public GameUI(JFrame frame, int width, int height) {
        this.frame = frame;
        this.world = World.getInstance(width, height,this);
        this.worldLog = new WorldLog(); // Initialize WorldLog
    }

    public void start(boolean isGameLoaded) {
        createWorldUI(world.getWidth(), world.getHeight());
        frame.requestFocusInWindow();

        this.human = new Human(0, 0);
        if(isGameLoaded) {
            world.readSave(human);
        } else {
            world.startingPopulation();
        }


        frame.addKeyListener(human);
        frame.setFocusable(true);

        world.startOfNewTurn();
        worldLog.addLog("New turn started");
    }



    private void createWorldUI(int width, int height) {
        JPanel worldPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        cells = new GridButton[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                cells[i][j] = new GridButton(i,j);
                cells[i][j].Refresh();
                cells[i][j].setFocusable(false);

                gbc.gridx = j;
                gbc.gridy = i;
                worldPanel.add(cells[i][j], gbc);
            }
        }

        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBorder(new EmptyBorder(0, 0, 0, 0)); // Set an empty border
        containerPanel.add(worldPanel, BorderLayout.CENTER);

        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());
        frame.add(containerPanel, BorderLayout.CENTER);

        worldLog.setPreferredSize(new Dimension(logWidth, frame.getHeight()));

        frame.add(worldLog, BorderLayout.EAST);

        frame.setVisible(true);
    }

    public void refreshCell(int row, int col){
        cells[row][col].Refresh();
    }

    public GridButton getCell(int row,int col){
        return cells[row][col];
    }

    public void addLog(String message){
        worldLog.addLog(message);
    }
    public void clearLog(){
        worldLog.clearLog();
    }
}