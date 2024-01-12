/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basicgraphics.examples;

import basicgraphics.CollisionEventType;
import basicgraphics.Sprite;
import basicgraphics.SpriteCollisionEvent;
import basicgraphics.SpriteComponent;
import basicgraphics.images.Picture;

/**
 *
 * @author sbrandt
 */
public class Bat extends Sprite {

    public Bat(SpriteComponent sc) {
        super(sc);
        setPicture(new Picture("bat.png"));
        setVel(1, 1);
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
