package tanks;

import basicgraphics.SpriteComponent;

import java.awt.*;

public class PlayerTank extends Tank {

    private int score;
    private int ammo;

    public PlayerTank(SpriteComponent sc, int initialAmmo) {
        super(sc);
        this.ammo = initialAmmo;
        setVelocity(1.6); // choo choo
    }

    @Override
    public Color getColor() {
        return Color.BLACK;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getAmmo() {
        return ammo;
    }

    public void setAmmo(int ammo) {
        this.ammo = ammo;
    }

    public boolean decrAmmo() {
        if (this.getAmmo() > 0) {
            this.ammo--;
            return true;
        } else {
            return false;
        }
    }
}
