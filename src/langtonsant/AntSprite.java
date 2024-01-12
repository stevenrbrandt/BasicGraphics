/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package langtonsant;

import basicgraphics.ClockWorker;
import basicgraphics.Sprite;
import basicgraphics.SpriteComponent;
import basicgraphics.Task;
import basicgraphics.images.Picture;
import java.awt.Dimension;

/**
 *
 * @author sbrandt
 */
class AntSprite extends Sprite {
    Picture pic;
    
    public AntSprite(SpriteComponent sc,boolean[][] grid) {
        super(sc);
        pic = new Picture("ant-small.png");
        setPicture(pic);
        this.grid = grid;
        ClockWorker.addTask(new Task() {
            @Override
            public void run() {
                doMove();
            }
        });
    }
    
    int i=Ant.NX/2,j=Ant.NY/2;
    int facing = 2;
    boolean[][] grid;
    
    public void doMove() {
        SpriteComponent sc = getSpriteComponent();
        Dimension d = sc.getSize();
        int delx = d.width / Ant.NX;
        int dely = d.height / Ant.NY;
        setX(i * delx + (delx - getPicture().getWidth())/2);
        setY(j * dely + (dely - getPicture().getHeight())/2);
        if(grid[i][j]) {
            facing += 1;
        } else {
            facing -= 1;
        }
        rotate(Math.PI*0.5*facing);
        grid[i][j] = !grid[i][j];
        int facingx = (int)Math.round(Math.cos(Math.PI*facing/2));
        int facingy = (int)Math.round(Math.sin(Math.PI*facing/2));
        
        i += facingx;
        j += facingy;
        if(j < 0) j += Ant.NY;
        if(i < 0) i += Ant.NX;
        if(j >= Ant.NY) j -= Ant.NY;
        if(i >= Ant.NX) i -= Ant.NX;
    }
}
