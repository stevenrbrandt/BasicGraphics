package tanks;

import basicgraphics.BasicFrame;
import basicgraphics.Sprite;
import basicgraphics.SpriteComponent;
import basicgraphics.images.Picture;

import java.awt.*;

public class Crosshair extends Sprite {
    public Crosshair(SpriteComponent sc) {
        super(sc);
        setPicture(drawCrosshair());
        setDrawingPriority(10);
    }

    private static Picture drawCrosshair() {
        Image im = BasicFrame.createImage(10, 10);
        Graphics g = im.getGraphics();

        g.setColor(Color.RED);
        g.fillOval(0, 0, 10, 10);

        var p = new Picture(im);
        p.transparentWhite();
        return p;
    }
}
