/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basicgraphics;

/**
 *
 * @author sbrandt
 */
public class GuiException extends RuntimeException {

    public GuiException() {
    }

    public GuiException(String str) {
        super(str);
    }
    
    public GuiException(Exception ex) {
        super(ex);
    }
    
}
