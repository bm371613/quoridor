package quoridor.gui.component.board;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JLabel;
import javax.swing.JPanel;

import quoridor.core.state.GameState;
import quoridor.core.state.WallOrientation;
import quoridor.core.state.PlayerState;
import quoridor.gui.event.EventListener;
import quoridor.gui.event.PawnMoveConsiderationEvent;
import quoridor.gui.event.WallMoveConsiderationEvent;

public class Board extends JPanel implements ComponentListener, MouseListener {

    private static final int PLACES_SIZE = GameState.PLACES;
    private static final int WALLS_SIZE = GameState.WALL_PLACES;

    private EventListener eventListener;

    private Place[][] places = new Place[PLACES_SIZE][PLACES_SIZE];
    private Wall[][] horizontalWalls = new Wall[WALLS_SIZE][WALLS_SIZE];
    private Wall[][] verticalWalls = new Wall[WALLS_SIZE][WALLS_SIZE];

    private Pawn topPlayersPawn = new Pawn(Color.BLACK);
    private Pawn bottomPlayersPawn = new Pawn(Color.WHITE);

    private JLabel topWallsLabel = new JLabel();
    private JLabel bottomWallsLabel = new JLabel();

    public Board() {
        setLayout(null);

        addComponentListener(this);

        for (int x = 0; x < PLACES_SIZE; ++x) {
            for (int y = 0; y < PLACES_SIZE; ++y) {
                Place place = new Place();
                place.addMouseListener(this);
                place.setPawnMoveConsiderationEvent(
                        new PawnMoveConsiderationEvent(x, y, place)
                );
                add(place);
                places[x][y] = place;
            }
        }

        for (int x = 0; x < PLACES_SIZE - 1; ++x) {
            for (int y = 0; y < PLACES_SIZE - 1; ++y) {
                Wall horizontalWall = new Wall();
                horizontalWall.addMouseListener(this);
                horizontalWall.setWallMoveConsiderationEvent(
                        new WallMoveConsiderationEvent(x, y,
                                WallOrientation.HORIZONTAL, horizontalWall)
                );
                add(horizontalWall);
                horizontalWalls[x][y] = horizontalWall;

                Wall verticalWall = new Wall();
                verticalWall.addMouseListener(this);
                verticalWall.setWallMoveConsiderationEvent(
                        new WallMoveConsiderationEvent(x, y,
                                WallOrientation.VERTICAL, verticalWall)
                );
                add(verticalWall);
                verticalWalls[x][y] = verticalWall;
            }
        }

        add(topWallsLabel);
        add(bottomWallsLabel);
    }

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void loadGameState(GameState gs) {
        loadPlayerState(gs.getTopPlayersState(),
                topPlayersPawn, topWallsLabel);
        loadPlayerState(gs.getBottomPlayersState(),
                bottomPlayersPawn, bottomWallsLabel);

        for (int x = 0; x < WALLS_SIZE; ++x) {
            for (int y = 0; y < WALLS_SIZE; ++y) {
                WallOrientation w = gs.getWallsState().get(x, y);
                horizontalWalls[x][y].setBuilt(w == WallOrientation.HORIZONTAL);
                verticalWalls[x][y].setBuilt(w == WallOrientation.VERTICAL);
            }
        }
    }

    private void loadPlayerState(PlayerState state, Pawn pawn, JLabel label) {
        Place currentPawnPlace = (Place) pawn.getParent();
        if (currentPawnPlace != null) {
            currentPawnPlace.liftPawn();
        }
        places[state.getX()][state.getY()].putPawn(pawn);
        label.setText("Walls left: " + state.getWallsLeft());
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension d;
        Container c = getParent();
        if (c != null) {
            d = c.getSize();
        } else {
            return null;
        }
        int w = (int) d.getWidth();
        int h = (int) d.getHeight();
        int s = (w < h ? w : h);
        return new Dimension(s, s);
    }

    // ComponentListener

    @Override
    public void componentResized(ComponentEvent e) {
        int boardSide = getHeight();
        int placeSide = boardSide / 14;
        int spaceBetween = placeSide / 2;
        int sideWithSpace = placeSide + spaceBetween;
        int margin =
                (boardSide - PLACES_SIZE * sideWithSpace + spaceBetween) / 2;

        for (int x = 0; x < PLACES_SIZE; ++x) {
            for (int y = 0; y < PLACES_SIZE; ++y) {
                places[x][y].setBounds(
                        margin + x * sideWithSpace,
                        margin + (PLACES_SIZE - 1 - y) * sideWithSpace,
                        placeSide, placeSide);
            }
        }

        int wallLength = 2 * placeSide + spaceBetween;
        int wallThickness = spaceBetween / 3;
        int wallMargin = (spaceBetween - wallThickness) / 2;

        for (int x = 0; x < WALLS_SIZE; ++x) {
            for (int y = 0; y < WALLS_SIZE; ++y) {
                horizontalWalls[x][y].setBounds(
                        margin + x * sideWithSpace,
                        margin + (WALLS_SIZE - y) * sideWithSpace
                                - wallMargin - wallThickness,
                        wallLength, wallThickness);
                verticalWalls[x][y].setBounds(
                        margin + (x + 1) * sideWithSpace
                                - wallMargin - wallThickness,
                        margin + (WALLS_SIZE - 1 - y) * sideWithSpace,
                        wallThickness, wallLength);
            }
        }

        topWallsLabel.setBounds(0, 0, boardSide, margin);
        bottomWallsLabel.setBounds(0, boardSide - margin, boardSide, margin);
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

    // MouseListener

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
        if (e.getSource() instanceof Wall) {
            Wall wall = (Wall) e.getSource();
            eventListener.notifyAboutEvent(
                    wall.getWallMoveConsiderationEvent());
        } else if (e.getSource() instanceof Place) {
            Place place = (Place) e.getSource();
            eventListener.notifyAboutEvent(
                    place.getPawnMoveConsiderationEvent()
            );
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (e.getSource() instanceof Wall) {
            Wall wall = (Wall) e.getSource();
            wall.setHighlighted(false);
        } else if (e.getSource() instanceof Place) {
            Place place = (Place) e.getSource();
            place.setHighlighted(false);
        }
    }
}
