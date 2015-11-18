package quoridor.gui.component;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import lombok.Setter;

import quoridor.core.GameRules;
import quoridor.core.direction.Direction;
import quoridor.core.direction.PerDirection;
import quoridor.core.state.GameState;
import quoridor.gui.Game;
import quoridor.gui.event.EventListener;
import quoridor.gui.event.LoadGameEvent;
import quoridor.gui.util.GuiHelper;

public class NewGameDialog extends JDialog implements ActionListener {

    private final JRadioButton twoPlayersRadioButton =
            new JRadioButton("Two players");
    private final JRadioButton fourPlayersRadioButton =
            new JRadioButton("Four players");
    private final PerDirection<PlayerForm> playerForms = PerDirection.of(
            (g) -> new PlayerForm(g.ordinal(), g == Direction.UP));
    private final JButton okButton = new JButton("OK");
    private final JButton cancelButton = new JButton("Cancel");

    @Setter private EventListener eventListener;

    public NewGameDialog(JFrame owner) {
        super(owner, "New Game");
        setModal(true);

        setSize(700, 400);
        GuiHelper.setLocationToCenter(this);

        setupLayout();

        ButtonGroup numberOfPlayersButtonGroup = new ButtonGroup();
        numberOfPlayersButtonGroup.add(twoPlayersRadioButton);
        numberOfPlayersButtonGroup.add(fourPlayersRadioButton);
        twoPlayersRadioButton.addActionListener(this);
        fourPlayersRadioButton.addActionListener(this);
        fourPlayersRadioButton.setSelected(true);

        getRootPane().setDefaultButton(okButton);
        okButton.addActionListener(e -> onOK());
        cancelButton.addActionListener(e -> dispose());
        setDefaultCloseOperation(HIDE_ON_CLOSE);
    }

    private void onOK() {
        GameState gs = twoPlayersRadioButton.isSelected()
                ? GameRules.makeInitialStateForTwo()
                : GameRules.makeInitialStateForFour();
        eventListener.notifyAboutEvent(null, new LoadGameEvent(new Game(gs,
                playerForms.map(PlayerForm::makePlayer))));
        setVisible(false);
    }

    private void setupLayout() {
        JPanel contentPane = new JPanel(new GridBagLayout());
        setContentPane(contentPane);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);

        // top: radio buttons
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.weighty = 0;
        c.gridy = 0;

        c.gridx = 0;
        contentPane.add(twoPlayersRadioButton, c);

        c.gridx = 1;
        contentPane.add(fourPlayersRadioButton, c);

        // middle: player forms
        c.fill = GridBagConstraints.BOTH;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.gridwidth = 2;

        JPanel formsPane = new JPanel(new GridBagLayout());
        GridBagConstraints fc = new GridBagConstraints();
        fc.insets = new Insets(10, 10, 10, 10);
        fc.fill = GridBagConstraints.NONE;
        fc.weightx = 1;
        fc.weighty = 1;

        fc.gridy = 1;
        fc.gridx = 0;
        formsPane.add(playerForms.get(Direction.RIGHT), fc);
        fc.gridx = 1;
        formsPane.add(playerForms.get(Direction.LEFT), fc);

        fc.gridwidth = 2;
        fc.gridx = 0;
        fc.gridy = 0;
        formsPane.add(playerForms.get(Direction.DOWN), fc);

        fc.gridy = 2;
        formsPane.add(playerForms.get(Direction.UP), fc);

        c.gridx = 0;
        contentPane.add(formsPane, c);

        // bottom: buttons
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.PAGE_END;
        c.weightx = 0.5;
        c.weighty = 0;
        c.gridy = 2;
        c.gridwidth = 1;

        c.gridx = 0;
        contentPane.add(okButton, c);

        c.gridx = 1;
        contentPane.add(cancelButton, c);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == twoPlayersRadioButton) {
            playerForms.get(Direction.LEFT).setVisible(false);
            playerForms.get(Direction.RIGHT).setVisible(false);
        } else if (e.getSource() == fourPlayersRadioButton) {
            playerForms.get(Direction.LEFT).setVisible(true);
            playerForms.get(Direction.RIGHT).setVisible(true);
        }
    }
}
