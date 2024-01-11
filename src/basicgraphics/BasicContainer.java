/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basicgraphics;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JComponent;

/**
 * This is a container that can be laid out as easily as a BasicFrame.
 * @author sbrandt
 */
public class BasicContainer extends Container {

    private final Map<String, Component> components = new HashMap<>();

    GridBagLayout gbl = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();
    ArrayList<ArrayList<String>> cells = new ArrayList<>();

    public BasicContainer() {
        setLayout(gbl);
    }

    /**
     * Enables you to design your screen with a 2D ascii array. The design takes
     * the form of a rectangular grid in which logically rectangular regions on
     * the screen are mapped to display components. Java determines the size of
     * the individual cells in the grid, based on the name.
     *
     * @param loc - One of the names in the layout.
     * @param jc - The component that should be placed at location loc.
     */
    public void add(String loc, JComponent jc) {
        int minI = Integer.MAX_VALUE, maxI = Integer.MIN_VALUE;
        int minJ = Integer.MAX_VALUE, maxJ = Integer.MIN_VALUE;
        boolean found = false;
        for (int i = 0; i < layout.length; i++) {
            for (int j = 0; j < layout[i].length; j++) {
                if (loc.equals(layout[i][j])) {
                    if (i < minI) {
                        minI = i;
                    }
                    if (i > maxI) {
                        maxI = i;
                    }
                    if (j < minJ) {
                        minJ = j;
                    }
                    if (j > maxJ) {
                        maxJ = j;
                    }
                    found = true;
                }
            }
        }
        if (!found) {
            throw new BasicLayoutException("No location '" + loc + "' in layout");
        }
        for (int i = minI; i <= maxI; i++) {
            for (int j = minJ; j <= maxJ; j++) {
                if (!loc.equals(layout[i][j])) {
                    throw new BasicLayoutException("Bad value at i=" + i + " j=" + j + " should be '" + loc + "'");
                }
            }
        }
        int width = maxI - minI + 1;
        int height = maxJ - minJ + 1;
        System.out.printf("add(%s,%d,%d,%d,%d)%n",loc,minI,minJ,width,height);
        add(loc, jc, minJ, minI, height, width);
    }

    private void add(String name, Component c, int x, int y, int xw, int yw) {
        if (components.containsKey(name)) {
            throw new GuiException("Two components with the same name: " + name);
        }

        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = xw;
        gbc.gridheight = yw;
        gbc.weightx = xw;
        gbc.weighty = yw;

        // Ensure the number of cells
        while (cells.size() < x + xw) {
            cells.add(new ArrayList<String>());
        }
        for (int i = 0; i < xw; i++) {
            for (int j = 0; j < yw; j++) {
                int xv = x + i;
                int yv = y + j;
                ArrayList<String> row = cells.get(xv);
                while (row.size() <= yv) {
                    row.add(null);
                }
                String owner = row.get(yv);
                if (owner != null) {
                    throw new GuiException("Conflict of ownership at x=" + xv + " y=" + yv + " owner1=" + owner + " owner2=" + name);
                }
                row.set(yv, name);
            }
        }
        components.put(name, c);
        System.out.println("put(" + name + ")");
        add(c, gbc);
    }
    
    String[][] layout;

    public void setStringLayout(String[][] lay2) {
        layout = lay2;
    }
    public void setStringLayout(List<List<String>> lay2) {
        layout = new String[lay2.size()][];
        for(int i=0;i<lay2.size();i++) {
            List<String> row = lay2.get(i);
            String[] row2 = new String[row.size()];
            for(int j=0;j<row2.length;j++) {
                row2[j] = row.get(j);
            }
            layout[i] = row2;
        }
    }
}
