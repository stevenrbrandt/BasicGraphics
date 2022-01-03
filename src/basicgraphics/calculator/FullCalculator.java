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
public class FullCalculator {
    public final static String[][] layout = {
        {"D","D","D","C","RA","RA","RA"},
        {"1","2","3","+","DECL","DECL","DECL"},
        {"4","5","6","-","ED","ED","ED"},
        {"7","8","9","*","SD","SD","SD"},
        {"0",".","?","/","=","Sin","Cos"},
        {"M","M","R","R","=","Log","Exp"}
    };
    double value_, previousValue, memory;
    String operator;
    double decimal=1.0;
    double sgn=1.0;
    JLabel display = new JLabel("0",JLabel.CENTER);
    public BasicFrame bf = new BasicFrame("Martian Calculator");
    public double getValue() {
        return value_ * sgn;
    }
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
        JButton memp = new JButton("M+");
        JButton memr = new JButton("MR");
        JButton sign = new JButton("+/-");
        JButton cos = new JButton("Cos");
        JButton sin = new JButton("Sin");
        JButton log = new JButton("log");
        JButton exp = new JButton("Exp");
        JButton ra = new JButton("Right Ascension");
        JButton decl = new JButton("Declination");
        JButton ed = new JButton("Earth Distance");
        JButton sd = new JButton("Sun Distance");

        memp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                memory = getValue();
            }
        });
        memr.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setValue(memory);
            }
        });
        sign.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sgn *= -1;
                update();
                System.out.println("value = "+value_+" "+sgn);
            }
        });
        sin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setValue(Math.sin(getValue()));
                update();
            }
        });
        cos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setValue(Math.cos(getValue()));
                update();
            }
        });
        log.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setValue(Math.log(getValue()));
                update();
            }
        });
        exp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setValue(Math.exp(getValue()));
                update();
            }
        });
        ra.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MarsData md = new MarsData();
                setValue(md.rightAscension);
                update();
            }
        });
        decl.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MarsData md = new MarsData();
                setValue(md.declination);
                update();
            }
        });
        ed.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MarsData md = new MarsData();
                setValue(md.earthDistance);
                update();
            }
        });
        sd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MarsData md = new MarsData();
                setValue(md.sunDistance);
                update();
            }
        });

        bf.add("M",memp);
        bf.add("R",memr);
        bf.add("?",sign);
        bf.add("Sin",sin);
        bf.add("Cos",cos);
        bf.add("Log",log);
        bf.add("Exp",exp);
        bf.add("RA",ra);
        bf.add("DECL",decl);
        bf.add("ED",ed);
        bf.add("SD",sd);
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
        FullCalculator c = new FullCalculator();
        c.init();
    }
}
