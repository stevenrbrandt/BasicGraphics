/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package basicgraphics;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author steve
 */
public class Util {
    
    /**
     * The java == operator does not compare String or Integer
     * objects correctly. Instead, one has to use the object's
     * equals method to compare them correctly.
     * @param a 
     * @param b 
     * @return 
     */
    public static boolean eq(Object a, Object b) {
        if(a == b) return true;
        if(a == null || b == null) return false;
        return a.equals(b);
    }
    
    /**
     * Example of how to use remove even numbers from an array.
     * Note the use of Integer instead of int. This is required
     * for List and ArrayList.
     * @param values
     * @return 
     */
    public static List<Integer> removeEvens(List<Integer> values) {
        ArrayList<Integer> newValues = new ArrayList<>();
        for(int i=0;i<values.size();i++){
            int v = values.get(i);
            if(v % 2 == 1) {
                newValues.add(v);
            }
        }
        return newValues;
    }
    
    /**
     * A simple example of how to perform a calculation
     * on arrays. In this case, remove the even numbers.
     * @param values
     * @return 
     */
    public static int[] removeEvens(int[] values) {
        
        int countOddValues = 0;
        for(int i=0;i<values.length;i++){
            if(values[i] % 2 == 1) {
                countOddValues++;
            }
        }
        
        int[] newValues = new int[countOddValues];
        
        int n = 0;
        for(int i=0;i<values.length;i++){
            if(values[i] % 2 == 1) {
                newValues[n++] = values[i];
            }
        }
        
        return newValues;
    }
    
    /**
     * Java arrays don't know how to print themselves.
     * We can write our own method to do this.
     * @param values 
     */
    public static void printArray(int[] values) {
        if(values == null) {
            System.out.println("null");
            return;
        }
        System.out.print("[");
        if(values.length > 0)
            System.out.print(values[0]);
        for(int i=1;i<values.length;i++){
            System.out.print(" "+values[i]);
        }
        System.out.println("]");
    }
    
    public static void main(String[] args) {
        String a = "hello";
        String b = "hell";
        b += "o";
        System.out.println("a="+a+" b="+b);
        System.out.println("eq: "+(a == b)+" "+eq(a,b));
        
        Integer c = 316;
        Integer d = 300;
        d += 16;
        System.out.println("c="+c+" d="+d);
        System.out.println("eq: "+(c == d)+" "+eq(c,d));
        
        int[] foo = new int[]{1,2,3,4,5,100,102,103};
        printArray(removeEvens(foo));
        printArray(new int[0]);
        printArray(null);
    }
}
