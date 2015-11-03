package quoridor.gui.component;

import quoridor.gui.event.EventListener;
import quoridor.gui.event.NewGameEvent;
import quoridor.gui.management.PlayerType;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import java.awt.Dimension;
import java.awt.Toolkit;

public class NewGameDialog extends JDialog {

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;

    private ButtonGroup numberOfPlayersButtonGroup = new ButtonGroup();
    private JRadioButton twoPlayersRadioButton;
    private JRadioButton fourPlayersRadioButton;

    private EventListener eventListener;


    public NewGameDialog(JFrame owner) {
        super(owner, "New Game");
        setModal(true);

        setSize(300, 300);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        setLocation((screenSize.width - getWidth()) / 2,
                (screenSize.height - getHeight()) / 2);


        setContentPane(contentPane);

        twoPlayersRadioButton.setSelected(true);
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
        eventListener.notifyAboutEvent(null,
                twoPlayersRadioButton.isSelected()
                ? NewGameEvent.gameForTwo()
                : NewGameEvent.gameForFour());
        setVisible(false);
    }
}
