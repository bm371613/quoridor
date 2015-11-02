package quoridor.gui.component.board;

import quoridor.gui.event.WallMoveConsiderationEvent;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class Wall extends JPanel {

    private static final Color TRANSPARENT = new Color(0, 0, 0, 0);

    private WallMoveConsiderationEvent wallMoveConsiderationEvent;
    private boolean built;
    private boolean highlighted;

    public Wall() {
        setOpaque(false);
        setBuilt(false);
    }

    public void setWallMoveConsiderationEvent(WallMoveConsiderationEvent e) {
        wallMoveConsiderationEvent = e;
    }

    public WallMoveConsiderationEvent getWallMoveConsiderationEvent() {
        return wallMoveConsiderationEvent;
    }

    public void setBuilt(boolean built) {
        this.built = built;
        updateBackground();
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
        updateBackground();
    }

    private void updateBackground() {
        if (built) {
            setBackground(Color.BLACK);
        } else if (highlighted) {
            setBackground(Color.LIGHT_GRAY);
        } else {
            setBackground(TRANSPARENT);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        // that is a hack which makes transparent backgrounds repaint correctly
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }
}
