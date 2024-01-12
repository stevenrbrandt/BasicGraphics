/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package robosorter;

import basicgraphics.BasicFrame;
import basicgraphics.ClockWorker;
import basicgraphics.SpriteComponent;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Random;

/**
 *
 * @author sbrandt
 */
public class Robo {
    static String[][] layout = {
        {"one"}
    };
    final static int NX = 10, NY = 3, BLOCK_SIZE = 50, PADDING=3, SPEED = 5;
    public final static Random RAND = new Random();
    
    public static void main(String[] args) {
        BasicFrame bf = new BasicFrame("Robo");
        SpriteComponent sc = new SpriteComponent() {
            @Override
            public void paintBackground(Graphics g) {
                Dimension d = getSize();
                g.clearRect(0,0,d.width,d.height);
                for(int i=0;i<NX;i++) {
                    for(int j=0;j<NY;j++) {
                        g.drawRect(PADDING+BLOCK_SIZE*i, PADDING+BLOCK_SIZE*j, BLOCK_SIZE, BLOCK_SIZE);
                    }
                }
            }
        };
        Board board = new Board();
        bf.setStringLayout(layout);
        bf.add("one", sc);
        sc.setPreferredSize(new Dimension(NX*BLOCK_SIZE+2*PADDING,NY*BLOCK_SIZE+2*PADDING));
        for(int i=0;i<NX;i++) {
            MartianEgg me = new MartianEgg(sc, RAND.nextInt(100));
            board.set(me,i,2);
        }
        Mover mv = new Mover(sc,board);
        bf.show();
        ClockWorker.initialize(20);
        board.m = mv;
        new EggSorter().sort(mv,board);
    }
}
