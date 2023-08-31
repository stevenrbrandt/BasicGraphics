/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crossTheFields;

import basicgraphics.Sprite;
import basicgraphics.SpriteComponent;
import java.awt.Color;

/**
 *
 * @author theha
 */

    public class GrayCircleEnemy extends Sprite {
    
    public GrayCircleEnemy(int startX, int startY, SpriteComponent sc) {
        super(sc);
        setPicture(Main.makeBall(Color.GRAY, 18));
        setX(startX);
        setY(startY);
    }
    
    public boolean checkAndExplode(Soldier soldier, SpriteComponent sc) {
        return ((soldier.isActive())&&(Math.abs(soldier.getY() - getY()) < 34)&&(Math.abs(soldier.getX() - getX())<34));
    }
}

