/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maze;

/**
 * The cell class represents one square on the maze game board.
 *
 * @author sbrandt
 */
class Cell {

    /**
     * If the sequence is greater than zero, it means the maze was visited by
     * the PathFinder interface while searching the shortest path.
     */
    int sequence = 0;
    boolean isWall = true;
    /**
     * The starting position in the maze.
     */
    boolean isStart = false;
    /**
     * The end or goal of the maze.
     */
    boolean isGoal = false;

    boolean canSearch() {
        return sequence == 0 && !isWall;
    }
}
