/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basicgraphics;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sbrandt
 */
public class FileUtility {
    

    public static URL findFile(String name) {
        int slash = name.replace('\\','/').lastIndexOf('/');
        if(slash > 0)
            name = name.substring(slash+1);
        File f = findFile(new File("."),name);
        if(f == null)
            f = findFileI(new File("."),name);
        //System.out.println("Search: "+name+" => "+f);
        if(f == null) {
            return null;
        } else {
            try {
                return f.toURI().toURL();
            } catch (MalformedURLException ex) {
                return null;
            }
        }
    }

    static File findFile(File file, String name) {
        File[] files = file.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                if(f.getName().startsWith(".")) {
                    continue;
                } else {
                    File s = findFile(f,name);
                    if(s != null)
                        return s;
                }  
            } else if (f.getName().equals(name)) {
                return f;
            }
        }
        return null;
    }

    static File findFileI(File file, String name) {
        File[] files = file.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                if(f.getName().startsWith(".")) {
                    continue;
                } else {
                    File s = findFile(f,name);
                    if(s != null)
                        return s;
                }  
            } else if (f.getName().equalsIgnoreCase(name)) {
                return f;
            }
        }
        return null;
    }
}
