/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basicgraphics.calculator;

import basicgraphics.BasicFrame;
import java.awt.AWTException;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.text.JTextComponent;

/**
 *
 * @author sbrandt
 */
public class Auto {
    Robot robot;
    public Auto() throws AWTException {
        robot = new Robot();
    }
    boolean set = false;
    ActionListener al = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            set = true;
        }
    };
    public void press(Component c) {
        delay();
        if(c instanceof JButton) {
            JButton jb = (JButton)c;
            jb.addActionListener(al);
        }
        set = false;
        Point pt = new Point(c.getLocationOnScreen());
        Dimension dim = c.getSize();
        robot.mouseMove(pt.x+dim.width/2, pt.y+dim.height/2);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        if(c instanceof JButton) {
            JButton jb = (JButton)c;
            jb.removeActionListener(al);
            assert set : "getLocationOnScreen() not working on this computer";
        }
    }

    public void press(BasicFrame bf, String b) {
        press(bf.getComponent(b));
    }

    private void delay() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
        }
    }
    
    public String getValue(BasicFrame bf, String txt) {
        delay();
        Component c = bf.getComponent(txt);
        if(c instanceof JLabel) {
            return ((JLabel) c).getText();
        } else if(c instanceof JTextArea) {
            return ((JTextArea) c).getText();
        } else if(c instanceof JTextComponent) {
            return ((JTextComponent) c).getText();
        }
        throw new Error("Not a text component");
    }
}
