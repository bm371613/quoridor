package quoridor.gui;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

import quoridor.core.GameRules;
import quoridor.core.direction.PerDirection;
import quoridor.core.move.Move;
import quoridor.core.state.GameState;
import quoridor.gui.player.Player;

public class Game {

    private final List<GameState> gameStates = new ArrayList<>();
    private int gameStateIx = 0;
    @Getter private final PerDirection<Player> players;

    public Game(GameState gameState, PerDirection<Player> players) {
        this.gameStates.add(gameState);
        this.players = players;
    }

    public GameState getState() {
        return gameStates.get(gameStateIx);
    }

    public Player getCurrentPlayer() {
        return players.get(getState().getCurrentPlayersState());
    }

    public void performMove(Move move) {
        for (int i = gameStates.size() - 1; gameStateIx < i; --i) {
            gameStates.remove(i);
        }
        gameStates.add(move.apply(getState()));
        gameStateIx = gameStates.size() - 1;
    }

    public boolean undo() {
        int newGameStateIx = gameStateIx - 1;
        while (newGameStateIx >= 0 && !players.get(gameStates
                .get(newGameStateIx).getCurrentPlayersState()).isHuman()) {
            --newGameStateIx;
        }
        if (newGameStateIx < 0) {
            return false;
        }
        gameStateIx = newGameStateIx;
        return true;
    }

    public boolean redo() {
        int newGameStateIx = gameStateIx + 1;
        while (newGameStateIx < gameStates.size() && !players.get(gameStates
                .get(newGameStateIx).getCurrentPlayersState()).isHuman()
                && !GameRules.isFinal(gameStates.get(newGameStateIx))) {
            ++newGameStateIx;
        }
        if (newGameStateIx >= gameStates.size()) {
            return false;
        }
        gameStateIx = newGameStateIx;
        return true;
    }
}
