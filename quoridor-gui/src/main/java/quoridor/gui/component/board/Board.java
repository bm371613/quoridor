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
import quoridor.core.state.GameState;
import quoridor.core.state.Goal;
import quoridor.core.state.WallOrientation;
import quoridor.core.state.PlayerState;
import quoridor.gui.util.PerPlayer;

public class Board extends JPanel implements ComponentListener {

    private static final int PLACES_SIZE = GameState.PLACES;
    private static final int WALLS_SIZE = GameState.WALL_PLACES;

    private Place[][] places = new Place[PLACES_SIZE][PLACES_SIZE];
    private Wall[][] horizontalWalls = new Wall[WALLS_SIZE][WALLS_SIZE];
    private Wall[][] verticalWalls = new Wall[WALLS_SIZE][WALLS_SIZE];

    private PerPlayer<Color> playerColors = new PerPlayer<Color>()
            .set(Goal.TOP, Color.BLACK)
            .set(Goal.BOTTOM, Color.GRAY)
            .set(Goal.LEFT, Color.BLUE)
            .set(Goal.RIGHT, Color.RED);
    private PerPlayer<Pawn> pawns = playerColors.map(Pawn::new);
    private PerPlayer<JLabel> wallLabels = playerColors.map((c) -> {
        JLabel result = new JLabel();
        result.setForeground(c);
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

        wallLabels.forEach(this::add);
    }

    public void loadGameState(GameState gs) {
        forEachPlace((p) -> {
            if (p.hasPawn()) {
                p.liftPawn();
            }
        });

        wallLabels.forEach((wallLabel) -> wallLabel.setText(""));

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

        wallLabels.get(Goal.TOP).setBounds(0, boardSide - margin, boardSide,
                margin);
        wallLabels.get(Goal.RIGHT).setBounds(0, 0, margin, boardSide);
        wallLabels.get(Goal.BOTTOM).setBounds(0, 0, boardSide, margin);
        wallLabels.get(Goal.LEFT).setBounds(boardSide - margin, 0, margin,
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
