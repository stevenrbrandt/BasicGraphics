/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package basicgraphics;

import basicgraphics.images.Picture;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 *
 * @author steve
 */
public class Card extends BasicContainer {    
    
    final String cardName;
    final Picture background;
    public Card(String name) {
        cardName = name;
        background = null;
    }
    public Card(String name,Picture bground) {
        cardName = name;
        background = bground;
    }

    public void showCard() {
        BasicFrame.cards.show(getParent(), cardName);
    }
    
    @Override
    public void paint(Graphics g) {
        if(background != null) {
            Dimension d = getSize();
            double scalex = background.getWidth()*1.0/d.width;
            double scaley = background.getHeight()*1.0/d.height;
            double scale = scalex < scaley ? scalex : scaley;
            double bgx = d.width*scale;
            double bgy = d.height*scale;
            g.drawImage(background.getImage(), 0, 0, d.width, d.height, 
                    0, 0, (int)bgx, (int)bgy, this);
        }
        super.paint(g);
    }
}
