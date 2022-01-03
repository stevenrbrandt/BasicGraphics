/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basicgraphics;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 *
 * @author sbrandt
 */
public class MouseWrapper implements MouseListener {
    
    MouseListener ml;
    public MouseWrapper(MouseListener ml) {
        this.ml = ml;
    }

    @Override
    public void mouseClicked(final MouseEvent e) {
        Runnable run = new Runnable() {
            public void run() {
                ml.mouseClicked(e);
            }
        };
        TaskRunner.runLater(e.getComponent(), run);
   }

    @Override
    public void mousePressed(final MouseEvent e) {
        Runnable run = new Runnable() {
            public void run() {
                ml.mousePressed(e);
            }
        };
        TaskRunner.runLater(e.getComponent(), run);
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
        Runnable run = new Runnable() {
            public void run() {
                ml.mouseReleased(e);
            }
        };
        TaskRunner.runLater(e.getComponent(), run);
    }

    @Override
    public void mouseEntered(final MouseEvent e) {
        Runnable run = new Runnable() {
            public void run() {
                ml.mouseEntered(e);
            }
        };
        TaskRunner.runLater(e.getComponent(), run);
    }

    @Override
    public void mouseExited(final MouseEvent e) {
        Runnable run = new Runnable() {
            public void run() {
                ml.mouseExited(e);
            }
        };
        TaskRunner.runLater(e.getComponent(), run);
     }
    
}
