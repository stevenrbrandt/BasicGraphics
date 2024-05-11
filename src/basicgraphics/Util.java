/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package basicgraphics;

import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/**
 *
 * @author steve
 */
public class Util {
    
    /**
     * The java == operator does not compare String or Integer
     * objects correctly. Instead, one has to use the object's
     * equals method to compare them correctly.
     * @param a 
     * @param b 
     * @return 
     */
    public static boolean eq(Object a, Object b) {
        if(a == b) return true;
        if(a == null || b == null) return false;
        return a.equals(b);
    }
    
    public static void invokeAndWait(Runnable r) {
        if(SwingUtilities.isEventDispatchThread())
            r.run();
        else {
            try {
                SwingUtilities.invokeAndWait(r);
            } catch(Exception ex) {
                TaskRunner.report(ex, null);
            }
        }
    }

    static void sleep(int duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException ex) {
            TaskRunner.report(ex, null);
        }
    }
}
