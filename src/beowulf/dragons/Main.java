/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beowulf.dragons;

import basicgraphics.BasicContainer;
import basicgraphics.BasicFrame;
import basicgraphics.ClockWorker;
import basicgraphics.Sprite;
import basicgraphics.SpriteComponent;
import basicgraphics.Task;
import basicgraphics.images.Picture;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 *
 * @author sbrandt
 */
public class Main {
    public final static int CLOCK_RATE = 10;
    
    private static Picture rescale(Picture picture, int size) {
        return rescale(picture,size,false);
    }

    private static Picture rescale(Picture picture, int size,boolean shrink) {
        if(shrink) {
            picture.transparentBorder();
            picture.shrinkToMinimum();
        }
        picture.makeSquare();
        double scalex = size/(1.0*picture.getWidth());
        double scaley = size/(1.0*picture.getHeight());
        double scale = scalex < scaley ? scalex : scaley;
        Picture p = picture.resize(scale);
        p.transparentBorder();
        return p;
    }
    
    static class DragonSprite extends Sprite {
        Picture master;
        Picture breath;
        Picture explosion;
        String name;
        public void arrived() {
            runNext();
        }
        DragonSprite(SpriteComponent sc) {
            super(sc);
        }
    }
    
    static int CELL;
    final static List<DragonSprite> sprites = new ArrayList<>();
    final static List<Command> commands = new ArrayList<>();
    static SpriteComponent sc;
    static int index = -1;
    static Picture flamePic;
    static Sprite flameSprite;
    static Picture explosion;
    public final static int EXPLOSION_STEPS = 30;

    final static List<Command> winkills = new ArrayList<>();
    
    private static void runNext() {
        while(++index < commands.size()) {
            Command c = commands.get(index);
            System.out.println("runNext: "+c);
            if(c.cmd.equals("moved")) {
                DragonSprite sp = sprites.get(c.id-1);
                sp.moveTo(c.x*CELL+CELL/2, c.y*CELL+CELL/2, 3.0);
                System.out.println("moveTo "+c);
                break;
            } else if(c.cmd.equals("move")) {
                DragonSprite sp = sprites.get(c.id-1);
                double angle = Math.atan2(c.y,c.x)+Math.PI/2;
                sp.rotate(angle);
            } else if(c.cmd.equals("fire")) {
                DragonSprite sp = sprites.get(c.id-1);
                flameSprite = new Sprite(sc) {
                    @Override
                    public void arrived() {
                        runNext();
                    }
                };
                double angle = Math.atan2(c.y,c.x)+Math.PI/2;
                sp.rotate(angle);
                flameSprite.rotate(angle);
                flameSprite.setX(sp.getX());
                flameSprite.setY(sp.getY());
            } else if(c.cmd.equals("flame")) {
                flameSprite.moveTo(c.x*CELL+CELL/2, c.y*CELL+CELL/2, 6.0);
                break;
            } else if(c.cmd.equals("miss")) {
                flameSprite.setActive(false);
            } else if(c.cmd.equals("hit")) {
                flameSprite.setActive(false);
            } else if(c.cmd.equals("dead")) {
                DragonSprite sp = sprites.get(c.id-1);
                sp.setActive(false);
                Sprite ex = new Sprite(sc) {
                    int count = 0;
                    
                    {
                        ClockWorker.addTask(new Task() {
                            @Override
                            public void run() {
                                double fac = 0.5 + 1.5 * count / EXPLOSION_STEPS;
                                if (count++ < EXPLOSION_STEPS) {
                                    setCenterX(c.x * CELL + CELL / 2);
                                    setCenterY(c.y * CELL + CELL / 2);
                                    setPicture(sp.explosion.resize(fac));
                                } else {
                                    setActive(false);
                                    setFinished();
                                    runNext();
                                }
                            }
                        });
                    }
                };
                ex.setPicture(sp.explosion);
                ex.getPicture().transparentBorder();
                ex.getPicture().shrinkToMinimum();
                ex.setX(sp.getX());
                ex.setY(sp.getY());
                break;
            } else if(c.cmd.equals("win")) {
                winkills.add(c);
            } else if(c.cmd.equals("kills")) {
                winkills.add(c);
            }
        }
        if(index >= commands.size()) {
            BasicContainer bc = new BasicContainer();
            List<List<String>> menu = new ArrayList<>();
            for(int i=0;i<winkills.size();i++) {
                List<String> row = new ArrayList<>();
                row.add("image"+i);
                row.add("text"+i);
                menu.add(row);
            }
            bc.setStringLayout(menu);
            Font font = new Font("Arial",Font.PLAIN,CELL/2);
            for(int i=0;i<winkills.size();i++) {
                Command c = winkills.get(i);
                DragonSprite ds = sprites.get(c.id-1);
                JLabel im = new JLabel();
                im.setIcon(new ImageIcon(ds.master.getImage()));
                JLabel nm = new JLabel();
                String msg = ds.name.substring(3);
                if(c.cmd.equals("win"))
                    msg += " is a winner!";
                else if(c.cmd.equals("kills")) 
                    msg += " earned "+c.x+" kills";
                nm.setText(msg);
                nm.setFont(font);
                bc.add("image"+i,im);
                bc.add("text"+i,nm);
            }
            ClockWorker.finish();
            JOptionPane.showMessageDialog(BasicFrame.getFrame().getContentPane(), bc);
            System.exit(0);
        }
    }
    static class Command {
        final String cmd;
        final int id,x,y;
        public Command(String c,int i,int x,int y) {
            this.cmd = c;
            this.id = i;
            this.x =  x;
            this.y =  y;
        }
        public String toString() {
            return cmd+" "+id+" "+x+" "+y;
        }
    }
    static class ImageCommand extends Command {
        final String image, breath, explosion;
        ImageCommand(int id,String im,String br,String ex) {
            super("images",id,0,0);
            image = im;
            breath = br;
            explosion = ex;
        }
    }
    public static void main(String[] args) throws FileNotFoundException, IOException {
        File logf = new File("/home/sbrandt/repos/heorot2/log1.txt");
        if(args.length == 1) {
            logf = new File(args[0]);
        }
        Map<Integer,String> names = new HashMap<>();
        Pattern p = Pattern.compile("(\\w+)\\s+(-?\\d+)\\s+(-?\\d+)\\s+(-?\\d+)");
        Pattern pi = Pattern.compile("images\\s+(\\d+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)");
        Pattern pn = Pattern.compile("name\\s+(\\d+)\\s+<([^>]+)>");
        for(Scanner s=new Scanner(logf);s.hasNextLine();) {
            String line = s.nextLine();
            Matcher m = p.matcher(line);
            if(m.find()) {
                int id = Integer.parseInt(m.group(2));
                int x = Integer.parseInt(m.group(3));
                int y = Integer.parseInt(m.group(4));
                Command c = new Command(m.group(1), id, x, y);
                commands.add(c);
                continue;
            }
            m = pi.matcher(line);
            if(m.find()) {
                int id = Integer.parseInt(m.group(1));
                ImageCommand ic = new ImageCommand(id,m.group(2),m.group(3),m.group(4));
                commands.add(ic);
                continue;
            }
            m = pn.matcher(line);
            if(m.find()) {
                int id = Integer.parseInt(m.group(1));
                names.put(id,m.group(2));
            }
        }
        BasicFrame bf = new BasicFrame("Dragons!");
        final int NX = commands.get(0).x;
        final int NY = commands.get(0).y;
        String[][] layout = {
            {"center"}
        };
        bf.setStringLayout(layout);
        final Dimension d = new Dimension(1700,700);//Toolkit.getDefaultToolkit().getScreenSize();
        System.out.println("d1="+d);
        d.width = (int)(d.width*.9);
        d.height = (int)(d.height*.9);
        int cellx = (int)(d.width/NX);
        int celly = (int)(d.height/NY);
        CELL = cellx < celly ? cellx : celly;
        d.width = CELL*NX;
        d.height = CELL*NY;
        System.out.println("d2="+d);
        
        sc = new SpriteComponent() {
            @Override
            public void paintBackground(Graphics g) {
                g.setColor(Color.white);
                g.fillRect(0, 0, d.width, d.height);
                g.setColor(Color.black);
                for(int j=0;j<NY;j++) {
                    g.drawLine(0,j*CELL,d.width,j*CELL);
                }
                for(int i=0;i<NX;i++) {
                    g.drawLine(i*CELL,0,i*CELL,d.height);
                }
            }
        };
        final File imageDir = new File(logf.getParent(), "images");
        final Image dragon = ImageIO.read(new File(imageDir,"Dragon0.gif"));
        final Image flame = ImageIO.read(new File(imageDir,"flame.gif"));
        final Image expl = ImageIO.read(new File(imageDir,"explosion2.jpg"));
        explosion = rescale(new Picture(expl),CELL);
        flamePic = rescale(new Picture(flame),CELL);
        Picture pic = rescale(new Picture(dragon),CELL);
        ImageCommand ic = null;
        for(Command c : commands) {
            if(c.cmd.equals("hello")) {
                DragonSprite sp = new DragonSprite(sc);
                assert ic.id == c.id;
                
                File fm = new File(imageDir,ic.image);
                if(!fm.exists()) throw new IOException("Does not exist: "+fm);
                System.out.println("fm="+fm);
                Picture pm = null;
                try {
                    pm = rescale(new Picture(ImageIO.read(fm)),CELL);
                } catch(Exception ex) {
                    fm = new File("images/lighting2.png");
                    pm = rescale(new Picture(ImageIO.read(fm)),CELL);
                }
                sp.master = pm;
                sp.setPicture(pm);
                sp.name = names.get(c.id);
                
                File fb = new File(imageDir,ic.breath);
                if(!fb.exists()) throw new IOException("Does not exist: "+fb);
                Picture pb = rescale(new Picture(ImageIO.read(fb)),CELL);
                sp.breath = pb;
                
                File fe = new File(imageDir,ic.explosion);
                if(!fe.exists()) throw new IOException("Does not exist: "+fe);
                Picture pe = rescale(new Picture(ImageIO.read(fe)),CELL);
                sp.explosion = pe;
                
                sp.setX(c.x*CELL);
                sp.setY(c.y*CELL);
                sprites.add(sp);
            } else if(c.cmd.equals("images")) {
                ic = (ImageCommand)c;
            } else if(c.cmd.equals("move")) {
                break;
            } else if(c.cmd.equals("fire")) {
                break;
            }
        }
        sc.setPreferredSize(d);
        bf.add("center", sc);
        bf.show();
        List<List<String>> lay2 = new ArrayList<>();
        for(int i=0;i<sprites.size();i++) {
            List<String> row = new ArrayList<>();
            row.add("image"+i);
            row.add("text"+i);
            lay2.add(row);
        }
        BasicContainer bc = new BasicContainer();
        bc.setStringLayout(lay2);
        Font font = new Font("Arial",Font.PLAIN,CELL/2);
        for(int i=0;i<sprites.size();i++) {
            DragonSprite ds = sprites.get(i);
            JLabel im = new JLabel();
            im.setIcon(new ImageIcon(ds.master.getImage()));
            JLabel nm = new JLabel();
            nm.setText(ds.name.substring(3));
            nm.setFont(font);
            bc.add("image"+i,im);
            bc.add("text"+i,nm);
        }
        BasicFrame.getFrame().getContentPane().setFont(font);
        UIManager.put("OptionPane.messageFont", font);
        UIManager.put("OptionPane.buttonFont", font);
        JOptionPane.showMessageDialog(BasicFrame.getFrame().getContentPane(), bc);
        ClockWorker.initialize(CLOCK_RATE);
        ClockWorker.addTask(sc.moveSprites());
        runNext();
    }
    
//    static void showMessage(String message) {
//        Clock.stop();
//        JOptionPane.showMessageDialog(BasicFrame.getFrame().jf, message);
//        Clock.start(CLOCK_RATE);
//    }
}
