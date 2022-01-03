package basicgraphics.sounds;

import basicgraphics.BasicFrame;
import basicgraphics.FileUtility;
import java.io.File;
import java.util.LinkedList;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

/**
 * An interface to the java sound system
 *
 * @author sbrandt
 */
public final class ReusableClip {

    byte[] buf;
    SourceDataLine sourceLine;
    
    static Thread thread;
    static boolean running = false;
    static LinkedList<ReusableClip> queue = new LinkedList<>();
    static boolean verbose = false;
    
    static {
        thread = new Thread(()->{});
        thread.start();
    }
    
    private static synchronized ReusableClip dequeue() {
        if(queue.size() == 0) return null;
        else return queue.removeLast();
    }
    
    private static synchronized void done() {
        running = false;
    }
    
    private static synchronized void enqueue(ReusableClip r) {
        queue.addFirst(r);
        if(!running) {
            try {
                thread.join();
            } catch (InterruptedException ex) {
            }
            running = true;
            thread = new Thread(()->{
                while(true) {
                    ReusableClip clip = dequeue();
                    if(clip == null)
                        break;
                    try {
                        clip.playNow();
                    if(clip.looping)
                        enqueue(clip);
                    } catch(Throwable e) {
                        System.err.println("endclip: "+e);
                    }
                }
                done();
            });
            thread.start();
        }
    }
    public ReusableClip(String name) {
        this(FileUtility.findFile(ReusableClip.class, name));
    }
    final String name;
    public ReusableClip(File f) {
        this.name = f.getName();
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(f);
            AudioFormat audioFormat = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
            sourceLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceLine.open(audioFormat);
            long nbytes = audioStream.getFrameLength();
            while(nbytes % 4 != 0) nbytes++;
            buf = new byte[(int)nbytes];
            audioStream.read(buf);
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }
    
    public void playNow() {
        if(verbose)
            System.out.println("playing sound: "+name);
        sourceLine.start();
        sourceLine.write(buf, 0, buf.length);
        sourceLine.drain();
        sourceLine.flush();
    }

    public synchronized void play() {
        looping = false;
        enqueue(this);
    }

    volatile boolean looping = false;
    public synchronized void loop() {
        looping = true;
        enqueue(this);
    }

    public void stop() {
        looping = false;
    }

    public static void main(String[] args) throws Exception {
        // It won't play without a JFrame.
        BasicFrame bf = new BasicFrame("title");
        bf.show();
        ReusableClip clip1 = new ReusableClip("arrow.wav");
        ReusableClip clip2 = new ReusableClip("beep.wav");
        long t1 = System.currentTimeMillis();
        clip1.loop();
        clip2.loop();
        Thread.sleep(15000);
        clip1.stop();
        clip2.stop();
        long t2 = System.currentTimeMillis();
        System.out.printf("time=%d%n",(t2-t1));
        bf.dispose();
    }
}
