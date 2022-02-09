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
        setVelX(1);
        setVelY(1);
    }

    @Override
    public void processEvent(SpriteCollisionEvent ce) {
        if (ce.eventType == CollisionEventType.WALL) {
            if (ce.xlo) {
                setVelX(Math.abs(getVelX()));
            }
            if (ce.xhi) {
                setVelX(-Math.abs(getVelX()));
            }
            if (ce.ylo) {
                setVelY(Math.abs(getVelY()));
            }
            if (ce.yhi) {
                setVelY(-Math.abs(getVelY()));
            }
        }
    }
}
