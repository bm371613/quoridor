package quoridor.gui.component;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.WindowConstants;

import lombok.Getter;

import quoridor.gui.component.board.Board;

public class MainWindow extends JFrame implements ActionListener {

    @Getter private final Board board;
    @Getter private final NewGameDialog newGameDialog = new NewGameDialog(this);

    private final JMenuItem newGameMenuItem = new JMenuItem("New Game");

    public MainWindow() {
        setTitle("Quoridor");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(400, 400));
        setSize(600, 600);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu gameMenu = new JMenu("Game");
        menuBar.add(gameMenu);
        newGameMenuItem.addActionListener(this);
        gameMenu.add(newGameMenuItem);

        board = new Board();
        setLayout(new GridBagLayout());
        add(board);
        pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newGameMenuItem) {
            newGameDialog.setVisible(true);
        }
    }
}
