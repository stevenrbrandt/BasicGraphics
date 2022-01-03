/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maze;

import java.util.Queue;
import java.util.Stack;

/**
 *
 * @author sbrandt
 */
interface PathFinder {
  /**
   * Starting from position 'p', examine
   * the adjacent cells to 'p' found by
   * looking to the north, east, south
   * and west.
   *
   * You can obtain the cell at position
   * x and y in the board by using the
   * following construct:
   *
   *   Cell c = board[x][y];
   *
   * Likewise, you can find the cell to
   * the east of x,y by using the code:
   *
   *   Cell c = board[x+1][y];
   *
   * Or north of the cell at x,y by using
   * this code:
   *
   *   Cell c = board[x][y-1];
   *
   * All four adjacent cells on the board
   * from x,y can be visualized like this:
   * [       ][x  ,y-1][       ]
   * [x-1,y+0][x  ,y  ][x+1,y  ]
   * [       ][x  ,y+1][       ]
   *
   * For each of these four cells:
   *   1) if the cell is a wall (has field isWall == true),
   *      do nothing.
   *   2) if the cell is the goal (has field isGoal == true),
   *      do nothing except updating the sequence (see #4 below).
   *   3) if the cell's field named 'sequence' has a value
   *      greater than zero, do nothing. If the value of
   *      sequence is nonzero, it means you have previously
   *      visited this cell.
   *   4) Otherwise, add the cell's position to the queue.
   *      In addition, set the value of 'sequence' on any
   *      newly added cell to one plus the value 'sequence' of
   *      the cell at p
   *      (i.e. 1 + board[p.x][p.y].sequence).
   *
   * If any positions were added to the queue,
   * return true, otherwise return false.
   */
  boolean addPathElement(Pos p,Cell[][] m,Queue<Pos> q);

  /**
   * If the queue is empty return false;
   * otherwise remove the first Pos from
   * the queue and use it to call addPathElement
   * then return true.
   *
   * This method will fill in the entier maze
   * with sequence values, each of which will
   * be the smallest number of moves needed to
   * arrive at that square.
   */
  boolean tracePath(Cell[][] m,Queue<Pos> q);

  /**
   * We find the path by starting at the goal and
   * looking for adjacent squares with decreasing
   * sequence number.
   *
   * Consider the sequence number 'n' for position
   * p (i.e. int n = board[p.x][p.y].sequence).
   *
   * If n is one, return.
   *
   * If n is more than one,
   *   1) look for a neighboring square with a sequence
   *      equal to n-1
   *   2) when you find one, add its position to the stack,
   *      and stop looking for others.
   *   3) call retracePath on the position you just found.
   */
  void retracePath(Pos p,Cell[][] m,Stack<Pos> s);
}

