package quoridor.gui;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import quoridor.core.GameRules;
import quoridor.core.move.Move;
import quoridor.gui.component.MainWindow;
import quoridor.gui.event.DumpEvent;
import quoridor.gui.event.EventListener;
import quoridor.gui.event.LoadGameEvent;
import quoridor.gui.event.MoveChoiceEvent;
import quoridor.gui.event.RedoEvent;
import quoridor.gui.event.UndoEvent;
import quoridor.gui.player.Player;

public class GameManager implements EventListener {

    private MainWindow mainWindow;
    private Game game;

    public GameManager(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.mainWindow.setEventListener(this);
        this.mainWindow.getStartGameDialog().setEventListener(this);
    }

    private void loadGame(LoadGameEvent loadGameEvent) {
        game = loadGameEvent.getGame();
        game.getPlayers().forEachEntry((e) -> mainWindow.getBoard()
                .setPlayerColor(e, e.getValue().getColor()));
        updateBoard();
        performTurn();
    }

    private void updateBoard() {
        mainWindow.getBoard().loadGameState(game.getState());
        mainWindow.getBoard().forEachWall((wall) -> wall.setVisible(
                        wall.isBuilt() || GameRules.isLegalMove(
                                game.getState(), wall.getMove()))
        );
    }

    private void performTurn() {
        if (GameRules.isFinal(game.getState())) {
            setMoveComponentsEventListener(null);
        } else {
            Player currentPlayer = game.getCurrentPlayer();
            setMoveComponentsEventListener(currentPlayer);
            currentPlayer.makeTurn(game.getState(), this);
        }
    }

    private void setMoveComponentsEventListener(EventListener eventListener) {
        this.mainWindow.getBoard().forEachPlace(
                (p) -> p.setEventListener(eventListener));
        this.mainWindow.getBoard().forEachWall(
                (w) -> w.setEventListener(eventListener));
    }

    @Override
    public void notifyAboutEvent(Object source, Object event) {
        if (event instanceof LoadGameEvent) {
            loadGame((LoadGameEvent) event);
        }  else if (event instanceof MoveChoiceEvent) {
            if (game == null) {
                return;
            }
            Player currentPlayer = game.getCurrentPlayer();
            if (source != currentPlayer) {
                return;
            }
            Move move = ((MoveChoiceEvent) event).getMove();
            if (GameRules.isLegalMove(game.getState(), move)) {
                currentPlayer.moveAccepted();
                game.performMove(move);
                updateBoard();
                performTurn();
            }
        } else if (event instanceof UndoEvent) {
            if (game == null) {
                return;
            }
            Player player = game.getCurrentPlayer();
            if (game.undo()) {
                player.moveCancelled();
                updateBoard();
                performTurn();
            }
        } else if (event instanceof RedoEvent) {
            if (game == null) {
                return;
            }
            Player player = game.getCurrentPlayer();
            if (game.redo()) {
                player.moveCancelled();
                updateBoard();
                performTurn();
            }
        } else if (event instanceof DumpEvent) {
            if (game == null) {
                return;
            }
            try {
                FileOutputStream fileOut = new FileOutputStream("dump.ser");
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(game.getState());
                out.close();
                fileOut.close();
            } catch (IOException e) {
                return;
            }
        }
    }
}
