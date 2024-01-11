/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basicgraphics.calculator;

import basicgraphics.BasicFrame;
import java.text.DecimalFormat;
import java.util.Random;

/**
 *
 * @author sbrandt
 */
public class CalcTester {

    public static final Random RAND = new Random();
    public static final DecimalFormat df = new DecimalFormat("#.######");

    public static void press(Auto a, BasicFrame bf, double d) {
        if (d < 0) {
            a.press(bf, "?");
            d = -d;
        }
        String s = df.format(d);
        for (int i = 0; i < s.length(); i++) {
            a.press(bf, "" + s.charAt(i));
        }
    }

    static double reval(double d) {
        return Double.parseDouble(df.format(d));
    }

    public static void main(String[] args) throws Exception {
        try {
            assert false;
            throw new Error("Enable assertions");
        } catch (AssertionError ae) {
        }
        Auto a = new Auto();
        Calculator c = (Calculator) Class.forName(args[0]).getDeclaredConstructor().newInstance();
        c.init();
        Thread.sleep(1000);
        double d1, d2, dval;
        String val;

        MarsData md = new MarsData();
        
        a.press(c.bf, "RA");
        val = a.getValue(c.bf, "D");
        dval = Double.parseDouble(val);
        assert dval == md.rightAscension;
        
        a.press(c.bf, "DECL");
        val = a.getValue(c.bf, "D");
        dval = Double.parseDouble(val);
        assert dval == md.declination;
        
        a.press(c.bf, "ED");
        val = a.getValue(c.bf, "D");
        dval = Double.parseDouble(val);
        assert dval == md.earthDistance;
        
        a.press(c.bf, "SD");
        val = a.getValue(c.bf, "D");
        dval = Double.parseDouble(val);
        assert dval == md.sunDistance;
        
        for (int i = 0; i < 3; i++) {

            d1 = Math.PI * RAND.nextInt(10);
            a.press(c.bf, "C");
            press(a, c.bf, d1);
            val = a.getValue(c.bf, "D");
            d1 = Double.parseDouble(val);
            a.press(c.bf, "Sin");
            val = a.getValue(c.bf, "D");
            dval = Double.parseDouble(val);
            d2 = reval(Math.sin(d1));
            assert dval == d2 : "" + dval + " != " + d2;

            d1 = Math.PI * RAND.nextInt(10);
            a.press(c.bf, "C");
            press(a, c.bf, d1);
            val = a.getValue(c.bf, "D");
            d1 = Double.parseDouble(val);
            a.press(c.bf, "Cos");
            val = a.getValue(c.bf, "D");
            dval = Double.parseDouble(val);
            d2 = reval(Math.cos(d1));
            assert dval == d2 : "" + dval + " != " + d2;

            d1 = 0.1 * RAND.nextInt(10);
            a.press(c.bf, "C");
            press(a, c.bf, d1);
            val = a.getValue(c.bf, "D");
            d1 = Double.parseDouble(val);
            a.press(c.bf, "Exp");
            val = a.getValue(c.bf, "D");
            dval = Double.parseDouble(val);
            d2 = reval(Math.exp(d1));
            assert dval == d2 : "" + dval + " != " + d2;

            a.press(c.bf, "Log");
            val = a.getValue(c.bf, "D");
            dval = Double.parseDouble(val);
            assert dval == d1 : "" + dval + " != " + d1;
        }

        for (int i = 0; i < 3; i++) {
            int v1, v2;

            v1 = RAND.nextInt(10);
            v2 = RAND.nextInt(10);

            a.press(c.bf, "C");
            a.press(c.bf, "" + v1);
            a.press(c.bf, "-");
            a.press(c.bf, "" + v2);
            a.press(c.bf, "=");
            val = a.getValue(c.bf, "D");
            System.out.println("val=" + val);
            dval = Double.parseDouble(val);
            assert dval == v1 - v2;

            a.press(c.bf, "C");
            a.press(c.bf, "" + v1);
            a.press(c.bf, "M");
            a.press(c.bf, "C");
            a.press(c.bf, "R");
            a.press(c.bf, "-");
            a.press(c.bf, "" + v2);
            a.press(c.bf, "=");
            val = a.getValue(c.bf, "D");
            System.out.println("val=" + val);
            dval = Double.parseDouble(val);
            assert dval == v1 - v2;

            a.press(c.bf, "C");
            a.press(c.bf, "" + v1);
            a.press(c.bf, "?");
            a.press(c.bf, "-");
            a.press(c.bf, "" + v2);
            a.press(c.bf, "=");
            val = a.getValue(c.bf, "D");
            System.out.println("val=" + val);
            dval = Double.parseDouble(val);
            assert dval == -v1 - v2;

            a.press(c.bf, "C");
            a.press(c.bf, "?");
            a.press(c.bf, "" + v1);
            a.press(c.bf, "-");
            a.press(c.bf, "" + v2);
            a.press(c.bf, "=");
            val = a.getValue(c.bf, "D");
            System.out.println("val=" + val);
            dval = Double.parseDouble(val);
            assert dval == -v1 - v2;

            a.press(c.bf, "C");
            a.press(c.bf, "" + v1);
            a.press(c.bf, "-");
            a.press(c.bf, "" + v2);
            a.press(c.bf, "?");
            a.press(c.bf, "=");
            val = a.getValue(c.bf, "D");
            System.out.println("val=" + val);
            dval = Double.parseDouble(val);
            assert dval == v1 + v2;

            a.press(c.bf, "C");
            a.press(c.bf, "" + v1);
            a.press(c.bf, "-");
            a.press(c.bf, "?");
            a.press(c.bf, "" + v2);
            a.press(c.bf, "=");
            val = a.getValue(c.bf, "D");
            System.out.println("val=" + val);
            dval = Double.parseDouble(val);
            assert dval == v1 + v2;
        }

        System.out.println("all tests passed");
    }
}
