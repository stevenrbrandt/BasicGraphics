package tanks;

import basicgraphics.BasicFrame;
import basicgraphics.Clock;
import basicgraphics.SpriteComponent;
import basicgraphics.Task;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static java.awt.event.KeyEvent.*;

public class TanksGame {

    public static final Dimension WINDOW_SIZE = new Dimension(800, 800);

    public static Point mousePosition = new Point(0, 0);

    private SpriteComponent sc;
    private final BasicFrame frame = new BasicFrame("Tanks Tanks Tanks Tanks!");

    public void run() {
        sc = new SpriteComponent();
        sc.setPreferredSize(WINDOW_SIZE);
        frame.createBasicLayout(sc);

        var player = new PlayerTank(sc);
        var crosshair = new Crosshair(sc);
        player.setX(400.);
        player.setY(400.);

        frame.show();
        Clock.start(10);
        Clock.addTask(sc.moveSprites());

        addTask(() -> {
            var m = sc.getMousePosition();
            mousePosition = m == null ? new Point(0, 0) : m;
            //System.out.println(mousePosition);
            crosshair.setCenterX(mousePosition.getX());
            crosshair.setCenterY(mousePosition.getY());
            player.setAimingAtX(mousePosition.getX());
            player.setAimingAtY(mousePosition.getY());
            player.draw();
            player.updateVelocity();
        });

        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();

                if (keyCode == VK_A) {
                    player.setHeading(player.getHeading() - 0.05);
                } else if (keyCode == VK_D) {
                    player.setHeading(player.getHeading() + 0.05);
                }

                //if (keyCode == VK_W) {
                //    player.setVelocity(1);
                //}

                player.draw();
            }
        });
    }

    private static void addTask(Runnable r) {
        Clock.addTask(new Task() {
            @Override
            public void run() {
                r.run();
            }
        });
    }

    public static void main(String[] args) {
        var game = new TanksGame();
        game.run();
    }
}
