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
class MazePathFinder implements PathFinder {
  boolean addOne(Pos p,int x,int y,Cell[][] m,Queue<Pos> q,int seq) {
    Pos p2 = new Pos(p.x+x,p.y+y);
    Cell c = m[p2.x][p2.y];
    if(c.isWall) return false;
    if(c.sequence > 0) return false;
    c.sequence = seq+1;
    q.add(p2);
    return true;
  }
  boolean done = false;
  public boolean addPathElement(Pos p,Cell[][] m,Queue<Pos> q) {
    Cell c = m[p.x][p.y];
    if(c.isGoal || done) {
      done = true;
      return false;
    }
    int s = c.sequence;
    int n = q.size();
    addOne(p, 0, 1,m,q,s);
    addOne(p, 0,-1,m,q,s);
    addOne(p, 1, 0,m,q,s);
    addOne(p,-1, 0,m,q,s);
    return q.size() > n;
  }
  public boolean tracePath(Cell[][] m,Queue<Pos> q) {
    if(q.isEmpty())
      return false;
    Pos p = q.remove();
    addPathElement(p,m,q);
    return true;
  }
  boolean retracePath(Pos p,int x,int y,Cell[][] m,Stack<Pos> s,int seq) {
    Pos p2 = new Pos(p.x+x,p.y+y);
    Cell c = m[p2.x][p2.y];
    if(c.isWall) return false;
    if(c.sequence == seq) {
      retracePath(p2,m,s);
      return true;
    }
    return false;
  }
  public void retracePath(Pos p,Cell[][] m,Stack<Pos> s) {
    Cell c = m[p.x][p.y];
    int seq = c.sequence - 1;
    s.push(p);
    if(seq == 0) {
      return;
    }
    if(retracePath(p, 0, 1,m,s,seq))
      return;
    else if(retracePath(p, 0,-1,m,s,seq))
      return;
    else if(retracePath(p, 1, 0,m,s,seq))
      return;
    else if(retracePath(p,-1, 0,m,s,seq))
      return;
  }
}

