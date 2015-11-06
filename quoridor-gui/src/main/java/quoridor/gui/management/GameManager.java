package quoridor.gui.management;

import quoridor.core.GameRules;
import quoridor.core.Move;
import quoridor.core.state.GameState;
import quoridor.gui.component.MainWindow;
import quoridor.gui.component.board.Place;
import quoridor.gui.component.board.Wall;
import quoridor.gui.event.EventListener;
import quoridor.gui.event.MoveChoiceEvent;
import quoridor.gui.event.MoveConsiderationEvent;
import quoridor.gui.event.NewGameEvent;
import quoridor.gui.util.PerPlayer;

public class GameManager implements EventListener {

    private GameState gameState;
    private PerPlayer<Player> players;
    private MainWindow mainWindow;

    public GameManager(MainWindow mainWindow) {
        this.mainWindow = mainWindow;

        this.mainWindow.getNewGameDialog().setEventListener(this);
        this.mainWindow.getBoard().forEachPlace(
                (p) -> p.setEventListener(this));
        this.mainWindow.getBoard().forEachWall((w) -> w.setEventListener(this));
    }

    public MainWindow getMainWindow() {
        return mainWindow;
    }

    private void updateBoard() {
        mainWindow.getBoard().loadGameState(gameState);
        mainWindow.getBoard().forEachWall((wall) -> wall.setVisible(
                        wall.isBuilt() || GameRules.isLegalMove(
                                gameState, wall.getMove()))
        );
    }

    private void newGame(NewGameEvent newGameEvent) {
        gameState = newGameEvent.getGameState();
        players = newGameEvent.getPlayers();
        updateBoard();
        performTurn();
    }

    private boolean isCurrentPlayerHuman() {
        return players.get(gameState.getCurrentPlayersState()).getBot() == null;
    }

    private void performTurn() {
        if (isCurrentPlayerHuman()) {
            return;
        }

        // TODO
    }

    @Override
    public void notifyAboutEvent(Object source, Object event) {
        if (event instanceof NewGameEvent) {
            newGame((NewGameEvent) event);
        } else if (event instanceof MoveConsiderationEvent) {
            if (gameState == null || !isCurrentPlayerHuman()) {
                return;
            }
            Move move = ((MoveConsiderationEvent) event).getMove();
            if (GameRules.isLegalMove(gameState, move)) {
                if (move.isPawnMove()) {
                    ((Place) source).setHighlighted(true);
                } else {
                    ((Wall) source).setHighlighted(true);
                }
            }
        } else if (event instanceof MoveChoiceEvent) {
            if (gameState == null || !isCurrentPlayerHuman()) {
                return;
            }
            Move move = ((MoveChoiceEvent) event).getMove();
            if (GameRules.isLegalMove(gameState, move)) {
                gameState = gameState.apply(move);
                updateBoard();
                performTurn();
            }
        }
    }
}
