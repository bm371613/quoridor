package quoridor.gui.component;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import javax.swing.*;

import lombok.Setter;

import quoridor.core.GameRules;
import quoridor.core.direction.Direction;
import quoridor.core.direction.PerDirection;
import quoridor.core.state.GameState;
import quoridor.gui.Game;
import quoridor.gui.event.EventListener;
import quoridor.gui.event.LoadGameEvent;
import quoridor.gui.util.GuiHelper;

public class StartGameDialog extends JDialog implements ActionListener {

    private final JRadioButton twoPlayersRadioButton =
            new JRadioButton("Two players");
    private final JRadioButton fourPlayersRadioButton =
            new JRadioButton("Four players");
    private final PerDirection<PlayerForm> playerForms = PerDirection.of(
            (g) -> new PlayerForm(g.ordinal(), g == Direction.UP));
    private final JButton startNewButton = new JButton("Start new game");
    private final JButton restoreButton = new JButton("Restore game state");
    private final JButton cancelButton = new JButton("Cancel");

    final JFileChooser restoreFileChooser = new JFileChooser(".");

    @Setter private EventListener eventListener;

    public StartGameDialog(JFrame owner) {
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

        getRootPane().setDefaultButton(startNewButton);
        startNewButton.addActionListener(e -> onStartNew());
        restoreButton.addActionListener(e -> onRestore());
        cancelButton.addActionListener(e -> dispose());
        setDefaultCloseOperation(HIDE_ON_CLOSE);
    }

    private void onStartNew() {
        GameState gs = twoPlayersRadioButton.isSelected()
                ? GameRules.makeInitialStateForTwo()
                : GameRules.makeInitialStateForFour();
        eventListener.notifyAboutEvent(null, new LoadGameEvent(new Game(gs,
                playerForms.map(PlayerForm::makePlayer))));
        setVisible(false);
    }

    private void onRestore() {
        if (restoreFileChooser.showOpenDialog(this)
                != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File file = restoreFileChooser.getSelectedFile();
        GameState gs;
        try {
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            gs = (GameState) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Failed to restore game state from selected file.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }
        if ((gs.getPlayerStates().size() == 2)
                ^ twoPlayersRadioButton.isSelected()) {
            JOptionPane.showMessageDialog(this,
                    "Number of players in game state and configuration differ.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
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
        c.weightx = 0;
        c.weighty = 0;
        c.gridy = 2;
        c.gridwidth = 1;

        c.gridx = 0;
        contentPane.add(startNewButton, c);

        c.gridx = 1;
        contentPane.add(restoreButton, c);

        c.gridx = 2;
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
