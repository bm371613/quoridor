package quoridor.gui.management;

import quoridor.core.GameRules;
import quoridor.core.Move;
import quoridor.core.state.GameState;
import quoridor.gui.component.MainWindow;
import quoridor.gui.event.EventListener;
import quoridor.gui.event.NewGameEvent;
import quoridor.gui.event.PawnMoveConsiderationEvent;
import quoridor.gui.event.WallMoveConsiderationEvent;

public class GameManager implements EventListener {

    private GameState gameState;
    private MainWindow mainWindow;

    public GameManager(MainWindow mainWindow) {
        this.mainWindow = mainWindow;

        this.mainWindow.setEventListener(this);
        this.mainWindow.getBoard().setEventListener(this);
    }

    public MainWindow getMainWindow() {
        return mainWindow;
    }

    private void updateBoard() {
        mainWindow.getBoard().loadGameState(gameState);
    }

    private void newGame() {
        gameState = GameRules.makeInitialState(false);
        updateBoard();
    }

    @Override
    public void notifyAboutEvent(Object event) {
        if (event instanceof NewGameEvent) {
            newGame();
        } else if (event instanceof WallMoveConsiderationEvent) {
            if (gameState == null) {
                return;
            }
            WallMoveConsiderationEvent wallEvent =
                    (WallMoveConsiderationEvent) event;
            Move move = Move.makeWallMove(wallEvent.getX(), wallEvent.getY(),
                    wallEvent.getWallOrientation());
            if (GameRules.isLegalMove(gameState, move)) {
                wallEvent.getWall().setHighlighted(true);
            }
        } else if (event instanceof PawnMoveConsiderationEvent) {
            if (gameState == null) {
                return;
            }
            PawnMoveConsiderationEvent pawnEvent =
                    (PawnMoveConsiderationEvent) event;
            Move move = Move.makePawnMove(pawnEvent.getX(), pawnEvent.getY());
            if (GameRules.isLegalMove(gameState, move)) {
                pawnEvent.getPlace().setHighlighted(true);
            }
        }
    }
}
