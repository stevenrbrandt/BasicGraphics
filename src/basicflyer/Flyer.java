/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basicflyer;

import basicgraphics.BasicContainer;
import basicgraphics.BasicFrame;
import basicgraphics.ClockWorker;
import basicgraphics.SpriteComponent;
import basicgraphics.sounds.ReusableClip;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 * This program creates a spaceship that flies
 * across a field at constant speed, turning left
 * or right when the player uses the arrow keys.
 * It also shoots of the spacebar is pressed.
 * @author sbrandt
 */
public class Flyer {
    public static void main(String[] args) throws IOException {
        final ReusableClip clip = new ReusableClip("beep.wav");
        final BasicFrame bf = new BasicFrame("Flyer");
        final Container content = bf.getContentPane();
        final CardLayout cards = new CardLayout();
        content.setLayout(cards);
        BasicContainer bc1 = new BasicContainer();
        content.add(bc1,"Splash");
        final BasicContainer bc2 = new BasicContainer();
        content.add(bc2,"Game");
        final SpriteComponent sc = new SpriteComponent() {
            @Override
            public void paintBackground(Graphics g) {
                Dimension d = getSize();
                g.setColor(Color.black);
                g.fillRect(0, 0, d.width, d.height);
                final int NUM_STARS = 30;
                Random rand = new Random();
                rand.setSeed(0);
                g.setColor(Color.white);
                for(int i=0;i<NUM_STARS;i++) {
                    int diameter = rand.nextInt(5)+1;
                    int xpos = rand.nextInt(d.width);
                    int ypos = rand.nextInt(d.height);
                    g.fillOval(xpos, ypos, diameter, diameter);
                }
            }
        };
        sc.setPreferredSize(new Dimension(800,400));
        String[][] splashLayout = {
            {"Title"},
            {"Button"}
        };
        bc1.setStringLayout(splashLayout);
        bc1.add("Title",new JLabel("Flyer Game"));
        JButton jstart = new JButton("Start");
        jstart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cards.show(content,"Game");
                // The BasicContainer bc2 must request the focus
                // otherwise, it can't get keyboard events.
                bc2.requestFocus();
                
                // Start the timer
                ClockWorker.initialize(5);
            }
        });
        bc1.add("Button",jstart);
        String[][] layout = {{
            "Flyer"
        }};
        bc2.setStringLayout(layout);
        bc2.add("Flyer",sc);
        bf.show();
        final Falcon f = new Falcon(sc);
        final double INCR = Math.PI*2/100.0;
        // Note: Adding the listener to basic container 2.
        bc2.addKeyListener(new KeyAdapter() {   
            @Override
            public void keyPressed(KeyEvent ke) {
                if(ke.getKeyCode() == KeyEvent.VK_RIGHT) {
                    f.turn( INCR);
                } else if(ke.getKeyCode() == KeyEvent.VK_LEFT) {
                    f.turn(-INCR);
                } else if(ke.getKeyChar() == ' ') {
                    Plasma pl = new Plasma(sc);
                    pl.setVel(f.getVelX()*2, f.getVelY()*2);
                    pl.setCenterX(f.centerX());
                    pl.setCenterY(f.centerY());
                    clip.play();
                }
            }
        });
        ClockWorker.addTask(sc.moveSprites());
    }
}
