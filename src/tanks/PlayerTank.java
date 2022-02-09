package tanks;

import basicgraphics.SpriteComponent;

import java.awt.*;

public class PlayerTank extends Tank {

    public PlayerTank(SpriteComponent sc) {
        super(sc);
        setVelocity(1.3); // choo choo
    }

    @Override
    public Color getColor() {
        return Color.BLACK;
    }
}
