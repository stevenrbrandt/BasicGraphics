/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package robosorter;

import basicgraphics.Sprite;

/**
 *
 * @author sbrandt
 */
public class Board {

    MartianEgg[][] cells = new MartianEgg[Robo.NX][];

    public Board() {
        for (int i = 0; i < cells.length; i++) {
            cells[i] = new MartianEgg[Robo.NY];
        }
    }

    public void set(MartianEgg me, int x, int y) {
        if (me == null) {
            return;
        }
        assert x >= 0 && x < Robo.NX;
        assert y >= 0 && y < Robo.NY;
        cells[x][y] = me;
        me.setX(Robo.PADDING + Robo.BLOCK_SIZE * x);
        me.setY(Robo.PADDING + Robo.BLOCK_SIZE * y);
    }

    Mover m;

    public void move(final int x2, final int y2) {
        System.out.printf("move(%d,%d)%n",x2,y2);
        move(x2, y2, false);
    }

    public void carry(final int x2, final int y2) {
        System.out.printf("carry(%d,%d)%n",x2,y2);
        move(x2, y2, true);
    }

    public void move(final Mover sp, final MartianEgg carry,final int delx, final int dely) {
//        System.out.printf("move(%d,%d)%n",delx,dely);
        m.actions.add(new Runnable() {
            public void run() {
                m.count = Math.max(Math.abs(delx),Math.abs(dely))*Robo.BLOCK_SIZE / Robo.SPEED;
//                System.out.printf("count=%d%n", m.count);
                if (delx < 0) {
                    sp.setVel(-Robo.SPEED, sp.getVelY());
                    if(carry != null)
                        carry.setVel(-Robo.SPEED, sp.getVelY());
                } else if (delx > 0) {
                    sp.setVel(Robo.SPEED, sp.getVelY());
                    if(carry != null)
                        carry.setVel(Robo.SPEED, carry.getVelY());
                } else if (dely < 0) {
                    sp.setVelY(-Robo.SPEED);
                    if(carry != null)
                        carry.setVelY(-Robo.SPEED);
                } else if (dely > 0) {
//                    System.out.printf("setVelY(%d)%n", Robo.SPEED);
                    sp.setVelY(Robo.SPEED);
                    if(carry != null)
                        carry.setVelY(Robo.SPEED);
                }
            }
        });
        m.actions.add(new Runnable() {
            public void run() {
                sp.setVel(0,0);
                if(carry != null) {
                    carry.setVel(0,0);
                }
            }
        });
    }

    public void move(final int x2, final int y2, final boolean carry) {
        assert x2 >= 0 && x2 < cells.length : "x2="+x2;
        assert y2 >= 0 && y2 < cells[0].length : "y2="+y2;
        final MartianEgg[] oa = new MartianEgg[2];
        for (int j = 0; j < cells[0].length; j++) {
            for (int i = 0; i < cells.length; i++) {
                if(i == x2 && j == y2) {
//                    System.out.print(">");
                } else {
//                    System.out.print(" ");
                }
                if (cells[i][j] != null) {
//                    System.out.print("X");
                } else {
//                    System.out.print("-");
                }
            }
//            System.out.println();
        }
        oa[0] = cells[m.x][m.y];
        oa[1] = cells[x2][y2];
        if (carry) {
            assert oa[0] != null : "m.x="+m.x+",m.y="+m.y;
            assert oa[1] == null;
            m.carrying = oa[0];
            cells[m.x][m.y] = null;

        }
        if(m.y < y2) {
            move(m,m.carrying,0,1);
            m.y++;
        } else if(m.y > y2) {
            m.y--;
            move(m,m.carrying,0,-1);
        }
        move(m,m.carrying,x2 - m.x,0);
        if(m.y < y2) {
            move(m,m.carrying,0,1);
        } else if(m.y > y2) {
            move(m,m.carrying,0,-1);
        }
        if(carry)
            cells[x2][y2] = oa[0];
//        System.out.printf("obs=%s,%s,%s%n",oa[0],oa[1],m.carrying);
        m.carrying = null;
        m.x = x2; m.y = y2;
    }
}
