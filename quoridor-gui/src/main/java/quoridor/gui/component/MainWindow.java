package quoridor.gui.component;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.WindowConstants;

public class MainWindow extends JFrame {

    Board board = new Board();
    JMenuItem newGameMenuItem = new JMenuItem("New Game");

    public MainWindow() {
        setTitle("Quoridor");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(350, 350));
        setSize(600, 600);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu gameMenu = new JMenu("Game");
        menuBar.add(gameMenu);
        gameMenu.add(newGameMenuItem);

        setLayout(new GridBagLayout());
        add(board);
        pack();
    }

    public Board getBoard() {
        return board;
    }

    public JMenuItem getNewGameMenuItem() {
        return newGameMenuItem;
    }
}
