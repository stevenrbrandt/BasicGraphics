/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crossTheFields;

import basicgraphics.Sprite;
import basicgraphics.SpriteComponent;
import basicgraphics.sounds.ReusableClip;

/**
 *
 * @author theha
 */
public class CircleEnemy extends Sprite {
    
    public CircleEnemy(int startX, int startY, SpriteComponent sc) {
        super(sc);
        setPicture(Main.makeBall(Main.CIRCLE_COLOR, 18));
        setX(startX);
        setY(startY);
    }
}
