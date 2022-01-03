/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package robosorter;

import basicgraphics.BasicFrame;
import basicgraphics.Sprite;
import basicgraphics.SpriteComponent;
import basicgraphics.images.Picture;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author sbrandt
 */
public class MartianEgg extends Sprite {
    final int num;
    public MartianEgg(SpriteComponent sc,int num) {
        super(sc);
        this.num = num;
        BufferedImage image = BasicFrame.createImage(Robo.BLOCK_SIZE, Robo.BLOCK_SIZE);
        Graphics g = image.getGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, Robo.BLOCK_SIZE, Robo.BLOCK_SIZE);
        g.setColor(new Color(0xEE,0xEE,0xFF));
        g.fillOval(0, 0, Robo.BLOCK_SIZE, Robo.BLOCK_SIZE);
        g.setColor(Color.BLACK);
        g.drawOval(0, 0, Robo.BLOCK_SIZE, Robo.BLOCK_SIZE);
        g.setFont(new Font(Font.MONOSPACED, Font.BOLD, Robo.BLOCK_SIZE/2));
        g.drawString(""+num, Robo.BLOCK_SIZE/5, Robo.BLOCK_SIZE*3/4);
        setPicture(new Picture(image));
        getPicture().transparentWhite();
    }
    public String toString() {
        return "Egg("+num+")";
    }
}
