package ru.nsu.ccfit.kuryatnikova.gui;

import ru.nsu.ccfit.kuryatnikova.model.MinesweeperTile;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

public class Tile extends JComponent {
    private final MinesweeperTile model;
    private final static Border border = BorderFactory.createLineBorder(Color.WHITE);

    public Tile(MinesweeperTile model) {
        this.model = model;
        setBorder(border);
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(30, 30);
    }

    @Override
    public Dimension getPreferredSize() {
        var dim = super.getPreferredSize();
        var l = Math.min(dim.height, dim.width);
        return new Dimension(l, l);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        var width = getWidth();
        var height = getHeight();

        switch (model.getStatus()) {
            case CLOSE -> {
                g.setColor(Color.DARK_GRAY);
                g.fillRect(0, 0, width, height);
            }
            case FLAGGED -> {
                g.setColor(Color.DARK_GRAY);
                g.fillRect(0, 0, width, height);
                var font = new Font( "Arial", Font.BOLD, width*3/4);
                var text = "⚑";
                g.setColor(Color.YELLOW);
                ((Graphics2D) g).setRenderingHint(
                        RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                centerString(g, width, height, text, font);
            }
            case OPEN -> {
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(0, 0, width, height);
                var minedNeighbourNumber = model.getMinedNeighboursNumber();
                if (minedNeighbourNumber != 0) {
                    g.setColor(textColors[minedNeighbourNumber]);
                    var font = new Font( "Arial", Font.BOLD, width*3/4);
                    var text = Integer.toString(minedNeighbourNumber);
                    ((Graphics2D) g).setRenderingHint(
                            RenderingHints.KEY_TEXT_ANTIALIASING,
                            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    centerString(g, width, height, text, font);
                }
            }
        }
    }
    private static void centerString(Graphics g, int width, int height, String s,
                             Font font) {
        FontRenderContext frc =
                new FontRenderContext(null, true, true);

        Rectangle2D r2D = font.getStringBounds(s, frc);
        int rWidth = (int) Math.round(r2D.getWidth());
        int rHeight = (int) Math.round(r2D.getHeight());
        int rX = (int) Math.round(r2D.getX());
        int rY = (int) Math.round(r2D.getY());

        int a = (width / 2) - (rWidth / 2) - rX;
        int b = (height / 2) - (rHeight / 2) - rY;

        g.setFont(font);
        g.drawString(s, a, b);
    }
    private static final Color[] textColors = new Color[]{Color.LIGHT_GRAY,
            new Color(80, 105, 243),
            new Color(30, 121, 21),
            new Color(219, 0, 0),
            new Color(33, 43, 100),
            new Color(107, 0, 28),
            new Color(44, 137, 150),
            new Color(0,0,0),
            Color.DARK_GRAY};
}
