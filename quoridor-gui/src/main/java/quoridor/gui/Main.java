package quoridor.gui;

import javax.swing.SwingUtilities;

import quoridor.gui.component.MainWindow;

public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow();
        new GameManager(mainWindow);
        SwingUtilities.invokeLater(() -> mainWindow.setVisible(true));
    }
}
