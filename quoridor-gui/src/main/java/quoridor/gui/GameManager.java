package quoridor.gui;

import quoridor.core.GameRules;
import quoridor.core.Move;
import quoridor.core.state.GameState;
import quoridor.gui.component.MainWindow;
import quoridor.gui.event.EventListener;
import quoridor.gui.event.LoadGameEvent;
import quoridor.gui.event.MoveChoiceEvent;
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

    private void loadGame(LoadGameEvent loadGameEvent) {
        gameState = loadGameEvent.getGameState();
        players = loadGameEvent.getPlayers();
        players.forEachEntry((e) -> mainWindow.getBoard().setPlayerColor(
                e.getGoal(), e.getValue().getColor()));
        updateBoard();
        performTurn();
    }

    private Player getCurrentPlayer() {
        return players.get(gameState.getCurrentPlayersState());
    }

    private void performTurn() {
        if (GameRules.isFinal(gameState)) {
            setMoveComponentsEventListener(null);
        } else {
            Player currentPlayer = getCurrentPlayer();
            setMoveComponentsEventListener(currentPlayer);
            currentPlayer.makeTurn(gameState, this);
        }
    }

    @Override
    public void notifyAboutEvent(Object source, Object event) {
        if (event instanceof LoadGameEvent) {
            loadGame((LoadGameEvent) event);
        }  else if (event instanceof MoveChoiceEvent) {
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
