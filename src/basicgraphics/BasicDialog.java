/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package basicgraphics;

import basicgraphics.images.Picture;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * A slightly simplified interface to JOptionPane.
 *
 * @author steve
 */
public class BasicDialog {

    public final static Icon EMPTY_ICON = new Icon() {
        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
        }

        @Override
        public int getIconWidth() {
            return 1;
        }

        @Override
        public int getIconHeight() {
            return 1;
        }
    };

    public static final String YES = "YES", NO = "NO", CANCEL = "CANCEL";

    public static void getOK(String message) {
        Container c = BasicFrame.getFrame().getContentPane();
        JOptionPane.showMessageDialog(c, message);
    }

    public static String getYesNoCancel(String message) {
        Container c = BasicFrame.getFrame().getContentPane();
        int rc = JOptionPane.showConfirmDialog(c, message, "Yes/No/Cancel", JOptionPane.YES_NO_CANCEL_OPTION);
        if (rc == JOptionPane.YES_OPTION) {
            return YES;
        }
        if (rc == JOptionPane.NO_OPTION) {
            return NO;
        }
        assert rc == JOptionPane.CANCEL_OPTION;
        return CANCEL;
    }

    public static String getYesNo(String message) {
        Container c = BasicFrame.getFrame().getContentPane();
        int rc = JOptionPane.showConfirmDialog(c, message, "Yes/No", JOptionPane.YES_NO_OPTION);
        if (rc == JOptionPane.YES_OPTION) {
            return YES;
        }
        assert rc == JOptionPane.NO_OPTION;
        return NO;
    }

    public static String getOption(String message, String title, String[] optionStrs, String initialStr) {
        return getOption(message, title, EMPTY_ICON, optionStrs, initialStr);
    }
    public static String getOption(String message, String title, Icon icon, String[] optionStrs, String initialStr) {
        Component c = BasicFrame.getFrame().getContentPane();
        Object[] options = new Object[optionStrs.length];
        assert initialStr != null;
        Object initial = initialStr;
        boolean found = false;
        for (int i = 0; i < optionStrs.length; i++) {
            String s = optionStrs[i];
            assert s != null;
            if (s.equals(initial)) {
                found = true;
            }
            options[i] = s;
        }
        assert found : "Initial string not in options";

        Object rc = JOptionPane.showInputDialog(
                c, message, title, JOptionPane.PLAIN_MESSAGE, icon, options, initial);
        return (String) rc;
    }

    public static void main(String[] args) {
        // Create a basic application
        BasicFrame bf = new BasicFrame("opt");
        final Icon sarah = new Picture("Sarah.png").getIcon();
        bf.createSingletonLayout(new JLabel(sarah));
        bf.show();
        
        // Prompt using an icon
        String option = getOption("Which fruit?", "Fruit Selection", sarah, new String[]{"Apple", "Pear", "Orange", "Banana"}, "Apple");
        System.out.println("option: " + option);
    }
}
