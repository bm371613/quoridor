package quoridor.gui;

import quoridor.gui.component.MainWindow;
import quoridor.gui.management.GameManager;

import javax.swing.SwingUtilities;

public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        GameManager gameManager = new GameManager(new MainWindow());
        SwingUtilities.invokeLater(
                () -> gameManager.getMainWindow().setVisible(true));
    }
}
