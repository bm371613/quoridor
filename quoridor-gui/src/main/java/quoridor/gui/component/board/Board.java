package quoridor.gui.component.board;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.function.Consumer;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import quoridor.core.Move;
import quoridor.core.direction.Directed;
import quoridor.core.direction.Direction;
import quoridor.core.direction.PerDirection;
import quoridor.core.state.GameState;
import quoridor.core.state.PlayerState;
import quoridor.core.state.WallOrientation;

public class Board extends JPanel implements ComponentListener {

    private static final int PLACES_SIZE = GameState.PLACES;
    private static final int WALLS_SIZE = GameState.WALL_PLACES;

    private final Place[][] places = new Place[PLACES_SIZE][PLACES_SIZE];
    private final Wall[][] horizontalWalls = new Wall[WALLS_SIZE][WALLS_SIZE];
    private final Wall[][] verticalWalls = new Wall[WALLS_SIZE][WALLS_SIZE];

    private PerDirection<Pawn> pawns = PerDirection.of((g) -> new Pawn());
    private PerDirection<JLabel> wallLabels = PerDirection.of((g) -> {
        JLabel result = new JLabel();
        result.setVerticalAlignment(SwingConstants.CENTER);
        result.setHorizontalAlignment(SwingConstants.CENTER);
        return result;
    });

    public Board() {
        setLayout(null);

        addComponentListener(this);

        for (int x = 0; x < PLACES_SIZE; ++x) {
            for (int y = 0; y < PLACES_SIZE; ++y) {
                Place place = new Place(Move.makePawnMove(x, y));
                add(place);
                places[x][y] = place;
            }
        }

        for (int x = 0; x < WALLS_SIZE; ++x) {
            for (int y = 0; y < WALLS_SIZE; ++y) {
                Wall horizontalWall = new Wall(Move.makeWallMove(x, y,
                        WallOrientation.HORIZONTAL));
                add(horizontalWall);
                horizontalWalls[x][y] = horizontalWall;

                Wall verticalWall = new Wall(Move.makeWallMove(x, y,
                        WallOrientation.VERTICAL));
                add(verticalWall);
                verticalWalls[x][y] = verticalWall;
            }
        }

        wallLabels.valueStream().forEach(this::add);
    }

    public void setPlayerColor(Directed goal, Color color) {
        pawns.get(goal).setColor(color);
        wallLabels.get(goal).setForeground(color);
    }

    public void loadGameState(GameState gs) {
        forEachPlace((p) -> {
            if (p.hasPawn()) {
                p.liftPawn();
            }
        });

        wallLabels.valueStream().forEach((wallLabel) -> wallLabel.setText(""));

        gs.getPlayerStates().forEach(this::loadPlayerState);

        for (int x = 0; x < WALLS_SIZE; ++x) {
            for (int y = 0; y < WALLS_SIZE; ++y) {
                WallOrientation w = gs.getWallsState().get(x, y);
                horizontalWalls[x][y].setBuilt(w == WallOrientation.HORIZONTAL);
                verticalWalls[x][y].setBuilt(w == WallOrientation.VERTICAL);
            }
        }
    }

    private void loadPlayerState(PlayerState state) {
        places[state.getX()][state.getY()].putPawn(pawns.get(state));
        wallLabels.get(state).setText("" + state.getWallsLeft());
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

    public void forEachPlace(Consumer<Place> consumer) {
        for (int x = 0; x < PLACES_SIZE; ++x) {
            for (int y = 0; y < PLACES_SIZE; ++y) {
                consumer.accept(places[x][y]);
            }
        }
    }

    public void forEachWall(Consumer<Wall> consumer) {
        for (int x = 0; x < WALLS_SIZE; ++x) {
            for (int y = 0; y < WALLS_SIZE; ++y) {
                consumer.accept(horizontalWalls[x][y]);
                consumer.accept(verticalWalls[x][y]);
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
        int margin = (boardSide - PLACES_SIZE * sideWithSpace + spaceBetween)
                / 2;

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

        wallLabels.get(Direction.TOP).setBounds(0, boardSide - margin,
                boardSide, margin);
        wallLabels.get(Direction.RIGHT).setBounds(0, 0, margin, boardSide);
        wallLabels.get(Direction.BOTTOM).setBounds(0, 0, boardSide, margin);
        wallLabels.get(Direction.LEFT).setBounds(boardSide - margin, 0, margin,
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
