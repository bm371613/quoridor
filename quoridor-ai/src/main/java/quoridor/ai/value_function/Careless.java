package quoridor.ai.value_function;

import quoridor.core.state.GameState;

import static quoridor.ai.value_function.Utils.distance;
import static quoridor.ai.value_function.Utils.minOtherPlayerDistance;

public class Careless implements ValueFunction {

    @Override
    public int apply(GameState gameState, int playerIx) {
        int myDistance = distance(gameState, playerIx);
        int otherDistance = minOtherPlayerDistance(gameState, playerIx);
        return otherDistance > 1 || myDistance == 0
                ? Integer.MAX_VALUE - myDistance
                : Integer.MIN_VALUE + otherDistance;
    }
}
