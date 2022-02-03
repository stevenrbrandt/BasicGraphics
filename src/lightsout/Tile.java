package lightsout;

import basicgraphics.BasicFrame;
import basicgraphics.Sprite;
import basicgraphics.SpriteComponent;
import basicgraphics.images.Picture;

import java.awt.*;
import java.awt.event.MouseEvent;

import static lightsout.LightsOutGame.TILE_SIZE;

public class Tile extends Sprite {
    public boolean lit;
    public final int x, y;
    private final LightsOutGame gameInstance;

    public Tile(SpriteComponent sc, LightsOutGame gameInstance, int x, int y, boolean startLit) {
        super(sc);
        this.lit = startLit;
        this.gameInstance = gameInstance;
        this.x = x;
        this.y = y;

        setPicture(makeTile(lit));
        setX(x * TILE_SIZE);
        setY(y * TILE_SIZE);
    }

    public void toggle() {
        lit = !lit;
        gameInstance.litTiles += lit ? 1 : -1;
        setPicture(makeTile(lit));
    }

    public void toggleWithNeighbors(boolean checkWin) {
        toggle();
        for (var neighbor : gameInstance.getNeighbors(x, y)) {
            neighbor.toggle();
        }
        if (checkWin) {
            gameInstance.checkWin();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        toggleWithNeighbors(true);
    }

    private static Picture makeTile(boolean lit) {
        Image im = BasicFrame.createImage(TILE_SIZE, TILE_SIZE);
        Graphics g = im.getGraphics();

        g.setColor(Color.RED);
        g.fillRect(0, 0, TILE_SIZE, TILE_SIZE);

        g.setColor(lit ? Color.WHITE : Color.BLACK);
        g.fillRect(1, 1, TILE_SIZE - 2, TILE_SIZE - 2);
        return new Picture(im);
    }
}
