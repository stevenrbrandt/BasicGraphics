/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package basicgraphics;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 *
 * @author steve
 */
public class BasicLayout implements LayoutManager {
    Map<GridDim, Component> map = new HashMap<>();
    Pattern pattern = Pattern.compile("x=(\\d+),y=(\\d+),w=(\\d+),h=(\\d+)");
    
    @Override
    public void addLayoutComponent(String name, Component comp) {
        Matcher matcher = pattern.matcher(name);
        boolean worked = matcher.matches();
        assert worked : "String does not match pattern: '"+name+"'";
        int x = Integer.parseInt(matcher.group(1));
        int y = Integer.parseInt(matcher.group(2));
        int w = Integer.parseInt(matcher.group(3));
        int h = Integer.parseInt(matcher.group(4));
        assert x >= 0;
        assert y >= 0;
        assert w > 0;
        assert h > 0;
        GridDim gd = new GridDim(x,y,w,h);
        for(var gd2 : map.keySet()) {
            if(gd.overlaps(gd2)) {
                throw new GuiException("Overlapping boxes: "+gd+" and "+gd2);
            }
        }
        map.put(gd, comp);
    }
    
    static class GridDim {
        final int x,y,w,h;
        public int hashCode() { return x+y+w+h; }
        public boolean equals(Object o) {
            GridDim that = (GridDim)o;
            return this.x==that.x && this.y==that.y && this.w==that.w && this.h==that.h;
        }
        GridDim(int x,int y,int w,int h) { this.x=x; this.y=y; this.w=w; this.h=h; }
        public boolean overlaps(GridDim gd) {
            int p1 = Math.min(x, gd.x);
            int p2 = Math.max(x+w, gd.x+gd.w);
            if(p2 - p1 >= w + gd.w) return false;
            int k1 = Math.min(y, gd.y);
            int k2 = Math.max(y+h, gd.y+gd.h);
            if(k2 - k1 >= h + gd.h) return false;
            return true;
        }
        public String toString() {
            return "x="+x+",y="+y+",w="+w+",h="+h;
        }
    }
    GridDim getGridDim() {
        int w=0, h=0;
        for(Map.Entry<GridDim, Component> entry : map.entrySet()) {
            GridDim k = entry.getKey();
            int wn = k.x + k.w;
            int hn = k.y + k.h;
            if(wn > w) w = wn;
            if(hn > h) h = hn;
        }
        return new GridDim(0,0,w,h);
    }
    
    GridDim[][] gmap;
    public void checkGridDim() {
        GridDim gd = getGridDim();
        gmap = new GridDim[gd.w][gd.h];
        for(Map.Entry<GridDim, Component> e : map.entrySet()) {
            GridDim k = e.getKey();
            for(int i=k.x;i < k.x+k.w;i++) {
                for(int j=k.y;j < k.y+k.h;j++) {
                    assert gmap[i][j] == null : String.format("i=%d,j=%d is doubly covered",i,j);
                    gmap[i][j] = k;
                }
            }
        }
        for(int i=0;i<gmap.length;i++) {
            for(int j=0;j<gmap[i].length;j++) {
                assert gmap[i][j] != null : String.format("i=%d,j=%d not covered",i,j);
            }
        }
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        Iterator<Map.Entry<GridDim, Component>> iter = map.entrySet().iterator();
        while(iter.hasNext()) {
            Map.Entry<GridDim, Component> entry = iter.next();
            if(entry.getValue() == comp) {
                iter.remove();
            }
        }
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        checkGridDim();
        return getSize(true);
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        checkGridDim();
        return getSize(false);
    }
    
    Dimension getSize(Component c,boolean preferred) {
        if(preferred) return c.getPreferredSize();
        else return c.getMinimumSize();
    }
    Dimension getSize(boolean preferred) {
        int maxh = 0, maxw = 0;
        GridDim gd = getGridDim();
        for(Map.Entry<GridDim, Component> entry : map.entrySet()) {
            GridDim k = entry.getKey();
            Component v = entry.getValue();
            Dimension d = getSize(v, preferred);
            int wsz = (int)Math.ceil(d.width*1.0*gd.w/k.w);
            int hsz = (int)Math.ceil(d.height*1.0*gd.h/k.h);
            if(wsz > maxw) maxw = wsz;
            if(hsz > maxh) maxh = hsz;
        }
        return new Dimension(maxw, maxh);
    }

    @Override
    public void layoutContainer(Container parent) {
        Dimension dim = parent.getSize();
        GridDim gd = getGridDim();
        double facx = dim.width*1.0/gd.w;
        double facy = dim.height*1.0/gd.h;
        for(Map.Entry<GridDim, Component> entry : map.entrySet()) {
            GridDim k = entry.getKey();
            Component v = entry.getValue();
            int xpos = (int) Math.ceil(k.x*facx);
            int ypos = (int) Math.ceil(k.y*facy);
            int width = (int) Math.ceil(k.w*facx);
            int height = (int) Math.ceil(k.h*facy);
            v.setBounds(xpos, ypos, width, height);
        }
    }
    
    static JLabel mkLabel(String text) {
        JLabel jl = new JLabel(text);
        jl.setHorizontalAlignment(SwingConstants.CENTER);
        jl.setVerticalAlignment(SwingConstants.CENTER);
        jl.setBorder(BorderFactory.createEtchedBorder());
        return jl;
    }
    
    public static void main(String[] args) {
        JFrame jf = new JFrame();
        Container c = jf.getContentPane();
        c.setLayout(new BasicLayout());
        c.add("x=0,y=0,w=1,h=1", mkLabel("Upper left"));
        c.add("x=1,y=0,w=1,h=1", mkLabel("Upper right"));
        c.add("x=0,y=1,w=2,h=2", mkLabel("The bottom"));
        c.add("x=2,y=0,w=1,h=3", mkLabel("Edge"));
        c.add("x=0,y=3,w=3,h=1", mkLabel("Very Bottom"));
        jf.pack();
        jf.setVisible(true);
    }
}
