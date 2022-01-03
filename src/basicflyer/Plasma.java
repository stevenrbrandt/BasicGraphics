/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basicflyer;

import basicgraphics.BasicFrame;
import basicgraphics.Sprite;
import basicgraphics.SpriteCollisionEvent;
import basicgraphics.SpriteComponent;
import basicgraphics.images.Picture;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

/**
 *
 * @author sbrandt
 */
public class Plasma extends Sprite {

    /**
     * Creates a picture of a ball with the given color and size.
     *
     * @param color
     * @param size
     * @return
     */
    public static Picture makeBall(Color color, int size) {
        Image im = BasicFrame.createImage(size, size);
        Graphics g = im.getGraphics();
        g.setColor(color);
        g.fillOval(0, 0, size, size);
        return new Picture(im);
    }

    /**
     * Just sets the picture.
     *
     * @param sc
     */
    public Plasma(SpriteComponent sc) {
        super(sc);
        setPicture(makeBall(Color.red, 10));
    }

    /**
     * Disappears if it comes in contact with the display boundary.
     *
     * @param se
     */
    @Override
    public void processEvent(SpriteCollisionEvent se) {
        setActive(false);
    }
}
