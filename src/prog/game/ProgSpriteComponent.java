/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prog.game;

import basicgraphics.Sprite;
import basicgraphics.SpriteComponent;
import basicgraphics.images.Picture;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.JButton;
import static prog.game.Game.board;

/**
 *
 * @author sbrandt
 */
public class ProgSpriteComponent extends SpriteComponent {
    
    Picture bground;
    BufferedImage[] board;
    List<Game.Position> positions;
    List<Sprite> tokens;
    JButton viz;

    public ProgSpriteComponent(Picture bground,BufferedImage[] board, 
            List<Game.Position> positions, List<Sprite> tokens,
            JButton viz) {
        this.bground = bground;
        this.board = board;
        this.positions = positions;
        this.tokens = tokens;
        this.viz = viz;
    }

    @Override
    public void paintBackground(Graphics g) {
        Dimension d = getSize();
        g.clearRect(0, 0, d.width, d.height);
        g.drawImage(bground.getImage(), 0, 0, this);
        g.drawImage(board[0], getOffsetX(), getOffsetY(), null);
        g.setFont(new Font(Font.MONOSPACED, Font.BOLD, Game.BOX * 9 / 10));
        int n = 1;
        for (Game.Position pos : positions) {
            Sprite sp = tokens.get(n - 1);
            sp.setY(pos.y * Game.BOX);
            sp.setX(pos.x * Game.BOX);
            sp.is_visible = "On".equals(viz.getText());
            n++;
        }
        while (n <= 6) {
            Sprite sp = tokens.get(n - 1);
            sp.setX(20 * Game.BOX);
            sp.is_visible = false;
            n++;
        }
    }
}
