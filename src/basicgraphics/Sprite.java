/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basicgraphics;

import basicgraphics.images.Picture;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.util.LinkedList;

/**
 * This class implements an animated object that moves
 * across the screen. It needs a picture, a position,
 * and a velocity. One can override it to tell it how
 * to react to collisions.
 * @author sbrandt
 */
public class Sprite implements MouseListener, Comparable<Sprite> {
    
    private SpriteComponent component;
    private boolean added = false;
    private double headingOffset=0, heading=0;
    public boolean freezeOrientation = false;
    
    private Sprite() {}
    public Sprite(SpriteComponent sc) {
        component = sc;
        assert sc != null;
        //sc.addSprite(this);
    }
    
    public SpriteComponent getSpriteComponent() {
        return component;
    }
    
    public boolean is_visible = true;
    
    /**
     * @return the x-coordinate of the center of the image
     */
    public double centerX() {
        return getX()+getWidth()/2;
    }
    /**
     * @return the y-coordinate of the center of the image
     */
    public double centerY() {
        return getY()+getHeight()/2;
    }
    public double collisionBufferFactor = 1.0;
    /**
     * @return the x-coordinate of the left side of the image
     */
    public double left() {
        return centerX()-collisionBufferFactor*getWidth()/2;
    }
    /**
     * @return the x-coordinate of the right side of the image
     */
    public double right() {
        return centerX()+collisionBufferFactor*getWidth()/2;
    }
    /**
     * @return the y-coordinate of the top of the image.
     */
    public double top() {
        return centerY()-collisionBufferFactor*getHeight()/2;
    }
    /**
     * @return the y-coordinate of the bottom of the image.
     */
    public double bottom() {
        return centerY()+collisionBufferFactor*getHeight()/2;
    }
    
    static boolean overlapx(Sprite sp1,Sprite sp2) {
        return sp1.left() <= sp2.right() && sp1.right() >= sp2.left();
    }
    
    static boolean overlapy(Sprite sp1,Sprite sp2) {
        return sp1.top() <= sp2.bottom() && sp1.bottom() >= sp2.top();
    }

    static boolean overlap(Sprite sp1, Sprite sp2) {
        boolean inx = overlapx(sp1,sp2);
        boolean iny = overlapy(sp1,sp2);
        return inx && iny;
    }
    
    private Picture p;
    private double velx, vely, x, y;
    private boolean active = true;
    private int drawingPriority;
    private CollisionEventType inWall;
    
    public int getDrawingPriority() {
        return drawingPriority;
    }
    /**
     * The image with the lowest drawing priority
     * is drawn first. Give the highest number to
     * images you wish to be on top.
     * @param dp 
     */
    public void setDrawingPriority(int dp) {
        drawingPriority = dp;
    }

    /**
     * Used for detecting collisions
     */
    int yindex;
    
    public boolean isActive() { return active; }
    public void setActive(boolean a) { active = a; }
    
    public void setPicture(Picture p) {
        if (p == null) {
            throw new NullPointerException("Can't set a null picture");
        }
        this.p = p;
        int delx = p.getWidth() - this.p.getWidth();
        int dely = p.getHeight() - this.p.getHeight();
        setX(getX() - delx / 2);
        setY(getY() - dely / 2);
        if(!added) {
            component.addSprite(this);
            added = true;
        }
    }
    public Picture getPicture() { return p; }
    public void setX(double x) { this.x = x; }
    public void setCenterX(double x) { this.x = x - getWidth()/2; }
    public void setCenterY(double y) { this.y = y - getHeight()/2; }
    public double getCenterX() { return x + getWidth()/2; }
    public double getCenterY() { return y + getHeight()/2; }
    
    public double getX() { return x; }
    public void setY(double y) { this.y = y; }
    public double getY() { return y; }
    public void setHeadingOffset(double h) {
        heading = heading + headingOffset - this.headingOffset;
        headingOffset = h;
    }
    public double getHeading() { return heading; }
    public double getSpeed() {
        return Math.sqrt(velx*velx+vely*vely);
    }
    public void rotate(double th) {
        heading += th;
        double sp = getSpeed();
        setVel(sp*Math.cos(heading),sp*Math.sin(heading));
    }
    public double getHeadingOffset() { return headingOffset; }
    public void setForwardDirection(double velx, double vely) {
        this.headingOffset = Math.atan2(vely,velx)-heading;
    }
    public void setVel(double velx, double vely) {
        this.velx = velx;
        this.vely = vely;
        if(velx != 0 || vely != 0) {
            this.heading = Math.atan2(vely,velx);
        }
    }
    public double getVelX() { return velx; }
    public void setVelY(double vely) { this.vely = vely; }
    public double getVelY() { return vely; }

    public double getWidth() {
        return p.getWidth();
    }
    public double getHeight() {
        return p.getHeight();
    }

    void move(Dimension d) {
        moveTo();
        x += velx;
        y += vely;
        boolean invis = false;
        boolean xlo=false, xhi=false, ylo=false, yhi=false;
        if(x < -getWidth()) {
            xlo = true;
            invis = true;
        } else if(x < 0 && velx < 0) {
            xlo = true;
        }
        
        if(x > d.width) {
            xhi = true;
            invis = true;
        } else if(x+getWidth() > d.width && velx > 0) {
            xhi = true;
        }
        
        if(y < -getHeight()) {
            ylo = true;
            invis = true;
        } else if(y < 0 && vely < 0) {
            ylo = true;
        }
        
        if(y > d.height) {
            yhi = true;
            invis = true;
        } else if(y+getHeight() > d.height && vely > 0) {
            yhi = true;
        }
        
        if(xlo || xhi || ylo || yhi) {
            CollisionEventType eventType = CollisionEventType.WALL;
            // Check inWall == null to ensure that first we get a
            // collision type of WALL then a type of inWall.
            if (invis && inWall != null) {
                eventType = CollisionEventType.WALL_INVISIBLE;
            }
            if (eventType != inWall) {
                doProcessEvent(new SpriteCollisionEvent(xlo, xhi, ylo, yhi, eventType));
                inWall = eventType;
            }
        } else {
            inWall = null;
        }
    }

    /**
     * Override this method to determine how the sprite
     * will react when it collides with another sprite
     * or with the boundaries of the screen.
     * @param ev 
     */
    public void processEvent(SpriteCollisionEvent ev) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
    
    boolean moveToEnabled = false;
    double moveToX, moveToY, moveToSpeed;
    public void moveTo(double destX,double destY,double speed) {
        moveToX = destX - getWidth()/2;
        moveToY = destY - getHeight()/2;
        moveToSpeed = speed;
        moveToEnabled = true;
        double xv = getX(), yv = getY();
        double delx = moveToX-xv, dely = moveToY-yv;
        double theta = Math.atan2(dely, delx);
        setVel(speed * Math.cos(theta), speed * Math.sin(theta));
        assert speed > 0;
    }
    public boolean moveToEnabled() {
        return moveToEnabled;
    }
    
    public void arrived() {}
    
    public void moveTo() {
        if(moveToEnabled) {
            double xv = getX(), yv = getY();
            double delx = moveToX-xv, dely = moveToY-yv;
            double r = Math.sqrt(delx*delx+dely*dely);
            if(r < moveToSpeed*2) {
                setX(moveToX);
                setY(moveToY);
                setVel(0,0);
                moveToEnabled = false;
                arrived();
            } else {
//                double theta = Math.atan2(dely, delx);
//                setVelX(moveToSpeed * Math.cos(theta));
//                setVelY(-moveToSpeed * Math.sin(theta));
            }
        }
    }
    
    /**
     * Block legacy methods.
     */
    final public void processEvent() {}
    final public void preMove() {}
    final public void postMove() {}

    final private LinkedList<SpriteCollisionEvent> events = new LinkedList<>();
    private boolean running;
    
    /**
     * This method serializes the events, preventing them happening all
     * at once.
     * @param spriteCollisionEvent 
     */
    private void doProcessEvent(SpriteCollisionEvent spriteCollisionEvent) {
        synchronized(events) {
            if(running) {
                events.addLast(spriteCollisionEvent);
                return;
            } else {
                running = true;
            }
        }
        while(true) {
            processEvent(spriteCollisionEvent);
            synchronized(events) {
                if(events.isEmpty()) {
                    running = false;
                    break;
                } else {
                    spriteCollisionEvent = events.removeFirst();
                }
            }
        }
    }

    @Override
    public int compareTo(Sprite that) {
        int r = this.drawingPriority - that.drawingPriority;
        if(r == 0)
            r = System.identityHashCode(this) - System.identityHashCode(that);
        return r;
    }

    AffineTransform getTransform() {
        AffineTransform af = new AffineTransform();
        af.translate(getX(),getY());
        if(freezeOrientation)
            return af;
        af.rotate(heading+headingOffset, getWidth()/2, getHeight()/2);
        return af;
    }
    
}
