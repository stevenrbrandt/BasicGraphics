/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crossTheFields;

import basicgraphics.Sprite;
import basicgraphics.SpriteComponent;
/**
 *
 * @author theha
 */
public class EnemyBullet extends Sprite {
    
    EnemyBullet(SpriteComponent sc, double startX, double startY, Soldier soldier) {
        super(sc);
        setPicture(Main.makeBall(Main.ENEMY_SHOT_COLOR, 4));
        setX(startX+10);
        setY(startY+10);
        
        double deltaX = (getX() - soldier.getX());
        double deltaY = (getY() - soldier.getY());
        double newHeading = Math.atan2(deltaY, deltaX);
        setVelY((Math.sin(newHeading+(Math.PI))));
        setVelX((Math.cos(newHeading + (Math.PI))));
    }
}
