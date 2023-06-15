package ru.nsu.ccfit.kuryatnikova.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Minesweeper {
    private final List<List<MinesweeperTile>> tiles;
    private final Scores scores;
    private final int x;
    private final int y;
    private final int minesNumber;
    private boolean first = true;

    public Minesweeper(int x, int y, int minesNumber) {
        this.x=x;
        this.y=y;
        this.minesNumber=minesNumber;

        Scores scores1;
        try (var objectInputStream = new ObjectInputStream(
                new FileInputStream("scores")) ) {
            scores1 = (Scores) objectInputStream.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to find scores file. Creating new one.");
            scores1 = new Scores();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error reading scores file. Creating new one.");
            scores1 = new Scores();
        }
        scores = scores1;

        Supplier<List<MinesweeperTile>> rowSupplier = () -> Stream
                .generate(MinesweeperTile::new)
                .limit(x)
                .toList();
        tiles = Stream
                .generate(rowSupplier)
                .limit(y)
                .toList();
    }

    private void firstOpen(MinesweeperTile firstTile) {
        Random rand = new Random(LocalDate.now().toEpochSecond(LocalTime.now(), ZoneOffset.ofHours(0)));
        for (int it = 0; it < minesNumber; ) {
            var i = rand.nextInt(x);
            var j = rand.nextInt(y);

            var tile = getTile(i, j);
            if (tile == firstTile || tile.mined())
                continue;

            tile.setMined(true);
            it++;
        }
        updateTileNeighbours(x, y);
    }

    private void updateTileNeighbours(int x, int y) {
        for (int j = 0; j < y; j++)
            for (int i = 0; i < x; i++) {
                var neighbours = getTile(i, j).getNeighbours();
                if (j!=0) {
                    if ( i!= 0) neighbours.add(getTile(i-1,j-1));
                    neighbours.add(getTile(i,j-1));
                    if ( i!= x-1) neighbours.add(getTile(i+1,j-1));
                }
                if ( i!= 0) neighbours.add(getTile(i-1,j));
                if ( i!= x-1) neighbours.add(getTile(i+1,j));
                if (j!=y-1){
                    if ( i!= 0) neighbours.add(getTile(i-1,j+1));
                    neighbours.add(getTile(i,j+1));
                    if ( i!= x-1) neighbours.add(getTile(i+1,j+1));
                }
            }

        tiles.forEach(rows -> rows.forEach(tile ->
                tile.setMinedNeighboursNumber(
                        (int) tile.getNeighbours().stream()
                                .filter(MinesweeperTile::mined)
                                .count())));
    }

    public MinesweeperTile getTile(int x, int y) {
        return tiles.get(y).get(x);
    }

    public boolean notFinished() {
        return tiles.stream().anyMatch((row) ->
                row.stream().anyMatch(tile ->
                        tile.getStatus() != MinesweeperTile.Status.OPEN
                        && !tile.mined()));
    }

    public boolean flipTile(int x, int y) {
        return flipTile(getTile(x,y));
    }

    public boolean flipTile(MinesweeperTile tile) {
        if (first) {
            firstOpen(tile);
            first = false;
        }
        if (tile.mined())
            return true;

        Queue<MinesweeperTile> neighboursQueue = new ArrayDeque<>();
        neighboursQueue.add(tile);

        while (!neighboursQueue.isEmpty()) {
            var t = neighboursQueue.poll();
            if (t.getMinedNeighboursNumber() == 0) {
                neighboursQueue.addAll(
                        t.getNeighbours().stream()
                                .filter(tl -> !neighboursQueue.contains(tl)
                                        && tl.getStatus()!= MinesweeperTile.Status.OPEN).toList());
            }
            t.setOpen();
        }

        return false;
    }

    public void flagTile(int x, int y) {
        flagTile(getTile(x, y));
    }

    public void flagTile(MinesweeperTile tile) {
        if (tile.getStatus() != MinesweeperTile.Status.OPEN)
            tile.setStatus(MinesweeperTile.Status.FLAGGED);
    }

    public List<List<MinesweeperTile>> getTiles() {
        return tiles;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Scores getScores() {
        return scores;
    }
}
