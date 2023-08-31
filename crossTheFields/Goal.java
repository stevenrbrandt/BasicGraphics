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
import java.awt.Graphics;
import java.awt.Image;

/**
 *
 * @author theha
 */
public class Goal extends Sprite {
    public Goal(SpriteComponent sc) {
        super(sc);
        Image im = BasicFrame.createImage(20, 100);
        Graphics graphic = im.getGraphics();
        graphic.setColor(java.awt.Color.orange);
        graphic.fillRect(0, 0, 20, 100);
        setX(780);
        setY(250);
        Picture p = new Picture(im);
        setPicture(p);
    }
}
