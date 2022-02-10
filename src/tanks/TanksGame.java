package tanks;

import basicgraphics.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static java.awt.event.KeyEvent.*;

public class TanksGame {

    public static final Dimension MAIN_GAME_SIZE = new Dimension(900, 900);

    public static Point mousePosition = new Point(0, 0);

    private SpriteComponent sc;
    private final JLabel[] headers = new JLabel[] { new JLabel("Tanks!"), new JLabel("Score: X"), new JLabel("Ammo: X") };

    private final BasicFrame frame = new BasicFrame("Tanks Tanks Tanks Tanks!");

    static PlayerTank player;

    public void updateHeaders() {
        headers[1].setText("Score: " + player.getScore());
        headers[2].setText("Ammo: " + player.getAmmo());
    }

    public void run() {
        sc = new SpriteComponent();

        sc.setPreferredSize(MAIN_GAME_SIZE);
        frame.setStringLayout(new String[][] {{"h0", "h1", "h2"},
                                              {"m0", "m0", "m0"}});
        frame.add("m0", sc);

        for (int i = 0; i < headers.length; i++) {
            frame.add("h" + i, headers[i]);
            headers[i].setPreferredSize(new Dimension(300, 20));
            headers[i].setFont(new Font("Verdana", Font.PLAIN, 20));
        }

        player = new PlayerTank(sc, 6);
        player.setX(400.);
        player.setY(400.);

        updateHeaders();

        //var s1 = new EnemyTanks.SmartTank(sc);
        //s1.setX(400);
        //s1.setY(100);
//
        //var s2 = new EnemyTanks.CrazyTank(sc);
        //s2.setX(200);
        //s2.setY(100);
//
        //var s3 = new EnemyTanks.CrazyTank(sc);
        //s3.setX(600);
        //s3.setY(100);

        var s4 = new EnemyTanks.SmartMobileBurstTank(sc);
        s4.setX(400);
        s4.setY(100);

        var crosshair = new Crosshair(sc);

        frame.show();
        Clock.start(10);
        Clock.addTask(sc.moveSprites());

        addTask(() -> {
            var m = sc.getMousePosition();
            mousePosition = m == null ? new Point(0, 0) : m;
            crosshair.setCenterX(mousePosition.getX());
            crosshair.setCenterY(mousePosition.getY());
            player.setAimingAtX(mousePosition.getX());
            player.setAimingAtY(mousePosition.getY());

            var a = frame.isKeyHeld(VK_A);
            var d = frame.isKeyHeld(VK_D);

            if (!a || !d) {
                if (a) {
                    player.setHeading(player.getHeading() - 0.05);
                } else if (d) {
                    player.setHeading(player.getHeading() + 0.05);
                }
            }

            player.draw();
            player.updateVelocity();
            updateHeaders();
        });

        sc.addSpriteSpriteCollisionListener(Bullet.class, Tank.class, (bullet, tank) -> {
            if (tank instanceof PlayerTank) {
                if (!bullet.isFriendly()) {
                    tank.setActive(false);
                    JOptionPane.showMessageDialog(sc, "You're loser.\nFinal Score: " + player.getScore());
                    System.exit(0);
                }
            } else if (bullet.isFriendly()) {
                tank.setActive(false);
                player.setAmmo(player.getAmmo() + 6);
                player.setScore(player.getScore() + ((EnemyTank) tank).getScoreValue());
            }
        });

        sc.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (player.decrAmmo()) {
                    new Bullet(sc, player, player.getAimingDirection(), 3., true);
                }
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
