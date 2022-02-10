package tanks;

import basicgraphics.BasicFrame;
import basicgraphics.Sprite;
import basicgraphics.SpriteComponent;
import basicgraphics.images.Picture;

import java.awt.*;

public abstract class Tank extends Sprite {

    private double aimingAtX, aimingAtY;
    private double heading;
    private double velocity;

    public Tank(SpriteComponent sc) {
        super(sc);
        draw();
    }

    public abstract Color getColor();

    public final void draw() {
        setPicture(drawTank());
    }

    private Picture drawTank() {
        var im = BasicFrame.createImage(35*2, 35*2);
        var g = (Graphics2D) im.getGraphics();

        g.setColor(getColor());

        var oldT = g.getTransform();
        g.rotate(heading, im.getWidth()/2., im.getHeight()/2.);

        g.fillRect(im.getWidth()/2 - 20, im.getWidth()/2 - 10, 40, 20);

        g.setTransform(oldT);

        var th = Math.atan2(aimingAtY - (getY() + im.getWidth()/2.), aimingAtX - (getX() + im.getHeight()/2.));
        g.rotate(th, im.getWidth()/2., im.getHeight()/2.);

        g.fillRect(im.getWidth()/2, im.getHeight()/2-3, 35, 6);

        var p = new Picture(im);
        p.transparentWhite();
        p.shrinkToMinimum();
        return p;
    }

    public double getAimingDirection() {
        return Math.atan2(aimingAtY - (getY() + 35), aimingAtX - (getX() + 35));
    }

    public void setAimingAtX(double aimingAtX) {
        this.aimingAtX = aimingAtX;
    }

    public void setAimingAtY(double aimingAtY) {
        this.aimingAtY = aimingAtY;
    }

    public double getHeading() {
        return heading;
    }

    public void setHeading(double heading) {
        this.heading = heading;
    }

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
        updateVelocity();
    }

    public void updateVelocity() {
        setVelX(Math.cos(heading) * velocity);
        setVelY(Math.sin(heading) * velocity);
    }
}
