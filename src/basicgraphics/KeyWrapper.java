/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basicgraphics;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author sbrandt
 */
public class KeyWrapper implements KeyListener {

    KeyListener kl;
    public KeyWrapper(KeyListener kl) {
        this.kl = kl;
    }
    
    @Override
    public void keyTyped(final KeyEvent e) {
        Runnable run = new Runnable() {
            public void run() {
                kl.keyTyped(e);
            }
        };
        TaskRunner.runLater(e.getComponent(), run);
    }

    @Override
    public void keyPressed(final KeyEvent e) {
        Runnable run = new Runnable() {
            public void run() {
                kl.keyPressed(e);
            }
        };
        TaskRunner.runLater(e.getComponent(), run);
    }

    @Override
    public void keyReleased(final KeyEvent e) {
        Runnable run = new Runnable() {
            public void run() {
                kl.keyReleased(e);
            }
        };
        TaskRunner.runLater(e.getComponent(), run);
    }
    
}
