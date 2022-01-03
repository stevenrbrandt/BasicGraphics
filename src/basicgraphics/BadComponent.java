/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basicgraphics;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 *
 * @author sbrandt
 */
class BadComponent extends Component {

    public BadComponent() {
    }
    
    @Override
    public void paint(Graphics g) {
        Dimension d = getSize();
        g.setColor(Color.red);
        g.fillRect(0, 0, d.width, d.height);
        g.setColor(Color.black);
        g.drawRect(0, 0, d.width, d.height);
        g.drawLine(0,0, d.width, d.height);
        g.drawLine(0, d.height, d.width, 0);
    }
    
}
