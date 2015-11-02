package quoridor.gui.component;

import quoridor.gui.component.board.Board;
import quoridor.gui.event.EventListener;
import quoridor.gui.event.NewGameEvent;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.WindowConstants;

public class MainWindow extends JFrame implements ActionListener {

    EventListener eventListener;
    Board board;
    JMenuItem newGameMenuItem = new JMenuItem("New Game");
    NewGameDialog newGameDialog = new NewGameDialog(this);

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
        newGameMenuItem.addActionListener(this);
        gameMenu.add(newGameMenuItem);

        newGameDialog.getOkButton().addActionListener(this);

        board = new Board();
        setLayout(new GridBagLayout());
        add(board);
        pack();
    }

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    public Board getBoard() {
        return board;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newGameMenuItem) {
            newGameDialog.setVisible(true);
        } else if (e.getSource() == newGameDialog.getOkButton()) {
            if (eventListener != null) {
                newGameDialog.setVisible(false);
                eventListener.notifyAboutEvent(new NewGameEvent(
                        newGameDialog.getTopPlayerType(),
                        newGameDialog.getBottomPlayerType()
                ));
            }
        }
    }
}
