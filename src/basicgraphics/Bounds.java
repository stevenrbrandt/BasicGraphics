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
public class Bounds {
    final public int left, right, above, below, width, height;
    public String toString() {
        return "{"+left+","+right+","+above+","+below+"}";
    }
    public Bounds(int left,int right,int above,int below) {
        this.left = left;
        this.right = right;
        this.above = above;
        this.below = below;
        this.width = left + right;
        this.height = above + below;
    }
}
