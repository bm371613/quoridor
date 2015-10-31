package quoridor.gui;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import quoridor.core.GameState;
import quoridor.gui.component.Board;

public class MainWindow extends JFrame {

    Board board = new Board();

    public MainWindow() {
        initUI();
        board.loadGameState(new GameState());
    }

    private void initUI() {
        setLayout(new GridBagLayout());
        add(board);
        pack();
        setMinimumSize(new Dimension(300, 300));
        setSize(600, 600);

        setTitle("Quoridor");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainWindow().setVisible(true));
    }
}
