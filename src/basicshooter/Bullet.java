/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basicshooter;

import basicgraphics.Sprite;
import basicgraphics.SpriteCollisionEvent;
import basicgraphics.SpriteComponent;
import java.awt.event.KeyEvent;

/**
 *
 * @author sbrandt
 */
class Bullet extends Sprite {
    Bullet(SpriteComponent sc,Sprite sp,int direction) {
        super(sc);
        setPicture(Game.makeBall(Game.BULLET_COLOR, Game.SMALL));
        setCenterX(sp.centerX());
        setCenterY(sp.centerY());
        if(direction == KeyEvent.VK_DOWN)
            setVelY(2.0);
        else if(direction == KeyEvent.VK_UP)
            setVelY(-2.0);
        else if(direction == KeyEvent.VK_RIGHT)
            setVelX(2.0);
        else if(direction == KeyEvent.VK_LEFT)
            setVelX(-2.0);
    }

    Bullet(SpriteComponent sc, Shooter sp, int x, int y) {
        super(sc);
        setPicture(Game.makeBall(Game.BULLET_COLOR, Game.SMALL));
        setX(sp.getX()+(Game.BIG-Game.SMALL)/2);
        setY(sp.getY()+(Game.BIG-Game.SMALL)/2);
        double delx = x-sp.getX()-sp.getWidth()/2;
        double dely = y-sp.getY()-sp.getHeight()/2;
        double dist = Math.sqrt(delx*delx+dely*dely);
        setVelX(2*delx/dist);
        setVelY(2*dely/dist);
    }
}
