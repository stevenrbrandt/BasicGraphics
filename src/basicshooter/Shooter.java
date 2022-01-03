/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basicshooter;

import basicgraphics.Sprite;
import basicgraphics.SpriteComponent;

/**
 *
 * @author sbrandt
 */
public class Shooter extends Sprite {
    public Shooter(SpriteComponent sc) {
        super(sc);
        setPicture(Game.makeBall(Game.SHOOTER_COLOR, Game.BIG));
        setX(Game.BOARD_SIZE.width/2);
        setY(Game.BOARD_SIZE.height/2);
    }
}
