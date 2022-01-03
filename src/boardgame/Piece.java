/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boardgame;

/**
 *
 * @author sbrandt
 */
public abstract class Piece {
    int x,y;
    static int nextId = 0;
    final int id = nextId++;
    public int getX() { return x; }
    public int getY() { return y; }
    public abstract void checkMove(int x1,int x2,int y1,int y2);
}
