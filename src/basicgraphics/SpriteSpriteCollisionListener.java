/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basicgraphics;

/**
 *
 * @author sbrandt
 */
public interface SpriteSpriteCollisionListener<T1,T2> {
    void collision(T1 sp1,T2 sp2);
}
