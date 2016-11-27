package quoridor.analysis;

import java.util.List;

import com.google.common.collect.Lists;

import quoridor.ai.bot.mcts.Node;
import quoridor.ai.bot.mcts.ReducedWallsSimulator;
import quoridor.ai.bot.mcts.Simulator;
import quoridor.core.GameRules;

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

        List<Simulator> simulators = Lists.newArrayList(
                new Simulator(),
                new Simulator(),
                new ReducedWallsSimulator(4)
        );

        simulators.forEach((simulator -> {
            long start = System.currentTimeMillis();
            run(simulator, node, 132);
            System.out.println("   " + (System.currentTimeMillis() - start));
        }));
    }
}
