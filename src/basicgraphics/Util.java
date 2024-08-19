/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package basicgraphics;

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
        if(a.getClass().isAssignableFrom(b.getClass()))
            return b.equals(a);
        else
            return a.equals(b);
    }
    
    /**
     * This method immediately invokes runnable method r
     * on the event dispatch thread.
     * @param r 
     */
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
