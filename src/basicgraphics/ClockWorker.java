/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basicgraphics;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 *
 * @author sbrandt
 */
public class ClockWorker {

    private static Timer t = null;
    private static Queue<Task> newTasks = new ConcurrentLinkedDeque<>();
    private final static Task SENTINAL = new Task() {
        @Override
        public void run() {
        }
    };

    public static void addTask(Task task) {
        if (task.isSubmitted()) {
            return;
        }
        task.setSubmitted();
        newTasks.add(task);
    }

    public static void finish() {
        t.cancel();
        t = null;
    }

    public static void initialize(int period) {
        if (t != null) {
//            throw new GuiException("SpriteComponent already started");
            t.cancel();
        }
        t = new Timer();
        TimerTask tt = new TimerTask() {
            int toc = 0;

            @Override
            public void run() {
                newTasks.add(SENTINAL);
                while (true) {
                    final Task task = newTasks.remove();
                    if (task == SENTINAL) {
                        return;
                    }
                    Util.invokeAndWait(()->{ task.run_(); });
                    if (!task.isFinished()) {
                        newTasks.add(task);
                    }
                    if (t == null) {
                        break;
                    }
                }
            }
        };
        t.schedule(tt, 0, period);
    }
}
