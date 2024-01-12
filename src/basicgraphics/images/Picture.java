/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basicgraphics.images;

import basicgraphics.BasicFrame;
import basicgraphics.FileUtility;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;

/**
 * This class encapsulates the creation and
 * use of images.
 * @author sbrandt
 */
public class Picture extends JComponent {
    
    private BufferedImage image;
    private int width, height;
    boolean[][] mask;
    
    /**
     * Set all white pixels to transparent
     */
    public void transparentWhite() {
        for(int j=0;j<height;j++) {
            for(int i=0;i<width;i++) {
                int color = image.getRGB(i,j);
                if((color & 0x00FFFFFF) == 0x00FFFFFF) {
                    image.setRGB(i, j, 0x00000000);
                }
            }
        }
    }
    /**
     * Set all colors found on the border of the
     * image to transparent
     */
    public void transparentBorder() {
        Set<Integer> set = new HashSet<>();
        for(int j=0;j<height;j++) {
            int value = image.getRGB(0, j) & 0x00FFFFFF;
            set.add(value);
            value = image.getRGB(width-1, j) & 0x00FFFFFF;
            set.add(value);
        }
        for(int i=0;i<width;i++) {
            int value = image.getRGB(i,0) & 0x00FFFFFF;
            set.add(value);
            value = image.getRGB(i, height-1) & 0x00FFFFFF;
            set.add(value);
        }
        for(int j=0;j<height;j++) {
            for(int i=0;i<width;i++) {
                int color = image.getRGB(i,j) & 0x00FFFFFF;
                if(set.contains(color)) {
                    image.setRGB(i, j, 0x00000000);
                }
            }
        }
    }
    
    public void makeSquare() {
        if(width == height)
            return;
        int w = width > height ? width : height;
        int zero = image.getRGB(0,0);
        BufferedImage bi = BasicFrame.createImage(w, w);
        for(int j=0;j<w;j++) {
            for(int i=0;i<w;i++) {
                bi.setRGB(i, j, zero);
            }
        }
        for(int j=0;j<height;j++) {
            for(int i=0;i<width;i++) {
                int color = image.getRGB(i,j);
                int i2 = i, j2 = j;
                if(width < w) i2 += (w-width)/2;
                if(height < w) j2 += (w-height)/2;
                bi.setRGB(i2, j2, color);
            }
        }
        image = bi;
        width = w;
        height = w;
    }
    
    public void shrinkToMinimum() {
        if(true) return;
        int lox = width, hix = 0;
        int loy = height, hiy = 0;
        for(int j=0;j<height;j++) {
            for(int i=0;i<width;i++) {
                int color = image.getRGB(i,j) & 0x00FFFFFF;
                if(color != 0) {
                    if(i < lox) lox = i;
                    if(i > hix) hix = i;
                    if(j < loy) loy = j;
                    if(j > hiy) hiy = j;
                }
            }
        }
        for(int i=0;i<3;i++) {
            if (lox > 0) {
                lox--;
            }
            if (loy > 0) {
                loy--;
            }
            if (hix + 1 < width) {
                hix++;
            }
            if (hiy + 1 < height) {
                hiy++;
            }
        }
        if(lox == 0 && loy == 0 && hix == width-1 && hiy == height-1) {
            return;
        }
        BufferedImage im = BasicFrame.createImage(1+hix-lox, 1+hiy-loy);
        for(int j=loy; j <= hiy; j++) {
            for(int i=lox;i <= hix; i++) {
                int color = image.getRGB(i,j);
                im.setRGB(i-lox, j-loy, color);
            }
        }
        image = im;
        width = (hix - lox + 1);
        height = (hiy - loy + 1);
    }
    
    /**
     * Get the raw image stored by this class.
     * @return 
     */
    public Image getImage() { return image; }
    
    /** You should store your images
     * in the same directory as the source for
     * this class (i.e. the same directory as
     * Picture.java). That will enable you to
     * load them either from the working directory
     * in Netbeans, or in the jar file you
     * distribute.
     * @param name
     */
    public Picture(String name) {
        URL src = null;
        try {
            src = new URL(name);
        } catch(MalformedURLException me) {
            ;
        }
        if(src == null)
            src = getClass().getResource(name);
        if(src == null) {
            src = FileUtility.findFile(name);
            if(src == null) {
                new RuntimeIOException("Could not load: "+name).printStackTrace();
                image = randBlock();
            }
        }
        try {
            image = ImageIO.read(src);
        } catch (Exception ex) {
            new RuntimeIOException("Could not load: "+name+" / "+src,ex).printStackTrace();
            image = randBlock();
        }
        width = image.getWidth();
        height = image.getHeight();
        System.out.printf("name=%s: %dx%d%n",name,width,height);
        setPreferredSize(new Dimension(width,height));
        setMinimumSize(getPreferredSize());
        createMask();
    }
    
    void createMask() {
        mask = new boolean[width][height];
        for(int j=0;j<height;j++) {
            for(int i=0;i<width;i++) {
                int color = image.getRGB(i, j);
                mask[i][j] = (color & 0xFF000000) != 0;
            }
        }
    }
    
    /**
     * You can also create a picture from an image
     * directly (using basicgraphics.BasicFrame.createImage())
     * and drawing on it.
     * @param im 
     */
    public Picture(Image im) {
        this.image = (BufferedImage) im;
        width = image.getWidth();
        height = image.getHeight();
        setPreferredSize(new Dimension(width,height));
        setMinimumSize(getPreferredSize());
        createMask();
    }
    
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    
    public Picture resize(double factor) {
        int w = (int) (image.getWidth()*factor);
        int h = (int) (image.getHeight()*factor);
        BufferedImage bi = BasicFrame.createImage(w,h);
        Graphics2D g2 = (Graphics2D)bi.getGraphics();
        AffineTransform xform = new AffineTransform();
        xform.setToScale(factor, factor);
        g2.drawImage(image, xform, this);
        return new Picture(bi);
    }
    
    public Picture flipLeftRight() {
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage bi = BasicFrame.createImage(w,h);
        Graphics2D g2 = (Graphics2D)bi.getGraphics();
        g2.drawImage(image, w, 0, -w, h, null);
        return new Picture(bi);
    }
    public Picture flipUpDown() {
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage bi = BasicFrame.createImage(w,h);
        Graphics2D g2 = (Graphics2D)bi.getGraphics();
        g2.drawImage(image, 0, h, w, -h, null);
        return new Picture(bi);
    }
    
    /**
     * Create a button that uses the same
     * image as the one stored in this Picture.
     * @return 
     */
    public JButton makeButton() {
        return new JButton(new ImageIcon(image));
    }
    
    public boolean mask(int i,int j) {
        if(i < 0 || i >= mask.length) return false;
        if(j < 0 || j >= mask[i].length) return false;
        return mask[i][j];
    }

    final static Random RAND = new Random();
    
    private BufferedImage randBlock() {
        int w = 10 + RAND.nextInt(30);
        int h = 10 + RAND.nextInt(30);
        BufferedImage im = BasicFrame.createImage(w,h);
        int r = RAND.nextInt(256);
        int g = RAND.nextInt(256);
        int b = RAND.nextInt(256);
        Color c = new Color(r,g,b);
        Graphics gr = im.getGraphics();
        gr.setColor(c);
        gr.fillRect(0, 0, w, h);
        return im;
    }
}
