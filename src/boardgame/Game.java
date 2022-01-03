/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boardgame;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sbrandt
 */
public class Game implements Runnable {
    Board board;
    List<Player> players = new ArrayList<>();
    int nextPlayer = 0;
    
    public void run() {
        Move move = players.get(nextPlayer).getMove();
        if(move == null) {
            nextPlayer = (nextPlayer + 1) % players.size();
            return;
        }
        board.move(move);
    }
}
