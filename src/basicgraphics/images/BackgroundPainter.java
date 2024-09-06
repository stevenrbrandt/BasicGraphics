/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package basicgraphics.images;

import basicgraphics.images.Picture;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 *
 * @author steve
 */
public class BackgroundPainter implements Painter {

    public final Picture background;

    public BackgroundPainter(Picture p) {
        background = p;
    }

    @Override
    public void paint(Graphics g, Dimension d) {
        double scalex = background.getWidth() * 1.0 / d.width;
        double scaley = background.getHeight() * 1.0 / d.height;
        double scale = scalex < scaley ? scalex : scaley;
        double bgx = d.width * scale;
        double bgy = d.height * scale;
        g.drawImage(background.getImage(), 0, 0, d.width, d.height,
                0, 0, (int) bgx, (int) bgy, null);
    }
}
