package lightsout;

import basicgraphics.BasicFrame;
import basicgraphics.Clock;
import basicgraphics.SpriteComponent;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

public class LightsOutGame {
    public static final int TILE_SIZE = 75;
    public static final int N_TILES = 3;
    public static final int PROBLEM_STEPS = 10;
    public static final Dimension WINDOW_SIZE = new Dimension(TILE_SIZE * N_TILES,TILE_SIZE * N_TILES);

    private SpriteComponent sc;
    private final BasicFrame frame = new BasicFrame("Lights Out");

    Tile[][] tiles;
    int litTiles = 0;

    public void run() {
        sc = new SpriteComponent();
        sc.setPreferredSize(WINDOW_SIZE);
        String[][] layout = {{"center"}};
        frame.setStringLayout(layout);
        frame.add("center", sc);

        tiles = new Tile[N_TILES][N_TILES];
        for (int i = 0; i < N_TILES; i++) {
            tiles[i] = new Tile[N_TILES];
            for (int j = 0; j < N_TILES; j++) {
                tiles[i][j] = new Tile(sc, this, j, i, false);
            }
        }

        generateProblem();

        frame.show();
        Clock.start(1);
        Clock.addTask(sc.moveSprites());
    }

    // Generating the board with a sequence of toggleWithNeighbors guarantees the board is solvable
    public void generateProblem() {
        var rand = new Random();
        for (int i = 0; i < PROBLEM_STEPS; i++) {
            var tile = tiles[rand.nextInt(N_TILES)][rand.nextInt(N_TILES)];
            tile.toggleWithNeighbors(false);
        }
    }

    public LinkedList<Tile> getNeighbors(int x, int y) {
        var l = new LinkedList<Tile>();
        tryAddTile(x - 1, y, l);
        tryAddTile(x + 1, y, l);
        tryAddTile(x, y - 1, l);
        tryAddTile(x, y + 1, l);
        return l;
    }

    public void tryAddTile(int x, int y, LinkedList<Tile> list) {
        if (x >= 0 && x < N_TILES && y >= 0 && y < N_TILES) {
            list.add(tiles[y][x]);
        }
    }

    public void checkWin() {
        if (litTiles == 0) {
            JOptionPane.showMessageDialog(sc, "You're winner!");
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        var game = new LightsOutGame();
        game.run();
    }
}
