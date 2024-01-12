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
public class SpriteCollisionEvent {
    public final boolean xlo, xhi, ylo, yhi;
    public final CollisionEventType eventType;

    public SpriteCollisionEvent(boolean xlo, boolean xhi, boolean ylo, boolean yhi, CollisionEventType ct) {
        this.xlo = xlo;
        this.xhi = xhi;
        this.ylo = ylo;
        this.yhi = yhi;
        this.eventType = ct;
        if (ct == null) {
            throw new NullPointerException();
        }
    }
    private final static String PRE = "CollisionEvent[";
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(PRE);
        append(sb,"xlo",xlo);
        append(sb,"xhi",xhi);
        append(sb,"ylo",ylo);
        append(sb,"yhi",yhi);
        append(sb,eventType.toString(),true);
        sb.append(']');
        return sb.toString();
    }
    private void append(StringBuilder sb,String s,boolean b) {
        if(b) {
            if(sb.length()>PRE.length())
                sb.append(',');
            sb.append(s);
        }
    }
}
