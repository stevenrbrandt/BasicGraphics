/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basicgraphics;

import basicgraphics.images.BackgroundPainter;
import basicgraphics.images.Painter;
import basicgraphics.images.Picture;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

/**
 *
 * @author sbrandt
 */
public class SpriteComponent extends JComponent implements MouseListener {
    Scene scene;
    Set<Sprite> sprites() { return scene.sprites; }
    
    public Scene swapScene(Scene scene) {
        if(scene == this.scene) {
            return scene;
        }
        Scene oldScene = this.scene;
        this.scene = scene;
        if(scene == null) System.out.println("Null scene");
        assert scene.spritecomponent == oldScene.spritecomponent;
        return oldScene;
    }

    public SpriteComponent() {
        scene = new Scene(this);
        int m = 50;
        setPreferredSize(new Dimension(BasicFrame.FRAME_SIZE.width-m, BasicFrame.FRAME_SIZE.height-m));
        setMinimumSize(new Dimension(300,300));
        addMouseListener(this);
    }

    public void paintBackground(Graphics g) {
        Dimension d = getFullSize();
        if (scene.painter == null) {
            g.setColor(Color.white);
            g.fillRect(0, 0, d.width, d.height);
        } else {
            scene.painter.paint(g, d);
        }
    }
    
    public Dimension getFullSize() {
        return scene.backgroundSize == null ? super.getSize() : scene.backgroundSize;
    }
    
    double setMinMax(double value, double minv, double maxv) {
        if(value < minv) value = minv;
        if(value > maxv) value = maxv;
        return value;
    }

    final void paintRegion(Graphics g, PaintRegion pr) {
        Dimension d = getSize();
        if (scene.background != null) {
            Dimension full = getFullSize();
            int fwidth = full.width - d.width;
            int fheight = full.height - d.height;
            int fw = full.width;
            int fh = full.height;
            int sx1 = scene.focus == null ? 0 : (int) (scene.focus.getCenterX() - d.width / 2);
            int sy1 = scene.focus == null ? 0 : (int) (scene.focus.getCenterY() - d.height / 2);
            int ux1 = sx1 + d.width;
            int uy1 = sy1 + d.height;
            // Region:
            // outside 
            if(scene.periodic_x || scene.periodic_y) {
                int px = 0, py = 0;
                if(scene.periodic_x == false) {
                    px = 0;
                    if(sx1 < 0) sx1 = 0;
                    else if(sx1 > fwidth) sx1 = fwidth;
                    ux1 = sx1 + d.width;
                }
                else if(sx1 < 0) px = -1;
                else if(ux1 >= fw) px = 1;
                if(scene.periodic_y == false) {
                    py = 0;
                    if(sy1 < 0) sy1 = 0;
                    else if(sy1 > fheight) sy1 = fheight;
                    uy1 = sy1 + d.height;
                }
                else if(sy1 < 0) py = -1;
                else if(uy1 > fh) py = 1;
                if (px == 0 && py == 0) {
                    pr.paintRegion(g, sx1, sy1, ux1, uy1, sx1, sy1, ux1, uy1);
                } else if (px < 0 && py < 0) {
                    pr.paintRegion(g, sx1, sy1, 0, 0,
                            sx1+fw, sy1+fh, fw, fh);
                    pr.paintRegion(g, 0, 0, ux1, uy1,
                            0, 0, ux1, uy1);
                    pr.paintRegion(g, 0, sy1, ux1, 0,
                            0, sy1+fh, ux1, fh);
                    pr.paintRegion(g, sx1, 0, 0, uy1,
                            sx1+fw, 0, fw, uy1);
                } else if(px < 0 && py == 0) {
                    pr.paintRegion(g, sx1, sy1, 0, uy1,
                            sx1+fw, sy1, fw, uy1);
                    pr.paintRegion(g, 0, sy1, ux1, uy1,
                            0, sy1, ux1, uy1);
                } else if(px == 0 && py < 0) {
                    pr.paintRegion(g, sx1, sy1, ux1, 0, 
                            sx1, sy1+fh, ux1, fh);
                    pr.paintRegion(g, sx1, 0, ux1, uy1, 
                            sx1, 0, ux1, uy1);
                } else if (px > 0 && py > 0) {
                    pr.paintRegion(g, sx1, sy1, fw, fh,
                            sx1, sy1, fw, fh);
                    pr.paintRegion(g, fw, fh, ux1, uy1,
                            0, 0, ux1-fw, uy1-fh);
                    pr.paintRegion(g, fw, sy1, ux1, fh,
                            0, sy1, ux1 - fw, fh);
                    pr.paintRegion(g, sx1, fh, fw, uy1,
                            sx1, 0, fw, uy1 - fh);
                } else if (px > 0 && py == 0) {
                    pr.paintRegion(g, sx1, sy1, fw, uy1,
                            sx1, sy1, fw, uy1);
                    pr.paintRegion(g, fw, sy1, ux1, uy1,
                            0, sy1, ux1-fw, uy1);
                } else if (px == 0 && py > 0) {
                    pr.paintRegion(g, sx1, sy1, ux1, fh,
                            sx1, sy1, ux1, fh);
                    pr.paintRegion(g, sx1, fh, ux1, uy1,
                            sx1, 0, ux1, uy1-fh);
                } else if (px < 0 && py > 0) {
                    pr.paintRegion(g, 0, sy1, ux1, fh,
                            0, sy1, ux1, fh);
                    pr.paintRegion(g, 0, fh, ux1, uy1,
                            0, 0, ux1, uy1-fh);
                    pr.paintRegion(g, sx1, sy1, 0, fh,
                            sx1+fw, sy1, fw, fh);
                    pr.paintRegion(g, sx1, fh, 0, uy1,
                            sx1+fw, 0, fw, uy1-fh);
                } else if (px > 0 && py < 0) {
                    pr.paintRegion(g, sx1, 0, fw, uy1,
                            sx1, 0, fw, uy1);
                    pr.paintRegion(g, sx1, sy1, fw, 0,
                            sx1, sy1+fh, fw, fh);
                    pr.paintRegion(g, fw, 0, ux1, uy1,
                            0, 0, ux1-fw, uy1);
                    pr.paintRegion(g, fw, sy1, ux1, 0,
                            0, sy1+fh, ux1-fw, fh);
                } else {
                    g.setColor(Color.red);
                    g.fillRect(sx1, sy1, d.width, d.height);
                }
            } else {
                sx1 = (int) setMinMax(sx1, 0, fwidth);
                sy1 = (int) setMinMax(sy1, 0, fheight);
                int dx1 = sx1;
                int dy1 = sy1;
                int sx2 = sx1 + d.width;
                int sy2 = sy1 + d.height;
                int dx2 = dx1 + d.width;
                int dy2 = dy1 + d.height;
                pr.paintRegion(g, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2);
            }
        } else {
            pr.paintRegion(g, 0, 0, d.width, d.height, 0, 0, d.width, d.height);
        }
    }
    
    public void setOffsetX(int x) { scene.offsetX = x; }
    public int getOffsetX() { return scene.offsetX; }
    public void setOffsetY(int y) { scene.offsetY = y; }
    public int getOffsetY() { return scene.offsetY; }
    
    Color drawBox = null;
    /**
     * This is a debugging tool.
     * Pass a color to draw a box of that color around sprites.
     * Pass null to turn off drawing a box around sprites.
     * @param b
     * @return 
     */
    public Color setDrawBox(Color b) {
        Color oldValue = drawBox;
        drawBox = b;
        return oldValue;
    }
    
    public boolean scroll(int x,int y,int s) {
        if(y + scene.offsetY < 0) {
            scene.offsetY += s;
            return true;
        } else {
            Dimension d = getSize();
            if (y + scene.offsetY > d.height) {
                scene.offsetY -= s;
                return true;
            }
        }
        return false;
    }

    Image image;
    Dimension sz;

    @Override
    public final void paintComponent(Graphics g) {
        Dimension d = getSize();
        Dimension b = scene.backgroundSize;
        if(b == null) b = d;
        // image = null;
        if (image == null || sz.width != b.width || sz.height != b.height) {
            image = createImage(b.width, b.height);
            sz = b;
        }
        Graphics gi = image.getGraphics();
        if (scene.focus != null) {
            AffineTransform at = new AffineTransform();
            if (scene.focus != null && scene.backgroundSize != null) {
                at = new AffineTransform(at);
                
//                subOffsetX = d.width/2-focus.getCenterX();
                if(!scene.periodic_x) {
//                    subOffsetX = setMinMax(subOffsetX,  -b.width+d.width,0);
                }
                
//                subOffsetY = d.height/2-focus.getCenterY();
                if(!scene.periodic_y) {
//                    subOffsetY = setMinMax(scenesubOffsetY, -b.height+d.height, 0);
                }
                
//                at.translate(subOffsetX, subOffsetY);
                Graphics2D g2 = (Graphics2D) gi;
                g2.setTransform(at);
            }
        }
        PaintRegion pr = new PaintRegion() {
            @Override
            public void paintRegion(Graphics g, int sx1, int sy1, int sx2, int sy2,
                    int dx1, int dy1, int dx2, int dy2) {
                Dimension d = getSize();
                if (scene.background != null) {
                    g.drawImage(scene.background.getImage(), sx1, sy1, sx2, sy2,
                            dx1, dy1, dx2, dy2, null);
                } else {
                    //g.setColor(Color.white);
                    //g.fillRect(0, 0, d.width, d.height);
                    paintBackground(g);
                }
            }
        };
        paintRegion(gi,pr);
        PaintRegion sp = new PaintRegion() {
            @Override
            public void paintRegion(Graphics g_, int sx1, int sy1, int sx2, int sy2, 
                    int dx1, int dy1, int dx2, int dy2) {
                Graphics2D g = (Graphics2D) g_;
                for (Sprite sprite : new ArrayList<>(sprites())) {
                    if (!sprite.is_visible) {
                        continue;
                    }
                    int cw = (int) sprite.getWidth()/2;
                    int ch = (int) sprite.getHeight()/2;
                    int cx = (int) sprite.getCenterX();
                    int cy = (int) sprite.getCenterY();
                    if (cx + cw < dx1 || cx - cw > dx2 || cy + ch < dy1 || cy - ch > dy2) {
                        continue;
                    }
                    AffineTransform old = g.getTransform();
                    AffineTransform at = sprite.getTransform(old, sx1 - dx1, sy1 - dy1);
                    g.setTransform(at);
                    g.drawImage(sprite.getPicture().getImage(), 0, 0, null);
                    if (drawBox != null) {
                        g.setColor(drawBox);
                        int w = (int) sprite.getWidth();
                        int h = (int) sprite.getHeight();
                        g.draw(new Rectangle(2, 2, w - 1, h - 1));
                    }
                    g.setTransform(old);
                }
            }
        };
        paintRegion(gi,sp);
        g.drawImage(image, 0, 0, this);
    }

    public static Comparator<Sprite> COMPX = new Comparator<Sprite>() {
        @Override
        public int compare(Sprite a, Sprite b) {
            double d = a.getX() - b.getX();
            if (d < 0) {
                return -1;
            }
            if (d > 0) {
                return 1;
            }
            return 0;
        }
    };

    public static Comparator<Sprite> DRAWING_PRIORITY = new Comparator<Sprite>() {
        @Override
        public int compare(Sprite a, Sprite b) {
            double d = a.getDrawingPriority() - b.getDrawingPriority();
            if (d < 0) {
                return -1;
            }
            if (d > 0) {
                return 1;
            }
            return 0;
        }
    };
    
    Map<Integer,Map<Integer,Integer>> cmap = new HashMap<>();

    /**
     * This should be a faster version of detecting collisions. It's still
     * O(n^2), unfortunately.
     */
    public void detectCollisions(List<Sprite> spriteLoop) {
        int n = spriteLoop.size();
        Collections.sort(spriteLoop, COMPX);
        for (int i = 0; i < n; i++) {
            Sprite sp1 = spriteLoop.get(i);
            for (int ii = i + 1; ii < n; ii++) {
                Sprite sp2 = spriteLoop.get(ii);
                if (Sprite.overlapx(sp1, sp2)) {
                    if (Sprite.overlapy(sp1, sp2)) {
                        // At this point we know the boxes overlap. Now to
                        // detect if drawing the images interferes.
                        if (overlapImage(sp1, sp2)) {
                            int id1 = System.identityHashCode(sp1);
                            int id2 = System.identityHashCode(sp2);
                            if (id1 > id2) {
                                int tmp = id1;
                                id1 = id2;
                                id2 = tmp;
                            }
                            Map<Integer, Integer> stage2 = cmap.get(id1);
                            if (stage2 == null) {
                                cmap.put(id1, stage2 = new HashMap<>());
                            }
                            Integer count = stage2.get(id2);
                            if (count == null) {
                                count = 0;
                                stage2.put(id2, 2);
                            } else {
                                stage2.put(id2, count + 1);
                            }
                            if (count == 0) {
                                assert sp1 != null;
                                assert sp2 != null;
                                fireSpriteSpriteCollision(sp1, sp2);
                            }
                        }
                    }
                } else {
                    break;
                }
            }
        }
        Iterator<Map<Integer, Integer>> i1 = cmap.values().iterator();
        while(i1.hasNext()) {
            Map<Integer, Integer> ei = i1.next();
            Iterator<Map.Entry<Integer, Integer>> i2 = ei.entrySet().iterator();
            while(i2.hasNext()) {
                Map.Entry<Integer, Integer> nv = i2.next();
                if(nv.getValue() <= 1) {
                    i2.remove();
                } else {
                    ei.put(nv.getKey(),nv.getValue()-1);
                }
            }
            if(ei.isEmpty())
                i1.remove();
        }
    }
    
    public Task moveSprites() {
        return new Task() {
            @Override
            public void run() {
                moveSprites_();
            }
        };
    }

    private void moveSprites_() {
        assert SwingUtilities.isEventDispatchThread();
        Dimension d = getFullSize();
        if (d.width == 0 || d.height == 0) {
            return;
        }
        for (Iterator<Sprite> iter = sprites().iterator(); iter.hasNext();) {
            Sprite sp = iter.next();
            if (!sp.isActive()) {
                iter.remove();
            }
        }
        List<Sprite> spriteLoop = new ArrayList<>(sprites().size());
        spriteLoop.addAll(sprites());
        for (Sprite sp : spriteLoop) {
            sp.move(d);
        }
        detectCollisions(spriteLoop);
        repaint();
    }

    /**
     * Usually you subclass a MouseAdapter to handle mouse events. It should
     * make things simpler for you if you give your MouseAdapters to the
     * component rather than the BasicFrame.
     *
     * @param kl
     */
    @Override
    public void addMouseListener(MouseListener ml) {
        super.addMouseListener(new MouseWrapper(ml));
    }
    
    int range(double d) {
        int n = (int)d;
        if(n < 0) return n;
        if(n > getWidth()) return getWidth();
        return n;
    }
    
    boolean overlapImage(Sprite sp1,Sprite sp2) {
        int xl1 = range(sp1.getX());
        int xh1 = xl1+sp1.getPicture().getWidth();
        
        int yl1 = (int)sp1.getY();
        int yh1 = yl1+sp1.getPicture().getHeight();
        
        int xl2 = (int)sp2.getX();
        int xh2 = (int)(sp2.getX()+sp2.getWidth());
        
        int yl2 = (int)sp2.getY();
        int yh2 = (int)(sp2.getY()+sp2.getHeight());
        
        int xl = xl1 > xl2 ? xl1 : xl2;
        int xh = xh1 < xh2 ? xh1 : xh2;
        
        if(xh < xl) return false;
        
        int yl = yl1 > yl2 ? yl1 : yl2;
        int yh = yh1 < yh2 ? yh1 : yh2;
        
        if(yh < yl) return false;
        
        Picture p1 = sp1.getPicture();
        Picture p2 = sp2.getPicture();
        for (int j = yl; j < yh; j++) {
            for (int i = xl; i < xh; i++) {
                if (p1.mask(i - xl1, j - yl1) && p2.mask(i - xl2, j - yl2)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    MouseEvent reMouse(MouseEvent me) {
        return new MouseEvent(
                me.getComponent(), 
                me.getID(),
                me.getWhen(), 
                me.getModifiers(),
                me.getX()-getOffsetX(),
                me.getY()-getOffsetY(),
                me.getXOnScreen(), 
                me.getYOnScreen(),
                me.getClickCount(),
                me.isPopupTrigger(),
                me.getButton());
    }
    
    List<Sprite> intersects(MouseEvent e) {
        List<Sprite> list = new ArrayList<>();
        double x = e.getX(), y = e.getY();
        for(Sprite sp : sprites()) {
            double xlo = sp.getX();
            double xhi = xlo + sp.getWidth();
            if(xlo <= x && x <= xhi) {
                double ylo = sp.getY();
                double yhi = ylo + sp.getHeight();
                if(ylo <= y && y <= yhi) {
                    int i = (int)(x - xlo);
                    int j = (int)(y - ylo);
                    if(sp.getPicture().mask(i,j)) {
                        list.add(sp);
                    }
                }
            }
        }
        return list;
    }

    @Override
    public final void mouseClicked(MouseEvent e) {
        SwingUtilities.invokeLater(() -> {
            for (Sprite sp : intersects(e)) {
                sp.mouseClicked(e);
            }
        });
    }
    
    List<Sprite> heardMousePressed = new ArrayList<>();

    @Override
    public final void mousePressed(MouseEvent e) {
        SwingUtilities.invokeLater(()->{
            MouseEvent e2 = reMouse(e);
            for (Sprite sp : intersects(e2)) {
                sp.mousePressed(e2);
                heardMousePressed.add(sp);
            }
        });
    }

    @Override
    public final void mouseReleased(MouseEvent e) {
        SwingUtilities.invokeLater(()->{
            for (Sprite sp : heardMousePressed) {
                sp.mouseReleased(reMouse(e));
            }
            heardMousePressed.clear();
        });
    }

    @Override
    public final void mouseEntered(MouseEvent e) {
        SwingUtilities.invokeLater(()->{
            for (Sprite sp : intersects(e)) {
                sp.mouseEntered(reMouse(e));
            }
        });
    }

    @Override
    public final void mouseExited(MouseEvent e) {
        SwingUtilities.invokeLater(() -> {
            for (Sprite sp : intersects(e)) {
                sp.mouseExited(reMouse(e));
            }
        });
    }

    Map<Class,Map<Class,SpriteSpriteCollisionListener>> cclisteners = new HashMap<>();
    
    public <T1 extends Sprite,T2 extends Sprite> void addSpriteSpriteCollisionListener(Class<T1> cl1,Class<T2> cl2,SpriteSpriteCollisionListener<T1,T2> ssl) {
//        Map<Class, SpriteSpriteCollisionListener> m = cclisteners.get(cl1);
//        if(m == null) cclisteners.put(cl1,m = new HashMap<>());
//        m.put(cl2,ssl);
        cclisteners.computeIfAbsent(cl1, (k)->{ return new HashMap<>(); }).computeIfAbsent(cl2, k->ssl);
    }
    
    Set<String> msgs = new HashSet<>();
    
    private void fireSpriteSpriteCollision(Sprite sp1,Sprite sp2) {
        assert sp1 != null;
        assert sp2 != null;
        fireSpriteSpriteCollision(sp1, sp1.getClass(), sp2, sp2.getClass());
    }

    private static Class getSup(Class c) {
        Class s = c.getSuperclass();
        if(s == null)
            return Sprite.class;
        else
            return s;
    }
    
    private boolean fireSpriteSpriteCollision(Sprite sp1,Class cl1, Sprite sp2, Class cl2) {
        assert sp1 != null;
        assert sp2 != null;
        assert cl1 != null;
        assert cl2 != null;
        boolean b1 = false, b2 = false;
        b1 = fireSpriteSpriteCollision_(sp1,cl1,sp2,cl2);
        if(cl1 != cl2)
            b2 = fireSpriteSpriteCollision_(sp2, cl2, sp1, cl1);
        if (b1 || b2) {
            return true;
        }
        String msg = "No handler for " + sp1.getClass().getName() + " => " + sp2.getClass().getName();
        if (!msgs.contains(msg)) {
            System.out.println(msg);
            msgs.add(msg);
        }
        assert cl1 != null;
        if(getSup(cl1) != Sprite.class) {
            assert sp1 != null;
            assert sp2 != null;
            boolean b = fireSpriteSpriteCollision(sp1, getSup(cl1), sp2, cl2);
            if(b)
                return true;
        }
        assert cl2 != null;
        if(getSup(cl2) != Sprite.class) {
            boolean b = fireSpriteSpriteCollision(sp1, cl1, sp2, getSup(cl2));
            if(b)
                return true;
        }
        return false;
    }
    
    private boolean fireSpriteSpriteCollision_(Sprite sp1, Class cl1, Sprite sp2, Class cl2) {
        Map<Class, SpriteSpriteCollisionListener> m = cclisteners.get(cl1);
        if(m != null) {
            SpriteSpriteCollisionListener ssl = m.get(cl2);
            if(ssl != null) {
                ssl.collision(sp1, sp2);
                return true;
            }
        }
        return false;
    }

    public Scene getScene() {
        return scene;
    }

    public Scene createScene() {
        return new Scene(this);
    }
}
