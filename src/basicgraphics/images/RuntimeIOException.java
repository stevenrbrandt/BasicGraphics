/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basicgraphics.images;

import java.io.IOException;

/**
 * Protect users from needing to catch IOException
 * @author sbrandt
 */
public class RuntimeIOException extends RuntimeException {
    public RuntimeIOException(String string,Throwable t) {
        super(string,t);
    }

    public RuntimeIOException(String string) {
        super(string);
    }

    RuntimeIOException(IOException ex) {
        super(ex);
    }
    
}
