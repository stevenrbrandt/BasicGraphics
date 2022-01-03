/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boardgame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author sbrandt
 */
public class Board {

    private List<List<List<Piece>>> board = new ArrayList<>();
    Map<Integer,Piece> pieceMap = new HashMap<>();

    public void move(Move m) {
        Piece p = pieceMap.get(m.pieceId);
        p.checkMove(m.fromX,m.fromY,m.toX,m.toY);
        fireMove(m);
    }
    
    private void fireMove(Move m) {
        Iterator<MoveListener> miter = moveListeners.iterator();
        while(miter.hasNext()) {
            try {
                miter.next().moveMade(m);
            } catch(Exception e) {
                e.printStackTrace(System.err);
                miter.remove();
            }
        }
    }
    
    List<MoveListener> moveListeners = new ArrayList<>();
    
    public void addMoveListener(MoveListener mv) {
        moveListeners.add(mv);
    }
}
