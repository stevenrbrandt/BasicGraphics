/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basicgraphics.examples;

import basicgraphics.BasicFrame;
import basicgraphics.ClockWorker;
import basicgraphics.CollisionEventType;
import basicgraphics.Scene;
import basicgraphics.Sprite;
import basicgraphics.SpriteCollisionEvent;
import basicgraphics.SpriteComponent;
import basicgraphics.SpriteSpriteCollisionListener;
import basicgraphics.Task;
import basicgraphics.images.Picture;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author sbrandt
 */
public class BasicGraphics {
    
    public final static int DIE_AFTER = 300;
    
    final static int nballs = 30;
    final static int ballSize = 20;
    
    
    static Picture createBall(Color color) {
        Image im1 = BasicFrame.createImage(ballSize, ballSize);
        Graphics imgr = im1.getGraphics();
        imgr.setColor(color);
        imgr.fillOval(0, 0, ballSize, ballSize);
        Picture p = new Picture(im1).transparentBright();
        return p;
    }
    final static Picture orangeBall = createBall(Color.orange);
    final static Picture blueBall = createBall(Color.blue);
    final static Picture greenBall = createBall(Color.green);
    final static Picture redBall = createBall(Color.red);

    static class Ball extends Sprite {
        boolean dead = false;

        Ball(Scene sc) {
            super(sc);
        }

        @Override
        public void mouseClicked(MouseEvent me) {
            setPicture(orangeBall);
            if(dead) {
                dead = false;
                setVel(getVelX()*2, getVelY()*2);
            }
        }

        @Override
        public void processEvent(SpriteCollisionEvent ce) {
            if (ce.eventType == CollisionEventType.WALL) {
                if (ce.xlo) {
                    setVel(Math.abs(getVelX()), getVelY());
                }
                if (ce.xhi) {
                    setVel(-Math.abs(getVelX()), getVelY());
                }
                if (ce.ylo) {
                    setVel(getVelX(), Math.abs(getVelY()));
                }
                if (ce.yhi) {
                    setVel(getVelX(), -Math.abs(getVelY()));
                }
            }
        }
    }

    static String[][] layout = {
        {"topl", "topm", "topr"},
        {"row2", "row2", "row2"},
        {"row3", "row3", "row3"},
        {"row4", "row4", "botr"}
    };

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BasicFrame f = new BasicFrame("Fish");
        SpriteComponent sc = new SpriteComponent();
        final JButton button1 = new JButton("Button 1");
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(button1, "Button 1 was pressed!");
            }
        });
        button1.addActionListener(new Foo());
        f.setStringLayout(layout);
        f.add("topl", button1);
        f.add("topm", new JButton("Button 2"));
        f.add("topr", new JButton("Button 3"));
        f.add("row2", new JLabel("Row 2"));
        f.add("row4", new Picture("sarah.png").makeButton());
        f.add("botr", new JLabel("corner", JLabel.CENTER));

        Bat bat = new Bat(sc.getScene());

        Dimension d = new Dimension(800, 400);
        sc.setPreferredSize(d);

        Random rand = new Random();
        KeyListener kl = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println("pressed: " + e.toString());
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        };
        f.addKeyListener(kl);
        MouseListener ml = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("click");
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        };
        sc.addMouseListener(ml);
        for (int i = 0; i < nballs; i++) {
            Ball sball = new Ball(sc.getScene());
            if (i % 2 == 0) {
                sball.setPicture(redBall);
            } else {
                sball.setPicture(greenBall);
            }
            sball.setVel(2 * rand.nextDouble() - 1, 2 * rand.nextDouble() -1);
            sball.setX(rand.nextInt(d.width));
            sball.setY(rand.nextInt(d.height));
        }
        
        sc.addSpriteSpriteCollisionListener(Ball.class,Ball.class,new SpriteSpriteCollisionListener<Ball,Ball>() {
            @Override
            public void collision(Ball sp1, Ball sp2) {
                double vx = sp1.getVelX();
                double vy = sp1.getVelY();
                sp1.setVel(sp2.getVelX(), sp2.getVelY());
                sp2.setVel(vx, vy);
            }
        });
        sc.addSpriteSpriteCollisionListener(Ball.class, Bat.class, new SpriteSpriteCollisionListener<Ball, Bat>() {
            @Override
            public void collision(Ball sp1, Bat sp2) {
                if(sp1.dead) return;
                sp1.dead = true;
                sp1.setVel(sp1.getVelX()/2, sp1.getVelY()/2);
                sp1.setPicture(blueBall);
                ClockWorker.addTask(new Task(DIE_AFTER) {
                    public void run() {
                        if (iteration() == DIE_AFTER) {
                            if(sp1.dead) sp1.destroy();
                        }
                    }
                });
                //Clock.addTask(new DeathTimer(DIE_AFTER,sp1));
            }
        });

        ClockWorker.initialize(20);
        ClockWorker.addTask(sc.moveSprites());

        f.add("row3", sc);
        f.show();
    }

}
