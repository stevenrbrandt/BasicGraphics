/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crossTheFields;

import basicgraphics.BasicFrame;
import basicgraphics.CollisionEventType;
import basicgraphics.Sprite;
import basicgraphics.SpriteCollisionEvent;
import basicgraphics.SpriteComponent;
import basicgraphics.images.Picture;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

/**
 *
 * @author theha
 */
public class Wall extends Sprite {
    int x1;
    int y1;
    int x2;
    int y2;
    public Wall(int x1, int y1, int x2, int y2, SpriteComponent sc) {
        super(sc);
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        Image im = BasicFrame.createImage(x2-x1, y2-y1);
        Graphics graphic = im.getGraphics();
        graphic.setColor(Main.WALL_COLOR);
        graphic.fillRect(0, 0, x2-x1, y2-y1);
        setX(x1);
        setY(y1);
        Picture p = new Picture(im);
        setPicture(p);
        
    }
}
