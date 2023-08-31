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
import java.awt.Graphics;
import java.awt.Image;

/**
 *
 * @author theha
 */
public class SquareEnemy extends Sprite {
    
    public SquareEnemy(int startX, int startY, SpriteComponent sc) {
        super(sc);
        Image im = BasicFrame.createImage(20, 20);
        Graphics graphic = im.getGraphics();
        graphic.setColor(Main.SQUARE_COLOR);
        graphic.fillRect(0, 0, 20, 20);
        setX(startX);
        setY(startY);
        Picture p = new Picture(im);
        setPicture(p);
    }
    public void shoot(Soldier soldier, SpriteComponent sc) {
        EnemyBullet nmebullet = new EnemyBullet(sc, getX(), getY(), soldier);
    }
}
