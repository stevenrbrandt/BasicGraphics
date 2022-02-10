package tanks;

import basicgraphics.*;
import basicgraphics.sounds.ReusableClip;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import static java.awt.event.KeyEvent.*;

public class TanksGame {

    public static final Dimension MAIN_GAME_SIZE = new Dimension(900, 900);

    public static Point mousePosition = new Point(0, 0);
    static PlayerTank player;
    private static int liveTanks;

    private SpriteComponent sc;
    private final JLabel[] headers = new JLabel[] { new JLabel("Tanks!"), new JLabel("Score: X"), new JLabel("Ammo: X") };
    private final BasicFrame frame = new BasicFrame("Tanks Tanks Tanks Tanks!");

    private final Random random = new Random();

    public static final ReusableClip boom = new ReusableClip("tanks/boom.wav");
    public static final ReusableClip shot = new ReusableClip("tanks/shot.wav");

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

        spawnWave();

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

            if (liveTanks == 0) {
                spawnWave();
            }

            updateHeaders();
        });

        sc.addSpriteSpriteCollisionListener(Bullet.class, Tank.class, (bullet, tank) -> {
            if (tank instanceof PlayerTank) {
                if (!bullet.isFriendly()) {
                    tank.setActive(false);
                    boom.play();
                    JOptionPane.showMessageDialog(sc, String.format("You're loser.\nFinal Score: %d\nYour conqueror: %s",
                                                                    player.getScore(),
                                                                    ((EnemyTank) bullet.getProgenitor()).getDisplayName()));
                    System.exit(0);
                }
            } else if (bullet.isFriendly()) {
                tank.setActive(false);
                boom.play();
                player.setAmmo(player.getAmmo() + 3);
                player.setScore(player.getScore() + ((EnemyTank) tank).getScoreValue());
                liveTanks--;
            }
        });

        sc.addSpriteSpriteCollisionListener(PlayerTank.class, EnemyTank.class, (pt, et) -> {
            et.setActive(false);
            boom.play();
            player.setAmmo(player.getAmmo() + 6);
            player.setScore(player.getScore() + et.getScoreValue());
            liveTanks--;
        });

        sc.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (player.decrAmmo()) {
                    new Bullet(sc, player, player.getAimingDirection(), 3., true);
                    shot.play();
                }
            }
        });
    }

    private void spawnWave() {
        var n = random.nextInt(2) + 2;

        for (int i = 0; i < n; i++) {
            var t = EnemyTanks.randomTank(sc);
            assert t != null;
            t.setX(random.nextInt(800));
            t.setY(random.nextInt(800));
            liveTanks++;
        }
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
