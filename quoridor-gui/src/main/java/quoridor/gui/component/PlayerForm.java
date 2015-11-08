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

import quoridor.ai.Bot;
import quoridor.ai.simple.GreedyBot;
import quoridor.ai.simple.RandomBot;
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
                    namedBotMaker("GreedyBot", new GreedyBot()),
                    namedBotMaker("RandomBot", new RandomBot()));

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

    private static Named<PlayerMaker> namedBotMaker(String name, Bot bot) {
        return new Named<>(name, (PlayerMaker) (n, c) ->
                new BotPlayer(n, c, bot));
    }

    @Value
    private static class Named<V> {
        private final String name;
        private final V value;

        public String toString() {
            return name;
        }
    }

    private static interface PlayerMaker {
        Player makePlayer(String name, Color color);
    }
}