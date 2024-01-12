/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basicgraphics;

/**
 *
 * @author sbrandt
 */
public abstract class Task implements Runnable {
    private boolean submitted = false;
    int iteration;
    private boolean finished = false;
    final private int minIter, maxIter;
    void setSubmitted() { submitted = true; }
    public boolean isSubmitted() { return submitted; }
    public int iteration() { return iteration; }
    public void setFinished() { finished = true; }
    public boolean isFinished() { return finished; }
    public int maxIteration() { return maxIter; }
    public abstract void run();
    final void run_() {
        iteration++;
        try {
            run();
        } catch(Throwable ex) {
            ex.printStackTrace();
            setFinished();
        }
        if(iteration == maxIter) {
            setFinished();
        }
    }
    
    public Task() {
        this.minIter = 0;
        this.maxIter = Integer.MAX_VALUE;
        this.iteration = this.minIter-1;
    }
    public Task(int maxIter) {
        this.minIter = 0;
        this.maxIter = maxIter;
        this.iteration = this.minIter-1;
    }
    public Task(int minIter, int maxIter) {
        this.minIter = minIter;
        this.maxIter = maxIter;
        this.iteration = this.minIter-1;
    }
}
