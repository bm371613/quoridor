package quoridor.gui.component.board;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

import lombok.Setter;

public class Pawn extends JPanel {

    @Setter private Color color;

    public Pawn() {
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.black);
        g.fillOval(0, 0, g.getClipBounds().width, g.getClipBounds().height);
        g.setColor(color);
        g.fillOval(2, 2, g.getClipBounds().width - 4,
                g.getClipBounds().height - 4);
    }
}
