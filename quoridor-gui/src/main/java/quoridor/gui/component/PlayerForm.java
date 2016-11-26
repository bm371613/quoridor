package quoridor.gui.component;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.Vector;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.google.common.collect.ImmutableList;
import lombok.Value;

import quoridor.ai.bot.AlphaBetaBot;
import quoridor.ai.bot.AlphaBetaTTBot;
import quoridor.ai.bot.Bot;
import quoridor.ai.bot.GreedyBot;
import quoridor.ai.bot.MinimaxBot;
import quoridor.ai.bot.MinimaxTTBot;
import quoridor.ai.bot.mcts.ChildSelector;
import quoridor.ai.bot.mcts.MCTSBot;
import quoridor.ai.bot.mcts.RandomSimulator;
import quoridor.ai.hash.Zobrista;
import quoridor.ai.value_function.TopOpponentDistanceComparison;
import quoridor.gui.player.BotPlayer;
import quoridor.gui.player.Human;
import quoridor.gui.player.Player;

public class PlayerForm extends JPanel {

    private static final ImmutableList<Named<Color>> COLORS =
            ImmutableList.of(
                    new Named<>("Black", Color.BLACK),
                    new Named<>("Red", Color.RED),
                    new Named<>("Green", Color.GREEN),
                    new Named<>("Blue", Color.BLUE),
                    new Named<>("Gray", Color.GRAY));

    private static final ImmutableList<Named<PlayerMaker>> PLAYER_TYPES =
            ImmutableList.of(
                    new Named<>("Human", (PlayerMaker) Human::new),
                    namedBotPlayerMaker("MCTS-30-Log-Random-40", () ->
                            new MCTSBot(30, ChildSelector.WITH_LOG_HOPE,
                                    new RandomSimulator(40))),
                    namedBotPlayerMaker("AlphaBetaTT", () -> new AlphaBetaTTBot(
                            TopOpponentDistanceComparison.getInstance(),
                            Zobrista.getInstance(),
                            4 * 1024 * 1024)),
                    namedBotPlayerMaker("AlphaBeta", () -> new AlphaBetaBot(
                            TopOpponentDistanceComparison.getInstance())),
                    namedBotPlayerMaker("MinimaxTT", () -> new MinimaxTTBot(
                            TopOpponentDistanceComparison.getInstance(),
                            Zobrista.getInstance(),
                            4 * 1024 * 1024)),
                    namedBotPlayerMaker("Minimax", () -> new MinimaxBot(
                            TopOpponentDistanceComparison.getInstance())),
                    namedBotPlayerMaker("GreedyBot", () -> new GreedyBot(
                            TopOpponentDistanceComparison.getInstance()))
                    );

    private final JTextField nameField = new JTextField();
    private final JComboBox<Named<Color>> colorChooser = new JComboBox<>(
            new Vector<>(COLORS));
    private final JComboBox<Named<PlayerMaker>> typeComboBox = new JComboBox<>(
            new Vector<>(PLAYER_TYPES));

    public PlayerForm(int ix, boolean human) {
        setLayout(new GridLayout(3, 2, 10, 10));
        add(new JLabel("Type: "));
        add(typeComboBox);
        add(new JLabel("Name: "));
        add(nameField);
        add(new JLabel("Color: "));
        add(colorChooser);
        nameField.setText(COLORS.get(ix).getName());
        colorChooser.setSelectedIndex(ix);
        typeComboBox.setSelectedIndex(human ? 0 : 1);
    }

    @SuppressWarnings("unchecked")
    public Player makePlayer() {
        Color color = ((Named<Color>) colorChooser.getSelectedItem())
                .getValue();
        return ((Named<PlayerMaker>) typeComboBox.getSelectedItem()).getValue()
                .makePlayer(nameField.getText(), color);
    }

    private static Named<PlayerMaker> namedBotPlayerMaker(String name,
                                                          BotMaker botMaker) {
        return new Named<>(name, (PlayerMaker) (n, c) ->
                new BotPlayer(n, c, botMaker.makeBot()));
    }

    @Value
    private static class Named<V> {
        private final String name;
        private final V value;

        public String toString() {
            return name;
        }
    }

    private static interface BotMaker {
        Bot makeBot();
    }

    private static interface PlayerMaker {
        Player makePlayer(String name, Color color);
    }
}
