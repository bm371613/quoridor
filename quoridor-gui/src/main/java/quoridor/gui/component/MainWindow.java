package quoridor.gui.component;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.WindowConstants;

import lombok.Getter;
import lombok.Setter;

import quoridor.gui.component.board.Board;
import quoridor.gui.event.DumpEvent;
import quoridor.gui.event.EventListener;
import quoridor.gui.event.RedoEvent;
import quoridor.gui.event.UndoEvent;
import quoridor.gui.util.GuiHelper;

public class MainWindow extends JFrame implements ActionListener {

    @Getter private final Board board;
    @Getter private final NewGameDialog newGameDialog = new NewGameDialog(this);

    private final JMenuItem newGameMenuItem = new JMenuItem("New Game");
    private final JMenuItem undoMenuItem = new JMenuItem("Undo");
    private final JMenuItem redoMenuItem = new JMenuItem("Redo");
    private final JMenuItem dumpMenuItem = new JMenuItem("Dump Game State");

    @Setter private EventListener eventListener;

    public MainWindow() {
        setTitle("Quoridor");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(600, 600));
        GuiHelper.setLocationToCenter(this);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        addMenuItem(menuBar, newGameMenuItem);
        addMenuItem(menuBar, undoMenuItem);
        addMenuItem(menuBar, redoMenuItem);
        addMenuItem(menuBar, dumpMenuItem);

        board = new Board();
        setLayout(new GridBagLayout());
        add(board);
        pack();
    }

    private void addMenuItem(JMenuBar menuBar, JMenuItem menuItem) {
        menuItem.addActionListener(this);
        menuItem.setMaximumSize(new Dimension(
                menuItem.getPreferredSize().width,
                menuItem.getMaximumSize().height));
        menuBar.add(menuItem);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newGameMenuItem) {
            newGameDialog.setVisible(true);
        } else if (e.getSource() == undoMenuItem) {
            eventListener.notifyAboutEvent(this, UndoEvent.makeUndoEvent());
        } else if (e.getSource() == redoMenuItem) {
            eventListener.notifyAboutEvent(this, RedoEvent.makeRedoEvent());
        } else if (e.getSource() == dumpMenuItem) {
            eventListener.notifyAboutEvent(this, DumpEvent.makeDumpEvent());
        }
    }
}
