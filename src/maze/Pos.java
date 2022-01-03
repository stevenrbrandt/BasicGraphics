/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maze;

/**
 * Represents a position (x and y coordinate) on the game board used 
 * by the Maze class. One can find a given cell on the board by typing:
 * 
 * <pre>
 * Cell c = maze.board[p.x][p.y];
 * </pre>
 * 
 * @author sbrandt
 */
class Pos {
    int x, y;
    Pos(int _x, int _y) {
        x=_x;
        y=_y;
    }
    @Override
    public boolean equals(Object o) {
        Pos that = (Pos)o;
        if(that == null) return false;
        return this.x == that.x && this.y == that.y;
    }
    public Pos add(Pos that) {
        return new Pos(this.x + that.x, this.y + that.y);
    }
    public String toString() {
      return "("+x+","+y+")";
    }
}

