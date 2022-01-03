/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basicgraphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;

/**
 * This component is displayed in regions
 * of the frame where the programmer has
 * not supplied anything to display.
 * @author sbrandt
 */
class ErrorComponent extends JComponent{
    @Override
    public void paintComponent(Graphics g) {
        Dimension d = getSize();
        g.setColor(Color.red);
        g.fillRect(0, 0, d.width, d.height);
        g.setColor(Color.black);
        g.drawRect(0, 0, d.width, d.height);
        g.drawLine(0,0,d.width,d.height);
        g.drawLine(d.width, 0, 0, d.height);
    }
}
