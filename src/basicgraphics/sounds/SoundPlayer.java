import basicgraphics.FileUtility;
import basicgraphics.images.RuntimeIOException;
import javax.sound.sampled.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
*
* @author - Jacob Nguyen @jacoobes 
* 
*/
public class SoundPlayer {
    /**
     * All clips are stored here. Make sure they are loaded before game starts!
     */
    private HashMap<String, Clip> mySounds = new HashMap<>();

    /**
     *
     * @param name the name of your sound
     * @param filename the file location of your .wav file
     * @throws Exception
     */
    public void newSound(String name, String filename) throws Exception {
        URL src = null;
        try {
            src = new URL(filename);
        } catch(MalformedURLException me) {
        }
        if(src == null)
            src = getClass().getResource(filename);
        if(src == null) {
            src = FileUtility.findFile(filename).toURL();
            if(src == null) {
                new RuntimeIOException("Could not load: "+filename).printStackTrace();
            }
        }
        if(src == null) throw new Exception("Cannot find file");
            Clip c =  AudioSystem.getClip();
            AudioInputStream ais = AudioSystem.getAudioInputStream(src);
            c.open(ais);
            mySounds.put(name, c);
    }

    /**
     *
     * @param name the name of the clip you want to loop
     * @param n how many times to loop. Use -1 if you want to loop infinitely
     * @throws Exception
     */
    public void loop (String name, int n) throws Exception {
        mySounds.get(name).loop(n);
    }

    /**
     *
     * @param name stop the given loop
     */
    public void stop(String name) {
        mySounds.get(name).stop();
    }
    /**
     * Stops all clips in map
     */
    public void stopAll() {
        for ( Clip c : mySounds.values() )
        {
            c.stop();
        }
    }
    /**
     * Loops all clips in map by n
     * @param n how many times to loop
     */
    public void loopAll (int n) {
        for ( Clip c : mySounds.values() )
        {
            c.loop(n);
        }
    }

    /**
     *
     * @param name name of clip
     * @return the volume of a clip
     */
    public float getVolume(String name) {
        FloatControl gainControl = (FloatControl) mySounds.get(name).getControl(FloatControl.Type.MASTER_GAIN);
        return (float) Math.pow(10f, gainControl.getValue() / 20f);
    }

    /**
     *
     * @param name name of clip
     * @param volume how much more volume u want. Range is 0f .. 1f
     */
    public void setVolume(String name, float volume) {
        if (volume < 0f || volume > 1f)
            throw new IllegalArgumentException("Volume not valid: " + volume);
        FloatControl gainControl = (FloatControl) mySounds.get(name).getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(20f * (float) Math.log10(volume));
    }
}