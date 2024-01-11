/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basicgraphics;

import java.awt.Component;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedList;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * The task runner attempts to avoid the complexity of
 * writing threaded programs. Putting the task on the
 * event dispatch queue is not enough. It is possible
 * for tasks to run simultaneously, <em>even within that
 * one thread</em>, because any call to a graphics function
 * may pull another task of the event queue and run it.
 * Thus we introduce the busy flag and a linked list of
 * tasks to ensure that doesn't happen.
 * @author sbrandt
 */
public class TaskRunner {
    static LinkedList<Runnable> tasks = new LinkedList<>();
    static boolean busy = false;
    /**
     * Run a task at a later time on the event dispatch thread.
     * @param c used to report exceptions
     * @param r the task to run
     */
    static void runLater(final Component c,final Runnable r) {
        Runnable re = new Runnable() {
            @Override
            public void run() {
                tasks.addLast(r);
                if(busy)
                    return;
                try {
                    busy = true;
                    // Catch up if there's a backlog
                    for (int i = 0; i < 3; i++) {
                        if(tasks.isEmpty())
                            break;
                        Runnable next = tasks.removeFirst();
                        try {
                            next.run();
                        } catch (Exception e) {
                          report(e,c);
                        }
                    }
                } finally {
                    busy = false;
                }
            }
        };
        SwingUtilities.invokeLater(re);
    }
    public static void report(Throwable e,Component c) {
      // We don't want to lose information
      // about exceptions!
      // First provide to the console.
      e.printStackTrace();
      // Next provide output to the GUI,
      // because the console may not be
      // visible.
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      e.printStackTrace(pw);
      pw.close();
      JOptionPane.showMessageDialog(c, sw.toString());
    }
}
