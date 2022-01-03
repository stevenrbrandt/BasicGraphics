package basicgraphics.sounds;

import basicgraphics.BasicFrame;
import basicgraphics.FileUtility;
import basicgraphics.GuiException;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An interface to the java sound system
 *
 * @author sbrandt
 */
public final class ReusableClip implements java.applet.AudioClip {

    private Clip clip;

    public ReusableClip(String name) {
        URL src = getClass().getResource(name);
        if (src == null) {
            src = FileUtility.findFile(name);
        }
        try {
            clip = AudioSystem.getClip();
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(src);
            clip.open(audioIn);
        } catch (Exception ex) {
            ;//throw new GuiException();
        }
    }

    @Override
    public void play() {
        if(!clip.isActive())
            clip.loop(1);
    }

    @Override
    public void loop() {
        if(!clip.isActive())
            clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    @Override
    public void stop() {
        if(clip.isActive())
            clip.stop();
    }

    public static void main(String[] args) throws Exception {
        // It won't play without a JFrame.
        BasicFrame bf = new BasicFrame("title");
        bf.show();
        ReusableClip clip1 = new ReusableClip("lazer.wav");
        long t1 = System.currentTimeMillis();
        clip1.loop();
        long t2 = System.currentTimeMillis();
        System.out.printf("time=%d%n",(t2-t1));
        bf.dispose();
    }
}
