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
public class Move {
    final int pieceId;
    final int fromX, fromY, toX, toY;
    public Move(int pid,int fx,int fy,int tx,int ty) {
        pieceId = pid;
        fromX = fx;
        fromY = fy;
        toX = tx;
        toY = ty;
    }
}
