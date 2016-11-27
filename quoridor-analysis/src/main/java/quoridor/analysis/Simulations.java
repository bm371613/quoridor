package quoridor.analysis;

import com.google.common.collect.Lists;
import quoridor.ai.bot.mcts.Node;
import quoridor.ai.bot.mcts.RandomSimulator;
import quoridor.ai.bot.mcts.Simulator;
import quoridor.ai.value_function.TopOpponentDistanceComparison;
import quoridor.ai.value_function.ValueFunction;
import quoridor.core.GameRules;

import java.util.List;

public final class Simulations {

    private Simulations() {
    }

    public static void run(Simulator simulator, Node node,
                           int count) {
        for (int i = 0; i < count; ++i) {
            simulator.simulate(node);
        }
    }

    public static void main(String[] args) {
        Node node = new Node(GameRules.makeInitialStateForTwo());
        ValueFunction valueFunction =
                TopOpponentDistanceComparison.getInstance();

        List<Simulator> simulators = Lists.newArrayList(
                new RandomSimulator()
        );

        simulators.forEach((simulator -> {
            long start = System.currentTimeMillis();
            run(simulator, node, 10000);
            System.out.println(System.currentTimeMillis() - start);
        }));
    }
}
