/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basicgraphics;

import java.io.File;
import java.net.URI;
import java.net.URL;

/**
 *
 * @author sbrandt
 */
public class FileUtility {
    

    public static URI findFile(String name) {
        int slash = name.replace('\\','/').lastIndexOf('/');
        if(slash > 0)
            name = name.substring(slash+1);
        File f = findFile(new File("."),name);
        if(f == null)
            f = findFileI(new File("."),name);
        //System.out.println(" --> findFile: name: "+name);
        //System.out.println("Search: "+name+" => "+f);
        if(f == null) {
            return null;
        } else {
            return f.toURI();
        }
    }
    
    public static File findFile(Class c, String name) {
        URL d = c.getResource(".");
        return findFile(new File(d.getFile().replace("%20"," ")),name);
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
            } else if (f.getName().equalsIgnoreCase(name)) {
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
