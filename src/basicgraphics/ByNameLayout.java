/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basicgraphics;

import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author sbrandt
 */
public class ByNameLayout {
    final private GridBagLayout gbl = new GridBagLayout();
    final private GridBagConstraints gbc = new GridBagConstraints();
    final private String[][] layout;
    final private Container container;
    final private Map<String,Component> map = new HashMap<>();
    final static private Component BAD = new BadComponent();
    public ByNameLayout(String[][] layoutNames,Container c) {
        this.container = c;
        c.setLayout(gbl);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        String[][] s = new String[layoutNames.length][];
        for(int i=0;i<s.length;i++) {
            String[] line = new String[layoutNames[i].length];
            for(int j=0;j<line.length;j++) {
                line[j] = layoutNames[i][j];
                if(layoutNames[i][j] == null)
                    throw new BasicLayoutException("layout element must not be null");
                if(layoutNames[i].length != layoutNames[0].length)
                    throw new BasicLayoutException("layout must be a square array");
            }
            s[i] = line;
            for(String entry : line)
                map.put(entry,BAD);
        }
        this.layout = s;
    }
    public void add(String loc,Component component) {
        if(BAD != map.get(loc)) {
            throw new BasicLayoutException("Duplicate assignment for layout element '"+loc+"'");
        }
        map.put(loc, component);
        int minI = Integer.MAX_VALUE, maxI = Integer.MIN_VALUE;
        int minJ = Integer.MAX_VALUE, maxJ = Integer.MIN_VALUE;
        boolean found = false;
        for(int i=0;i<layout.length;i++) {
            for(int j=0;j<layout[i].length;j++) {
                if(loc.equals(layout[i][j])) {
                    if(i < minI) minI = i;
                    if(i > maxI) maxI = i;
                    if(j < minJ) minJ = j;
                    if(j > maxJ) maxJ = j;
                    found = true;
                }
            }
        }
        if(!found) throw new BasicLayoutException("No location '"+loc+"' in layout");
        for(int i=minI;i <= maxI;i++) {
            for(int j=minJ;j <= maxJ;j++) {
                if(!loc.equals(layout[i][j]))
                    throw new BasicLayoutException("Bad value at i="+i+" j="+j+" should be '"+loc+"'");
            }
        }
        int height = maxI-minI+1;
        int width = maxJ-minJ+1;
        gbc.gridx = minJ;
        gbc.gridy = minI;
        gbc.gridwidth = width;
        gbc.gridheight = height;
//        gbc.weightx = height;
//        gbc.weighty = width;

        System.out.println("Add component i="+gbc.gridx+", j="+gbc.gridy+
                ", w="+gbc.gridwidth+", h="+gbc.gridheight);
        container.add(component, gbc);
    }
    
    public static void main(String[] args) {
        JFrame jf = new JFrame("By Name");
        Font f = new Font("Courier", Font.BOLD, 50);
        Container pane = jf.getContentPane();
        String[][] s = {
            {"a","b","c"},
            {"d","d","d"}
        };
        ByNameLayout bnl = new ByNameLayout(s, pane);
        bnl.add("a",new JLabel("Ape"));
        bnl.add("b",new JLabel("Bear"));
        bnl.add("c",new JLabel("Cat"));
        bnl.add("d",new JLabel("Dungeon",JLabel.CENTER));
        bnl.setFont(f);
        System.out.println("Ready");
        jf.pack();
        jf.show(true);
    }
    
    public ArrayList<Component> all() {
        return new ArrayList<>(map.values());
    }

    public void setFont(Font f) {
        for(Component v : map.values()) {
            v.setFont(f);
        }
    }
}
