package quoridor.gui.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import quoridor.core.GameRules;
import quoridor.core.state.GameState;
import quoridor.core.state.Goal;
import quoridor.gui.event.EventListener;
import quoridor.gui.event.NewGameEvent;
import quoridor.gui.player.BotPlayer;
import quoridor.gui.player.Human;
import quoridor.gui.player.Player;
import quoridor.gui.util.PerPlayer;

public class NewGameDialog extends JDialog {

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;

    private JRadioButton twoPlayersRadioButton;
    private JRadioButton fourPlayersRadioButton;

    private EventListener eventListener;

    private PerPlayer<Color> colors = new PerPlayer<Color>()
            .set(Goal.TOP, Color.BLACK)
            .set(Goal.BOTTOM, Color.GRAY)
            .set(Goal.LEFT, Color.BLUE)
            .set(Goal.RIGHT, Color.RED);


    public NewGameDialog(JFrame owner) {
        super(owner, "New Game");
        setModal(true);

        setSize(300, 300);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        setLocation((screenSize.width - getWidth()) / 2,
                (screenSize.height - getHeight()) / 2);

        contentPane = new JPanel(new GridBagLayout());
        setContentPane(contentPane);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;

        twoPlayersRadioButton = new JRadioButton("Two players");
        c.gridx = 0;
        c.gridy = 0;
        contentPane.add(twoPlayersRadioButton, c);

        fourPlayersRadioButton = new JRadioButton("Four players");
        c.gridx = 1;
        c.gridy = 0;
        contentPane.add(fourPlayersRadioButton, c);

        c.anchor = GridBagConstraints.PAGE_END;
        c.weighty = 1.0;

        buttonOK = new JButton("OK");
        c.gridx = 0;
        c.gridy = 1;
        contentPane.add(buttonOK, c);

        buttonCancel = new JButton("Cancel");
        c.gridx = 1;
        c.gridy = 1;
        contentPane.add(buttonCancel, c);

        twoPlayersRadioButton.setSelected(true);
        ButtonGroup numberOfPlayersButtonGroup = new ButtonGroup();
        numberOfPlayersButtonGroup.add(twoPlayersRadioButton);
        numberOfPlayersButtonGroup.add(fourPlayersRadioButton);

        getRootPane().setDefaultButton(buttonOK);
        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> dispose());
        setDefaultCloseOperation(HIDE_ON_CLOSE);
    }

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    private void onOK() {
        GameState gs = twoPlayersRadioButton.isSelected()
                ? GameRules.makeInitialStateForTwo()
                : GameRules.makeInitialStateForFour();

        PerPlayer<Player> players = PerPlayer.of(
                (g) -> g == Goal.TOP
                        ? new Human(g.name(), colors.get(g))
                        : new BotPlayer(g.name(), colors.get(g), null));
        eventListener.notifyAboutEvent(null, new NewGameEvent(gs, players));
        setVisible(false);
    }
}
