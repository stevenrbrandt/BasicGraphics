/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package walkthedog;

import basicgraphics.BasicFrame;
import basicgraphics.Clock;
import basicgraphics.CollisionEventType;
import basicgraphics.Sprite;
import basicgraphics.SpriteCollisionEvent;
import basicgraphics.SpriteComponent;
import basicgraphics.Task;
import basicgraphics.images.Picture;
import java.awt.Dimension;
import java.io.IOException;

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
        Clock.addTask(new Task() {
            @Override
            public void run() {
                count++;
                if (count == 100) {
                    setPicture(basePic.rotate(.1));
                } else if (count == 200) {
                    setPicture(basePic.rotate(-.1));
                    count = 0;
                }
            }
        });
    }
    int count = 0;
    @Override
    public void processEvent(SpriteCollisionEvent ev) {
        if(ev.eventType == CollisionEventType.WALL_INVISIBLE) {
            setX(800);
            basePic = basePic.resize(1.1);
        }
    }
}
public class WalkDog {
    public static void main(String[] args) {
        SpriteComponent sc = new SpriteComponent();
        Dog dog = new Dog(sc);
        dog.setVelX(-1.0);
        dog.setX(750);
        
        BasicFrame bf = new BasicFrame("Walk the Dog");
        String[][] layout = {{"dog"}};
        bf.setStringLayout(layout);
        bf.add("dog",sc);
        sc.setPreferredSize(new Dimension(800,400));
        bf.show();
        Clock.addTask(sc.moveSprites());
        Clock.start(10);
    }
}
