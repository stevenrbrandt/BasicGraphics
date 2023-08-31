/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crossTheFields;

/**
 *
 * @author theha
 * */
import basicgraphics.CollisionEventType;
import basicgraphics.Sprite;
import basicgraphics.SpriteCollisionEvent;
import basicgraphics.SpriteComponent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/*
public class Shooter extends Sprite {
    public Shooter(SpriteComponent sc) {
        super(sc);
        setPicture(Game.makeBall(Game.SHOOTER_COLOR, Game.BIG));
        setX(Game.BOARD_SIZE.width/2);
        setY(Game.BOARD_SIZE.height/2);
    }
}
 */
public class Soldier extends Sprite {
    
    public Soldier(SpriteComponent sc) {
        super(sc);
        setPicture(Main.makeBall(Main.SOLDIER_COLOR, 16));
        setX(10);
        setY(Main.BOARD_SIZE.height/2);
    }
    
    public void moveBack() {
        setX(10);
        setY(Main.BOARD_SIZE.height/2);
    }
    
    /*
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
    */
    @Override
    public void processEvent(SpriteCollisionEvent se) {
        SpriteComponent sc = getSpriteComponent();
        if(se.eventType == CollisionEventType.WALL_INVISIBLE) {
            if (se.xlo) {
                setX(0);
            }
            if (se.xhi) {
                setX(770);
            }
            if (se.ylo) {
                setY(0);
            }
            if (se.yhi) {
                setY(570);
            }
        }
        if(se.eventType == CollisionEventType.WALL) {
            if (se.xlo) {
                setX(getX() - 5);
            }
            if (se.ylo) {
                setY(getY() - 5);
            }
            if (se.xhi) {
                setX(getX() + 5);
            }
            if (se.yhi) {
                setY(getY() + 5);
            }
        }
    }
    
}
