package tanks;

import basicgraphics.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static java.awt.event.KeyEvent.*;

public class TanksGame {

    public static final Dimension WINDOW_SIZE = new Dimension(800, 800);

    public static Point mousePosition = new Point(0, 0);

    private SpriteComponent sc;
    private final BasicFrame frame = new BasicFrame("Tanks Tanks Tanks Tanks!");

    static PlayerTank player;

    public void run() {
        sc = new SpriteComponent();

        sc.setPreferredSize(WINDOW_SIZE);
        frame.createBasicLayout(sc);

        player = new PlayerTank(sc);
        player.setX(400.);
        player.setY(400.);

        var s1 = new EnemyTanks.SmartTank(sc);
        s1.setX(100);
        s1.setY(100);

        var crosshair = new Crosshair(sc);

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

        sc.addSpriteSpriteCollisionListener(Bullet.class, Tank.class, (bullet, tank) -> {
            if (tank instanceof PlayerTank) {
                if (!bullet.isFriendly()) {
                    tank.setActive(false);
                    JOptionPane.showMessageDialog(sc, "You're loser.");
                    System.exit(0);
                }
            } else if (bullet.isFriendly()) {
                tank.setActive(false);
            }
        });

        sc.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                new Bullet(sc, player, player.getAimingDirection(), 3., true);
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
