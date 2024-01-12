/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maze;

import basicgraphics.BasicFrame;
import basicgraphics.ClockWorker;
import basicgraphics.Sprite;
import basicgraphics.SpriteComponent;
import basicgraphics.Task;
import basicgraphics.images.Picture;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

/**
 * The Maze class generates
 * @author sbrandt and knarf
 */
public class Maze {
    public static final Random RAND = new Random();
    int sizex, sizey;
    Cell[][] board;
    MazeViz mv;

    Maze() {}
    Maze(MazeViz mv) { this.mv = mv; }
    
    /**
     * Students don't need to follow this logic
     * for the lab. However, they are encouraged
     * to trace it through on their own.
     * @param sx size of the maze in x
     * @param sy size of the maze in y
     */
    public void initBoard(int sx, int sy) {
        sizex = 2*sx+1;
        sizey = 2*sy+1;
        board = new Cell[sizex][sizey];
        for (int x=0; x<sizex; x++)
            for (int y=0; y<sizey; y++) {
                board[x][y] = new Cell();
            }
        int startx = RAND.nextInt(sx)*2+1;
        int starty = RAND.nextInt(sy)*2+1;
        board[startx][starty].isWall = false;

        ArrayList<Pos> walls = new ArrayList<>();

        if (startx-1 > 0)
            walls.add(new Pos(startx-1, starty  ));
        if (startx+1 < sizex-1)
            walls.add(new Pos(startx+1, starty  ));
        if (starty-1 > 0)
            walls.add(new Pos(startx  , starty-1));
        if (starty+1 < sizey-1)
            walls.add(new Pos(startx  , starty+1));

        while (walls.size() > 0) {
            Pos cell = null;
            Pos wall = walls.get(RAND.nextInt(walls.size()));
            if (wall.x%2 == 1) {
                if (board[wall.x][wall.y-1].isWall)
                    cell = new Pos(wall.x, wall.y-1);
                if (board[wall.x][wall.y+1].isWall)
                    cell = new Pos(wall.x, wall.y+1);
            } else {
                if (board[wall.x-1][wall.y].isWall)
                    cell = new Pos(wall.x-1, wall.y);
                if (board[wall.x+1][wall.y].isWall)
                    cell = new Pos(wall.x+1, wall.y);
            }
            if (cell != null) {
                board[wall.x][wall.y].isWall = false;
                board[cell.x][cell.y].isWall = false;
                if (cell.x-1 > 0 && board[cell.x-1][cell.y  ].isWall)
                    walls.add(new Pos(cell.x-1, cell.y  ));
                if (cell.x+1 < sizex-1 && board[cell.x+1][cell.y  ].isWall)
                    walls.add(new Pos(cell.x+1, cell.y  ));
                if (cell.y-1 > 0 && board[cell.x  ][cell.y-1].isWall)
                    walls.add(new Pos(cell.x  , cell.y-1));
                if (cell.y+1 < sizey-1 && board[cell.x][cell.y+1].isWall)
                    walls.add(new Pos(cell.x  , cell.y+1));
            }
            walls.remove(wall);
        }

    }

    char toChar(int n) {
      while(true) {
        if(n <= 9)
          return (char)(n+'0');
        n -= 10;
        if(n <= 'z'-'a')
          return (char)(n+'a');
        n -= ('z' - 'a' + 1);
        if(n <= 'Z'-'A')
          return (char)(n+'A');
        n -= ('Z' - 'A' + 1);
      }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int y=0; y<sizey; y++) {
            for (int x=0; x<sizex; x++) {
                if (board[x][y].isWall)
                    sb.append("##");
                else if (board[x][y].sequence > 0)
                  sb.append("."+toChar(board[x][y].sequence));
                else if (board[x][y].isStart)
                  sb.append(".!");
                else if (board[x][y].isGoal)
                  sb.append(".!");
                else
                    sb.append("..");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public Stack<Pos> solveAnim(final PathFinder m) {
        // Start from upper left corner and try to get to lower right corner
        Pos start = new Pos(1,1);
        final Pos goal  = new Pos(sizex-2, sizey-2);
        // Make sure these positions are not walls
        assert !board[start.x][start.y].isWall;
        assert !board[goal.x] [goal.y].isWall;
        
        board[start.x][start.y].isStart = true;
        board[goal.x][goal.y].isGoal = true;
        
        final Stack<Pos> s = new Stack<>();

        // Put positions we need to continue to search from in a queue
        final Queue<Pos> toSearch = new LinkedList<>();
        // We need to start searching from the start position
        toSearch.add(start);
        // Mark the start position on the board (as already searched)
        board[start.x][start.y].sequence=1;
        Sprite sp = new Sprite(mv.jp) {
            int step = 0;
            // Constructor for anonymous
            // inner class
            {
                setX(-100);
                // This "sprite" doesn't really display, it just
                // uses the timer. Create a 1x1 image and put it
                // offscreen.
                setPicture(new Picture(BasicFrame.createImage(1, 1)));
                
                ClockWorker.addTask(new Task() {
                    @Override
                    public void run() {
                        doMove();
                    }
                });
            }
            /**
             * Step through the algorithm to find the shortest path.
             */
            public void doMove() {
                if(step == 0) {
                    if(!m.tracePath(board, toSearch))
                        step=1;
                } else if(step == 1) {
                    m.retracePath(goal, board, s);
                    clear();
                    step = 2;
                } else if(step >= 2 && step - 2 < s.size()) {
                    Pos p = s.get(step-2);
                    step++;
                    board[p.x][p.y].sequence = s.size() - (step - 2)+1;
                }
            }
        };
//        mv.jp.addSprite(sp);
//        mv.jp.start(0, 100);
        mv.initMaze(this);
        ClockWorker.addTask(mv.jp.moveSprites());
        System.out.println("Got here");
        return s;
    }
    public Stack<Pos> solve(PathFinder m) {
        // Start from upper left corner and try to get to lower right corner
        Pos start = new Pos(1,1);
        Pos goal  = new Pos(sizex-2, sizey-2);
        // Make sure these positions are not walls
        assert !board[start.x][start.y].isWall;
        assert !board[goal.x] [goal.y].isWall;
        
        board[start.x][start.y].isStart = true;
        board[goal.x][goal.y].isGoal = true;

        // Put positions we need to continue to search from in a queue
        Queue<Pos> toSearch = new LinkedList<>();
        // We need to start searching from the start position
        toSearch.add(start);
        // Mark the start position on the board (as already searched)
        board[start.x][start.y].sequence=1;
        
        int chk_size = sizex;
        if(sizey > sizex) chk_size = sizey;
        chk_size /= 3;
        for(int n=2;n<chk_size;n++) {
            Pos pos = toSearch.remove();
            int sizeBefore = toSearch.size();
            boolean bchk = m.addPathElement(pos, board, toSearch);
            int sizeAfter = toSearch.size();
            assert sizeAfter >= sizeBefore :
                    "The queue should not shrink in addPathElement()";
            LinkedList<Pos> ll = (LinkedList<Pos>) toSearch;
            for (int i = sizeBefore; i < sizeAfter; i++) {
                Pos p = ll.get(i);
                Cell c = board[p.x][p.y];
                assert minc(p) : "Bad value for sequence in cell at "+p;
            }
        }
        
        while(true) {
            boolean b2 = !toSearch.isEmpty();
            boolean b = m.tracePath(board,toSearch);
            assert b == b2 : "tracePath() returned the wrong value";
            if(!b)
                break;
        }

        Stack<Pos> s = new Stack<>();
        m.retracePath(goal,board,s);
        int cval = s.size();
        for(Pos p : s) {
            Cell c = board[p.x][p.y];
            assert c.sequence == cval : "Bad sequence in answer: "+c.sequence+" != "+cval;
            assert minc(p) : "bad predecessor for "+c.sequence+" pos="+p;
            if(c.sequence == 1) assert c.isStart;
            if(c.sequence == s.size()) assert c.isGoal;
            cval--;
        }
        int n = s.size();
        for(Pos p : s) {
            assert board[p.x][p.y].sequence == n--;
        }
        int req = board.length + board[0].length - 5;
        assert s.size() >= req : "Too few elements in solution: "+s.size()+" < "+req;
        Pos pstart = s.get(s.size()-1);
        assert board[pstart.x][pstart.y].isStart == true;
        assert board[pstart.x][pstart.y].sequence > 0;
        Pos pgoal = s.get(0);
        assert board[pgoal.x][pgoal.y].isGoal == true;
        assert board[pgoal.x][pgoal.y].sequence > 0;
        clear();
        n = s.size();
        for(Pos p : s) {
          board[p.x][p.y].sequence = n--;
        }
        return s;
    }

    public void clear() {
      for(int i=0;i<board.length;i++) {
        for(int j=0;j<board[i].length;j++) {
          board[i][j].sequence = 0;
        }
      }
    }

    public static void main(String[] args) throws Exception {
        if(args.length==0) {
            System.out.println("Usage: Maze MazeClass");
            System.exit(0);
        }
        try {
            assert false;
            throw new Error("Please enable assertions");
        } catch(AssertionError ae) {
            ;
        }
        int seed = RAND.nextInt();
        System.out.println("seed="+seed);
        // You can fix the seed here if you want to.
//        seed=1337092206;
        RAND.setSeed(seed);
        Maze maze = new Maze();
        final boolean[] worked = {false};
        final PathFinder solver = (PathFinder) Class.forName(args[0]).getDeclaredConstructor().newInstance();
        final PathFinder solver2 = (PathFinder) Class.forName(args[0]).getDeclaredConstructor().newInstance();
        try {
            //maze.initBoard(40,20);
            maze.initBoard(15, 15);
            maze.solve(solver);
            worked[0] = true;
        } finally {
            System.out.println(maze);
            MazeViz mv = new MazeViz();
            mv.after = new Runnable() {
                public void run() {
                    if (worked[0]) {
                        Maze maze = new Maze(new MazeViz());
                        maze.initBoard(7,7);
                        maze.solveAnim(solver2);
                        System.out.println("All tests passed");
                    }
                }
            };
            mv.initMaze(maze);
        }
    }

    private boolean minc(Pos p) {
        Cell c = board[p.x][p.y];
        int s = c.sequence;
        if(s == 1)
            return true;
        c = board[p.x+1][p.y];
        if(c.sequence+1 == s)
            return true;
        c = board[p.x-1][p.y];
        if(c.sequence+1 == s)
            return true;
        c = board[p.x][p.y+1];
        if(c.sequence+1 == s)
            return true;
        c = board[p.x][p.y-1];
        if(c.sequence+1 == s)
            return true;
        return false;
    }
}

