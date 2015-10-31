package quoridor.gui.component;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import javax.swing.JPanel;

public class Pawn extends JPanel {

    private Color color;

    public Pawn(Color color) {
        setOpaque(false);
        this.color = color;
    }

    public void lift() {
        Container parent = getParent();
        if (parent != null) {
            parent.remove(this);
            parent.repaint();
            parent.revalidate();
        }
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
