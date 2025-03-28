/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basicgraphics.images;

import basicgraphics.BasicFrame;
import basicgraphics.FileUtility;
import basicgraphics.TaskRunner;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;

/**
 * This class encapsulates the creation and use of images.
 *
 * @author sbrandt
 */
public class Picture extends JComponent {

    private BufferedImage image;
    private int width, height;
    
    @Override
    public Dimension getSize() {
        return new Dimension(width, height);
    }

    BufferedImage addAlpha(BufferedImage bi) {
        BufferedImage newbi = BasicFrame.createImage(bi.getWidth(), bi.getHeight());
        Graphics2D g = newbi.createGraphics();
        g.drawImage(bi, 0, 0, null);
        return newbi;
    }

    /**
     * Set all white pixels to transparent
     */
    public Picture transparentBright() {
        BufferedImage newImage = addAlpha(image);
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                int color = image.getRGB(i, j);
                if ((color & 0x00FFFFFF) == 0x00FFFFFF) {
                    newImage.setRGB(i, j, 0x00FFFFFF);
                }
            }
        }
        return new Picture(newImage);
    }
    public Picture transparentBright(int brightness) {
        BufferedImage newImage = addAlpha(image);
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                int color = image.getRGB(i, j);
                int br = getBrightness(color);
                if (br >= brightness) {
                    newImage.setRGB(i, j, 0x00FFFFFF);
                }
            }
        }
        return new Picture(newImage);
    }

    private int getBrightness(int color) {
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;
        return Math.min(Math.min(r,g),b);
    }

    /**
     * Set all colors found on the border of the image to transparent
     */
    public Picture transparentBorder() {
        BufferedImage newImage = addAlpha(image);
        Set<Integer> set = new HashSet<>();
        for (int j = 0; j < height; j++) {
            int value = image.getRGB(0, j) & 0x00FFFFFF;
            set.add(value);
            value = image.getRGB(width - 1, j) & 0x00FFFFFF;
            set.add(value);
        }
        for (int i = 0; i < width; i++) {
            int value = image.getRGB(i, 0) & 0x00FFFFFF;
            set.add(value);
            value = image.getRGB(i, height - 1) & 0x00FFFFFF;
            set.add(value);
        }
        int count = 0;
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                int color = image.getRGB(i, j) & 0x00FFFFFF;
                if (set.contains(color)) {
                    newImage.setRGB(i, j, color);
                    count++;
                }
            }
        }
        return new Picture(newImage);
    }

    public void makeSquare() {
        if (width == height) {
            return;
        }
        int w = width > height ? width : height;
        int zero = image.getRGB(0, 0);
        BufferedImage bi = BasicFrame.createImage(w, w);
        for (int j = 0; j < w; j++) {
            for (int i = 0; i < w; i++) {
                bi.setRGB(i, j, zero);
            }
        }
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                int color = image.getRGB(i, j);
                int i2 = i, j2 = j;
                if (width < w) {
                    i2 += (w - width) / 2;
                }
                if (height < w) {
                    j2 += (w - height) / 2;
                }
                bi.setRGB(i2, j2, color);
            }
        }
        image = bi;
        width = w;
        height = w;
    }

    public Picture shrinkToMinimum() {
        int lox = width, hix = 0;
        int loy = height, hiy = 0;
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                int color = image.getRGB(i, j) & 0xFF000000;
                if (color != 0) {
                    if (i < lox) {
                        lox = i;
                    }
                    if (i > hix) {
                        hix = i;
                    }
                    if (j < loy) {
                        loy = j;
                    }
                    if (j > hiy) {
                        hiy = j;
                    }
                }
            }
        }
        if (lox == 0 && loy == 0 && hix == width - 1 && hiy == height - 1) {
            return this;
        }
        BufferedImage im = BasicFrame.createImage(1 + hix - lox, 1 + hiy - loy);
        for (int j = loy; j <= hiy; j++) {
            for (int i = lox; i <= hix; i++) {
                int color = image.getRGB(i, j);
                im.setRGB(i - lox, j - loy, color);
            }
        }
        image = im;
        width = (hix - lox + 1);
        height = (hiy - loy + 1);
        return new Picture(im);
    }

    /**
     * Get the raw image stored by this class.
     *
     * @return
     */
    public Image getImage() {
        return image;
    }

    /**
     * You should store your images in the same directory as the source for this
     * class (i.e. the same directory as Picture.java). That will enable you to
     * load them either from the working directory in Netbeans, or in the jar
     * file you distribute.
     *
     * @param name
     */
    public Picture(String name) {
        if(name == null) throw new NullPointerException("No name given to Picture");
        if (loadedImages.containsKey(name)) {
            // Load from cache
            image = loadedImages.get(name);
        } else {
            URI src = null;
            try {
                src = new URI(name);
                image = ImageIO.read(src.toURL());
            } catch (Exception ex) {
                // We don't care why this fails
            }
            if (image == null) {
                src = FileUtility.findFile(name);
                try {
                    image = ImageIO.read(src.toURL());
                } catch (MalformedURLException ex) {
                    TaskRunner.report(ex, null);
                } catch (Exception ex) {
                    TaskRunner.report(ex, null);
                }
            }
            if(image == null) image = randBlock();
        }
        width = image.getWidth();
        height = image.getHeight();
        setPreferredSize(new Dimension(width, height));
        setMinimumSize(getPreferredSize());
        loadedImages.put(name, image);
    }
    static Map<String, BufferedImage> loadedImages = new HashMap<>();

    /**
     * You can also create a picture from an image directly (using
     * basicgraphics.BasicFrame.createImage()) and drawing on it.
     *
     * @param im
     */
    public Picture(Image im) {
        this.image = (BufferedImage) im;
        width = image.getWidth();
        height = image.getHeight();
        setPreferredSize(new Dimension(width, height));
        setMinimumSize(getPreferredSize());
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Picture resize(double factor) {
        int w = (int) (image.getWidth() * factor);
        int h = (int) (image.getHeight() * factor);
        BufferedImage bi = BasicFrame.createImage(w, h);
        Graphics2D g2 = (Graphics2D) bi.getGraphics();
        AffineTransform xform = new AffineTransform();
        xform.setToScale(factor, factor);
        g2.drawImage(image, xform, this);
        return new Picture(bi);
    }

    public Picture flipLeftRight() {
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage bi = BasicFrame.createImage(w, h);
        Graphics2D g2 = (Graphics2D) bi.getGraphics();
        g2.drawImage(image, w, 0, -w, h, null);
        return new Picture(bi);
    }

    public Picture flipUpDown() {
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage bi = BasicFrame.createImage(w, h);
        Graphics2D g2 = (Graphics2D) bi.getGraphics();
        g2.drawImage(image, 0, h, w, -h, null);
        return new Picture(bi);
    }

    /**
     * Create a button that uses the same image as the one stored in this
     * Picture.
     *
     * @return
     */
    public JButton makeButton() {
        return new JButton(getIcon());
    }

    public ImageIcon getIcon() {
        return new ImageIcon(image);
    }

    public boolean mask(int i, int j) {
        if(i < 0 || i >= width || j < 0 || j >= height) return false;
        int val = image.getRGB(i, j);
        return (val & 0xFF000000) != 0;
    }

    final static Random RAND = new Random();

    private BufferedImage randBlock() {
        int w = 10 + RAND.nextInt(30);
        int h = 10 + RAND.nextInt(30);
        BufferedImage im = BasicFrame.createImage(w, h);
        int r = RAND.nextInt(256);
        int g = RAND.nextInt(256);
        int b = RAND.nextInt(256);
        Color c = new Color(r, g, b);
        Graphics gr = im.getGraphics();
        gr.setColor(c);
        gr.fillRect(0, 0, w, h);
        return im;
    }

    public static void main(String[] args) {
        Picture p = new Picture("meleeBasicAttackL.png");
    }
}
