package quoridor.gui.component;

import quoridor.gui.management.PlayerType;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

public class NewGameDialog extends JDialog {

    JButton okButton = new JButton("OK");

    public NewGameDialog(JFrame owner) {
        super(owner, "New Game");
        setDefaultCloseOperation(HIDE_ON_CLOSE);

        setSize(200, 200);

        // center
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        setLocation((screenSize.width - getWidth()) / 2,
                (screenSize.height - getHeight()) / 2);

        JPanel buttonPane = new JPanel();
        buttonPane.add(okButton);
        getContentPane().add(buttonPane, BorderLayout.PAGE_END);
    }

    public JButton getOkButton() {
        return okButton;
    }

    public PlayerType getTopPlayerType() {
        return PlayerType.HUMAN;
    }

    public PlayerType getBottomPlayerType() {
        return PlayerType.HUMAN;
    }
}
