/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package basicgraphics;

import basicgraphics.images.Painter;
import basicgraphics.images.Picture;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author steve
 */
public class Scene {
    SpriteComponent spritecomponent;
    final Set<Sprite> sprites = new TreeSet<>();
    Picture background;
    Dimension backgroundSize;
    Sprite focus = null;
    int offsetX=0, offsetY=0;
    
    public boolean periodic_x = false;
    public boolean periodic_y = false;

    void addSprite(Sprite sp) {
        Util.invokeAndWait(()->{ sprites.add(sp);});
    }
    
    Scene(SpriteComponent sc) {
        spritecomponent = sc;
    }
    public void setFocus(Sprite s) {
        focus = s;
    }
    
    public void setBackgroundSize(Dimension d) {
        backgroundSize = d;
        if(background == null) {
            BufferedImage bimage = BasicFrame.createImage(d.width, d.height);
            spritecomponent.paintBackground(bimage.getGraphics());
            background = new Picture(bimage);
            System.out.println("Image size: "+d);
        }
    }

    Painter painter = null;
    public void setPainter(Painter p) {
        painter = p;
    }
    public Painter getPainter() {
        return painter;
    }
    
    public Dimension getSize() {
        return spritecomponent.getSize();
    }

    public SpriteComponent getSpriteComponent() {
        return spritecomponent;
    }
}
