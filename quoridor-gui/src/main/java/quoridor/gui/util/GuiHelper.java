package quoridor.gui.util;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;

public final class GuiHelper {
    private GuiHelper() {
    }

    public static void setLocationToCenter(Window window) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        window.setLocation((screenSize.width - window.getWidth()) / 2,
                (screenSize.height - window.getHeight()) / 2);
    }
}
