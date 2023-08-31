/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crossTheFields;

import basicgraphics.BasicFrame;
import basicgraphics.Sprite;
import basicgraphics.SpriteComponent;
import basicgraphics.images.Picture;
import basicgraphics.sounds.ReusableClip;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

/**
 *
 * @author theha
 */
public class TriangleEnemy extends Sprite {
    Picture pic;
    
    public TriangleEnemy(int startX, int startY, SpriteComponent sc) {
        
        super(sc);
        Image im = BasicFrame.createImage(20, 20);
        Graphics graphic = im.getGraphics();
        graphic.setColor(Main.TRIANGLE_COLOR);
        int[] xPoints = {4 ,16, 10};
        int[] yPoints = {20, 20, 0};
        graphic.fillPolygon(xPoints, yPoints, 3);
        graphic.setColor(Color.red);
        graphic.drawLine(10, 0, 10, 20);
        setX(startX);
        setY(startY);
        pic = new Picture(im);
        setPicture(pic);
        
    }
    
    public void adjust(Soldier soldier) {
        double deltaX = (getX() - soldier.getX());
        double deltaY = (getY() - soldier.getY());
        
        double newHeading = Math.atan2(deltaY, deltaX);
        setVelY((Math.sin(newHeading+(Math.PI))) / 2);
        setVelX((Math.cos(newHeading + (Math.PI))) / 2);
        
        setPicture(pic.rotate(newHeading - (Math.PI / 2)));
    }
    
    /*
    public void turn(double incr) {
        double heading = Math.atan2(getVelY(),getVelX());
        heading += incr;
        setVelY(Math.sin(heading));
        setVelX(Math.cos(heading));
        setPicture(initialPic.rotate(heading));
    }
    */
}
