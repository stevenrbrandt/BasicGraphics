/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basicflyer;

import basicgraphics.BasicFrame;
import basicgraphics.Card;
import basicgraphics.ClockWorker;
import basicgraphics.SpriteComponent;
import basicgraphics.Task;
import basicgraphics.images.Picture;
import basicgraphics.sounds.ReusableClip;
import java.awt.Color;
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
    public final static int PLASMA_SIZE=10;
    public static void main(String[] args) throws IOException {
        final ReusableClip clip = new ReusableClip("beep.wav");
        final ReusableClip boom = new ReusableClip("die.wav");
        final BasicFrame bf = new BasicFrame("Flyer");
        Card bc1 = bf.getCard(new Picture("freespace.png"));
        final Card bc2 = bf.getCard();
        final SpriteComponent sc = new SpriteComponent() {
            @Override
            public void paintBackground(Graphics g) {
                Dimension d = getFullSize();
                final int BORDER_SZ=10;
                // 0 . . . . . 0
                // . 1 . . . 1 0
                // . . . . . . .
                // . 1 . . . 1 .
                // 0 . . . . . 0
                g.setColor(Color.blue);
                g.fillRect(0, 0, d.width-BORDER_SZ, BORDER_SZ);
                g.setColor(Color.pink);
                g.fillRect(d.width-BORDER_SZ, 0, BORDER_SZ, d.height-BORDER_SZ);
                g.setColor(Color.cyan);
                g.fillRect(0, d.height-BORDER_SZ, d.width, BORDER_SZ);
                g.setColor(Color.orange);
                g.fillRect(0, BORDER_SZ, BORDER_SZ, d.height);
                g.setColor(Color.black);
                g.fillRect(BORDER_SZ, BORDER_SZ, d.width-2*BORDER_SZ, d.height-2*BORDER_SZ);
                final int NUM_STARS = 9000;
                Random rand = new Random();
                rand.setSeed(0);
                g.setColor(Color.white);
                for(int i=0;i<NUM_STARS;i++) {
                    int diameter = rand.nextInt(5)+1;
                    int xpos = (int)(rand.nextGaussian()*d.width/2);
                    int ypos = (int)(rand.nextGaussian()*d.height/2);
                    g.fillOval(xpos, ypos, diameter, diameter);
                }
            }
        };
        String[][] splashLayout = {
            {"Title"},
            {"Button"},
            {"Button2"}
        };
        bc1.setStringLayout(splashLayout);
        bc1.add("Title",new JLabel("Flyer Game"));
        JButton jstart = new JButton("Start");
        jstart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bc2.showCard();
                // The BasicContainer bc2 must request the focus
                // otherwise, it can't get keyboard events.
                bc2.requestFocus();
                
                // Start the timer
                ClockWorker.initialize(7);
            }
        });
        bc1.add("Button",jstart);
        bc1.add("Button2", new JButton("Button2"));
        String[][] layout = {{
            "Flyer"
        }};
        bc2.setStringLayout(layout);
        bc2.add("Flyer",sc);
        bf.show();
        final Falcon f = new Falcon(sc);
        sc.setFocus(f);
        
        // Set the screen behavior
        sc.setBackgroundSize(new Dimension(10000,8000));
        //sc.periodic_y = true;
        sc.periodic_x = true;
        
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
                    final Plasma pl = new Plasma(sc);
                    pl.setVel(f.getVelX()*2, f.getVelY()*2);
                    pl.setCenterX(f.centerX());
                    pl.setCenterY(f.centerY());
                    final int steps = 225, bloom = 30;
                    ClockWorker.addTask(new Task(steps) {
                        @Override
                        public void run() {
                            if(iteration() + bloom >= maxIteration()) {
                                Color c = Color.white;
                                if(iteration() + bloom/4 < maxIteration())
                                    c = Color.yellow;
                                pl.setPicture(
                                    Plasma.makeBall(c,PLASMA_SIZE + iteration()-steps+bloom));
                            }
                            if(iteration() == maxIteration()) {
                                pl.setActive(false);
                                boom.play();
                            }
                        }
                    });
                    clip.play();
                }
            }
        });
        
        ClockWorker.addTask(sc.moveSprites());
    }
}
