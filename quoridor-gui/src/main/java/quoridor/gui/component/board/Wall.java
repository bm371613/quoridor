package quoridor.gui.component.board;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;

import quoridor.core.Move;
import quoridor.gui.event.EventListener;
import quoridor.gui.event.MoveChoiceEvent;
import quoridor.gui.event.MoveConsiderationEvent;

public class Wall extends JPanel implements Highlightable, MouseListener {

    private static final Color TRANSPARENT = new Color(0, 0, 0, 0);

    private EventListener eventListener;

    private Move move;
    private boolean built;
    private boolean highlighted;

    public Wall(Move move) {
        this.move = move;

        addMouseListener(this);
        setOpaque(false);
        setBuilt(false);
    }

    public Move getMove() {
        return move;
    }

    public boolean isBuilt() {
        return built;
    }

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void setBuilt(boolean built) {
        this.built = built;
        updateBackground();
    }

    @Override
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
        eventListener.notifyAboutEvent(this, new MoveChoiceEvent(move));
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (eventListener != null) {
            eventListener.notifyAboutEvent(this,
                    new MoveConsiderationEvent(move, this));
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setHighlighted(false);
    }
}
