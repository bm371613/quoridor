package quoridor.gui.component.board;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.function.Function;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import quoridor.core.state.GameState;
import quoridor.core.state.Goal;
import quoridor.core.state.WallOrientation;
import quoridor.core.state.PlayerState;
import quoridor.gui.event.PawnMoveConsiderationEvent;
import quoridor.gui.event.WallMoveConsiderationEvent;

public class Board extends JPanel implements ComponentListener {

    private static final int PLACES_SIZE = GameState.PLACES;
    private static final int WALLS_SIZE = GameState.WALL_PLACES;

    private Place[][] places = new Place[PLACES_SIZE][PLACES_SIZE];
    private Wall[][] horizontalWalls = new Wall[WALLS_SIZE][WALLS_SIZE];
    private Wall[][] verticalWalls = new Wall[WALLS_SIZE][WALLS_SIZE];

    private Color[] playerColors = {
            Color.BLACK, Color.GRAY, Color.BLUE, Color.RED
    };

    private Pawn[] pawns;
    private JLabel[] wallLabels;

    public Board() {
        setLayout(null);

        addComponentListener(this);

        for (int x = 0; x < PLACES_SIZE; ++x) {
            for (int y = 0; y < PLACES_SIZE; ++y) {
                Place place = new Place();
                place.setPawnMoveConsiderationEvent(
                        new PawnMoveConsiderationEvent(x, y)
                );
                add(place);
                places[x][y] = place;
            }
        }

        for (int x = 0; x < WALLS_SIZE; ++x) {
            for (int y = 0; y < WALLS_SIZE; ++y) {
                Wall horizontalWall = new Wall();
                horizontalWall.setWallMoveConsiderationEvent(
                        new WallMoveConsiderationEvent(x, y,
                                WallOrientation.HORIZONTAL)
                );
                add(horizontalWall);
                horizontalWalls[x][y] = horizontalWall;

                Wall verticalWall = new Wall();
                verticalWall.setWallMoveConsiderationEvent(
                        new WallMoveConsiderationEvent(x, y,
                                WallOrientation.VERTICAL)
                );
                add(verticalWall);
                verticalWalls[x][y] = verticalWall;
            }
        }

        pawns = new Pawn[playerColors.length];
        wallLabels = new JLabel[playerColors.length];
        for (int i = 0; i < playerColors.length; ++i) {
            pawns[i] = new Pawn(playerColors[i]);

            wallLabels[i] = new JLabel();
            wallLabels[i].setForeground(playerColors[i]);
            wallLabels[i].setVerticalAlignment(SwingConstants.CENTER);
            wallLabels[i].setHorizontalAlignment(SwingConstants.CENTER);
            add(wallLabels[i]);
        }
    }

    public void loadGameState(GameState gs) {
        forEachPlace((p) -> {
            if (p.hasPawn()) {
                p.liftPawn();
            }
            return null;
        });

        for (JLabel wallLabel : wallLabels) {
            wallLabel.setText("");
        }

        gs.getPlayerStates().forEach((player) -> {
            int ix = player.getGoal().ordinal();
            loadPlayerState(player, pawns[ix], wallLabels[ix]);
        });

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
        label.setText("" + state.getWallsLeft());
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

    public void forEachPlace(Function<Place, ?> function) {
        for (int x = 0; x < PLACES_SIZE; ++x) {
            for (int y = 0; y < PLACES_SIZE; ++y) {
                function.apply(places[x][y]);
            }
        }
    }

    public void forEachWall(Function<Wall, ?> function) {
        for (int x = 0; x < WALLS_SIZE; ++x) {
            for (int y = 0; y < WALLS_SIZE; ++y) {
                function.apply(horizontalWalls[x][y]);
                function.apply(verticalWalls[x][y]);
            }
        }
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

        wallLabels[Goal.TOP.ordinal()].setBounds(0, boardSide - margin,
                boardSide, margin);
        wallLabels[Goal.RIGHT.ordinal()].setBounds(0, 0, margin, boardSide);
        wallLabels[Goal.BOTTOM.ordinal()].setBounds(0, 0, boardSide, margin);
        wallLabels[Goal.LEFT.ordinal()].setBounds(boardSide - margin, 0, margin,
                boardSide);
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
