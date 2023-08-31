/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crossTheFields;

import basicgraphics.Sprite;
import basicgraphics.SpriteComponent;
import basicgraphics.sounds.ReusableClip;
import java.awt.event.KeyEvent;
import java.awt.MouseInfo;

/**
 *
 * @author theha
 */
public class FriendlyBullet extends Sprite {
    
    FriendlyBullet(SpriteComponent sc, Soldier so, int xClick, int yClick) {
        super(sc);
        
        
        double xSoldier = so.getX();
        double ySoldier = so.getY(); 
        setPicture(Main.makeBall(Main.SOLDIER_SHOT_COLOR, 4));
        setX(xSoldier+6);
        setY(ySoldier+6);
        double cosComponent = xClick-xSoldier-so.getWidth()/2;
        double sinComponent = yClick-ySoldier-so.getHeight()/2;
        double pythag = Math.sqrt(cosComponent*cosComponent+sinComponent*sinComponent);
        setVelX(4*cosComponent/pythag);
        setVelY(4*sinComponent/pythag);
    }
}
