/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prog.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Random;
import javax.swing.JComponent;

/**
 *
 * @author sbrandt
 */
public class Dice extends JComponent {
    private int spots = -1;
    public static final Random RAND = new Random();
    
    public Dice() {
//        roll();
    }
    
    public void roll() {
        setSpots(1+RAND.nextInt(6));
    }
    public void setSpots(int n) {
        spots = n;
        repaint();
    }
    public int getSpots() {
        return spots;
    }
    @Override
    public void paint(Graphics g) {
        Dimension d = getSize();
        g.setColor(Color.white);
        g.fillRect(0, 0, d.width, d.height);
        int SS = d.width / 10;
        g.setColor(Color.black);
        g.drawRect(0, 0, d.width-1, d.height-1);
        switch(spots) {
            case 1:
                g.fillOval(d.width/2-SS, d.height/2-SS, 2*SS, 2*SS);
                break;
            case 2:
                g.fillOval(d.width/4-SS, d.height/4-SS, 2*SS, 2*SS);
                g.fillOval(3*d.width/4-SS, 3*d.height/4-SS, 2*SS, 2*SS);
                break;
            case 3:
                g.fillOval(d.width/2-SS, d.height/2-SS, 2*SS, 2*SS);
                g.fillOval(d.width/4-SS, d.height/4-SS, 2*SS, 2*SS);
                g.fillOval(3*d.width/4-SS, 3*d.height/4-SS, 2*SS, 2*SS);
                break;
            case 4:
                g.fillOval(d.width/4-SS, d.height/4-SS, 2*SS, 2*SS);
                g.fillOval(3*d.width/4-SS, 3*d.height/4-SS, 2*SS, 2*SS);
                g.fillOval(3*d.width/4-SS, d.height/4-SS, 2*SS, 2*SS);
                g.fillOval(d.width/4-SS, 3*d.height/4-SS, 2*SS, 2*SS);
                break;
            case 5:
                g.fillOval(d.width/4-SS, d.height/4-SS, 2*SS, 2*SS);
                g.fillOval(3*d.width/4-SS, 3*d.height/4-SS, 2*SS, 2*SS);
                g.fillOval(3*d.width/4-SS, d.height/4-SS, 2*SS, 2*SS);
                g.fillOval(d.width/4-SS, 3*d.height/4-SS, 2*SS, 2*SS);
                g.fillOval(d.width/2-SS, d.height/2-SS, 2*SS, 2*SS);
                break;
            case 6:
                g.fillOval(d.width/4-SS, d.height/4-SS, 2*SS, 2*SS);
                g.fillOval(3*d.width/4-SS, 3*d.height/4-SS, 2*SS, 2*SS);
                g.fillOval(3*d.width/4-SS, d.height/4-SS, 2*SS, 2*SS);
                g.fillOval(d.width/4-SS, 3*d.height/4-SS, 2*SS, 2*SS);
                g.fillOval(3*d.width/4-SS, d.height/2-SS, 2*SS, 2*SS);
                g.fillOval(d.width/4-SS, d.height/2-SS, 2*SS, 2*SS);
                break;
        }
    }
}
