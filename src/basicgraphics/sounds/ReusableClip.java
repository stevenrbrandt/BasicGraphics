package basicgraphics.sounds;

import basicgraphics.BasicFrame;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * An interface to the java sound system
 *
 * @author sbrandt
 */
public final class ReusableClip {

    byte[] buf;
    SourceDataLine sourceLine;
    Thread thread;
    
    public ReusableClip(String name) {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(name));
            AudioFormat audioFormat = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
            sourceLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceLine.open(audioFormat);
            long nbytes = audioStream.getFrameLength();
            buf = new byte[(int)nbytes];
            audioStream.read(buf);
            thread = new Thread(()->{});
            thread.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            Logger.getLogger(ReusableClip.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void play() {
        try {
            looping = false;
            thread.join();
        } catch (InterruptedException ex) {
        }
        thread = new Thread(() -> {
            sourceLine.start();
            sourceLine.write(buf, 0, buf.length);
            sourceLine.drain();
        });
        thread.start();
    }

    boolean looping = true;
    public synchronized void loop() {
        try {
            looping = false;
            thread.join();
        } catch (InterruptedException ex) {
        }
        thread = new Thread(() -> {
            while (looping) {
                sourceLine.start();
                sourceLine.write(buf, 0, buf.length);
                sourceLine.drain();
            }
        });
        thread.start();
    }

    public synchronized void stop() {
        looping = false;
    }

    public static void main(String[] args) throws Exception {
        // It won't play without a JFrame.
        BasicFrame bf = new BasicFrame("title");
        bf.show();
        ReusableClip clip1 = new ReusableClip("C:\\Users\\Steve\\Downloads\\arrow_x.wav");
        long t1 = System.currentTimeMillis();
        clip1.play();
        long t2 = System.currentTimeMillis();
        System.out.printf("time=%d%n",(t2-t1));
        bf.dispose();
    }
}
