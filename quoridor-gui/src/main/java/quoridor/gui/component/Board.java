package quoridor.gui.component;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JLabel;
import javax.swing.JPanel;

import quoridor.core.GameState;
import quoridor.core.GrooveCrossState;
import quoridor.core.PlayerState;

public class Board extends JPanel implements ComponentListener {

    private static final int PLACES_SIZE = GameState.BOARD_SIZE;
    private static final int WALLS_SIZE = GameState.BOARD_SIZE - 1;

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
                add(place);
                places[x][y] = place;
            }
        }

        for (int x = 0; x < PLACES_SIZE - 1; ++x) {
            for (int y = 0; y < PLACES_SIZE - 1; ++y) {
                Wall verticalWall = new Wall();
                add(verticalWall);
                verticalWalls[x][y] = verticalWall;

                Wall horizontalWall = new Wall();
                add(horizontalWall);
                horizontalWalls[x][y] = horizontalWall;
            }
        }

        add(topWallsLabel);
        add(bottomWallsLabel);
    }

    public void loadGameState(GameState gameState) {
        loadPlayerState(gameState.getTopPlayerState(),
                topPlayersPawn, topWallsLabel);
        loadPlayerState(gameState.getBottomPlayerState(),
                bottomPlayersPawn, bottomWallsLabel);

        GrooveCrossState[][] gcss = gameState.getGrooveCrossStates();
        for (int x = 0; x < PLACES_SIZE - 1; ++x) {
            for (int y = 0; y < PLACES_SIZE - 1; ++y) {
                horizontalWalls[x][y].setBuilt(
                        GrooveCrossState.HORIZONTAL_WALL.equals(gcss[x][y]));
                verticalWalls[x][y].setBuilt(
                        GrooveCrossState.VERTICAL_WALL.equals(gcss[x][y]));
            }
        }
    }

    private void loadPlayerState(PlayerState state, Pawn pawn, JLabel label) {
        pawn.lift();
        places[state.getPosX()][state.getPosY()].putPawn(pawn);
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
}
