package quoridor.gui.component.board;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;

public class Wall extends JPanel implements MouseListener {

    private static final Color TRANSPARENT = new Color(0, 0, 0, 0);

    private boolean built;
    private boolean highlighted;

    public Wall() {
        setOpaque(false);
        addMouseListener(this);
        setBuilt(false);
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

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        setHighlighted(true); // TODO
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setHighlighted(false); // TODO
    }
}
