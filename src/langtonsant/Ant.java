/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package langtonsant;

import basicgraphics.BasicFrame;
import basicgraphics.ClockWorker;
import basicgraphics.SpriteComponent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 *
 * @author sbrandt
 */
public class Ant {
    public static String[][] layout = new String[][]{
        new String[]{"C"}
    };
    int delx, dely;
    boolean[][] grid = new boolean[NX][NY];
    final static int NX = 100, NY = 40, PIXELS_X = 1400, PIXELS_Y = 800, SPEED = 5;
    public static void main(String[] args) {
        Ant ant = new Ant();
    }
    public Ant() {
        BasicFrame bf = new BasicFrame("Langton's Ant");
        SpriteComponent sc = new SpriteComponent() {
          @Override
          public void paintBackground(Graphics g) {
              Dimension d = getSize();
              g.setColor(Color.white);
              g.fillRect(0, 0, d.width, d.height);
              g.setColor(Color.green);
              delx = d.width/NX;
              dely = d.height/NY;
              for(int i=0;i<NX;i++) {
                  for(int j=0;j<NY;j++) {
                      if(grid[i][j]) {
                          g.fillRect(i*delx,j*dely,delx,dely);
                      } else {
                          g.drawRect(i*delx,j*dely,delx,dely);
                      }
                  }
              }
          }
        };
        new AntSprite(sc,grid);
        bf.setStringLayout(layout);
        bf.add("C", sc);
        sc.setPreferredSize(new Dimension(PIXELS_X,PIXELS_Y));
        ClockWorker.addTask(sc.moveSprites());
        ClockWorker.initialize(SPEED);
        bf.show();
    }
}
