/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maze;

import basicgraphics.BasicFrame;
import basicgraphics.ClockWorker;
import basicgraphics.SpriteComponent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 *
 * @author sbrandt
 */
public class MazeViz {

    BasicFrame bf;
    Maze maze;
    
    MazeViz() {
        bf = BasicFrame.getFrame();
        if(bf != null)
            bf.disposeFrame();
        bf = new BasicFrame("Maze Runner");
        bf.setStringLayout(layout);
        bf.add( "C", jp);
    }
    final static String[][] layout = new String[][]{new String[]{"C"}};
    SpriteComponent jp = new SpriteComponent() {
        @Override
        public void paintBackground(Graphics g) {
            g.setFont(getFont());
            Dimension d = getSize();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, d.width, d.height);
            g.setColor(Color.BLACK);
            int delx = d.width / maze.sizex;
            int dely = d.height / maze.sizey;
            for (int i = 0; i < maze.sizex; i++) {
                g.drawRect(i * delx, 0, delx, d.height);
            }
            for (int j = 0; j < maze.sizey; j++) {
                g.drawRect(0, j * dely, d.width, dely);
            }
            for (int i = 0; i < maze.sizex; i++) {
                for (int j = 0; j < maze.sizey; j++) {
                    Cell c = maze.board[i][j];
                    if (c.isWall) {
                        g.setColor(Color.BLACK);
                        g.fillRect(i * delx, j * dely, delx, dely);
                    } else if (c.isStart || c.isGoal) {
                        g.setColor(Color.GREEN);
                        g.fillRect(i * delx, j * dely, delx, dely);
                    }
                    if (c.sequence > 0) {
                        final int fac = 6;
                        g.setColor(Color.BLUE);
                        g.drawString("" + c.sequence, i * delx + delx / fac, (j + 1) * dely - dely / fac);
                    }
                }
            }
        }
    };

    public Runnable after = new Runnable() {
        public void run() {}
    };
    public void initMaze(Maze m) {
        this.maze = m;
        jp.setPreferredSize(new Dimension((int)(500 / 15. * m.sizex), (int)(500 / 15. * m.sizey)));
        Font f = new Font("Courier", Font.BOLD, 20);
        bf.setAllFonts(f);
        bf.jf.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                if(after != null)
                    after.run();
            }
        });
        bf.jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ClockWorker.initialize(10);
        ClockWorker.addTask(jp.moveSprites());
        bf.show();
    }
}
