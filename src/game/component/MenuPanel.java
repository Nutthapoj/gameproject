package game.component;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.image.BufferedImage;

public class MenuPanel extends JPanel {
    private JButton startButton;
    private JButton exitButton;
    private BufferedImage backgroundImage;

    public MenuPanel() {
        // Load the background image
        try {
            backgroundImage = ImageIO.read(getClass().getResource("/game/image/sky.png"));
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception if the image is not found
        }

        setLayout(new BorderLayout());
        ImageIcon startIcon = new ImageIcon(getClass().getResource("/game/image/start.png"));
        ImageIcon exitIcon = new ImageIcon(getClass().getResource("/game/image/EXit.png"));
        startButton = new JButton(startIcon);
        exitButton = new JButton(exitIcon);

        // Set button sizes
        Dimension buttonSize = new Dimension(200, 70); // Width: 200px, Height: 70px
        startButton.setPreferredSize(buttonSize);
        exitButton.setPreferredSize(buttonSize);

        // Add action listeners for buttons
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Start the game
                startGame();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Exit the application
            }
        });

        // Add buttons to the panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 150)); // Center alignment with gaps
        buttonPanel.setOpaque(false); // Make button panel transparent
        buttonPanel.add(startButton);
        buttonPanel.add(exitButton);

        add(buttonPanel, BorderLayout.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this); // Draw the background image
        }
    }

    private void startGame() {
        // Switch to the game panel
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        topFrame.getContentPane().removeAll();
        PanelGame panelGame = new PanelGame();
        topFrame.add(panelGame);
        topFrame.revalidate();
        topFrame.repaint();
        panelGame.start(); // Start the game
    }
}