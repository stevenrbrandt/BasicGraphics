/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package walkthedog;

import basicgraphics.BasicFrame;
import basicgraphics.ClockWorker;
import basicgraphics.CollisionEventType;
import basicgraphics.Sprite;
import basicgraphics.SpriteCollisionEvent;
import basicgraphics.SpriteComponent;
import basicgraphics.Task;
import basicgraphics.images.Picture;
import java.awt.Dimension;

/**
 *
 * @author sbrandt
 */
class Dog extends Sprite {
    Picture basePic;
    Dog(SpriteComponent sc) {
        super(sc);
        basePic = new Picture("dog.jpg");
        setPicture(basePic);
        final double del = .1;
        ClockWorker.addTask(new Task() {
            @Override
            public void run() {
                count++;
                if (count == 100) {
                    setHeadingOffset(Math.PI+del);
                } else if (count == 200) {
                    setHeadingOffset(Math.PI-del);
                    count = 0;
                }
            }
        });
    }
    int count = 0;
    @Override
    public void processEvent(SpriteCollisionEvent ev) {
        if(ev.eventType == CollisionEventType.WALL_INVISIBLE) {
            setVel(-getVelX(), 0);
            setPicture(getPicture().flipUpDown());
        }
    }
}
public class WalkDog {
    public static void main(String[] args) {
        SpriteComponent sc = new SpriteComponent();
        Dog dog = new Dog(sc);
        dog.setVel(-1.0, 0);
        dog.setY(100);
        dog.setX(800);
        dog.setHeadingOffset(Math.PI);
        
        BasicFrame bf = new BasicFrame("Walk the Dog");
        String[][] layout = {{"dog"}};
        bf.setStringLayout(layout);
        bf.add("dog",sc);
        sc.setPreferredSize(new Dimension(800,400));
        bf.show();
        ClockWorker.addTask(sc.moveSprites());
        ClockWorker.initialize(10);
    }
}
