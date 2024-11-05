package game.main;

import game.component.MenuPanel;
import java.awt.BorderLayout;
import javax.swing.JFrame;

public class Main extends JFrame {

    public Main() {
        init();
    }

    private void init() {
        setTitle("Save Kelly's House");
        setSize(1336, 768);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Add the menu panel initially
        MenuPanel menuPanel = new MenuPanel();
        add(menuPanel);
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.setVisible(true);
    }
}
