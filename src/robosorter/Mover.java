/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package robosorter;

import basicgraphics.Clock;
import basicgraphics.Sprite;
import basicgraphics.SpriteComponent;
import basicgraphics.Task;
import basicgraphics.images.Picture;
import java.util.LinkedList;

/**
 *
 * @author sbrandt
 */
public class Mover extends Sprite {

    MartianEgg carrying;
    Board board;
    int count, x, y;
    LinkedList<Runnable> actions = new LinkedList<>();

    public Mover(SpriteComponent sc, Board board) {
        super(sc);
        Picture p = new Picture("claw.png");
        this.board = board;
        p.transparentWhite();
        setPicture(p);
        Clock.addTask(new Task() {
            @Override
            public void run() {
                doMove();
            }
        });
    }
    
    Runnable running = null;

    public void doMove() {
        if (count > 0) {
            count--;
            if (count == 0) {
                setVelX(0);
                setVelY(0);
            }
        }
        if (count == 0 || running == null) {
            if (!actions.isEmpty()) {
                Runnable r = actions.removeFirst();
                running = r;
                try {
                    r.run();
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                    System.exit(0);
                }
            }
        }
        if (carrying != null) {
            carrying.setX(getX());
            carrying.setY(getY());
        }
    }

    public String toString() {
        return "Mover";
    }
}
