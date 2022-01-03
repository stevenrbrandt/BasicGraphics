/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lunarlander;

import basicgraphics.BasicFrame;
import basicgraphics.Clock;
import basicgraphics.CollisionEventType;
import basicgraphics.Sprite;
import basicgraphics.SpriteCollisionEvent;
import basicgraphics.SpriteComponent;
import basicgraphics.SpriteSpriteCollisionListener;
import basicgraphics.Task;
import basicgraphics.images.Picture;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.swing.JOptionPane;

class LandingPad extends Sprite {
    public LandingPad(SpriteComponent sc) {
        super(sc);
        BufferedImage image = BasicFrame.createImage(100,20);
        Graphics g = image.getGraphics();
        g.setColor(Color.orange);
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
        setX(350);
        setY(380);
        setPicture(new Picture(image));
    }
}

class Rocket extends Sprite {
    SpriteComponent sc;
    Picture rocket, rocketAndFlame;
    public Rocket(SpriteComponent sc) {
        super(sc);
        this.sc = sc;
        BufferedImage image = BasicFrame.createImage(20,30);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setColor(Color.blue);
        int w = image.getWidth();
        int h = image.getHeight()*2/3;
        g.fillPolygon(
                new int[]{w/2,w,w,0,0},
                new int[]{0,h/2,h,h,h/2},5);
        g.drawLine(0,h,0,4*h/3);
        g.drawLine(w-1,h,w-1,4*h/3);
        rocket = new Picture(image);
        
        image = BasicFrame.createImage(20, 30);
        g = (Graphics2D) image.getGraphics();
        g.setColor(Color.blue);
        g.fillPolygon(
                new int[]{w/2,w,w,0,0},
                new int[]{0,h/2,h,h,h/2},5);
        g.drawLine(0,h,0,4*h/3);
        g.drawLine(w-1,h,w-1,4*h/3);
        g.setColor(Color.orange);
        g.fillPolygon(
                new int[]{w/4,w/2,3*w/4},
                new int[]{h,4*h/3,h},3);
        rocketAndFlame = new Picture(image);
        setPicture(rocket);
        setX(10);
        setY(10);
        Clock.addTask(new Task() {
            @Override
            public void run() {
                setVelY(getVelY()+.002);
            }
        });
    }
    @Override public void processEvent(SpriteCollisionEvent se) {
        if(se.eventType == CollisionEventType.WALL) {
            if (se.xlo) {
                setX(sc.getSize().width - getWidth());
            }
            if (se.xhi) {
                setX(0);
            }
            if (se.ylo) {
                setActive(false);
                JOptionPane.showMessageDialog(sc, "You flew off into space");
                System.exit(0);
            }
            if (se.yhi) {
                setActive(false);
                JOptionPane.showMessageDialog(sc, "You missed the landing pad");
                System.exit(0);
            }
        }
    }
}
/**
 * The Lunar Lander was given as an assignment. As such, documentation and
 * source code for this project are not shared. There's nothing to see here.
 * Move along!
 * 
 * @author sbrandt
 */
public class LunarLander {
    public static void main(String[] args) {
        BasicFrame bf = new BasicFrame("Lunar Lander");
        SpriteComponent sc = new SpriteComponent();
        sc.setPreferredSize(new Dimension(800,400));
        String[][] layout = {{"center"}};
        bf.setStringLayout(layout);
        bf.add("center",sc);
        bf.show();
        final Rocket ll = new Rocket(sc);
        LandingPad lp = new LandingPad(sc);
        sc.addSpriteSpriteCollisionListener(Rocket.class, LandingPad.class, new SpriteSpriteCollisionListener<Rocket,LandingPad>() {
            @Override
            public void collision(Rocket r, LandingPad sp2) {
                r.setActive(false);
                String speed = String.format("velx=%.2f, vely=%.2f", r.getVelX(), r.getVelY());
                if (Math.abs(r.getVelX()) > .10) {
                    JOptionPane.showMessageDialog(sc, "X-Velocity too large! " + speed);
                } else if (Math.abs(r.getVelY()) > .40) {
                    JOptionPane.showMessageDialog(sc, "Y-Velocity too large! " + speed);
                } else {
                    JOptionPane.showMessageDialog(sc, "Landed Safely: " + speed);
                }
                System.exit(0);
            }
        });
        Clock.addTask(sc.moveSprites());
        Clock.start(10);
        KeyAdapter ka = new KeyAdapter() {
            @Override public void keyPressed(KeyEvent ke) {
                int c = ke.getKeyCode();
                switch (c) {
                    case KeyEvent.VK_UP:
                        ll.setVelY(ll.getVelY()-.04);
                        ll.setPicture(ll.rocketAndFlame);
                        break;
                    case KeyEvent.VK_RIGHT:
                        ll.setVelX(ll.getVelX()+.02);
                        break;
                    case KeyEvent.VK_LEFT:
                        ll.setVelX(ll.getVelX()-.02);
                        break;
                    default:
                        break;
                }
            }
            @Override public void keyReleased(KeyEvent ke) {
                ll.setPicture(ll.rocket);
            }
        };
        bf.addKeyListener(ka);
    }
}