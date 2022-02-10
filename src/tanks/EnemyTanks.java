package tanks;

import basicgraphics.SpriteComponent;

import java.awt.*;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.Random;

import static tanks.TanksGame.*;

public class EnemyTanks {
    private EnemyTanks() { }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    private @interface Weight {
        int value() default 0;
    }

    @SuppressWarnings("unchecked")
    private static final WeightedSet<Class<? extends EnemyTank>> tankClasses = new WeightedSet<>() {{
        var tanksClass = (Class<EnemyTanks>) getClass().getEnclosingClass();

        Arrays.stream(tanksClass.getClasses()).filter(EnemyTank.class::isAssignableFrom).forEach(clazz -> {
            add((Class<? extends EnemyTank>) clazz, clazz.getAnnotation(Weight.class).value());
        });
    }};

    public static Tank randomTank(SpriteComponent sc) {
        try {
            return tankClasses.getRandom().getConstructor(SpriteComponent.class).newInstance(sc);
        } catch (ReflectiveOperationException ignored) { return null; }
    }

    @Weight(10)
    public static class StupidTank extends EnemyTank {

        public StupidTank(SpriteComponent sc) {
            super(sc, Color.ORANGE, 5, "Stationary Stupid Tank");
        }

        @Override
        public void brain(long stopWatch) {
            setAimingAtX(player.centerX());
            setAimingAtY(player.centerY());

            if (stopWatch > 1500) {
                fireBullet(3, getAimingDirection());
                resetStopwatch();
            }
        }
    }

    @Weight(8)
    public static class SmartTank extends EnemyTank {

        public SmartTank(SpriteComponent sc) {
            super(sc, Color.RED, 10, "Stationary Smart Tank");
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
                    4
            );

            if (t != null) {
                setAimingAtX(t.getA());
                setAimingAtY(t.getB());
            } else {
                setAimingAtX(player.centerX());
                setAimingAtY(player.centerY());
            }

            if (stopWatch > 1500) {
                fireBullet(4, getAimingDirection());
                resetStopwatch();
            }
        }
    }

    @Weight(3)
    public static class SharpshooterTank extends EnemyTank {

        public SharpshooterTank(SpriteComponent sc) {
            super(sc, new Color(208, 12, 241), 10, "Sharpshooter");
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
                    15
            );

            if (t != null) {
                setAimingAtX(t.getA());
                setAimingAtY(t.getB());
            } else {
                setAimingAtX(player.centerX());
                setAimingAtY(player.centerY());
            }

            if (stopWatch > 5000) {
                fireBullet(15, getAimingDirection());
                resetStopwatch();
            }
        }
    }

    @Weight(8)
    public static class MobileStupidTank extends EnemyTank {

        private double dHeading;
        private final Random r = new Random();

        public MobileStupidTank(SpriteComponent sc) {
            super(sc, Color.ORANGE, 10, "Stupid Tank");
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
                    fireBullet(3, getAimingDirection());
                }
            }
        }
    }

    @Weight(5)
    public static class CrazyTank extends EnemyTank {

        private double dHeading;
        private final Random r = new Random();

        public CrazyTank(SpriteComponent sc) {
            super(sc, new Color(23, 91, 1), 50, "Crazy Tank");
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
                    fireBullet(3, getAimingDirection() + deviation);
                }
            }
        }
    }

    @Weight(8)
    public static class MobileBurstTank extends EnemyTank {

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
            super(sc, new Color(23, 91, 1), 20, "Burstfire Tank");
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
                        fireBullet(3, getAimingDirection() + deviation);
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

    @Weight(1)
    public static class MobileFlowerTank extends EnemyTank {

        private long shotTimer, roamTimer;
        private double dHeading, fireOffset;
        private State state = State.DRIVING;

        private final Random r = new Random();

        private enum State {
            TURNING,
            DRIVING
        }

        public MobileFlowerTank(SpriteComponent sc) {
            super(sc, new Color(189, 89, 3), 35, "Bloomflower Tank");
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
                    if (roamTimer > 2000) {
                        roamTimer = 0;
                        state = State.TURNING;
                        dHeading = r.nextBoolean() ? -0.05 : 0.05;
                        setVelocity(0);
                    } else if (shotTimer > 100) {
                        shotTimer = 0;
                        fireBullet(3, fireOffset);
                        fireOffset += Math.PI / 6;
                        if (fireOffset > 2 * Math.PI) {
                            fireOffset = -2 * Math.PI;
                        }
                    }
                    break;
                case TURNING:
                    if (roamTimer > 1500) {
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

    @Weight(7)
    public static class SmartMobileBurstTank extends EnemyTank {

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
            super(sc, new Color(41, 148, 4), 30, "Smart Burstfire Tank");
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
                    3
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
                        fireBullet(3, getAimingDirection() + deviation);
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

    @Weight(5)
    public static class MobileScatterTank extends EnemyTank {

        private int shotsLeft = 6;
        private long shotTimer, roamTimer;
        private double dHeading;
        private State state = State.DRIVING;

        private final Random r = new Random();

        private enum State {
            TURNING,
            DRIVING
        }

        public MobileScatterTank(SpriteComponent sc) {
            super(sc, new Color(114, 98, 239), 20, "Scattershot Tank");
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
                    3
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
                    } else if (shotsLeft > 0 && shotTimer > 2300) {
                        --shotsLeft;
                        shotTimer = 0;
                        fireBullet(3, getAimingDirection());
                        fireBullet(3, getAimingDirection() - 0.05 - r.nextDouble(0.03));
                        fireBullet(3, getAimingDirection() - 0.1 - r.nextDouble(0.03));
                        fireBullet(3, getAimingDirection() + 0.05 + r.nextDouble(0.03));
                        fireBullet(3, getAimingDirection() + 0.1 + r.nextDouble(0.03));
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

    @Weight(1)
    public static class CrazyMobileScatterTank extends EnemyTank {

        private int shotsLeft = 6;
        private long shotTimer, roamTimer;
        private double dHeading;
        private State state = State.DRIVING;

        private final Random r = new Random();

        private enum State {
            TURNING,
            DRIVING
        }

        public CrazyMobileScatterTank(SpriteComponent sc) {
            super(sc, new Color(13, 1, 117), 40, "Crazy Scattershot Tank");
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
                    } else if (shotsLeft > 0 && shotTimer > 1500) {
                        --shotsLeft;
                        shotTimer = 0;
                        for (int i = 0; i < 20; i++) {
                            fireBullet(2, getAimingDirection() + r.nextDouble() - 0.5);
                        }
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
