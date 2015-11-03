package quoridor.gui.management;

import quoridor.core.GameRules;
import quoridor.core.Move;
import quoridor.core.state.GameState;
import quoridor.core.state.Goal;
import quoridor.gui.component.MainWindow;
import quoridor.gui.component.board.Place;
import quoridor.gui.component.board.Wall;
import quoridor.gui.event.EventListener;
import quoridor.gui.event.NewGameEvent;
import quoridor.gui.event.PawnMoveConsiderationEvent;
import quoridor.gui.event.WallMoveConsiderationEvent;

import java.util.function.Function;

public class GameManager implements EventListener {

    private GameState gameState;
    private MainWindow mainWindow;

    public GameManager(MainWindow mainWindow) {
        this.mainWindow = mainWindow;

        this.mainWindow.getNewGameDialog().setEventListener(this);
        this.mainWindow.getBoard().forEachPlace((p) -> {
            p.setEventListener(this);
        });
        this.mainWindow.getBoard().forEachWall((w) -> {
            w.setEventListener(this);
        });
    }

    public MainWindow getMainWindow() {
        return mainWindow;
    }

    private void updateBoard() {
        mainWindow.getBoard().loadGameState(gameState);
    }

    private void newGame(NewGameEvent newGameEvent) {
        gameState = newGameEvent.getPlayerTypes().size() == 2
                ? GameRules.makeInitialStateForTwo()
                : GameRules.makeInitialStateForFour();
        updateBoard();
    }

    @Override
    public void notifyAboutEvent(Object source, Object event) {
        if (event instanceof NewGameEvent) {
            newGame((NewGameEvent) event);
        } else if (event instanceof WallMoveConsiderationEvent) {
            if (gameState == null) {
                return;
            }
            WallMoveConsiderationEvent wallEvent =
                    (WallMoveConsiderationEvent) event;
            Move move = Move.makeWallMove(wallEvent.getX(), wallEvent.getY(),
                    wallEvent.getWallOrientation());
            if (GameRules.isLegalMove(gameState, move)) {
                ((Wall) source).setHighlighted(true);
            }
        } else if (event instanceof PawnMoveConsiderationEvent) {
            if (gameState == null) {
                return;
            }
            PawnMoveConsiderationEvent pawnEvent =
                    (PawnMoveConsiderationEvent) event;
            Move move = Move.makePawnMove(pawnEvent.getX(), pawnEvent.getY());
            if (GameRules.isLegalMove(gameState, move)) {
                ((Place) source).setHighlighted(true);
            }
        }
    }
}
