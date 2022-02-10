package tanks;

import basicgraphics.*;
import basicgraphics.images.Picture;

import java.awt.*;

public class Bullet extends Sprite {

    private final boolean isFriendly;
    private final Tank progenitor;

    public Bullet(SpriteComponent sc, Tank progenitor, double direction, double velocity, boolean isFriendly) {
        super(sc);
        this.isFriendly = isFriendly;
        this.progenitor = progenitor;

        setPicture(drawBullet());
        setDrawingPriority(-3); // Draw under the tanks

        setCenterX(progenitor.centerX());
        setCenterY(progenitor.centerY());

        setVelX(Math.cos(direction) * velocity);
        setVelY(Math.sin(direction) * velocity);
    }

    private Picture drawBullet() {
        Image im = BasicFrame.createImage(5, 5);
        Graphics g = im.getGraphics();

        g.setColor(isFriendly ? Color.BLUE : Color.RED);
        g.fillOval(0, 0, 5, 5);

        var p = new Picture(im);
        p.transparentWhite();
        return p;
    }

    @Override
    public void processEvent(SpriteCollisionEvent ev) {
        if (ev.xlo || ev.xhi || ev.ylo || ev.yhi) {
            setActive(false);
        }
    }

    public boolean isFriendly() {
        return isFriendly;
    }

    public Tank getProgenitor() {
        return progenitor;
    }
}
