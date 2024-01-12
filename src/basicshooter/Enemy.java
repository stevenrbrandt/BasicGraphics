/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basicshooter;

import basicgraphics.CollisionEventType;
import basicgraphics.Sprite;
import basicgraphics.SpriteCollisionEvent;
import basicgraphics.SpriteComponent;
import basicgraphics.sounds.ReusableClip;

/**
 *
 * @author sbrandt
 */
public class Enemy extends Sprite {
    static int enemyCount;
    
    @Override
    public void setActive(boolean b) {
        if(isActive() == b)
            return;
        if(b)
            enemyCount++;
        else
            enemyCount--;
        super.setActive(b);
    }

    public Enemy(SpriteComponent sc) {
        super(sc);
        enemyCount++;
        setPicture(Game.makeBall(Game.ENEMY_COLOR, Game.BIG));
        while (true) {
            setX(Game.RAND.nextInt(Game.BOARD_SIZE.width)-Game.SMALL);
            setY(Game.RAND.nextInt(Game.BOARD_SIZE.height)-Game.SMALL);
            if (Math.abs(getX() - Game.BOARD_SIZE.width / 2) < 2 * Game.BIG
                    && Math.abs(getY() - Game.BOARD_SIZE.height / 2) < 2 * Game.BIG) {
                // Overlaps with center, try again
            } else {
                break;
            }
        }
        // A random speed
        setVel(2 * Game.RAND.nextDouble() - 1, 2 * Game.RAND.nextDouble());
    }
    
    final static ReusableClip clip = new ReusableClip("die.wav");

    @Override
    public void processEvent(SpriteCollisionEvent se) {
        SpriteComponent sc = getSpriteComponent();
        if(se.eventType == CollisionEventType.WALL_INVISIBLE) {
            if (se.xlo) {
                setX(sc.getSize().width - getWidth());
            }
            if (se.xhi) {
                setX(0);
            }
            if (se.ylo) {
                setY(sc.getSize().height - getHeight());
            }
            if (se.yhi) {
                setY(0);
            }
        }

//        if (se.eventType == CollisionEventType.SPRITE) {
//        }
    }
}
