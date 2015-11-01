package quoridor.gui.management;

import quoridor.core.GameState;
import quoridor.gui.component.MainWindow;
import quoridor.gui.component.NewGameDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameManager implements ActionListener {

    private GameState gameState;
    private MainWindow mainWindow;

    public GameManager(MainWindow mainWindow) {
        this.mainWindow = mainWindow;

        setUpCallbacks();
    }

    public MainWindow getMainWindow() {
        return mainWindow;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        NewGameDialog newGameDialog = mainWindow.getNewGameDialog();
        if (e.getSource() == newGameDialog.getOkButton()) {
            newGameDialog.setVisible(false);
            newGame();

            // TODO
            newGameDialog.getTopPlayerType();
            newGameDialog.getBottomPlayerType();
        }
    }

    private void updateBoard() {
        mainWindow.getBoard().loadGameState(gameState);
    }

    private void setUpCallbacks() {
        mainWindow.getNewGameDialog().getOkButton().addActionListener(this);
    }

    private void newGame() {
        gameState = new GameState();
        updateBoard();
    }
}
