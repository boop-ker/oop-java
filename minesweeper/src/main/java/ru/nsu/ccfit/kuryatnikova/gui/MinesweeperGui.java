package ru.nsu.ccfit.kuryatnikova.gui;

import ru.nsu.ccfit.kuryatnikova.model.Minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.security.InvalidParameterException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class MinesweeperGui extends JFrame {
    private final JMenuBar menuBar;
    private JPanel minesweeperPanel;

    private Minesweeper game = new Minesweeper(10, 10, 12);
    private Long currentScore;
    private final Timer timer;

    public MinesweeperGui() {
        timer = new Timer(1000, (e) -> {
            if (currentScore != 0)
                currentScore--;
        });
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        var size = new Dimension(800, 600);
        setSize(size);
        setLocationByPlatform(true);
        setLocationRelativeTo(null);
        setTitle("Minesweeper");
        setMinimumSize(size);

        menuBar.setVisible(true);

        addSubMenu("Game", KeyEvent.VK_G);
        addMenuItem("Game/New game", "Start game with new settings",
                KeyEvent.CTRL_DOWN_MASK | KeyEvent.VK_N, this::newGame);
        addMenuItem("Game/High scores", "Show high scores",
                KeyEvent.CTRL_DOWN_MASK | KeyEvent.VK_S, this::highScores);
        addSubMenu("Help", KeyEvent.VK_H);
        addMenuItem("Help/About", "Some info",
                KeyEvent.CTRL_DOWN_MASK | KeyEvent.VK_A, this::onAbout);

        initMinesweeperPanel();
        pack();
        setVisible(true);
    }

    private void initMinesweeperPanel() {
        setLayout(new GridBagLayout());
        var x = game.getX();
        var y = game.getY();
        currentScore = ((long) x *y);
        minesweeperPanel = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                Dimension d = this.getParent().getSize();
                int newSize = Math.min(d.width, d.height);
                newSize = (newSize == 0) ? 100 : newSize;
                return new Dimension((int) (newSize* (float)x /y), newSize );
            }
        };
        add(minesweeperPanel);
        minesweeperPanel.setLayout(new GridLayout(y, x));

        var gui = this;
        for (int j = 0; j < y; j++)
            for (int i = 0; i < x; i++) {
                var modelTile = game.getTile(i, j);
                var tile = new Tile(modelTile);
                tile.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);

                        SwingUtilities.invokeLater(() -> {
                            if (e.getButton() == MouseEvent.BUTTON3)
                                game.flagTile(modelTile);
                            else {
                                var lose = game.flipTile(modelTile);
                                if (lose) {
                                    JOptionPane.showMessageDialog(gui,
                                            "You lose", "It's a mine", JOptionPane.INFORMATION_MESSAGE);
                                    newGame();
                                }
                                if (!game.notFinished()) {
                                    revalidate();
                                    repaint();
                                    win();
                                    newGame();
                                }
                            }

                            revalidate();
                            repaint();
                        });
                    }
                });
                minesweeperPanel.add(tile);
                tile.setVisible(true);
            }
        timer.start();
    }

    private void win() {
        timer.stop();

        var name = JOptionPane.showInputDialog(this,
                "You won. Your score is " + currentScore, "Win", JOptionPane.INFORMATION_MESSAGE);
        SwingUtilities.invokeLater(() -> {
            var scores = game.getScores();
            scores.addScore(name, currentScore);
            try (var objectOutputStream = new ObjectOutputStream(
                    new FileOutputStream("scores")) ) {
                objectOutputStream.writeObject(scores);
            } catch (FileNotFoundException e) {
                System.out.println("Unable to find scores file. Creating new one.");
            } catch (IOException e) {
                System.out.println("Error writing scores file.");
            }
        });
    }

    private void newGame() {
        var parameters = newGameDialog(new String[]{"Width", "Height", "Mines"},
                new SpinnerNumberModel[]{
                        new SpinnerNumberModel(10, 1, 100, 1),
                        new SpinnerNumberModel(10, 1, 100, 1),
                        new SpinnerNumberModel(10, 1, 100, 1),
                });
        parameters.ifPresent(p -> {
            var x = p[0];
            var y = p[1];
            var m = p[2];
            if (x*y < m) {
                JOptionPane.showMessageDialog(this,
                        "Too many mines", "Impossible", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            SwingUtilities.invokeLater(() -> {
                        game = new Minesweeper(x, y, m);
                        remove(minesweeperPanel);
                        initMinesweeperPanel();
                        revalidate();
                        repaint();
                    });
        });
    }


    private Optional<int[]> newGameDialog(String[] parameters, SpinnerNumberModel[] models) {
        var parametersCount = parameters.length;
        var dialog = new JDialog(this, "New game", true);
        var layout = new GridLayout(1 + parametersCount, 2, 10, 15);
        var labels = new JLabel[parametersCount];

        var spinners = new JSpinner[parametersCount];
        for (int i = 0; i < parametersCount; i++) {
            labels[i] = new JLabel(parameters[i]);
            spinners[i] = new JSpinner(models[i]);
        }

        JButton okButton = new JButton("OK"),
                cancelButton = new JButton("Cancel");
        okButton.addActionListener(e -> dialog.setVisible(false));
        AtomicReference<Boolean> canceled = new AtomicReference<>(false);
        cancelButton.addActionListener(e -> {
            canceled.set(true);
            dialog.setVisible(false);
        });


        var panel = new JPanel(layout);
        int eb = 10;
        panel.setBorder(BorderFactory.createEmptyBorder(eb, eb, eb, eb));
        for (int i = 0; i < parametersCount; i++) {
            panel.add(labels[i]);
            panel.add(spinners[i]);
        }
        panel.add(okButton);
        panel.add(cancelButton);
        dialog.add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

        if (canceled.get()) return Optional.empty();
        var res = new int[parametersCount];
        for (int i = 0; i < parametersCount; i++)
            res[i] = (int) spinners[i].getValue();
        return Optional.of(res);
    }

    public void addMenuItem(String title, String tooltip, int mnemonic, Runnable actionMethod) throws SecurityException {
        MenuElement element = getParentMenuElement(title);
        if (element == null)
            throw new InvalidParameterException("Menu path not found: " + title);
        JMenuItem item = createMenuItem(getMenuPathName(title), tooltip, mnemonic, actionMethod);
        if (element instanceof JMenu)
            ((JMenu) element).add(item);
        else if (element instanceof JPopupMenu)
            ((JPopupMenu) element).add(item);
        else
            throw new InvalidParameterException("Invalid menu path: " + title);
    }

    public JMenuItem createMenuItem(String title, String tooltip, int mnemonic, Runnable actionMethod) throws SecurityException {
        JMenuItem item = new JMenuItem(title);
        item.setMnemonic(mnemonic);
        item.setToolTipText(tooltip);
        item.addActionListener((e) -> {
            actionMethod.run();
        });
        return item;
    }

    public void addSubMenu(String title, int mnemonic) {
        MenuElement element = getParentMenuElement(title);
        if (element == null)
            throw new InvalidParameterException("Menu path not found: " + title);
        JMenu subMenu = createSubMenu(getMenuPathName(title), mnemonic);
        if (element instanceof JMenuBar)
            ((JMenuBar) element).add(subMenu);
        else if (element instanceof JMenu)
            ((JMenu) element).add(subMenu);
        else if (element instanceof JPopupMenu)
            ((JPopupMenu) element).add(subMenu);
        else
            throw new InvalidParameterException("Invalid menu path: " + title);
    }

    public JMenu createSubMenu(String title, int mnemonic) {
        JMenu menu = new JMenu(title);
        menu.setMnemonic(mnemonic);
        return menu;
    }

    private String getMenuPathName(String menuPath) {
        int pos = menuPath.lastIndexOf('/');
        return (pos > 0) ? menuPath.substring(pos + 1) : menuPath;
    }

    private MenuElement getParentMenuElement(String menuPath) {
        int pos = menuPath.lastIndexOf('/');
        if (pos > 0)
            return getMenuElement(menuPath.substring(0, pos));
        else
            return menuBar;
    }

    public MenuElement getMenuElement(String menuPath) {
        MenuElement element = menuBar;
        for (String pathElement : menuPath.split("/")) {
            MenuElement newElement = null;
            for (MenuElement subElement : element.getSubElements()) {
                if ((subElement instanceof JMenu && ((JMenu) subElement).getText().equals(pathElement))
                        || (subElement instanceof JMenuItem && ((JMenuItem) subElement).getText().equals(pathElement))) {
                    if (subElement.getSubElements().length == 1 && subElement.getSubElements()[0] instanceof JPopupMenu)
                        newElement = subElement.getSubElements()[0];
                    else
                        newElement = subElement;
                    break;
                }
            }
            if (newElement == null) return null;
            element = newElement;
        }
        return element;
    }

    public void onAbout() {
        JOptionPane.showMessageDialog(this,
                "Minesweeper by lizya", "About", JOptionPane.INFORMATION_MESSAGE);
    }

    public void highScores() {
        JOptionPane.showMessageDialog(this, "High scores are:\n"
                + game.getScores().toString(), "High scores", JOptionPane.INFORMATION_MESSAGE);
    }
}
