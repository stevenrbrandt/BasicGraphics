/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basicshooter;

import basicgraphics.BasicFrame;
import basicgraphics.ClockWorker;
import basicgraphics.Sprite;
import basicgraphics.SpriteComponent;
import basicgraphics.SpriteSpriteCollisionListener;
import basicgraphics.images.Picture;
import static basicshooter.Enemy.enemyCount;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import javax.swing.JOptionPane;

/**
 *
 * @author sbrandt
 */
public class Game {
    final public static Random RAND = new Random();
    final public static Color SHOOTER_COLOR = Color.blue;
    final public static Color BULLET_COLOR = Color.blue;
    final public static Color ENEMY_COLOR = Color.red;
    final public static Color EXPLOSION_COLOR = Color.orange;
    final public static int BIG = 20;
    final public static int SMALL = 5;
    final public static int ENEMIES = 10;
    final public static Dimension BOARD_SIZE = new Dimension(800,400);
    
    BasicFrame bf = new BasicFrame("Shooter!");
    
    static Picture makeBall(Color color,int size) {
        Image im = BasicFrame.createImage(size, size);
        Graphics g = im.getGraphics();
        g.setColor(color);
        g.fillOval(0, 0, size, size);
        return new Picture(im);
    }
    
    public void run() {
        final SpriteComponent sc = new SpriteComponent();
        sc.setPreferredSize(BOARD_SIZE);
        String[][] layout = {{"center"}};
        bf.setStringLayout(layout);
        bf.add("center",sc);
        final Shooter shooter = new Shooter(sc);
        for(int i=0;i<ENEMIES;i++) {
            Enemy en = new Enemy(sc);
        }
        KeyAdapter key = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent ke) {
                new Bullet(sc,shooter,ke.getKeyCode());
            }
        };
        bf.addKeyListener(key);
        bf.addMenuAction("Help", "About", new Runnable() {
            @Override
            public void run() {
                JOptionPane.showMessageDialog(bf.getContentPane(), "Have fun!");
            }
        });
        bf.addMenuAction("Help", "New Game", new Runnable() {
            @Override
            public void run() {
                JOptionPane.showMessageDialog(bf.getContentPane(), "Have fun!");
            }
        });
        bf.addMenuAction("File", "Load", new Runnable() {
            @Override
            public void run() {
                JOptionPane.showMessageDialog(bf.getContentPane(), "Have fun!");
            }
        });
        bf.addMenuAction("File", "Store", new Runnable() {
            @Override
            public void run() {
                JOptionPane.showMessageDialog(bf.getContentPane(), "Have fun!");
            }
        });
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                new Bullet(sc,shooter,me.getX(),me.getY());
            }
        };
        sc.addMouseListener(ma);
        
        sc.addSpriteSpriteCollisionListener(Enemy.class, Shooter.class, new SpriteSpriteCollisionListener<Enemy, Shooter>() {
            @Override
            public void collision(Enemy sp1, Shooter sp2) {
                Enemy.clip.play();
                sp1.setActive(false);
                sp2.setActive(false);
                JOptionPane.showMessageDialog(sc, "You lose! Game Over!");
                System.exit(0);
            }
        });
        sc.addSpriteSpriteCollisionListener(Enemy.class, Bullet.class, new SpriteSpriteCollisionListener<Enemy, Bullet>() {
            @Override
            public void collision(Enemy sp1, Bullet sp2) {
                Enemy.clip.play();
                sp1.setActive(false);
                sp2.setActive(false);
                if (enemyCount == 0) {
                    JOptionPane.showMessageDialog(sc, "You win! Game Over!");
                    System.exit(0);
                }
            }
        });
        
        bf.show();
        ClockWorker.initialize(10);
        ClockWorker.addTask(sc.moveSprites());
    }
    
    public static void main(String[] args) {
        Game g = new Game();
        g.run();
    }
}
