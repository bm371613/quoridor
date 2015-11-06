package quoridor.gui;

import quoridor.gui.component.MainWindow;

import javax.swing.SwingUtilities;

public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow();
        new GameManager(mainWindow);
        SwingUtilities.invokeLater(() -> mainWindow.setVisible(true));
    }
}
