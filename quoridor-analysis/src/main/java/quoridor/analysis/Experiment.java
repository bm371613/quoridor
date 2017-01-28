package quoridor.analysis;

import java.util.ArrayList;
import java.util.List;

import quoridor.ai.bot.AlphaBetaBot;
import quoridor.ai.bot.AlphaBetaTTBot;
import quoridor.ai.bot.Bot;
import quoridor.ai.bot.MinimaxBot;
import quoridor.ai.bot.MinimaxTTBot;
import quoridor.ai.bot.mcts.ChildSelector;
import quoridor.ai.bot.mcts.MCTSBot;
import quoridor.ai.bot.mcts.ReducedWallsSimulator;
import quoridor.ai.hash.Zobrista;
import quoridor.ai.value_function.TopOpponentDistanceComparison;

public final class Experiment {
    private final List<Bot> bots = new ArrayList<>();
    private final List<String> names = new ArrayList<>();

    public int getSize() {
        return bots.size();
    }

    public Bot getBot(int i) {
        return bots.get(i);
    }

    public String getName(int i) {
        return names.get(i);
    }

    private void add(String name, Bot bot) {
        names.add(name);
        bots.add(bot);
    }

    private void addParameterless() {
        add("Minimax",
                new MinimaxBot(TopOpponentDistanceComparison.getInstance()));
        add("AlphaBeta",
                new AlphaBetaBot(TopOpponentDistanceComparison.getInstance()));
    }

    private void addBotsWithTT(int... tableSizes) {
        for (int tableSize : tableSizes) {
            add("MinimaxTT " + tableSize, new MinimaxTTBot(
                    TopOpponentDistanceComparison.getInstance(),
                    Zobrista.getInstance(),
                    tableSize));
            add("AlphaBetaTT " + tableSize, new AlphaBetaTTBot(
                    TopOpponentDistanceComparison.getInstance(),
                    Zobrista.getInstance(),
                    tableSize));
        }
    }

    private void addMcts(int[] expansionThresholds,
                         ChildSelector[] childSelectors,
                         int[] wallLimits) {
        for (int expansionThreshold : expansionThresholds) {
            for (ChildSelector childSelector : childSelectors) {
                String childSelectorString = "?";
                if (childSelector == ChildSelector.WITH_LOG_HOPE) {
                    childSelectorString = "LOG";
                } else if (childSelector == ChildSelector.WITH_SQRT_HOPE) {
                    childSelectorString = "SQRT";
                }
                for (int wallLimit : wallLimits) {
                    String name = "MCTSBot"
                            + " " + expansionThreshold
                            + " " + childSelectorString
                            + " " + wallLimit;
                    add(name, new MCTSBot(
                            expansionThreshold,
                            childSelector,
                            new ReducedWallsSimulator(wallLimit)
                    ));
                }
            }
        }
    }

    public static Experiment experiment1() {
        Experiment result = new Experiment();
        result.add("Minimax",
                new MinimaxBot(TopOpponentDistanceComparison.getInstance()));
        result.addMcts(
                new int[] {50, 100, 200},
                new ChildSelector[] {
                        ChildSelector.WITH_LOG_HOPE,
                        ChildSelector.WITH_SQRT_HOPE},
                new int[] {2, 8, 16}
        );
        return result;
    }

}
