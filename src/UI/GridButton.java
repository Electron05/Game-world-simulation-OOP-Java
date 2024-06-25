package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import Environment.World;
import Organisms.Organism;

public class GridButton extends JButton {
    private int row;
    private int col;
    private Color bgColor = null;

    public GridButton(int row, int col) {
        this.row = row;
        this.col = col;
        setPreferredSize(new Dimension(30, 30));
        setMargin(new Insets(0, 0, 0, 0));
        setContentAreaFilled(false);
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Organism target = World.getInstance().getOrganism(row, col);
                if(target==null)
                    addOrganism();
            }
        });
    }

    private void addOrganism() {
        String[] options = {"Wolf", "Sheep", "Turtle", "Fox", "Antilope", "Grass", "Dandelion", "Guarana", "Nightshade", "Hogweed"};
        int indexOfFirstPlant = 5;
        String organismType = (String) JOptionPane.showInputDialog(null, "Choose organism type", "Add Organism", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (organismType != null) {
            try {
                Class organismClass = Class.forName("Organisms." + (Arrays.asList(options).indexOf(organismType) < indexOfFirstPlant ? "Animals.Species." : "Plants.Species.") + organismType);
                Organism newOrganism = (Organism) organismClass.getConstructor(int.class, int.class).newInstance(row, col);
            } catch (Exception ex) {
                System.out.println("Error adding organism: " + ex.getMessage());
            }
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        if (bgColor != null) {
            g.setColor(bgColor);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        super.paintComponent(g);
    }

    public void Refresh() {
        Organism organism = World.getInstance().getOrganism(row, col);

        setForeground(Color.BLACK);
        setText("");
        bgColor = null; // reset to default color when no organism

        if (organism != null) {
            switch (organism.getSymbol()){
                case '#':
                    bgColor = Color.GREEN;
                    break;
                case '@':
                    bgColor = Color.YELLOW;
                    break;
                case '&':
                    bgColor = Color.GREEN;
                    setText("&");
                    break;
                case '%':
                    bgColor = Color.GREEN;
                    setForeground(Color.RED);
                    setText("%");
                    break;
                case '!':
                    bgColor = Color.GREEN;
                    setText("!");
                    break;
                default:
                    bgColor = null;
                    setText(String.valueOf(organism.getSymbol()));
                    break;
            }
        }
        repaint(); // repaint the button after changing the color
    }

    public void getBurned(){
        bgColor = Color.GRAY;
        setText("");
        repaint();
    }

}