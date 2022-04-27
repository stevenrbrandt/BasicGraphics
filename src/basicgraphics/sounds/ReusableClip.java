package basicgraphics.sounds;

import basicgraphics.BasicFrame;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

/**
 * An interface to the java sound system
 *
 * @author Steven R. Brandt
 */
public final class ReusableClip {

    byte[] buf;
    SourceDataLine sourceLine;
    
    static Thread thread;
    static boolean running = false;
    static LinkedList<ReusableClip> queue = new LinkedList<>();
    public static boolean verbose = false;

    static ExecutorService overlappingSoundPool = Executors.newCachedThreadPool(r -> {
        Thread t = Executors.defaultThreadFactory().newThread(r);
        t.setDaemon(true);
        return t;
    });

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
                        if(verbose)
                            e.printStackTrace();
                        else
                            System.err.println("endclip: "+e);
                    }
                }
                done();
            });
            thread.start();
        }
    }
    
    private static URI getURI(String name) {
        try {
            return ReusableClip.class.getResource(name).toURI();
        } catch (URISyntaxException ex) {
            return null;
        }
    }
    public ReusableClip(String name) {
        this(getURI(name));
    }
    final String name;
    public ReusableClip(URI uri) {
        this.name = uri == null ? "?" : uri.getPath();
        try {
            if(verbose)
                System.out.println("loading sound: "+uri);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(uri.toURL());
            AudioFormat audioFormat = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
            sourceLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceLine.open(audioFormat);
            long nbytes = audioStream.getFrameLength();
            while(nbytes % 4 != 0) nbytes++;
            buf = new byte[(int)nbytes];
            audioStream.read(buf);
        } catch (Exception ex) {
            if(verbose)
                ex.printStackTrace();
            else
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

    public void playOverlapping() {
        overlappingSoundPool.submit(this::playNow);
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

    public static void testOverlapping(ReusableClip clip1, ReusableClip clip2) {
        clip1.playOverlapping();
        clip2.playOverlapping();
    }

    public static void main(String[] args) throws Exception {
        // It won't play without a JFrame.
        verbose = true;
        BasicFrame bf = new BasicFrame("title");
        bf.show();
        ReusableClip clip1 = new ReusableClip("arrow.wav");
        ReusableClip clip2 = new ReusableClip("beep.wav");
        long t1 = System.currentTimeMillis();
        clip1.loop();
        clip2.loop();
        Thread.sleep(5000);
        clip1.stop();
        clip2.stop();
        long t2 = System.currentTimeMillis();
        System.out.printf("time=%d%n",(t2-t1));
        Thread.sleep(5000);
        testOverlapping(clip1, clip2);
        bf.dispose();
    }
}
