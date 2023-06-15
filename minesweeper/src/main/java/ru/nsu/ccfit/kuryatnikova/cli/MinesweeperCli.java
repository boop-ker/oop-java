package ru.nsu.ccfit.kuryatnikova.cli;

import ru.nsu.ccfit.kuryatnikova.model.Minesweeper;

import java.util.Scanner;
import java.util.regex.Pattern;

public class MinesweeperCli {
    private final Minesweeper game;
    private final Scanner sc = new Scanner(System.in);

    public MinesweeperCli() {
        int x = Integer.parseInt(sc.nextLine());
        int y = Integer.parseInt(sc.nextLine());
        int mines = Integer.parseInt(sc.nextLine());

        game = new Minesweeper(x, y, mines);
    }

    private void printGame() {
        var tiles = game.getTiles();
        for (var row : tiles) {
            for (var tile : row) {
                System.out.print(switch (tile.getStatus()) {
                    case OPEN -> tile.getMinedNeighboursNumber();
                    case CLOSE -> "*";
                    case FLAGGED -> "F";
                });
            }
            System.out.print("\n");
        }
        System.out.print("\n");
    }


    public void start() {
        var commandPattern = Pattern.compile("(f\\s+)?(\\d+)\\s+(\\d+)\\s*");

        while (game.notFinished()) {
            printGame();
            var line = sc.nextLine();
            var m = commandPattern.matcher(line);
            while (!m.matches()) {
                System.out.println("Try command as \"[f] <x> <y>\"");
                line = sc.nextLine();
                m = commandPattern.matcher(line);
            }
            boolean flagTile = m.group(1) != null;
            int x = Integer.parseInt(m.group(2));
            int y = Integer.parseInt(m.group(3));

            if (flagTile) {
                game.flagTile(x, y);
                continue;
            }

            boolean lose = game.flipTile(x, y);
            if (lose) {
                System.out.println("You lose.\n");
                break;
            }
        }
    }
}
