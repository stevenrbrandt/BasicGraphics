/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package robosorter;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sbrandt
 */
public class EggSorter {
    public final Random RAND = new Random();
    public void sort_(Mover m,Board board) {
        board.move(0,2);
        board.carry(0,0);
        board.carry(0,1);
        board.carry(3,1);
        board.carry(2,1);
    }
    // Bubble sorter
    public void bsort(Mover mv,Board board) {
        boolean done = false;
        while (!done) {
            done = true;
            for (int i = 1; i < board.cells.length; i++) {
                MartianEgg m1 = board.cells[i - 1][2];
                MartianEgg m2 = board.cells[i][2];
                if (m1.num > m2.num) {
                    board.move(i - 1, 2);
                    board.carry(i - 1, 0);
                    board.move(i, 2);
                    board.carry(i - 1, 2);
                    board.move(i - 1, 0);
                    board.carry(i, 2);
                    done = false;
                }
            }
        }
    }
    public void msort(Mover mv,Board board) {
        msort(mv,board,0,board.cells.length);
    }
    void msort(Mover mv,Board board,int start,int end) {
        if(end - start < 2)
            return;
        int mid = (start+end)/2;
        msort(mv,board,start,mid);
        msort(mv,board,mid,end);
        int upper=0, lower=0;
        for(int i=start;i<end;i++) {
            if(board.cells[i][0] != null) upper++;
            if(board.cells[i][2] != null) lower++;
        }
        
        int from = 0, to = 2;
        if(lower > upper) {
            from = 2;
            to = 0;
        }
        
        for (int i = start; i < end; i++) {
            if (board.cells[i][to] != null) {
                assert board.cells[i][from] == null;
                board.move(i, to);
                board.carry(i, from);
            }
        }
        int i1 = start, i2 = mid, i0 = start;
        while(i1 < mid && i2 < end) {
            if(board.cells[i1][from].num < board.cells[i2][from].num) {
                board.move(i1++,from);
                board.carry(i0++,to);
            } else {
                board.move(i2++,from);
                board.carry(i0++,to);
            }
        }
        while(i1 < mid) {
            board.move(i1++,from);
            board.carry(i0++, to);
        }
        while(i2 < end) {
            board.move(i2++,from);
            board.carry(i0++,to);
        }
        assert i1==mid;
        assert i2==end;
        assert i0==end;
        for(int i=start;i<end;i++) {
            assert board.cells[i][to] != null;
            assert board.cells[i][from] == null;
        }
    }
    public void sort(Mover mv,Board board) {
        msort(mv,board);
        for(int i=0;i<board.cells.length;i++) {
            MartianEgg me = board.cells[i][0];
            if(me != null) {
                board.move(i,0);
                board.carry(i,2);
            }
        }
    }
    public void qsort(Mover mv,Board board) {
        qsort(mv,board,0,board.cells.length,0);
    }
    public void qsort(Mover mv,Board board,int start,int end,int row) {
        if(end - start < 2)
            return;
        for(int i=start;i<end;i++) {
            assert board.cells[i][2-row] != null;
            assert board.cells[i][row] == null;
        }
        int pivot = RAND.nextInt(end-start)+start;
        MartianEgg me = board.cells[pivot][2-row];
        int i1 = start, i2 = end;
        for(int i=start;i<end;i++) {
            if(board.cells[i][2-row].num < me.num) {
                board.move(i,2-row);
                board.carry(i1++,row);
            } else if(board.cells[i][2-row].num > me.num) {
                board.move(i,2-row);
                board.carry(--i2,row);
            }
        }
        int imid = i1;
        for(int i=start;i<end;i++) {
            if(board.cells[i][2-row] != null) {
                board.move(i,2-row);
                board.carry(imid++,row);
            }
        }
        qsort(mv,board,start,i1,2-row);
        qsort(mv,board,i2,end,2-row);
    }
}
