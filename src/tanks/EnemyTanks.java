package tanks;

import basicgraphics.SpriteComponent;

import java.awt.*;
import java.util.Random;

import static tanks.TanksGame.*;

public class EnemyTanks {
    private EnemyTanks() { }

    static class StupidTank extends EnemyTank {

        public StupidTank(SpriteComponent sc) {
            super(sc, Color.ORANGE, 5);
        }

        @Override
        public void brain(long stopWatch) {
            setAimingAtX(player.centerX());
            setAimingAtY(player.centerY());

            if (stopWatch > 1500) {
                new Bullet(getSpriteComponent(), this, getAimingDirection(), 3, false);
                resetStopwatch();
            }
        }
    }

    static class SmartTank extends EnemyTank {

        public SmartTank(SpriteComponent sc) {
            super(sc, Color.RED, 10);
        }

        @Override
        public void brain(long stopWatch) {
            var t = leadProjectile(
                    centerX(),
                    centerY(),
                    player.centerX(),
                    player.centerY(),
                    player.getVelX(),
                    player.getVelY(),
                    2
            );

            if (t != null) {
                setAimingAtX(t.getA());
                setAimingAtY(t.getB());
            } else {
                setAimingAtX(player.centerX());
                setAimingAtY(player.centerY());
            }

            if (stopWatch > 1500) {
                new Bullet(getSpriteComponent(), this, getAimingDirection(), 2, false);
                resetStopwatch();
            }
        }
    }

    static class MobileStupidTank extends EnemyTank {

        private double dHeading;
        private final Random r = new Random();

        public MobileStupidTank(SpriteComponent sc) {
            super(sc, Color.ORANGE, 10);
            setVelocity(1.5);
        }

        @Override
        public void brain(long stopWatch) {
            setAimingAtX(player.centerX());
            setAimingAtY(player.centerY());

            if (dHeading != 0) {
                if (stopWatch > 500) {
                    resetStopwatch();
                    dHeading = 0;
                    setVelocity(1.5);
                } else {
                    setHeading(getHeading() + dHeading);
                }
            } else if (stopWatch > 1000) {
                resetStopwatch();
                if (r.nextInt(5) < 2 ) {
                    dHeading = r.nextBoolean() ? -0.05 : 0.05;
                    setVelocity(0);
                } else {
                    new Bullet(getSpriteComponent(), this, getAimingDirection(), 3, false);
                }
            }
        }
    }

    static class CrazyTank extends EnemyTank {

        private double dHeading;
        private final Random r = new Random();

        public CrazyTank(SpriteComponent sc) {
            super(sc, new Color(23, 91, 1), 50);
            setVelocity(1.5);
        }

        @Override
        public void brain(long stopWatch) {
            setAimingAtX(player.centerX());
            setAimingAtY(player.centerY());

            if (dHeading != 0) {
                if (stopWatch > 500) {
                    resetStopwatch();
                    dHeading = 0;
                    setVelocity(1.5);
                } else {
                    setHeading(getHeading() + dHeading);
                }
            } else if (stopWatch > 100) {
                resetStopwatch();
                if (r.nextInt(5) == 0) {
                    dHeading = r.nextBoolean() ? -0.05 : 0.05;
                    setVelocity(0);
                } else {
                    var deviation = r.nextDouble(0.1) - 0.05;
                    new Bullet(getSpriteComponent(), this, getAimingDirection() + deviation, 3, false);
                }
            }
        }
    }

    static class MobileBurstTank extends EnemyTank {

        private int shotsLeft = 6;
        private long shotTimer, roamTimer;
        private double dHeading;
        private State state = State.DRIVING;

        private final Random r = new Random();

        private enum State {
            TURNING,
            DRIVING
        }

        public MobileBurstTank(SpriteComponent sc) {
            super(sc, new Color(23, 91, 1), 20);
            setVelocity(1.5);
        }

        @Override
        public void brain(long stopWatch) {
            resetStopwatch();
            shotTimer += stopWatch;
            roamTimer += stopWatch;

            setAimingAtX(player.centerX());
            setAimingAtY(player.centerY());

            switch (state) {
                case DRIVING:
                    if (shotsLeft == 0 && roamTimer > 2000) {
                        roamTimer = 0;
                        state = State.TURNING;
                        shotsLeft = r.nextInt(11 - 4) + 4;
                        dHeading = r.nextBoolean() ? -0.05 : 0.05;
                        setVelocity(0);
                    } else if (shotsLeft > 0 && shotTimer > 100) {
                        --shotsLeft;
                        shotTimer = 0;
                        var deviation = r.nextDouble(0.1) - 0.05;
                        new Bullet(getSpriteComponent(), this, getAimingDirection() + deviation, 3, false);
                    }
                    break;
                case TURNING:
                    if (roamTimer > 500) {
                        roamTimer = 0;
                        state = State.DRIVING;
                        dHeading = 0;
                        setVelocity(1.5);
                    } else {
                        setHeading(getHeading() + dHeading);
                    }
                    break;
            }
        }
    }

    static class SmartMobileBurstTank extends EnemyTank {

        private int shotsLeft = 6;
        private long shotTimer, roamTimer;
        private double dHeading;
        private State state = State.DRIVING;

        private final Random r = new Random();

        private enum State {
            TURNING,
            DRIVING
        }

        public SmartMobileBurstTank(SpriteComponent sc) {
            super(sc, new Color(41, 148, 4), 30);
            setVelocity(1.5);
        }

        @Override
        public void brain(long stopWatch) {
            resetStopwatch();
            shotTimer += stopWatch;
            roamTimer += stopWatch;

            var t = leadProjectile(
                    centerX(),
                    centerY(),
                    player.centerX(),
                    player.centerY(),
                    player.getVelX(),
                    player.getVelY(),
                    2
            );

            if (t != null) {
                setAimingAtX(t.getA());
                setAimingAtY(t.getB());
            } else {
                setAimingAtX(player.centerX());
                setAimingAtY(player.centerY());
            }

            switch (state) {
                case DRIVING:
                    if (shotsLeft == 0 && roamTimer > 2000) {
                        roamTimer = 0;
                        state = State.TURNING;
                        shotsLeft = r.nextInt(11 - 4) + 4;
                        dHeading = r.nextBoolean() ? -0.05 : 0.05;
                        setVelocity(0);
                    } else if (shotsLeft > 0 && shotTimer > 100) {
                        --shotsLeft;
                        shotTimer = 0;
                        var deviation = r.nextDouble(0.1) - 0.05;
                        new Bullet(getSpriteComponent(), this, getAimingDirection() + deviation, 3, false);
                    }
                    break;
                case TURNING:
                    if (roamTimer > 500) {
                        roamTimer = 0;
                        state = State.DRIVING;
                        dHeading = 0;
                        setVelocity(1.5);
                    } else {
                        setHeading(getHeading() + dHeading);
                    }
                    break;
            }
        }
    }

    private static Tuple2<Double, Double> leadProjectile(double sx, double sy, double tx, double ty, double vtx, double vty, double v) {
        var dx = tx - sx;
        var dy = ty - sy;

        var roots = roots(
                vtx * vtx + vty * vty - v * v,
                2 * (vtx * dx + vty * dy),
                dx * dx + dy * dy
        );

        if (roots == null) {
            return null;
        }

        var time = Math.min(roots.getA(), roots.getB());

        if (time < 0) {
            time = Math.max(roots.getA(), roots.getB());
        }

        if (time <= 0) {
            return null;
        }

        return new Tuple2<>(tx + vtx * time, ty + vty * time);
    }

    private static Tuple2<Double, Double> roots(double a, double b, double c) {
        var d = b * b - (4 * a * c);
        if (d >= 0) {
            d = Math.sqrt(d);
            a *= 2;
            return new Tuple2<>((-b - d) / a, (-b + d) / a);
        } else return null;
    }
}
