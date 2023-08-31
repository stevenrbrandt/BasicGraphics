/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crossTheFields;

import basicgraphics.BasicContainer;
import basicgraphics.BasicFrame;
import basicgraphics.Clock;
import basicgraphics.Sprite;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Random;
import basicgraphics.SpriteComponent;
import basicgraphics.SpriteSpriteCollisionListener;
import basicgraphics.Task;
import basicgraphics.images.Picture;
import basicgraphics.sounds.ReusableClip;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author theha
 */
public class Main {
    final public static Random RAND = new Random();
    final public static Color CIRCLE_COLOR = Color.green;
    final public static Color TRIANGLE_COLOR = Color.blue;
    final public static Color SQUARE_COLOR = Color.getHSBColor(53, 68, 50);
    final public static Color SOLDIER_COLOR = Color.black;
    final public static Color SOLDIER_SHOT_COLOR = Color.RED;
    final public static Color ENEMY_SHOT_COLOR = Color.blue;
    final public static Color WALL_COLOR = Color.gray;
    final public static Dimension BOARD_SIZE = new Dimension(800,600);
    BasicFrame bf = new BasicFrame("Cross the Fields");
    final SpriteComponent sc = new SpriteComponent();
    final Soldier soldier = new Soldier(sc);
    ArrayList<Sprite> sprites = new ArrayList<Sprite>();
    int currentLevel;
    Task currentTask;
    final static ReusableClip explode = new ReusableClip("die.wav");
    
    static Picture makeBall(Color color,int size) {
        Image im = BasicFrame.createImage(size, size);
        Graphics g = im.getGraphics();
        g.setColor(color);
        g.fillOval(0, 0, size, size);
        return new Picture(im);
    }
    
    public void startUp() {
        
        final Container content = bf.getContentPane(); //start
        final CardLayout cards = new CardLayout();
        content.setLayout(cards);
        BasicContainer bc1 = new BasicContainer();
        content.add(bc1,"Splash");
        final BasicContainer bc2 = new BasicContainer();
        content.add(bc2,"Game"); //end
        sc.setPreferredSize(BOARD_SIZE);
        String[][] splashLayout = { //stsart
            {"Title"},
            {"Button"}
        };
        bc1.setStringLayout(splashLayout);
        bc1.add("Title",new JLabel("Cross The Fields"));//title
        JButton jstart = new JButton("Start");
        jstart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cards.show(content,"Game");
                bc2.requestFocus();
                //timer
                Clock.start(3);
            }
        });
        bc1.add("Button",jstart);
        String[][] layout = {{
            "Level One"
        }};
        bc2.setStringLayout(layout);
        bc2.add("Level One",sc);
        
        final Goal goal = new Goal(sc);
        
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent moe) {
                new FriendlyBullet(sc,soldier,moe.getX(),moe.getY());
                //shoot bullet
            }
        };
        sc.addMouseListener(ma);
        
        //user interface
        
        bf.addMenuAction("Help", "About", new Runnable() {
            @Override
            public void run() {
                JOptionPane.showMessageDialog(bf.getContentPane(), "Use WASD to move. Click to shoot a bullet towards your mouse. Don't let enemies shoot/touch you!");
            }
        });
        bf.addMenuAction("File", "Store", new Runnable() {
            @Override
            public void run() {
                JOptionPane.showMessageDialog(bf.getContentPane(), "You're in the middle of a war, what are you shopping for?");
            }
        });
        bf.addMenuAction("File", "Cheats", new Runnable() {
            @Override
            public void run() {
                JOptionPane.showMessageDialog(bf.getContentPane(), "No.");
            }
        });
        bf.addMenuAction("Load", "2", new Runnable() {
            @Override
            public void run() {
                currentTask.setFinished();
                loadLevelTwo();
            }
        });
        bf.addMenuAction("Load", "3", new Runnable() {
            @Override
            public void run() {
                currentTask.setFinished();
                loadLevelThree();
            }
        });
        bf.addMenuAction("Load", "4", new Runnable() {
            @Override
            public void run() {
                currentTask.setFinished();
                loadLevelFour();
            }
        });
        bf.addMenuAction("Load", "5", new Runnable() {
            @Override
            public void run() {
                currentTask.setFinished();
                loadLevelFive();
            }
        });
        bf.addMenuAction("Load", "6", new Runnable() {
            @Override
            public void run() {
                currentTask.setFinished();
                loadLevelSix();
            }
        });
        bf.addMenuAction("Load", "7", new Runnable() {
            @Override
            public void run() {
                currentTask.setFinished();
                loadLevelSeven();
            }
        });
        bf.addMenuAction("Load", "8", new Runnable() {
            @Override
            public void run() {
                currentTask.setFinished();
                loadLevelEight();
            }
        });
        bf.addMenuAction("Load", "9", new Runnable() {
            @Override
            public void run() {
                currentTask.setFinished();
                loadLevelNine();
            }
        });
        bf.addMenuAction("Load", "10", new Runnable() {
            @Override
            public void run() {
                currentTask.setFinished();
                loadLevelTen();
            }
        });
        bf.addMenuAction("Load", "11", new Runnable() {
            @Override
            public void run() {
                currentTask.setFinished();
                loadLevelEleven();
            }
        });
        bf.addMenuAction("Load", "12", new Runnable() {
            @Override
            public void run() {
                currentTask.setFinished();
                loadLevelTwelve();
            }
        });
        bf.addMenuAction("Load", "13", new Runnable() {
            @Override
            public void run() {
                currentTask.setFinished();
                loadLevelThirteen();
            }
        });
        bf.addMenuAction("Load", "14", new Runnable() {
            @Override
            public void run() {
                currentTask.setFinished();
                loadLevelFourteen();
            }
        });
        bf.addMenuAction("Load", "15", new Runnable() {
            @Override
            public void run() {
                currentTask.setFinished();
                loadLevelFifteen();
            }
        });
        bf.show();
        
        
        //soldieer motion and stuff
        
        bc2.addKeyListener(new KeyAdapter() {
            @Override //not a normal instance of keypressed
            public void keyPressed(KeyEvent e) {
                e.getKeyCode();
                if (e.getKeyCode() == KeyEvent.VK_W) { 
                    soldier.setVelY(-1); 
                    //move the soldier up and so on
                }
                if (e.getKeyCode() == KeyEvent.VK_A) { 
                    soldier.setVelX(-1);
                    
                }
                if (e.getKeyCode() == KeyEvent.VK_S) { 
                    soldier.setVelY(1);
                }
                if (e.getKeyCode() == KeyEvent.VK_D) {
                    soldier.setVelX(1);
                }
            }
            @Override
            public void keyReleased(KeyEvent d) { //stop the soldier when buttons aren't pressed
                soldier.setVelX(0);
                soldier.setVelY(0);
            }
        });
        
        
        
        //collision listeners
        
        //shooting wall
        sc.addSpriteSpriteCollisionListener(FriendlyBullet.class, Wall.class, new SpriteSpriteCollisionListener<FriendlyBullet, Wall>() {
            @Override
            public void collision(FriendlyBullet fb, Wall wall) {
                fb.setActive(false);
           }
        });
        
        sc.addSpriteSpriteCollisionListener(EnemyBullet.class, Wall.class, new SpriteSpriteCollisionListener<EnemyBullet, Wall>() {
            @Override
            public void collision(EnemyBullet eb, Wall wall) {
                eb.setActive(false);
           }
        });//no need to make a collision listener for piercing bullets
        
        
        //shooting enemies
        sc.addSpriteSpriteCollisionListener(FriendlyBullet.class, TriangleEnemy.class, new SpriteSpriteCollisionListener<FriendlyBullet, TriangleEnemy>() {
            @Override
            public void collision(FriendlyBullet fb, TriangleEnemy te) {
                fb.setActive(false);
                te.setActive(false);
                
           }
        });
        sc.addSpriteSpriteCollisionListener(FriendlyBullet.class, SquareEnemy.class, new SpriteSpriteCollisionListener<FriendlyBullet, SquareEnemy>() {
            @Override
            public void collision(FriendlyBullet fb, SquareEnemy se) {
                fb.setActive(false);
                se.setActive(false);
                
           }
        });
        sc.addSpriteSpriteCollisionListener(FriendlyBullet.class, CircleEnemy.class, new SpriteSpriteCollisionListener<FriendlyBullet, CircleEnemy>() {
            @Override
            public void collision(FriendlyBullet fb, CircleEnemy ce) {
                fb.setActive(false);
                ce.setActive(false);
                
           }
        });
        sc.addSpriteSpriteCollisionListener(FriendlyBullet.class, PinkTriangleEnemy.class, new SpriteSpriteCollisionListener<FriendlyBullet, PinkTriangleEnemy>() {
            @Override
            public void collision(FriendlyBullet fb, PinkTriangleEnemy pte) {
                fb.setActive(false);
                pte.setActive(false);
                
           }
        });
        sc.addSpriteSpriteCollisionListener(FriendlyBullet.class, OrangeSquareEnemy.class, new SpriteSpriteCollisionListener<FriendlyBullet, OrangeSquareEnemy>() {
            @Override
            public void collision(FriendlyBullet fb, OrangeSquareEnemy ose) {
                fb.setActive(false);
                ose.setActive(false);
                
           }
        });
        
        
        
        //crashing into a wall
        sc.addSpriteSpriteCollisionListener(Soldier.class, Wall.class, new SpriteSpriteCollisionListener<Soldier, Wall>() {
            @Override
            public void collision(Soldier soldier, Wall wall) {
                
                double currentVelX = soldier.getVelX();
                double currentVelY = soldier.getVelY();
                soldier.setX(soldier.getX()-soldier.getVelX());
                soldier.setY(soldier.getY()-soldier.getVelY());
                soldier.setVelX(-currentVelX);
                soldier.setVelY(-currentVelY);
                soldier.setVelX(0);
                soldier.setVelY(0);
                          
           }
        });
        sc.addSpriteSpriteCollisionListener(TriangleEnemy.class, Wall.class, new SpriteSpriteCollisionListener<TriangleEnemy, Wall>() {
            @Override
            public void collision(TriangleEnemy te, Wall wall) {
                
                double currentVelX = te.getVelX();
                double currentVelY = te.getVelY();
                te.setX(te.getX()-te.getVelX());
                te.setY(te.getY()-te.getVelY());
                te.setVelX(-currentVelX);
                te.setVelY(-currentVelY);
                
                          
           }
        });
        sc.addSpriteSpriteCollisionListener(PinkTriangleEnemy.class, Wall.class, new SpriteSpriteCollisionListener<PinkTriangleEnemy, Wall>() {
            @Override
            public void collision(PinkTriangleEnemy pte, Wall wall) {
                
                double currentVelX = pte.getVelX();
                double currentVelY = pte.getVelY();
                pte.setX(pte.getX()-pte.getVelX());
                pte.setY(pte.getY()-pte.getVelY());
                pte.setVelX(-currentVelX);
                pte.setVelY(-currentVelY);
                
                          
           }
        });
        
        //getting killed 
        
        
        sc.addSpriteSpriteCollisionListener(EnemyBullet.class, Soldier.class, new SpriteSpriteCollisionListener<EnemyBullet, Soldier>() {
            @Override
            public void collision(EnemyBullet eb, Soldier soldier) {
                eb.setActive(false);
                soldier.setActive(false);
                JOptionPane.showMessageDialog(bf.getContentPane(), "You died. Sad moment.");
                System.exit(0); //maybe replace this with an option to restart level?
           }
        });
        
        sc.addSpriteSpriteCollisionListener(PiercingEnemyBullet.class, Soldier.class, new SpriteSpriteCollisionListener<PiercingEnemyBullet, Soldier>() {
            @Override
            public void collision(PiercingEnemyBullet eb, Soldier soldier) {
                eb.setActive(false);
                soldier.setActive(false);
                JOptionPane.showMessageDialog(bf.getContentPane(), "You died. Sad moment.");
                System.exit(0); //maybe replace this with an option to restart level?
           }
        });
        
        sc.addSpriteSpriteCollisionListener(TriangleEnemy.class, Soldier.class, new SpriteSpriteCollisionListener<TriangleEnemy, Soldier>() {
            @Override
            public void collision(TriangleEnemy te, Soldier soldier) {
                te.setActive(false);
                soldier.setActive(false);
                JOptionPane.showMessageDialog(bf.getContentPane(), "You died. Sad moment.");
                System.exit(0); //maybe replace this with an option to restart level?
           }
        });
        
        sc.addSpriteSpriteCollisionListener(CircleEnemy.class, Soldier.class, new SpriteSpriteCollisionListener<CircleEnemy, Soldier>() {
            @Override
            public void collision(CircleEnemy ce, Soldier soldier) {
                ce.setActive(false);
                soldier.setActive(false);
                JOptionPane.showMessageDialog(bf.getContentPane(), "You died. Sad moment.");
                System.exit(0); //maybe replace this with an option to restart level?
                //remember to reset current tasks every time someone dies or loads a level
           }
        });
        
        sc.addSpriteSpriteCollisionListener(PinkTriangleEnemy.class, Soldier.class, new SpriteSpriteCollisionListener<PinkTriangleEnemy, Soldier>() {
            @Override
            public void collision(PinkTriangleEnemy pte, Soldier soldier) {
                pte.setActive(false);
                soldier.setActive(false);
                JOptionPane.showMessageDialog(bf.getContentPane(), "You died. Sad moment.");
                System.exit(0); //maybe replace this with an option to restart level?
                //remember to reset current tasks every time someone dies or loads a level
           }
        });
        
        
        
        //how to load each level after reaching the gate
        sc.addSpriteSpriteCollisionListener(Soldier.class, Goal.class, new SpriteSpriteCollisionListener<Soldier, Goal>() {
                    @Override
                    public void collision(Soldier soldier, Goal goal) {
                        
                        
                        if(currentLevel == 1) {
                            currentTask.setFinished();
                            loadLevelTwo();
                            System.out.println("starting level two");
                        } else 
                        if(currentLevel == 2) {
                            currentTask.setFinished();
                            loadLevelThree();
                            System.out.println("Starting level three");
                        } else
                        if(currentLevel == 3) {
                            currentTask.setFinished();
                            loadLevelFour();
                            System.out.println("Starting level four");
                        } else
                        if(currentLevel == 4) {
                            currentTask.setFinished();
                            loadLevelFive();
                            System.out.println("starting level five");
                             
                        } else
                        if(currentLevel == 5) {
                            currentTask.setFinished();
                            loadLevelSix();
                            System.out.println("starting level six");
                        } else
                        if(currentLevel == 6) {
                            currentTask.setFinished();
                            loadLevelSeven();
                            System.out.println("starting level seven");
                        } else
                        if(currentLevel == 7) {
                            currentTask.setFinished();
                            loadLevelEight();
                            System.out.println("loading level eight");
                        } else
                        if(currentLevel == 8) {
                            currentTask.setFinished();
                            loadLevelNine();
                            System.out.println("loading level nine");
                        } else
                        if(currentLevel == 9) {
                            currentTask.setFinished();
                            loadLevelTen();
                            System.out.println("loaidng level ten");
                        } else
                        if(currentLevel == 10) {
                            currentTask.setFinished();
                            loadLevelEleven();
                            System.out.println("loading level eleven");
                        } else
                        if (currentLevel == 11) {
                            currentTask.setFinished();
                            
                            loadLevelTwelve();
                            System.out.println("loading level twelve");
                        } else
                        if (currentLevel == 12) {
                            currentTask.setFinished();
                            loadLevelThirteen();
                            System.out.println("loading level thirteen");
                        }else
                        if (currentLevel == 13) {
                            currentTask.setFinished();
                            loadLevelFourteen();
                            System.out.println("loading level fourteen");
                        }else
                        if (currentLevel == 14) {
                            currentTask.setFinished();
                            loadLevelFifteen();
                            System.out.println("loading level fifteen");
                        }
                        if (currentLevel == 15) {
                            currentTask.setFinished();
                            soldier.setActive(false);
                            JOptionPane.showMessageDialog(bf.getContentPane(), "You Win!");
                            System.exit(0);
                        }
                        
                    } //remember to add a new loader after creating each level!
                });
        
        
        
        
        //start out the game
        loadLevelOne();
        
    }
    
    //levels
    
    public void loadLevelOne() {
        //this level is unique because the clock starts with this one.
        currentLevel = 1;
        sprites.add(new Wall(400, 0, 402, 300, sc));//0
        sprites.add(new Wall(400, 300, 600, 302, sc));//1
        sprites.add(new SquareEnemy(600, 150, sc));//2
        sprites.add(new CircleEnemy(320, 300, sc));//3
        sprites.add(new TriangleEnemy(600, 200, sc));//4
        Clock.start(10);
        Clock.addTask(sc.moveSprites());
        //every level has a set of tasks for the enemies
        currentTask = new Task() {
            public void run() {
                TriangleEnemy triangleOne = (TriangleEnemy) sprites.get(4);
                SquareEnemy squareOne = (SquareEnemy) sprites.get(2);
                if(iteration() % 25 == 24) {
                    triangleOne.adjust(soldier);
                    
                }
                if(iteration() % 500 == 499) {
                   if(squareOne.isActive()) {
                       squareOne.shoot(soldier, sc);
                   }
                }
                
            }
        };
        Clock.addTask(currentTask);
        //current task is a class variable
    }
    
    public void loadLevelTwo() {
        //stuff for every loading level except one
        
        soldier.moveBack();
        int originalSize = sprites.size();
        for(int i = 0; i < originalSize; i++) {
            sprites.get(0).setActive(false);
            sprites.remove(0); //testing
        }
        sprites = new ArrayList<Sprite>();
        currentLevel = 2;
        //specific sprites, walls
        sprites.add(new Wall(200, 0, 202, 100, sc));//0
        sprites.add(new Wall(200, 130, 202, 300, sc));//1
        sprites.add(new Wall(200, 330, 202, 450, sc));//2
        sprites.add(new Wall(200, 480, 202, 600, sc));//3
        //enemies
        sprites.add(new SquareEnemy(700, 100, sc));//4
        sprites.add(new SquareEnemy(700, 200, sc));//5
        sprites.add(new SquareEnemy(700, 300, sc));//6
        sprites.add(new SquareEnemy(700, 400, sc));//7
        sprites.add(new SquareEnemy(700, 500, sc));//8
        sprites.add(new SquareEnemy(700, 550, sc));//9
        sprites.add(new SquareEnemy(700, 50, sc));//10
        currentTask = new Task() {
            public void run() {
                
                                
                if(iteration() % 500 == 499) {
                   for(int i = 4; i < 11;i++) {
                        SquareEnemy square = (SquareEnemy) sprites.get(i);
                        if(square.isActive()) {
                            square.shoot(soldier,sc);
                        }
                    }
                }
                
            }
        };
        Clock.addTask(currentTask);
    }
    
    public void loadLevelThree() {
        //stuff for every loading level except one
        soldier.moveBack();
        int originalSize = sprites.size();
        for(int i = 0; i < originalSize; i++) {
            sprites.get(0).setActive(false);
            sprites.remove(0);
        }
        sprites = new ArrayList<Sprite>();
        currentLevel = 3;
        //Specific sprites, walls
        sprites.add(new Wall(150, 0, 152, 500, sc));//0
        sprites.add(new Wall(300, 100, 302, 600, sc));//1
        sprites.add(new Wall(450, 0, 452, 500, sc));//2
        sprites.add(new Wall(600, 100, 602, 600, sc));//3
        //enemies
        sprites.add(new SquareEnemy(225, 50, sc));//4
        sprites.add(new SquareEnemy(375, 550, sc));//5
        sprites.add(new SquareEnemy(525, 50, sc));//6
        sprites.add(new TriangleEnemy(775, 300, sc));//7
        
        currentTask = new Task() {
            public void run() {
                SquareEnemy square1 = (SquareEnemy) sprites.get(4);
                SquareEnemy square2 = (SquareEnemy) sprites.get(5);
                SquareEnemy square3 = (SquareEnemy) sprites.get(6);
                TriangleEnemy triangle1 = (TriangleEnemy) sprites.get(7);
                if(iteration() % 500 == 499) {
                   if(square1.isActive()) {
                       square1.shoot(soldier, sc);
                   }
                   if(square2.isActive()) {
                       square2.shoot(soldier, sc);
                   }
                   if(square3.isActive()) {
                       square3.shoot(soldier,sc);
                   }
                   
                    
                }
                if(iteration() % 25 == 24) {
                    triangle1.adjust(soldier);
                }
               
            }
        };
        Clock.addTask(currentTask);
        
    }
    
    public void loadLevelFour() {
        //stuff for every loading level except one
        soldier.moveBack();
        int originalSize = sprites.size();
        for(int i = 0; i < originalSize; i++) {
            sprites.get(0).setActive(false);
            sprites.remove(0);
        }
        sprites = new ArrayList<Sprite>();
        currentLevel = 4;
        //specific walls
        sprites.add(new Wall(100, 150, 102, 450, sc));//0
        sprites.add(new Wall(600, 150, 602, 450, sc));//1
        //enemies
        sprites.add(new TriangleEnemy(610, 200, sc));//2
        sprites.add(new TriangleEnemy(610, 400, sc));//3
        sprites.add(new SquareEnemy(620, 290, sc));//4
        sprites.add(new SquareEnemy(620, 310, sc));//5
        //tasks
        currentTask = new Task() {
            public void run() {
                SquareEnemy square1 = (SquareEnemy) sprites.get(4);
                SquareEnemy square2 = (SquareEnemy) sprites.get(5);
                TriangleEnemy triangle1 = (TriangleEnemy) sprites.get(2);
                TriangleEnemy triangle2 = (TriangleEnemy) sprites.get(3);
                if(iteration() % 500 == 499) {
                   if(square1.isActive()) {
                       square1.shoot(soldier, sc);
                   }
                   if(square2.isActive()) {
                       square2.shoot(soldier, sc);
                   }
                   
                   
                    
                }
                if(iteration() % 25 == 24) {
                    triangle1.adjust(soldier);
                    triangle2.adjust(soldier);
                }
            }
        };
        Clock.addTask(currentTask);
    }
    
    public void loadLevelFive() {
        //stuff for every loading level except one
        soldier.moveBack();
        int originalSize = sprites.size();
        for(int i = 0; i < originalSize; i++) {
            sprites.get(0).setActive(false);
            sprites.remove(0);
        }
        sprites = new ArrayList<Sprite>();
        currentLevel = 5;
        //specific walls
        
        //enemies
        sprites.add(new SquareEnemy(700, 300, sc));//0
        sprites.add(new TriangleEnemy(700, 100, sc));//1
        sprites.add(new TriangleEnemy(700, 500, sc));//2
        //this whole lot of circle enemies form the barriers that restrict
        //and challenge the soldier. the triangle enemies should move through 
        //them and the square enemies will shoot through them.
        //"They're in the trees!"
        for(int j = 100; j < 501; j +=50) {
        for(int i = 0; i < 601; i+=20) {
            sprites.add(new CircleEnemy(j, i, sc));
        }
    }
        //active sprites
        currentTask = new Task() {
            public void run() {
                SquareEnemy square1 = (SquareEnemy) sprites.get(0);
               
                TriangleEnemy triangle1 = (TriangleEnemy) sprites.get(1);
                TriangleEnemy triangle2 = (TriangleEnemy) sprites.get(2);
                if(iteration() % 500 == 499) {
                   if(square1.isActive()) {
                       square1.shoot(soldier, sc);
                   }
                }
                if(iteration() % 25 == 24) {
                    triangle1.adjust(soldier);
                    triangle2.adjust(soldier);
                }
            }
        };
        Clock.addTask(currentTask);
    }
    
    public void loadLevelSix() {
        //stuff for every loading level except one
        soldier.moveBack();
        int originalSize = sprites.size();
        for(int i = 0; i < originalSize; i++) {
            sprites.get(0).setActive(false);
            sprites.remove(0);
        }
        sprites = new ArrayList<Sprite>();
        currentLevel = 6;
        //enemies
        for(int i = 600; i < 641; i+=20) {
            for(int j = 0; j < 101; j+= 20) {
                sprites.add(new TriangleEnemy(i, j, sc));
            }
        }
        
        
        sprites.add(new CircleEnemy(200, 400, sc));
        sprites.add(new CircleEnemy(200, 420, sc));
        sprites.add(new CircleEnemy(200, 440, sc));
        sprites.add(new CircleEnemy(200, 200, sc));
        sprites.add(new CircleEnemy(200, 180, sc));
        sprites.add(new CircleEnemy(200, 160, sc));
        sprites.add(new CircleEnemy(600, 400, sc));
        sprites.add(new CircleEnemy(600, 420, sc));
        sprites.add(new CircleEnemy(600, 440, sc));
        sprites.add(new CircleEnemy(600, 200, sc));
        sprites.add(new CircleEnemy(600, 180, sc));
        sprites.add(new CircleEnemy(600, 160, sc));
        //tasks
        currentTask = new Task() {
            public void run() {
                
             
                
                if(iteration() % 25 == 24) {
                    
                    for(int i=0;i<18;i++) {
                        TriangleEnemy tri = (TriangleEnemy)sprites.get(i);
                        tri.adjust(soldier);
                    }
                    
                    
                }
            }
        };
        Clock.addTask(currentTask);
    }
    
    public void loadLevelSeven() {
        //stuff for every loading level except one
        soldier.moveBack();
        int originalSize = sprites.size();
        for(int i = 0; i < originalSize; i++) {
            sprites.get(0).setActive(false);
            sprites.remove(0);
        }
        sprites = new ArrayList<Sprite>();
        currentLevel = 7;
        //sprites
        sprites.add(new Wall(200, 150, 202, 450,sc));//0
        sprites.add(new Wall(490, 40, 492, 60, sc));//1
        sprites.add(new Wall(490, 120, 492, 140, sc));//2
        sprites.add(new Wall(490, 200, 492, 220, sc));//3
        sprites.add(new Wall(490, 280, 492, 300, sc));//4
        sprites.add(new Wall(490, 360, 492, 380, sc));//5
        sprites.add(new Wall(490, 440, 492, 460, sc));//6
        sprites.add(new Wall(490, 520, 492, 540, sc));//7
        //enemies in in particular
        //complex formation of enemies this time
        sprites.add(new CircleEnemy(500,20, sc));//8
        sprites.add(new SquareEnemy(500,40, sc));//9
        sprites.add(new CircleEnemy(500,60, sc));//10
       
        sprites.add(new CircleEnemy(500,100, sc));//11
        sprites.add(new SquareEnemy(500,120, sc));//12
        sprites.add(new CircleEnemy(500,140, sc));//13
        
        sprites.add(new CircleEnemy(500,180, sc));//14
        sprites.add(new SquareEnemy(500,200, sc));//15
        sprites.add(new CircleEnemy(500,220, sc));//16
        
        sprites.add(new CircleEnemy(500,260, sc));//17
        sprites.add(new SquareEnemy(500,280, sc));//18
        sprites.add(new CircleEnemy(500,300, sc));//19
        
        sprites.add(new CircleEnemy(500,340, sc));//20
        sprites.add(new SquareEnemy(500,360, sc));//21
        sprites.add(new CircleEnemy(500,380, sc));//22
        
        sprites.add(new CircleEnemy(500,420, sc));//23
        sprites.add(new SquareEnemy(500,440, sc));//24
        sprites.add(new CircleEnemy(500,460, sc));//25
        
        sprites.add(new CircleEnemy(500,500, sc));//26
        sprites.add(new SquareEnemy(500,520, sc));//27
        sprites.add(new CircleEnemy(500,540, sc));//28
        //active tasks
        currentTask = new Task() {
            public void run() {
                SquareEnemy square1 = (SquareEnemy) sprites.get(9);
                SquareEnemy square2 = (SquareEnemy) sprites.get(12);
                SquareEnemy square3 = (SquareEnemy) sprites.get(15);
                SquareEnemy square4 = (SquareEnemy) sprites.get(18);
                SquareEnemy square5 = (SquareEnemy) sprites.get(21);
                SquareEnemy square6 = (SquareEnemy) sprites.get(24);
                SquareEnemy square7 = (SquareEnemy) sprites.get(27);
                if(iteration() % 500 == 499) {
                   if(square1.isActive()) {
                       square1.shoot(soldier, sc);
                       
                   }
                   if(square2.isActive()) {
                       square2.shoot(soldier, sc);
                       
                   }
                   if(square3.isActive()) {
                       square3.shoot(soldier, sc);
                       
                   }
                   if(square4.isActive()) {
                       square4.shoot(soldier, sc);
                       
                   }
                   if(square5.isActive()) {
                       square5.shoot(soldier, sc);
                       
                   }
                   if(square6.isActive()) {
                       square6.shoot(soldier, sc);
                       
                   }
                   if(square7.isActive()) {
                       square7.shoot(soldier, sc);
                       
                   }
                }
                
            }
        };
        Clock.addTask(currentTask);
        
    }
    
    public void loadLevelEight() {
        //stuff for every loading level except one
        soldier.moveBack();
        int originalSize = sprites.size();
        for(int i = 0; i < originalSize; i++) {
            sprites.get(0).setActive(false);
            sprites.remove(0);
        }
        sprites = new ArrayList<Sprite>();
        currentLevel = 8;
        //sprites
        sprites.add(new SquareEnemy(200, 10, sc));//0
        sprites.add(new SquareEnemy(260, 10, sc));//1
        sprites.add(new SquareEnemy(320, 10, sc));//2
        sprites.add(new SquareEnemy(380, 10, sc));//3
        sprites.add(new SquareEnemy(440, 10, sc));//4
        
        sprites.add(new SquareEnemy(200, 590, sc));//5
        sprites.add(new SquareEnemy(260, 590, sc));//6
        sprites.add(new SquareEnemy(320, 590, sc));//7
        sprites.add(new SquareEnemy(380, 590, sc));//8
        sprites.add(new SquareEnemy(440, 590, sc));//9
        
        sprites.add(new Wall(0, 280, 300, 282, sc));
        sprites.add(new Wall(0, 320, 300, 322, sc));
        
        sprites.add(new Wall(500, 280, 800, 282, sc));
        sprites.add(new Wall(500, 320, 800, 322, sc));

        //active tasks
        currentTask = new Task() {
            public void run() {
                SquareEnemy square1 = (SquareEnemy) sprites.get(0);
                SquareEnemy square2 = (SquareEnemy) sprites.get(1);
                SquareEnemy square3 = (SquareEnemy) sprites.get(2);
                SquareEnemy square4 = (SquareEnemy) sprites.get(3);
                SquareEnemy square5 = (SquareEnemy) sprites.get(4);
                SquareEnemy square6 = (SquareEnemy) sprites.get(5);
                SquareEnemy square7 = (SquareEnemy) sprites.get(6);
                SquareEnemy square8 = (SquareEnemy) sprites.get(7);
                SquareEnemy square9 = (SquareEnemy) sprites.get(8);
                SquareEnemy square10 = (SquareEnemy) sprites.get(9);
                if(iteration() % 500 == 499) {
                   if(square1.isActive()) {
                       square1.shoot(soldier, sc);                      
                   }
                   if(square2.isActive()) {
                       square2.shoot(soldier, sc);                       
                   }
                   if(square3.isActive()) {
                       square3.shoot(soldier, sc);                      
                   }
                   if(square4.isActive()) {
                       square4.shoot(soldier, sc);                       
                   }
                   if(square5.isActive()) {
                       square5.shoot(soldier, sc);                       
                   }
                   if(square6.isActive()) {
                       square6.shoot(soldier, sc);                      
                   }
                   if(square7.isActive()) {
                       square7.shoot(soldier, sc);                      
                   }
                   if(square8.isActive()) {
                       square8.shoot(soldier, sc);                      
                   }
                   if(square9.isActive()) {
                       square9.shoot(soldier, sc);                      
                   }
                   if(square10.isActive()) {
                       square10.shoot(soldier, sc);                      
                   }

                }
                
            }
        };
        Clock.addTask(currentTask);
    }
    
    public void loadLevelNine() {
        //stuff for every loading level except one
        soldier.moveBack();
        int originalSize = sprites.size();
        for(int i = 0; i < originalSize; i++) {
            sprites.get(0).setActive(false);
            sprites.remove(0);
        }
        sprites = new ArrayList<Sprite>();
        currentLevel = 9;
        //sprites
        sprites.add(new Wall(200, 150, 202, 450, sc));//0
        sprites.add(new Wall(600, 150, 602, 450, sc));//1
        sprites.add(new Wall(200, 150, 350, 152,sc));//2
        sprites.add(new Wall(200, 450, 350, 452,sc));//3
        sprites.add(new Wall(450, 150, 600, 152, sc));//4
        sprites.add(new Wall(450, 450, 600, 452, sc));//5
        
        for(int i = 160; i < 441; i+=20) {
            sprites.add(new TriangleEnemy(400, i, sc));
        }
        for(int i = 180; i < 421; i += 20) {
            sprites.add(new TriangleEnemy(700, i, sc));
        }
        
        
        //tasks
        currentTask = new Task() {
            public void run() {
                
               
               
                
                if(iteration() % 25 == 24) {
                    
                    for(int i=6;i<34;i++) {
                        TriangleEnemy tri = (TriangleEnemy)sprites.get(i);
                        tri.adjust(soldier);
                    }
                    
                    
                }
            }
        };
        Clock.addTask(currentTask);
        
    }
    
    public void loadLevelTen() {
        //stuff for every loading level except one
        soldier.moveBack();
        int originalSize = sprites.size();
        for(int i = 0; i < originalSize; i++) {
            sprites.get(0).setActive(false);
            sprites.remove(0);
        }
        sprites = new ArrayList<Sprite>();
        currentLevel = 10;
        //sprites
        sprites.add(new SquareEnemy(620, 160, sc));//0
        sprites.add(new SquareEnemy(620, 220, sc));//1
        sprites.add(new SquareEnemy(620, 280, sc));//2
        sprites.add(new SquareEnemy(620, 340, sc));//3
        sprites.add(new SquareEnemy(620, 400, sc));//4
        sprites.add(new SquareEnemy(620, 460, sc));//5
        
        for(int i = 160; i < 461; i+= 20) {
            sprites.add(new CircleEnemy(600, i, sc));
        }
        
        
        sprites.add(new TriangleEnemy(600, 0, sc));//22
        sprites.add(new TriangleEnemy(600, 20, sc));//23
        sprites.add(new TriangleEnemy(600, 40, sc));//24
        sprites.add(new TriangleEnemy(600, 60, sc));//25
        
        sprites.add(new TriangleEnemy(600, 600, sc));//26
        sprites.add(new TriangleEnemy(600, 580, sc));//27
        sprites.add(new TriangleEnemy(600, 560, sc));//28
        sprites.add(new TriangleEnemy(600, 540, sc));//29
        
        //active sprites
        currentTask = new Task() {
            public void run() {
                SquareEnemy square1 = (SquareEnemy) sprites.get(0);
                SquareEnemy square2 = (SquareEnemy) sprites.get(1);
                SquareEnemy square3 = (SquareEnemy) sprites.get(2);
                SquareEnemy square4 = (SquareEnemy) sprites.get(3);
                SquareEnemy square5 = (SquareEnemy) sprites.get(4);
                SquareEnemy square6 = (SquareEnemy) sprites.get(5);
               
                
                
                if(iteration() % 500 == 499) {
                   if(square1.isActive()) {
                       square1.shoot(soldier, sc);
                   }
                   if(square2.isActive()) {
                       square2.shoot(soldier, sc);
                   }
                   if(square3.isActive()) {
                       square3.shoot(soldier, sc);
                   }
                   if(square4.isActive()) {
                       square4.shoot(soldier, sc);
                   }
                   if(square5.isActive()) {
                       square5.shoot(soldier, sc);
                   }
                   if(square6.isActive()) {
                       square6.shoot(soldier, sc);
                   }
                   
                   
                    
                }
                if(iteration() % 25 == 24) {
                    
                    for(int i=22;i<30;i++) {
                        TriangleEnemy tri = (TriangleEnemy)sprites.get(i);
                        tri.adjust(soldier);
                    }
                    
                    
                }
            }
        };
        Clock.addTask(currentTask);
    }
    
    public void loadLevelEleven() {
        //stuff for every loading level except one
        soldier.moveBack();
        int originalSize = sprites.size();
        for(int i = 0; i < originalSize; i++) {
            sprites.get(0).setActive(false);
            sprites.remove(0);
        }
        sprites = new ArrayList<Sprite>();
        currentLevel = 11;
        //sprites
        
        for(int i = 50; i < 91; i+=20) {
            for(int j = 0; j < 101; j+= 20) {
                sprites.add(new TriangleEnemy(i, j, sc));
            }
        }
        for(int i = 50; i < 91; i+=20) {
            for(int j = 500; j < 601; j+= 20) {
                sprites.add(new TriangleEnemy(i, j, sc));
            }
        }
        
        for(int i = 0; i < 601; i+=20) {
            sprites.add(new CircleEnemy(200, i, sc));
        }
        for(int i = 0; i < 601; i+=20) {
            sprites.add(new CircleEnemy(220, i, sc));
        }
        for(int i = 0; i < 601; i+=20) {
            sprites.add(new CircleEnemy(240, i, sc));
        }
        currentTask = new Task() {
            public void run() {
                //List<TriangleEnemy> triEnemies = new ArrayList();
                //for(int i=0;i<sprites.size();i++)
                    //triEnemies.add((TriangleEnemy)sprites.get(i));
               
                
                
                
                
                if(iteration() % 25 == 24) {
                    
                    for(int i=1;i<=36;i++) {
                        TriangleEnemy tri = (TriangleEnemy)sprites.get(i-1);
                        tri.adjust(soldier);
                    }
                    
                    
                }
            }
        };
        Clock.addTask(currentTask);
        
        
        
    }
    
    public void loadLevelTwelve() {
        //stuff for every loading level except one
        soldier.moveBack();
        int originalSize = sprites.size();
        for(int i = 0; i < originalSize; i++) {
            sprites.get(0).setActive(false);
            sprites.remove(0);
        }
        sprites = new ArrayList<Sprite>();
        currentLevel = 12;
        //sprites
        
        sprites.add(new Wall(50, 0, 52, 550, sc));//0
        sprites.add(new Wall(150, 0, 152, 550, sc));//1
        sprites.add(new Wall(250, 0, 252, 550, sc));//2
        sprites.add(new Wall(350, 0, 352, 550, sc));//3
        sprites.add(new Wall(450, 0, 452, 550, sc));//4
        sprites.add(new Wall(550, 0, 552, 550, sc));//5
        
        sprites.add(new Wall(100, 50, 102, 600, sc));//6
        sprites.add(new Wall(200, 50, 202, 600, sc));//7
        sprites.add(new Wall(300, 50, 302, 600, sc));//8
        sprites.add(new Wall(400, 50, 402, 600, sc));//9
        sprites.add(new Wall(500, 50, 502, 600, sc));//10
        sprites.add(new Wall(600, 50, 602, 600, sc));//11
        
        for(int i = 75; i < 576; i += 100) {
            sprites.add(new SquareEnemy(i, 25, sc));
            sprites.add(new SquareEnemy(i+30, 25, sc));
            sprites.add(new SquareEnemy(i, 575, sc));
            sprites.add(new SquareEnemy(i+30, 575, sc));
        }
        
        
        currentTask = new Task() {
            public void run() {
                if(iteration() % 500 == 499) {                    
                    for(int i = 12; i < 36; i++) {
                        SquareEnemy square = (SquareEnemy) sprites.get(i);
                        if(square.isActive()) square.shoot(soldier,sc);
                    }
                }
            }
        };
        Clock.addTask(currentTask);
        
        
    }
    
    public void loadLevelThirteen() {
        //stuff for every loading level except one
        soldier.moveBack();
        int originalSize = sprites.size();
        for(int i = 0; i < originalSize; i++) {
            sprites.get(0).setActive(false);
            sprites.remove(0);
        }
        sprites = new ArrayList<Sprite>();
        currentLevel = 13;
        //enemies
        for(int i = 600; i < 641; i+=20) {
            for(int j = 0; j < 101; j+= 20) {
                sprites.add(new PinkTriangleEnemy(i, j, sc));
            }
        }
        
        
        sprites.add(new CircleEnemy(200, 400, sc));
        sprites.add(new CircleEnemy(200, 420, sc));
        sprites.add(new CircleEnemy(200, 440, sc));
        sprites.add(new CircleEnemy(200, 200, sc));
        sprites.add(new CircleEnemy(200, 180, sc));
        sprites.add(new CircleEnemy(200, 160, sc));
        sprites.add(new CircleEnemy(600, 400, sc));
        sprites.add(new CircleEnemy(600, 420, sc));
        sprites.add(new CircleEnemy(600, 440, sc));
        sprites.add(new CircleEnemy(600, 200, sc));
        sprites.add(new CircleEnemy(600, 180, sc));
        sprites.add(new CircleEnemy(600, 160, sc));
        //tasks
        currentTask = new Task() {
            public void run() {
                if(iteration() % 25 == 24) {
                    
                    for(int i=0;i<18;i++) {
                        PinkTriangleEnemy tri = (PinkTriangleEnemy)sprites.get(i);
                        tri.adjust(soldier);
                    }
                    
                    
                }
            }
        };
        Clock.addTask(currentTask);
    }
    
    public void loadLevelFourteen() {
            //stuff for every loading level except one
        soldier.moveBack();
        int originalSize = sprites.size();
        for(int i = 0; i < originalSize; i++) {
            sprites.get(0).setActive(false);
            sprites.remove(0);
        }
        sprites = new ArrayList<Sprite>();
        currentLevel = 14;
        //sprites
        
        sprites.add(new Wall(200, 150, 202, 450,sc));//0
        sprites.add(new Wall(490, 40, 492, 60, sc));//1
        sprites.add(new Wall(490, 120, 492, 140, sc));//2
        sprites.add(new Wall(490, 200, 492, 220, sc));//3
        sprites.add(new Wall(490, 280, 492, 300, sc));//4
        sprites.add(new Wall(490, 360, 492, 380, sc));//5
        sprites.add(new Wall(490, 440, 492, 460, sc));//6
        sprites.add(new Wall(490, 520, 492, 540, sc));//7
        //enemies in in particular
        //complex formation of enemies this time
        sprites.add(new CircleEnemy(500,20, sc));//8
        sprites.add(new OrangeSquareEnemy(500,40, sc));//9
        sprites.add(new CircleEnemy(500,60, sc));//10
       
        sprites.add(new CircleEnemy(500,100, sc));//11
        sprites.add(new OrangeSquareEnemy(500,120, sc));//12
        sprites.add(new CircleEnemy(500,140, sc));//13
        
        sprites.add(new CircleEnemy(500,180, sc));//14
        sprites.add(new OrangeSquareEnemy(500,200, sc));//15
        sprites.add(new CircleEnemy(500,220, sc));//16
        
        sprites.add(new CircleEnemy(500,260, sc));//17
        sprites.add(new OrangeSquareEnemy(500,280, sc));//18
        sprites.add(new CircleEnemy(500,300, sc));//19
        
        sprites.add(new CircleEnemy(500,340, sc));//20
        sprites.add(new OrangeSquareEnemy(500,360, sc));//21
        sprites.add(new CircleEnemy(500,380, sc));//22
        
        sprites.add(new CircleEnemy(500,420, sc));//23
        sprites.add(new OrangeSquareEnemy(500,440, sc));//24
        sprites.add(new CircleEnemy(500,460, sc));//25
        
        sprites.add(new CircleEnemy(500,500, sc));//26
        sprites.add(new OrangeSquareEnemy(500,520, sc));//27
        sprites.add(new CircleEnemy(500,540, sc));//28
        
        sprites.add(new Wall(0, 150, 180, 152, sc));
        sprites.add(new Wall(0, 450, 180, 452, sc));
        //active tasks
        currentTask = new Task() {
            public void run() {
                OrangeSquareEnemy square1 = (OrangeSquareEnemy) sprites.get(9);
                OrangeSquareEnemy square2 = (OrangeSquareEnemy) sprites.get(12);
                OrangeSquareEnemy square3 = (OrangeSquareEnemy) sprites.get(15);
                OrangeSquareEnemy square4 = (OrangeSquareEnemy) sprites.get(18);
                OrangeSquareEnemy square5 = (OrangeSquareEnemy) sprites.get(21);
                OrangeSquareEnemy square6 = (OrangeSquareEnemy) sprites.get(24);
                OrangeSquareEnemy square7 = (OrangeSquareEnemy) sprites.get(27);
                if(iteration() % 500 == 499) {
                   if(square1.isActive()) {
                       square1.shoot(soldier, sc);
                       
                   }
                   if(square2.isActive()) {
                       square2.shoot(soldier, sc);
                       
                   }
                   if(square3.isActive()) {
                       square3.shoot(soldier, sc);
                       
                   }
                   if(square4.isActive()) {
                       square4.shoot(soldier, sc);
                       
                   }
                   if(square5.isActive()) {
                       square5.shoot(soldier, sc);
                       
                   }
                   if(square6.isActive()) {
                       square6.shoot(soldier, sc);
                       
                   }
                   if(square7.isActive()) {
                       square7.shoot(soldier, sc);
                       
                   }
                }
                
            }
        };
        Clock.addTask(currentTask);
        
    }
    
    public void loadLevelFifteen() {
        //stuff for every loading level except one
        soldier.moveBack();
        int originalSize = sprites.size();
        for(int i = 0; i < originalSize; i++) {
            sprites.get(0).setActive(false);
            sprites.remove(0);
        }
        sprites = new ArrayList<Sprite>();
        currentLevel = 15;
        //sprites
        
        sprites.add(new GrayCircleEnemy(10, 350, sc));
        sprites.add(new GrayCircleEnemy(30, 350, sc));
        sprites.add(new GrayCircleEnemy(50, 350, sc));
        sprites.add(new GrayCircleEnemy(70, 350, sc));
        sprites.add(new GrayCircleEnemy(90, 350, sc));
        sprites.add(new GrayCircleEnemy(110, 350, sc));
        sprites.add(new GrayCircleEnemy(130, 350, sc));
        sprites.add(new GrayCircleEnemy(150, 350, sc));
        sprites.add(new GrayCircleEnemy(170, 350, sc));
        sprites.add(new GrayCircleEnemy(190, 350, sc));
        sprites.add(new GrayCircleEnemy(210, 350, sc));
        sprites.add(new GrayCircleEnemy(230, 350, sc));
        sprites.add(new GrayCircleEnemy(250, 350, sc));
        sprites.add(new GrayCircleEnemy(270, 350, sc));
        sprites.add(new GrayCircleEnemy(290, 350, sc));
        sprites.add(new GrayCircleEnemy(310, 350, sc));
        sprites.add(new GrayCircleEnemy(330, 350, sc));
        sprites.add(new GrayCircleEnemy(350, 350, sc));
        sprites.add(new GrayCircleEnemy(370, 350, sc));//
        sprites.add(new GrayCircleEnemy(10, 250, sc));
        sprites.add(new GrayCircleEnemy(30, 250, sc));
        sprites.add(new GrayCircleEnemy(50, 250, sc));
        sprites.add(new GrayCircleEnemy(70, 250, sc));
        sprites.add(new GrayCircleEnemy(90, 250, sc));
        sprites.add(new GrayCircleEnemy(110, 250, sc));
        sprites.add(new GrayCircleEnemy(130, 250, sc));
        sprites.add(new GrayCircleEnemy(150, 250, sc));
        sprites.add(new GrayCircleEnemy(170, 250, sc));
        sprites.add(new GrayCircleEnemy(190, 250, sc));
        sprites.add(new GrayCircleEnemy(210, 250, sc));
        sprites.add(new GrayCircleEnemy(230, 250, sc));
        sprites.add(new GrayCircleEnemy(250, 250, sc));
        sprites.add(new GrayCircleEnemy(270, 250, sc));
        
        sprites.add(new GrayCircleEnemy(270, 230, sc));
        sprites.add(new GrayCircleEnemy(270, 210, sc));
        sprites.add(new GrayCircleEnemy(270, 190, sc));
        sprites.add(new GrayCircleEnemy(270, 170, sc));
        sprites.add(new GrayCircleEnemy(270, 150, sc));
        sprites.add(new GrayCircleEnemy(270, 130, sc));
        sprites.add(new GrayCircleEnemy(270, 110, sc));
        sprites.add(new GrayCircleEnemy(270, 90, sc));
        sprites.add(new GrayCircleEnemy(270, 70, sc));
        sprites.add(new GrayCircleEnemy(270, 50, sc));
        sprites.add(new GrayCircleEnemy(270, 30, sc));
        sprites.add(new GrayCircleEnemy(270, 10, sc));
        
        sprites.add(new GrayCircleEnemy(270, 10, sc));
        sprites.add(new GrayCircleEnemy(290, 10, sc));
        sprites.add(new GrayCircleEnemy(310, 10, sc));
        sprites.add(new GrayCircleEnemy(330, 10, sc));
        sprites.add(new GrayCircleEnemy(350, 10, sc));
        sprites.add(new GrayCircleEnemy(370, 10, sc));
        sprites.add(new GrayCircleEnemy(390, 10, sc));
        sprites.add(new GrayCircleEnemy(410, 10, sc));
        sprites.add(new GrayCircleEnemy(430, 10, sc));
        sprites.add(new GrayCircleEnemy(450, 10, sc));
        sprites.add(new GrayCircleEnemy(470, 10, sc));
        sprites.add(new GrayCircleEnemy(490, 10, sc));
        sprites.add(new GrayCircleEnemy(510, 10, sc));
        sprites.add(new GrayCircleEnemy(530, 10, sc));
        sprites.add(new GrayCircleEnemy(550, 10, sc));
        sprites.add(new GrayCircleEnemy(570, 10, sc));
        sprites.add(new GrayCircleEnemy(590, 10, sc));
        sprites.add(new GrayCircleEnemy(610, 10, sc));
        sprites.add(new GrayCircleEnemy(630, 10, sc));
        sprites.add(new GrayCircleEnemy(650, 10, sc));
        sprites.add(new GrayCircleEnemy(670, 10, sc));
        sprites.add(new GrayCircleEnemy(690, 10, sc));
        sprites.add(new GrayCircleEnemy(710, 10, sc));
        sprites.add(new GrayCircleEnemy(730, 10, sc));
        sprites.add(new GrayCircleEnemy(750, 10, sc));
        sprites.add(new GrayCircleEnemy(770, 10, sc));
        sprites.add(new GrayCircleEnemy(790, 10, sc));
        
        
        
        
        sprites.add(new GrayCircleEnemy(400, 300, sc));
        sprites.add(new GrayCircleEnemy(400, 320, sc));
        sprites.add(new GrayCircleEnemy(400, 280, sc));
        sprites.add(new GrayCircleEnemy(400, 260, sc));
        sprites.add(new GrayCircleEnemy(400, 240, sc));
        sprites.add(new GrayCircleEnemy(400, 220, sc));
        sprites.add(new GrayCircleEnemy(400, 200, sc));
        sprites.add(new GrayCircleEnemy(400, 180, sc));
        sprites.add(new GrayCircleEnemy(400, 160, sc));
        sprites.add(new GrayCircleEnemy(400, 140, sc));
        sprites.add(new GrayCircleEnemy(400, 120, sc));
        sprites.add(new GrayCircleEnemy(400, 100, sc));
        
        sprites.add(new GrayCircleEnemy(400, 100, sc));
        sprites.add(new GrayCircleEnemy(420, 100, sc));
        sprites.add(new GrayCircleEnemy(460, 100, sc));
        sprites.add(new GrayCircleEnemy(480, 100, sc));
        sprites.add(new GrayCircleEnemy(500, 100, sc));
        sprites.add(new GrayCircleEnemy(520, 100, sc));
        sprites.add(new GrayCircleEnemy(540, 100, sc));
        sprites.add(new GrayCircleEnemy(560, 100, sc));
        sprites.add(new GrayCircleEnemy(580, 100, sc));
        sprites.add(new GrayCircleEnemy(600, 100, sc));
        sprites.add(new GrayCircleEnemy(620, 100, sc));
        sprites.add(new GrayCircleEnemy(640, 100, sc));
        sprites.add(new GrayCircleEnemy(660, 100, sc));
        sprites.add(new GrayCircleEnemy(680, 100, sc));
        
        sprites.add(new GrayCircleEnemy(620, 200, sc));
        sprites.add(new GrayCircleEnemy(640, 200, sc));
        sprites.add(new GrayCircleEnemy(660, 200, sc));
        sprites.add(new GrayCircleEnemy(680, 200, sc));
        sprites.add(new GrayCircleEnemy(700, 200, sc));
        sprites.add(new GrayCircleEnemy(720, 200, sc));
        sprites.add(new GrayCircleEnemy(740, 200, sc));
        sprites.add(new GrayCircleEnemy(760, 200, sc));
        sprites.add(new GrayCircleEnemy(780, 200, sc));
        sprites.add(new GrayCircleEnemy(800, 200, sc));
       
        //active tasks
        currentTask = new Task() {
            public void run() {
                
                if(iteration() % 25 == 24) {
                    for(int i = 0; i < sprites.size(); i++) {
                        GrayCircleEnemy gray1 = (GrayCircleEnemy) sprites.get(i);
                    
                    if(gray1.checkAndExplode(soldier, sc)) {
                        
                        soldier.setActive(false);
                        explode.play();
                        JOptionPane.showMessageDialog(bf.getContentPane(), "You died. Sad moment.");
                        System.exit(0);
                    }
                    }
                }
                
            }
        };
        Clock.addTask(currentTask);
    }
    
    public static void main(String[] args) throws IOException {
        Main m = new Main();
        m.startUp();
    }
}
