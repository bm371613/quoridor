package quoridor.gui.component.board;

import quoridor.gui.event.PawnMoveConsiderationEvent;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class Place extends JPanel implements ComponentListener {

    private static final Color TRANSPARENT = new Color(0, 0, 0, 0);

    private Pawn pawn;
    private PawnMoveConsiderationEvent pawnMoveConsiderationEvent;

    public Place() {
        setOpaque(false);
        setLayout(null);
        addComponentListener(this);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    public PawnMoveConsiderationEvent getPawnMoveConsiderationEvent() {
        return pawnMoveConsiderationEvent;
    }

    public void setPawnMoveConsiderationEvent(
            PawnMoveConsiderationEvent pawnMoveConsiderationEvent) {
        this.pawnMoveConsiderationEvent = pawnMoveConsiderationEvent;
    }

    public void putPawn(Pawn pawn) {
        if (this.pawn != null) {
            throw new RuntimeException("Cannot put pawn on non-empty place");
        }
        this.pawn = pawn;
        setPawnSize();
        add(pawn);
        revalidate();
        repaint();
    }

    public Pawn liftPawn() {
        Pawn pawn = this.pawn;
        if (pawn == null) {
            throw new RuntimeException("Cannot lift pawn from empty place");
        }
        this.pawn = null;
        remove(pawn);
        revalidate();
        repaint();
        return pawn;
    }

    private void setPawnSize() {
        int w = getWidth();
        int h = getHeight();
        pawn.setBounds(w / 4, h / 4, w / 2, h / 2);
    }

    public void setHighlighted(boolean highlighted) {
        if (highlighted) {
            setBackground(Color.LIGHT_GRAY);
        } else {
            setBackground(TRANSPARENT);
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        // that is a hack which makes transparent backgrounds repaint correctly
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }

    // ComponentListener

    @Override
    public void componentResized(ComponentEvent e) {
        if (pawn != null) {
            setPawnSize();
        }
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }
}
