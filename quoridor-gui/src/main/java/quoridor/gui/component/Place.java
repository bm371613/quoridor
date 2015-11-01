package quoridor.gui.component;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class Place extends JPanel implements ComponentListener {

    private Pawn pawn;

    public Place() {
        setLayout(new GridBagLayout());
        addComponentListener(this);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    public void putPawn(Pawn pawn) {
        if (this.pawn != null) {
            throw new RuntimeException("Cannot put pawn on non-empty place");
        }
        this.pawn = pawn;
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

    // ComponentListener

    @Override
    public void componentResized(ComponentEvent e) {
        if (pawn != null) {
            int w = getWidth();
            int h = getHeight();
            pawn.setBounds(w / 4, h / 4, w / 2, h / 2);
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
