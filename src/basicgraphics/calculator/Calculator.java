/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basicgraphics.calculator;

import basicgraphics.BasicFrame;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 *
 * @author sbrandt
 */
public class Calculator {
    public final static String[][] layout = {
        {"D","D","D","C"},
        {"1","2","3","+"},
        {"4","5","6","-"},
        {"7","8","9","*"},
        {"0",".","=","/"}
    };
    double value_, previousValue, memory;
    String operator;
    /**
     * This value keeps getting multiplied by .1
     * after we click the "." key, allowing us to
     * update the current value by the right fraction of one.
     */
    double decimal=1.0;
    /*
     * The current sign, +1 or -1.
     */
    double sgn=1.0;
    JLabel display = new JLabel("0",JLabel.CENTER);
    public BasicFrame bf = new BasicFrame("Martian Calculator");
    public double getValue() {
        return value_ * sgn;
    }
    /**
     * Set the current value. Note that the calculator GUI
     * won't reflect the new value until you call update().
     */
    public void setValue(double v) {
        if(v < 0) {
            sgn = -1.0;
            value_ = -v;
        } else {
            sgn = 1.0;
            value_ = v;
        }
    }

    public void init() {
        bf.setStringLayout(layout);
        bf.add("D",display);

        for(int i=0;i<=9;i++) {
            final int key = i;
            JButton b = new JButton(""+i);
            b.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(decimal == 1.0) {
                        value_ = 10*value_+key;
                    } else {
                        value_ = value_ + key*decimal;
                        decimal *= 0.1;
                    }
                    update();
                }
            });
            bf.add(""+i, b);
        }
        for(String s : new String[]{"+","-","/","*","="}) {
            final String op = s;
            JButton b = new JButton(op);
            b.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if("=".equals(op)) {
                        calc(operator);
                        setValue(previousValue);
                        operator = null;
                    } else {
                        previousValue = getValue();
                        setValue(0.0);
                        operator = op;
                        sgn = 1.0;
                    }
                    decimal = 1.0;
                    update();
                }
            });
            bf.add(op,b);
        }

        // The text on the button is "Clr"
        JButton clear = new JButton("Clr");
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setValue(0.0);
                previousValue = 0;
                operator = null;
                decimal = 1.0;
                update();
            }
        });
        // The place of the button in the layout is
        // given by "C".
        bf.add("C",clear);

        JButton dot = new JButton(".");
        dot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                decimal *= .1;
            }
        });
        bf.add(".",dot);

        Font f = new Font("Courier",Font.BOLD,30);
        bf.setAllFonts(f);
        bf.show();
    }
    public void calc(String op) {
        if ("+".equals(op)) {
            previousValue = previousValue + getValue();
        } else if ("-".equals(op)) {
            previousValue = previousValue - getValue();
        } else if ("*".equals(op)) {
            previousValue = previousValue * getValue();
        } else if ("/".equals(op)) {
            previousValue = previousValue / getValue();
        }
    }
    final static DecimalFormat df = new DecimalFormat("#.######");
    public void update() {
        if(operator == null) {
            display.setText(df.format(getValue()));
        } else {
            display.setText(df.format(previousValue)+operator+df.format(getValue()));
        }
    }
    public static void main(String[] args) {
        Calculator c = new Calculator();
        c.init();
    }
}
