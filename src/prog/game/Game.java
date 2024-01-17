/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prog.game;

import basicgraphics.BasicFrame;
import basicgraphics.Bounds;
import basicgraphics.ClockWorker;
import basicgraphics.Sprite;
import basicgraphics.SpriteComponent;
import basicgraphics.Task;
import basicgraphics.images.Picture;
import edu.lsu.cct.piraha.Group;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import static prog.game.Game2.readGameFile;

/**
 *
 * @author sbrandt
 */
public class Game {
    final static Color OFF_WHITE = new Color(200,255,255);
         
    final public static int BOUNDARY = 3;

    final public static int BOX = 50;
    
    static int mousex, mousey;
    volatile static int starting_square = 0;
    
    static int divup(int num,int denom) {
        int d = num/denom;
        if(num % denom != 0)
            d ++;
        return d;
    }
    
    static Font theFont = new Font("Arial", Font.BOLD, BOX);
    static BufferedImage[] board = new BufferedImage[1];
    static int storex = BOX, storey = BOX;
    
    static BufferedImage createBoard() {
        final BufferedImage im = BasicFrame.createImage(BOX * 20, BOX * Game2._by_pos.size());
        Graphics gr = im.getGraphics();
        gr.setFont(theFont);
        for (int i = 0; i < Game2._by_pos.size(); i++) {
            Group group = Game2._by_pos.get(i).gr;
            String line = group.substring();
            boolean is_assign = false;
            if("assign".equals(group.group(1).getPatternName()))
                is_assign = true;
            int j = 0;
            while (j < line.length() && line.charAt(j) == ' ') {
                j++;
            }
            j++;
            gr.setColor(Color.white);
            gr.setFont(theFont);
            if(is_assign) {
                drawBox("x",gr,i,j);
                drawBox(line,gr,i,j+1);
            } else {
                drawBox(line,gr,i,j);
            }
        }
        return im;
    }
    
    
    static int score=0, turn_score = 10;
    
    static String getScore() {
        return String.format("%03d/%02d", score, turn_score);
    }

    private static Bounds expand(Bounds bounds) {
        Bounds b = bounds;
        final int boxy = BOX - 2*BOUNDARY;
        final int boxx = BOX*divup(bounds.width,BOX)-2*BOUNDARY;
        if (bounds.width < boxx) {
            int addw = boxx - bounds.width;
            int n1 = addw/2;
            int n2 = addw - n1;
            if (bounds.height < boxy) {
                int addh = (boxy - bounds.height);
                int m1 = addh/2;
                int m2 = addh - m1;
                b = new Bounds(bounds.left + n1, bounds.right + n2, bounds.above + m1, bounds.below + m2);
            } else {
                b = new Bounds(bounds.left + n1, bounds.right + n2, bounds.above, bounds.below);
            }
        } else if(bounds.height < boxy) {
            int addh = (boxy - bounds.height);
            int m1 = addh / 2;
            int m2 = addh - m1;
            b = new Bounds(bounds.left, bounds.right, bounds.above + m1, bounds.below + m2);
        }

        if(b != bounds)
            System.out.printf("%s -> %s%n",bounds,b);
        
        return b;
    }

    static Map<Integer,Integer> indents = new HashMap<>();
    
    private static void drawBox(String line,Graphics gr, int i, int j) {
        line = line.trim();
        Bounds b = expand(BasicFrame.getBounds(line, theFont));
        if("x".equals(line) || "g".equals(line)) {
            System.out.println("starting_square="+starting_square);
            if(i == starting_square) {
                gr.setColor(Color.yellow);
                System.out.println("yellow is set");
            } else {
                gr.setColor(OFF_WHITE);
            }
        } else {
            gr.setColor(Color.cyan);
        }
        gr.fillRect(BOX * j, BOX * i, b.width + BOUNDARY * 2, b.height + BOUNDARY * 2);
        gr.setColor(Color.black);
        gr.drawRect(BOX * j, BOX * i, b.width + BOUNDARY * 2, b.height + BOUNDARY * 2);
        gr.drawString(line, BOX * j + BOUNDARY + b.left, BOX * i + BOUNDARY + b.above);
        int nx = b.width / BOX+ j;
        if(b.width % BOX != 0) nx++;
        indents.put(i,nx);
    }
    
    static class Position {
        final int x,y;
        public Position(int x,int y) {
            this.x = x;
            this.y = y;
        }
    }
    
    static void resetTokens(List<Sprite> tokens) {
        for(int i=0;i<tokens.size();i++) {
            Sprite token = tokens.get(i);
            token.setX(BOX*20);
            token.setY(i*BOX);
            token.is_visible = false;
        }
    }

    public static void main(String[] args) throws Exception {
        UIManager.put("OptionPane.messageFont", theFont);
        UIManager.put("OptionPane.buttonFont", theFont);
        readGameFile("board.txt");
        Game2.GameIter gi = new Game2.GameIter();
        final Dice d1 = new Dice(), d2 = new Dice();

        BasicFrame bf = new BasicFrame("Programming Game");
        Picture bground1 = new Picture("scenery.jpeg");
        System.out.printf("background: %dx%d%n", bground1.getWidth(), bground1.getHeight());
        Dimension psize = new Dimension(BOX * 30, BOX * 20);
        System.out.println("psize: "+psize);
        double sfac = Math.max((1.0*psize.width) / bground1.getWidth(), (1.0*psize.height) / bground1.getHeight());
        System.out.println("sfac="+sfac);
        final Picture bground = bground1.resize(sfac);
        final List<Position> positions = new ArrayList<>();
        List<Sprite> tokens = new ArrayList<>();
        JButton viz = new JButton("On");
        final SpriteComponent sprc = new ProgSpriteComponent(
                bground,
                board,
                positions,
                tokens,
                viz);
        
        for(int i=1;i<=6;i++) {
            BufferedImage im = BasicFrame.createImage(BOX, BOX);
            Graphics g = im.getGraphics();
            g.setFont(new Font(Font.MONOSPACED, Font.BOLD, BOX * 9 / 10));
            g.setColor(new Color(200,255,255));
            g.fillOval(0, 0, BOX, BOX);
            g.setColor(Color.black);
            g.drawOval(0, 0, BOX, BOX);
            g.drawString(""+i,2*BOX/10,9*BOX/10);
            Picture p = new Picture(im);
            Sprite sp = new Sprite(sprc);
            sp.setDrawingPriority(1);
            sp.setPicture(p);
            tokens.add(sp);
        }
        resetTokens(tokens);
        
//        List<String> lines = new ArrayList<>();
//        Scanner sc = new Scanner(new File("/home/sbrandt/repos/beowulf/game/board1.txt"));
//        int maxLen = 0;
//        while (sc.hasNextLine()) {
//            String line = sc.nextLine();
//            if (line.length() > maxLen) {
//                maxLen = line.length();
//            }
//            lines.add(line);
//        }
//        final BufferedImage im = bf.createImage(BOX * 20, BOX * Game2._by_pos.size());
//        Graphics gr = im.getGraphics();
//        final List<Integer> moves = new ArrayList<>();
////        Font f = new Font("Arial", Font.BOLD, BOX);
//        gr.setFont(f);
//        for (int i = 0; i < Game2._by_pos.size(); i++) {
//            Group group = Game2._by_pos.get(i).gr;
//            String line = group.substring();
//            boolean is_assign = false;
//            if("assign".equals(group.group(1).getPatternName()))
//                is_assign = true;
//            int j = 0;
//            while (j < line.length() && line.charAt(j) == ' ') {
//                j++;
//            }
//            j++;
//            System.out.printf("line=%s : ",line);
////            assert b.height == BOX;
//            gr.setColor(Color.white);
//            gr.setFont(f);
//            if(is_assign) {
//                drawBox("x",gr,i,j);
//                drawBox(line,gr,i,j+1);
//            } else {
//                drawBox(line,gr,i,j);
//            }
//        }
//        positions.add(new Position(1,0));
        bf.getContentPane().setPreferredSize(psize);
        final int[] imageOffsets = new int[2];
        
        JLabel jscore = new JLabel(getScore());
        
        sprc.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                mousex = me.getX()-sprc.getOffsetX();
                mousey = me.getY()-sprc.getOffsetY();
            }
        });
        for(int i=1;i<=6;i++) {
            Sprite sp = tokens.get(i-1);
            sp.setX(20*BOX);
            sp.setY(BOX*i);
        }

        Sprite sprite = new Sprite(sprc) {
            int rebox(int n) {
                return (n / BOX) * BOX + BOX / 2;
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                int xv = rebox(me.getX() - imageOffsets[0]) + imageOffsets[0];
                int yv = rebox(me.getY() - imageOffsets[1]) + imageOffsets[1];
                moveTo(xv, yv, 3);
            }

            {
                ClockWorker.addTask(new Task() {
                    @Override
                    public void run() {
                        //moveTo();
                        final int scrollSpeed = 2;
                        int amount = 3 * BOX;
                        sprc.scroll(mousex + amount, mousey + amount, scrollSpeed);
                        sprc.scroll(mousex - amount, mousey - amount, scrollSpeed);
                        sprc.scroll(mousex - amount, mousey + amount, scrollSpeed);
                        sprc.scroll(mousex + amount, mousey - amount, scrollSpeed);
                    }
                });
            }
        };
        sprite.setDrawingPriority(2);
        Picture pawnPic = new Picture("pawn2.png");
        pawnPic = pawnPic.resize((1.0*BOX) / Math.max(pawnPic.getWidth(),pawnPic.getHeight()));
        //pawnPic.transparentWhite();
        sprite.setPicture(pawnPic);
        System.out.println("sprite: "+sprite.getWidth()+"x"+sprite.getHeight());
        sprite.is_visible = true;
        sprite.setX(storex);
        sprite.setY(storey);
        System.out.printf("store=%d, %d%n", storex, storey);
        ClockWorker.addTask(sprc.moveSprites());
        ClockWorker.initialize(10);
        String[][] layout = {
            {"l1" , "d1" , "l2" , "d2" , "roll", "ival0","ival","viz","score"},
            {"box", "box", "box", "box", "box" , "box"  ,"box" ,"box","box"}
        };
        //bf.setWidths(1, 1, 1, 1, 1, 3);
        bf.setHeights(1, 9);
        bf.setStringLayout(layout);
        bf.add("score", jscore);
        bf.add("box", sprc);
        bf.add("d1", d1);
        bf.add("d2", d2);
        bf.add("l1", new JLabel("d1="));
        bf.add("l2", new JLabel("d2="));
//        bf.add("blank", new JPanel());
        JButton rollButton = new JButton("Roll");
        JLabel ival = new JLabel("i=0");
        bf.add("ival",ival);
        JLabel ival0 = new JLabel("old: i=0");
        bf.add("ival0",ival0);
        bf.add("viz",viz);
        viz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = viz.getText();
                if("On".equals(s)) {
                    viz.setText("Off");
                } else {
                    viz.setText("On");
                    turn_score = 0;
                    jscore.setText(getScore());
                }
            }
        });
        rollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double ox = sprite.getX();
                double oy = sprite.getY();
                int py = ((int) oy ) / BOX;
                int px = ((int) ox ) / BOX;
                boolean on_position = positions.isEmpty() || (
                        positions.get(positions.size()-1).y == py &&
                        Game2._by_pos.get(py).depth+1 == px);
                System.out.println("on position: "+on_position);
                System.out.println("positions: "+positions.size());
                if (!on_position) {
                    JOptionPane.showMessageDialog(null, "Your playing piece is not on the right square");
                    turn_score -= 3;
                    if(turn_score <= 0) {
                        turn_score = 0;
                        viz.setText("On");
                    }
                    jscore.setText(getScore());
                    sprite.moveTo(storex,storey,3);
                } else {
                    storex = (int)(ox + sprite.getWidth() / 2);
                    storey = (int)(oy + sprite.getHeight() / 2);
                    starting_square = py;
                    System.out.printf("starting square: %d, px=%d, py=%d, ox=%f, oy=%f%n",starting_square,px,py,ox,oy);
                    board[0] = createBoard();
                    sprc.repaint();
                    if(turn_score > 0) {
                        score += turn_score;
                        jscore.setText(getScore());
                    }
                    turn_score = 10;
                    viz.setText("Off");
                    d1.roll();
//                    d1.setSpots(2);
                    d2.roll();
                    Game2.values.put("d1", d1.getSpots());
                    Game2.values.put("d2",d2.getSpots());
                    positions.clear();
                    resetTokens(tokens);
                    Map<Integer,Integer> offsets = new HashMap<>();
                    boolean game_on = false;
                    for(int ii=0;ii<d1.getSpots();ii++) {
                        if(gi.hasNext()) {
                            game_on = true;
                            Game2._Cell c = gi.next();
                            assert !(c instanceof Game2._Block);
                            int spacing = indents.get(c.pos-1);
                            Integer offset = offsets.get(c.pos-1);
                            if(offset == null)
                                offset = 0;
                            else
                                offset ++;
                            offsets.put(c.pos-1,offset);
                            
                            positions.add(new Position(spacing+offset, c.pos-1));
                        }
                    }
                    if(!game_on) {
                        JOptionPane.showMessageDialog(null, "Game Over! Final Score="+score);
                        System.exit(0);
                    }
                        
                    Number ii = Game2.values.get("i");
                    ival0.setText("old: "+ival.getText());
                    ival.setText("i="+ii);
                    sprc.repaint();
//                    System.out.println(positions);
                }
            }
        });
        bf.add("roll", rollButton);
        bf.setAllFonts(new Font(Font.MONOSPACED, Font.BOLD, BOX * 9 / 10));
        bf.show();
    }
    static int pos = 0;

    static int num(String s, int d1, int d2) {
        if ("d1".equals(s)) {
            return d1;
        }
        if ("d2".equals(s)) {
            return d2;
        }
        return Integer.parseInt(s);
    }
}
