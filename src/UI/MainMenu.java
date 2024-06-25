package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.util.Scanner;

public class MainMenu {
    private JFrame frame;
    private JButton loadGameButton;
    private JButton newGameButton;

    public MainMenu() {
        prepareGUI();
    }

    private void prepareGUI() {
        setupFrame();
        setupLoadGameButton();
        setupNewGameButton();
        finalizeFrameSetup();
    }

    private void setupFrame() {
        frame = new JFrame("Game Main Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout()); // Use FlowLayout
    }

    private void setupLoadGameButton() {
        loadGameButton = new JButton("Load Game");
        loadGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { handleLoadGameButtonClick(); }
        });
        frame.add(loadGameButton);
    }

    private void setupNewGameButton() {
        newGameButton = new JButton("New Game");
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleNewGameButtonClick();
            }
        });
        frame.add(newGameButton);
    }

    private void handleNewGameButtonClick() {
        // Handle New Game button click here
        int width = 0;
        int height = 0;
        int minWidth = 6;
        int minHeight = 6;
        int maxWidth = 40;
        int maxHeight = 28;
        boolean validInput = false;

        while (!validInput) {
            String widthString = JOptionPane.showInputDialog(frame, "Enter game width:");
            String heightString = JOptionPane.showInputDialog(frame, "Enter game height:");

            // If the user clicked "Cancel", return to the main menu
            if (widthString == null || heightString == null) {
                return;
            }

            // Convert the input to integers
            width = Integer.parseInt(widthString);
            height = Integer.parseInt(heightString);

            if (width > maxWidth || height > maxHeight || width < minWidth || height < minHeight) {
                JOptionPane.showMessageDialog(frame, "Width must be between " + minWidth + " and " + maxWidth + ", and height must be between " + minHeight + " and " + maxHeight );
            } else {
                validInput = true;
            }
        }

        System.out.println("New Game button clicked. Width: " + width + ", Height: " + height);

        // Create a new Game instance
        GameUI game = new GameUI(frame, width, height);
        game.start(false);
    }

    private void handleLoadGameButtonClick() {
        try (Scanner scanner = new Scanner(new File("save.txt"))) {
            int width = scanner.nextInt();
            int height = scanner.nextInt();
            GameUI game = new GameUI(frame, width, height);
            game.start(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error loading game: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void finalizeFrameSetup() {
        frame.setSize(800, 600);
        frame.setVisible(true);
    }

    public void show() {
        frame.setVisible(true);
    }

}