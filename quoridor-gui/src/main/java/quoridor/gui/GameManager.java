package quoridor.gui;

import quoridor.core.GameRules;
import quoridor.core.Move;
import quoridor.core.state.GameState;
import quoridor.gui.component.MainWindow;
import quoridor.gui.event.EventListener;
import quoridor.gui.event.MoveChoiceEvent;
import quoridor.gui.event.MoveConsiderationEvent;
import quoridor.gui.event.NewGameEvent;
import quoridor.gui.player.Player;
import quoridor.gui.util.PerPlayer;

public class GameManager implements EventListener {

    private GameState gameState;
    private PerPlayer<Player> players;
    private MainWindow mainWindow;

    public GameManager(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.mainWindow.getNewGameDialog().setEventListener(this);
    }

    private void setMoveComponentsEventListener(EventListener eventListener) {
        this.mainWindow.getBoard().forEachPlace(
                (p) -> p.setEventListener(eventListener));
        this.mainWindow.getBoard().forEachWall(
                (w) -> w.setEventListener(eventListener));
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

    private Player getCurrentPlayer() {
        return players.get(gameState.getCurrentPlayersState());
    }

    private void performTurn() {
        Player currentPlayer = getCurrentPlayer();
        setMoveComponentsEventListener(currentPlayer);
        currentPlayer.makeTurn(this);
        // TODO
    }

    @Override
    public void notifyAboutEvent(Object source, Object event) {
        if (event instanceof NewGameEvent) {
            newGame((NewGameEvent) event);
        } else if (event instanceof MoveConsiderationEvent) {
            if (gameState == null) {
                return;
            }
            MoveConsiderationEvent mce = (MoveConsiderationEvent) event;
            mce.getMoveComponent().setHighlighted(
                    GameRules.isLegalMove(gameState, mce.getMove()));
        } else if (event instanceof MoveChoiceEvent) {
            if (gameState == null) {
                return;
            }
            Player currentPlayer = getCurrentPlayer();
            if (source != currentPlayer) {
                return;
            }
            Move move = ((MoveChoiceEvent) event).getMove();
            if (GameRules.isLegalMove(gameState, move)) {
                currentPlayer.moveAccepted();
                gameState = gameState.apply(move);
                updateBoard();
                performTurn();
            }
        }
    }
}